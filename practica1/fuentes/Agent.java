package practica_busqueda;

import java.util.ArrayList;
import java.util.Stack;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

public class Agent extends BaseAgent{
	
	PathFinder pf;									// Referencia al PathFinder
	Vector2d fescala; 								// Factor de escala del mapa
	Vector2d ultimaPos;								// Última posición del avatar		
	ArrayList<Node> path;							// Camino del plan actual
	boolean esquivarRoca;							// True si el avatar está esquivando una roca que le cae de encima
	boolean picar;									// True si el avatar va a picar para que caiga una roca
	boolean estoyAtrapado;							// True si el avatar no puede ir hacia ninguna gema
	boolean estoyHuyendo;							// True si el avatar está huyendo de un enemigo
	boolean moviendoPiedra;							// True si el avatar tiene un plan para mover una piedra
	Stack<Types.ACTIONS> accionesProgramadas;		// Una pila que guarda acciones en el comportamiento reactivo				
	Types.ACTIONS ultimaAccion;						// Última acción del avatar
	ArrayList<Vector2d> gemas;						// Posiciones de las gemas en el mapa
	ArrayList<Node> casillasQueAbrenHabitaculos;	// Nodos que hacen que los enemigos sean liberados
	ArrayList<Node> piedrasQuePuedoMover;			// Nodos con las piedras que se pueden mover para liberar una gema
	
	/**
	 * Inicializar variables del agente
	 * @param stateObs Observación del estado actual.
	 * @param elapsedTimer Temporizador.
	 */
	public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
		super(stateObs, elapsedTimer);
		
		pf = new PathFinder();
		pf.run(stateObs);
		
		fescala = new Vector2d((double) stateObs.getWorldDimension().width / stateObs.getObservationGrid().length, (double) stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);
		
		ultimaPos = new Vector2d(stateObs.getAvatarPosition().x / fescala.x, stateObs.getAvatarPosition().y / fescala.y);
		
		path = new ArrayList<Node>();
		
		esquivarRoca = false;
		
		picar = false;
		
		accionesProgramadas = new Stack<Types.ACTIONS>();
		
		ultimaAccion = Types.ACTIONS.ACTION_NIL;
		
		estoyAtrapado = false;
		
		estoyHuyendo = false;
		
		casillasQueAbrenHabitaculos = new ArrayList<Node>();
		
		piedrasQuePuedoMover = new ArrayList<Node>();
		
		moviendoPiedra = false;
	}
	
	/**
	 * Encontrar casillas de tierra adyacentes a los enemigos
	 * @param stateObs Observación del estado actual.
	 */
	public void taparCasillasMonstruos(StateObservation stateObs) {
		// Se coge la posición de todos los enemigos del mapa
		ArrayList<Observation>[] monstruos = stateObs.getNPCPositions();
		
		// Se añade al vector posiciones_monstruos la posición de cada enemigo con la escala del mapa
		ArrayList<Vector2d> posiciones_monstruos = new ArrayList<Vector2d>();
		for(int j=0; j < monstruos.length; ++j) {
			for(int k=0; k < monstruos[j].size(); ++k) {
				Vector2d enemigo = monstruos[j].get(k).position;
				
				enemigo.x = enemigo.x / fescala.x;
				enemigo.y = enemigo.y / fescala.y;
				
				posiciones_monstruos.add(enemigo);
			}
		}
		
		// Para cada enemigo, se calcula un camino que pase sólo por casillas vacías
		// desde el enemigo hasta las casillas de tierra adyacentes.
		// Esas casillas serán las que se deberán evitar explorar.
		casillasQueAbrenHabitaculos.clear();
		for(Vector2d position : posiciones_monstruos) {
			for(Node n : pf.anchura.findPath(new Node(position), 0)) {
				casillasQueAbrenHabitaculos.add(n);
			}
		}
	}
	
	/**
	 * Ver si un enemigo puede ir hacia una gema 
	 * @param stateObs Observación del estado actual
	 * @param gema Posición de la gema en el mapa
	 * @return true Si hay un enemigo que puede ir hasta la gema en las mismas condiciones que el avatar
	 */
	public boolean hayPeligroMonstruos(StateObservation stateObs, Vector2d gema) {
		// Se coge la posición de todos los enemigos del mapa
		ArrayList<Observation>[] monstruos = stateObs.getNPCPositions();
		
		// Se añade al vector posiciones_monstruos la posición de cada enemigo con la escala del mapa
		ArrayList<Vector2d> posiciones_monstruos = new ArrayList<Vector2d>();
		for(int j=0; j < monstruos.length; ++j) {
			for(int k=0; k < monstruos[j].size(); ++k) {
				// Cojo la posición del enemigo
				Vector2d enemigo = monstruos[j].get(k).position;
				
				enemigo.x = enemigo.x / fescala.x;
				enemigo.y = enemigo.y / fescala.y;
				
				posiciones_monstruos.add(enemigo);
			}
		}
		
		// Para cada enemigo, se calcula un camino que va desde el enemigo hasta la gema (en las mismas condiciones
		// que si el avatar va hasta la gema)
		for(Vector2d position : posiciones_monstruos) {
			ArrayList<Node> camino = pf.astar.findPath(new Node(position), new Node(gema), 2, casillasQueAbrenHabitaculos);
			
			if(camino != null)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Calcular un camino hasta una gema o un portal
	 * @param nGemas Número de gemas que posee el avatar
	 * @param avatar Posición del avatar
	 * @param stateObs Observación del estado actual
	 * @return path Una lista de nodos que contiene el camino
	 */
	public ArrayList<Node> calcularNuevoCamino(int nGemas, Vector2d avatar, StateObservation stateObs){
		
		// Si el avatar posee 9 gemas o más, se calcula un camino hasta el portal
		if(nGemas >= 9) {
			Vector2d portal;
			
			// Se busca la posición del portal
			ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
			
			portal = posiciones[0].get(0).position;
			
			// Se hace la escala para obtener las coordenadas del portal en el mapa
			portal.x = portal.x / fescala.x;
			portal.y = portal.y / fescala.y;
			
			// Se calcula el camino desde el avatar hasta el portal
			path = pf.astar.findPath(new Node(avatar), new Node(portal), 0, casillasQueAbrenHabitaculos);	
		}
		
		else {
				
			Vector2d gema;
			
			// Se buscan las posiciones de las gemas
			ArrayList<Observation>[] posiciones = stateObs.getResourcesPositions(stateObs.getAvatarPosition());
			
			// Variable que se pondrá a true cuando se haya encontrado un camino
			boolean caminoEncontrado = false;
			
			// Se recorren todas las gemas (empezando por la más cercana)
			for(int i=0; i < posiciones[0].size(); ++i) {
				gema = posiciones[0].get(i).position;
				
				gema.x = gema.x / fescala.x;
				gema.y = gema.y / fescala.y;
				
				// Si la gema es inalcanzable por algún enemigo
				if(!hayPeligroMonstruos(stateObs, gema)) {
					piedrasQuePuedoMover.clear();
					
					// Se calcula un camino desde el avatar hasta la gema
					path = pf.astar.findPath(new Node(avatar), new Node(gema), 1, casillasQueAbrenHabitaculos);	
						
					// Si se ha encontrado un camino, finaliza la búsqueda
					if(path != null) {
						caminoEncontrado = true;
						break;
					}
					
					// Si no se ha encontrado un camino, significa que la gema está encerrada entre rocas
					else {	
						
						// Se calculan las piedras más cercanas que están a la misma altura o más abajo que la gema
						piedrasQuePuedoMover.clear();
						for(Node n : pf.anchura.findPath(new Node(gema), 1)) {
							piedrasQuePuedoMover.add(n);
						}
						
						// Se guarda la posición que está abajo de la primera piedra encontrada
						Vector2d posicionPiedraQuePuedoMover = new Vector2d();
						posicionPiedraQuePuedoMover.x = piedrasQuePuedoMover.get(0).position.x;
						posicionPiedraQuePuedoMover.y = piedrasQuePuedoMover.get(0).position.y+1;
						
						// Se calcula un camino desde el avatar hasta la piedra, de tal forma que la piedra tenga que caer
						path = pf.astar.findPath(new Node(avatar), new Node(posicionPiedraQuePuedoMover), 1, casillasQueAbrenHabitaculos);
						
						// Si se ha encontrado un camino, finaliza la búsqueda
						if(path != null) {
							caminoEncontrado = true;
							moviendoPiedra = true;
						}	
					}
				}
				
				// Si se ha encontrado un camino, finaliza la búsqueda
				if(caminoEncontrado)
					break;
				
				// Si no se ha encontrado un camino, se pasa a la siguiente gema
				else
					continue;
			}
			
			// Si no se ha encontrado un camino aún, se recorren las gemas de nuevo
			if(!caminoEncontrado) {
				for(int i=0; i < posiciones[0].size(); ++i) {
					gema = posiciones[0].get(i).position;
					
					gema.x = gema.x / fescala.x;
					gema.y = gema.y / fescala.y;
					
					// Si la gema es alcanzable por algún enemigo
					if(hayPeligroMonstruos(stateObs, gema)) {
						piedrasQuePuedoMover.clear();
						
						// Se calcula un camino desde el avatar hasta la gema
						path = pf.astar.findPath(new Node(avatar), new Node(gema), 1, casillasQueAbrenHabitaculos);	
						
						// Si se ha encontrado un camino, se pone fin a la búsqueda
						if(path != null) {
							caminoEncontrado = true;
							break;
						}
						
						// Si no se ha encontrado un camino, significa que la gema está encerrada entre rocas
						else {
							
							// Se calculan las piedras más cercanas que están a la misma altura o más abajo que la gema
							piedrasQuePuedoMover.clear();
							for(Node n : pf.anchura.findPath(new Node(gema), 1)) {
								piedrasQuePuedoMover.add(n);
							}
							
							// Se guarda la posición que está abajo de la primera piedra encontrada
							Vector2d posicionPiedraQuePuedoMover = new Vector2d();
							posicionPiedraQuePuedoMover.x = piedrasQuePuedoMover.get(0).position.x;
							posicionPiedraQuePuedoMover.y = piedrasQuePuedoMover.get(0).position.y+1;
							
							// Se calcula un camino desde el avatar hasta la piedra, de tal forma que la piedra tenga que caer
							path = pf.astar.findPath(new Node(avatar), new Node(posicionPiedraQuePuedoMover), 1, casillasQueAbrenHabitaculos);
							
							// Si se ha encontrado un camino, finaliza la búsqueda
							if(path != null) {
								caminoEncontrado = true;
								moviendoPiedra = true;
							}	
						}
					}
					
					// Si se ha encontrado un camino, finaliza la búsqueda
					if(caminoEncontrado)
						break;
					
					// Si no se ha encontrado un camino, se pasa a la siguiente gema
					else
						continue;
					
				}
			}
			
			// Si no se ha encontrado un camino a ninguna gema, significa que el avatar está atrapado
			if(!caminoEncontrado) {
				estoyAtrapado = true;
				esperar();
			}
		}
		
		return path;
	}
	
	/**
	 * Añadir ACTION_LEFT al comportamiento reactivo
	 */
	public void aniadirAccionesIzquierda() {
		accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
		accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
	}
	
	/**
	 * Añadir ACTION_RIGHT al comportamiento reactivo
	 */
	public void aniadirAccionesDerecha() {
		accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
		accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
	}
	
	/**
	 * Añadir ACTION_DOWN al comportamiento reactivo
	 */
	public void aniadirAccionesAbajo() {
		accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
		accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
	}
	
	/**
	 * Mirar si el avatar puede avanzar a la izquierda
	 * @param stateObs Observación del estado actual
	 * @param avatar Posición del avatar
	 */
	public void mirarPosibilidadesIzquierda(StateObservation stateObs, Vector2d avatar) {
		 /* -> SI EL HUECO DE LA IZQUIERDA NO ESTÁ VACÍO
		 * 		-> SI EL HUECO DE LA IZQUIERDA NO ES UN MURO, PIEDRA O MONSTRUO
		 * 			mirarPosibilidadesArribaIzquierda();
		 * 		-> SI EL HUECO DE LA IZQUIERDA ES UN MURO, PIEDRA O MONSTRUO
		 * 			mirarPosibilidadesAbajo();
		 * 	-> SI EL HUECO DE LA IZQUIERDA ESTÁ VACÍO
		 * 		mirarPosibilidadesArribaIzquierda();
		 * 	
		 */
		
		ArrayList<Observation> huecoIzquierda = stateObs.getObservationGrid()[(int) (avatar.x-1)][(int) avatar.y];
		
		if(!huecoIzquierda.isEmpty()) {
			if(huecoIzquierda.get(0).itype != 0 && huecoIzquierda.get(0).itype != 7 && huecoIzquierda.get(0).itype != 10 && huecoIzquierda.get(0).itype != 11) {
				mirarPosibilidadesArribaIzquierda(stateObs, avatar);
			}
			
			else {
				mirarPosibilidadesAbajo(stateObs, avatar);
			}
		}
		
		else {
			mirarPosibilidadesArribaIzquierda(stateObs, avatar);
		}
	}
	
	/**
	 * Mirar si el avatar puede avanzar a la izquierda sin que le caiga una roca
	 * @param stateObs Observación del estado actual
	 * @param avatar Posición del avatar
	 */
	public void mirarPosibilidadesArribaIzquierda(StateObservation stateObs, Vector2d avatar) {
		 /* -> SI EL HUECO DE ARRIBA A LA IZQUIERDA NO ESTÁ VACÍO
		 * 		-> SI EL HUECO DE ARRIBA A LA IZQUIERDA NO ES UNA PIEDRA
		 * 			Voy hacia la izquierda
		 * 		-> SI EL HUECO DE ARRIBA A LA IZQUIERDA ES UNA PIEDRA
		 * 			Voy hacia abajo y vuelvo a buscar un hueco a la izquierda o derecha de forma recursiva
		 * 	-> SI EL HUECO DE ARRIBA A LA IZQUIERDA ESTÁ VACÍO
		 * 		Voy hacia la izquierda
		 */
		
		ArrayList<Observation> huecoArribaIzquierda = stateObs.getObservationGrid()[(int) (avatar.x-1)][(int) avatar.y-1];
		
		if(!huecoArribaIzquierda.isEmpty()) {
			if(huecoArribaIzquierda.get(0).itype != 7) {
				aniadirAccionesIzquierda();
			}
			
			else {
				mirarPosibilidadesAbajo(stateObs, avatar);
			}
		}
		
		else {
			aniadirAccionesIzquierda();
		}	
	}
	
	/**
	 * Mirar si el avatar puede avanzar a la derecha
	 * @param stateObs Observación del estado actual
	 * @param avatar Posición del avatar
	 */
	public void mirarPosibilidadesDerecha(StateObservation stateObs, Vector2d avatar) {
		 /* -> SI EL HUECO DE LA DERECHA NO ESTÁ VACÍO
		 * 		-> SI EL HUECO DE LA DERECHA NO ES UN MURO, PIEDRA O MONSTRUO
		 * 			mirarPosibilidadesArribaDerecha();
		 * 		-> SI EL HUECO DE LA DERECHA ES UN MURO, PIEDRA O MONSTRUO
		 * 			mirarPosibilidadesAbajo();
		 * 	-> SI EL HUECO DE LA DERECHA ESTÁ VACÍO
		 * 		mirarPosibilidadesArribaDerecha();
		 * 	
		 */
		
		ArrayList<Observation> huecoDerecha = stateObs.getObservationGrid()[(int) (avatar.x+1)][(int) avatar.y];
		
		if(!huecoDerecha.isEmpty()) {	// Si la casilla derecha tiene algo
			if(huecoDerecha.get(0).itype != 0 && huecoDerecha.get(0).itype != 7 && huecoDerecha.get(0).itype != 10 && huecoDerecha.get(0).itype != 11) {	// Si no es una piedra, ni un murciélago, ni un escorpión, ni un muro
				mirarPosibilidadesArribaDerecha(stateObs, avatar);
			}
			
			else {	// Si es una piedra, murciélago o escorpión tengo que ir hacia abajo
				mirarPosibilidadesAbajo(stateObs, avatar);
			}
		}
		
		else {
			mirarPosibilidadesArribaDerecha(stateObs, avatar);
		}
	}
	
	/**
	 * Mirar si el avatar puede avanzar a la derecha sin que le caiga una roca
	 * @param stateObs Observación del estado actual
	 * @param avatar Posición del avatar
	 */
	public void mirarPosibilidadesArribaDerecha(StateObservation stateObs, Vector2d avatar) {
		 /* -> SI EL HUECO DE ARRIBA A LA DERECHA NO ESTÁ VACÍO
		 * 		-> SI EL HUECO DE ARRIBA A LA DERECHA NO ES UNA PIEDRA
		 * 			Voy hacia la derecha
		 * 		-> SI EL HUECO DE ARRIBA A LA DERECHA ES UNA PIEDRA
		 * 			Voy hacia abajo y vuelvo a buscar un hueco a la izquierda o derecha de forma recursiva
		 * 	-> SI EL HUECO DE ARRIBA A LA DERECHA ESTÁ VACÍO
		 * 		Voy hacia la derecha
		 */
		
		ArrayList<Observation> huecoArribaDerecha = stateObs.getObservationGrid()[(int) (avatar.x+1)][(int) avatar.y-1];
		
		if(!huecoArribaDerecha.isEmpty()) {
			if(huecoArribaDerecha.get(0).itype != 7) {
				aniadirAccionesDerecha();
			}
			
			else {
				mirarPosibilidadesAbajo(stateObs, avatar);
			}
		}
		
		else {
			aniadirAccionesDerecha();
		}	
	}
	
	/**
	 * Mirar si el avatar puede avanzar hacia abajo
	 * @param stateObs Observación del estado actual
	 * @param avatar Posición del avatar
	 */
	public void mirarPosibilidadesAbajo(StateObservation stateObs, Vector2d avatar) {
		ArrayList<Observation> huecoAbajo = stateObs.getObservationGrid()[(int) avatar.x][(int) (avatar.y+1)];
		
		if(!huecoAbajo.isEmpty()) {	// Si la casilla de abajo tiene algo	
			if(huecoAbajo.get(0).itype != 0 && huecoAbajo.get(0).itype != 7 && huecoAbajo.get(0).itype != 10 && huecoAbajo.get(0).itype != 11) {	// Si no es una piedra, ni un murciélago, ni un escorpión, ni un muro
				aniadirAccionesAbajo();
				buscarMejorHuecoParaEsquivar(stateObs, new Vector2d(avatar.x, avatar.y+1));	// Como la piedra va a caer sobre el avatar, busco otro hueco para esquivar que sea izquierda o derecha
			}
			
			else {
				estoyAtrapado = true;
				esperar();
			}
		}
		
		else {
			aniadirAccionesAbajo();
			buscarMejorHuecoParaEsquivar(stateObs, new Vector2d(avatar.x, avatar.y+1));	// Como la piedra va a caer sobre el avatar, busco otro hueco para esquivar que sea izquierda o derecha
		}
	}
	
	/**
	 * Buscar la mejor casilla para ir cuando el avatar está esquivando una roca
	 * @param stateObs Observación del estado actual
	 * @param avatar Posición del avatar
	 */
	public void buscarMejorHuecoParaEsquivar(StateObservation stateObs, Vector2d avatar) {
		
		ArrayList<Observation> huecoIzquierda = stateObs.getObservationGrid()[(int) (avatar.x-1)][(int) avatar.y];
		ArrayList<Observation> huecoDerecha = stateObs.getObservationGrid()[(int) (avatar.x+1)][(int) avatar.y];
		
		ArrayList<Observation> huecoArribaIzquierda = stateObs.getObservationGrid()[(int) (avatar.x-1)][(int) avatar.y-1];
		ArrayList<Observation> huecoArribaDerecha = stateObs.getObservationGrid()[(int) (avatar.x+1)][(int) avatar.y-1];
		
		ArrayList<Observation>[] posiciones = stateObs.getResourcesPositions(stateObs.getAvatarPosition());
		Vector2d proxima_gema = posiciones[0].get(0).position;
		
		proxima_gema.x = proxima_gema.x / fescala.x;
		proxima_gema.y = proxima_gema.y / fescala.y;
		
		accionesProgramadas.push(Types.ACTIONS.ACTION_UP);	// Porque primero hay que ir hacia arriba para coger la gema
		
		/*
		 * ALGORITMO EXPLICADO
		 * 
		 * -> SI LA GEMA MÁS CERCANA ESTÁ A LA IZQUIERDA
		 * 		-> SI EL HUECO DE LA IZQUIERDA NO ESTÁ VACÍO
		 * 			-> SI EL HUECO DE LA IZQUIERDA NO ES UN MURO, PIEDRA O MONSTRUO
		 * 				-> SI EL HUECO DE ARRIBA A LA IZQUIERDA NO ESTÁ VACÍO 
		 * 					-> SI EL HUECO DE ARRIBA A LA IZQUIERDA NO ES UNA PIEDRA
		 * 						Voy hacia la izquierda
		 * 					-> SI EL HUECO DE ARRIBA A LA IZQUIERDA ES UNA PIEDRA
		 * 						mirarPosibilidadesDerecha();
		 * 				-> SI EL HUECO DE ARRIBA A LA IZQUIERDA ESTÁ VACÍO
		 * 						Voy hacia la izquierda
		 * 			-> SI EL HUECO DE LA IZQUIERDA ES UN MURO, PIEDRA O MONSTRUO
		 * 				mirarPosibilidadesDerecha();
		 * 		-> SI EL HUECO DE LA IZQUIERDA ESTÁ VACÍO
		 * 			-> SI EL HUECO DE ARRIBA A LA IZQUIERDA NO ESTÁ VACÍO 
		 * 				-> SI EL HUECO DE ARRIBA A LA IZQUIERDA NO ES UNA PIEDRA
		 * 					Voy hacia la izquierda
		 * 				-> SI EL HUECO DE ARRIBA A LA IZQUIERDA ES UNA PIEDRA
		 * 					mirarPosibilidadesDerecha();
		 * 			-> SI EL HUECO DE ARRIBA A LA IZQUIERDA ESTÁ VACÍO
		 * 					Voy hacia la izquierda	
		 */
		
		if(proxima_gema.x <= avatar.x) {
			if(!huecoIzquierda.isEmpty()) {	
				if(huecoIzquierda.get(0).itype != 0 && huecoIzquierda.get(0).itype != 7 && huecoIzquierda.get(0).itype != 10 && huecoIzquierda.get(0).itype != 11) {
					if(!huecoArribaIzquierda.isEmpty()) {
						if(huecoArribaIzquierda.get(0).itype != 7) {
							aniadirAccionesIzquierda();
						}
						
						else {
							mirarPosibilidadesDerecha(stateObs, avatar);
						}
					}
					
					else {
						aniadirAccionesIzquierda();
					}
				}
		
				else {
					mirarPosibilidadesDerecha(stateObs, avatar);
				}
			}
			
			else{
				if(!huecoArribaIzquierda.isEmpty()) {
					if(huecoArribaIzquierda.get(0).itype != 7) {
						aniadirAccionesIzquierda();
					}
					
					else {
						mirarPosibilidadesDerecha(stateObs, avatar);
					}
				}
				
				else {
					aniadirAccionesIzquierda();
				}
			}
		}
		
		// Si la gema más cercana está a la derecha del avatar
		else {
			if(!huecoDerecha.isEmpty()) {	
				if(huecoDerecha.get(0).itype != 0 && huecoDerecha.get(0).itype != 7 && huecoDerecha.get(0).itype != 10 && huecoDerecha.get(0).itype != 11) {
					if(!huecoArribaDerecha.isEmpty()) {
						if(huecoArribaDerecha.get(0).itype != 7) {
							aniadirAccionesDerecha();
						}
						
						else {
							mirarPosibilidadesIzquierda(stateObs, avatar);
						}
					}
					
					else {
						aniadirAccionesDerecha();
					}
				}
		
				else {
					mirarPosibilidadesIzquierda(stateObs, avatar);
				}
			}
			
			else{
				if(!huecoArribaDerecha.isEmpty()) {
					if(huecoArribaDerecha.get(0).itype != 7) {
						aniadirAccionesDerecha();
					}
					
					else {
						mirarPosibilidadesIzquierda(stateObs, avatar);
					}
				}
				
				else {
					aniadirAccionesDerecha();
				}
			}
		}
	}
	
	/**
	 * Añadir acciones para picar en el comportamiento reactivo
	 * @param girarParaPicar True si el avatar no está orientado y tiene que girar para usar la pala
	 */
	public void picarRocaDerecha(boolean girarParaPicar) {
		esperar();
		esperar();
		esperar();
		accionesProgramadas.push(Types.ACTIONS.ACTION_USE);
		
		if(girarParaPicar)
			accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
	}
	
	/**
	 * Añadir acciones para picar en el comportamiento reactivo
	 * @param girarParaPicar True si el avatar no está orientado y tiene que girar para usar la pala
	 */
	public void picarRocaIzquierda(boolean girarParaPicar) {
		esperar();
		esperar();
		esperar();
		accionesProgramadas.push(Types.ACTIONS.ACTION_USE);
		
		if(girarParaPicar)
			accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
	}
	
	/**
	 * No hacer nada
	 */
	public void esperar() {
		accionesProgramadas.push(Types.ACTIONS.ACTION_NIL);
		accionesProgramadas.push(Types.ACTIONS.ACTION_NIL);
	}
	
	/**
	 * Añadir acciones al comportamiento reactivo para que el avatar huya si hay peligro a la derecha
	 * @param stateObs Observación del estado actual
	 * @param avatar Posición del avatar
	 */
	public void huirDerecha(StateObservation stateObs, Vector2d avatar){
		ArrayList<Observation> casillaIzquierda = stateObs.getObservationGrid()[(int) avatar.x-1][(int) avatar.y];
		ArrayList<Observation> casillaArriba = stateObs.getObservationGrid()[(int) avatar.x][(int) avatar.y-1];
		ArrayList<Observation> casillaAbajo = stateObs.getObservationGrid()[(int) avatar.x][(int) avatar.y+1];
		
		if(!casillaIzquierda.isEmpty()) {
			if(casillaIzquierda.get(0).itype == 7 || casillaIzquierda.get(0).itype == 10 || casillaIzquierda.get(0).itype == 11) {
				if(!casillaArriba.isEmpty()) {
					if(casillaArriba.get(0).itype == 7 || casillaArriba.get(0).itype == 10 || casillaArriba.get(0).itype == 11) {
						if(!casillaAbajo.isEmpty()) {
							if(casillaAbajo.get(0).itype == 7 || casillaAbajo.get(0).itype == 10 || casillaAbajo.get(0).itype == 11) {
								estoyAtrapado = true;
								esperar();
								path.clear();
							}
							
							else {
								accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
								accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
							}
						}
						
						else {
							accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
							accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
						}
					}
					
					else {
						accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
						accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
					}
				}
				
				else {
					accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
					accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
				}
			}
			
			else {
				accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
				accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
			}
		}
		
		else {
			accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
			accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
		}	
	}
	
	/**
	 * Añadir acciones al comportamiento reactivo para que el avatar huya si hay peligro a la izquierda
	 * @param stateObs Observación del estado actual
	 * @param avatar Posición del avatar
	 */
	public void huirIzquierda(StateObservation stateObs, Vector2d avatar){
		ArrayList<Observation> casillaDerecha = stateObs.getObservationGrid()[(int) avatar.x+1][(int) avatar.y];
		ArrayList<Observation> casillaArriba = stateObs.getObservationGrid()[(int) avatar.x][(int) avatar.y-1];
		ArrayList<Observation> casillaAbajo = stateObs.getObservationGrid()[(int) avatar.x][(int) avatar.y+1];
		
		if(!casillaDerecha.isEmpty()) {
			if(casillaDerecha.get(0).itype == 7 || casillaDerecha.get(0).itype == 10 || casillaDerecha.get(0).itype == 11) {
				if(!casillaArriba.isEmpty()) {
					if(casillaArriba.get(0).itype == 7 || casillaArriba.get(0).itype == 10 || casillaArriba.get(0).itype == 11) {
						if(!casillaAbajo.isEmpty()) {
							if(casillaAbajo.get(0).itype == 7 || casillaAbajo.get(0).itype == 10 || casillaAbajo.get(0).itype == 11) {
								estoyAtrapado = true;
								esperar();
								path.clear();
							}
							
							else {
								accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
								accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
							}
						}
						
						else {
							accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
							accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
						}
					}
					
					else {
						accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
						accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
					}
				}
				
				else {
					accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
					accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
				}
			}
			
			else {
				accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
				accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
			}
		}
		
		else {
			accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
			accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
		}	
	}
	
	/**
	 * Añadir acciones al comportamiento reactivo para que el avatar huya si hay peligro abajo
	 * @param stateObs Observación del estado actual
	 * @param avatar Posición del avatar
	 */
	public void huirAbajo(StateObservation stateObs, Vector2d avatar){
		ArrayList<Observation> casillaDerecha = stateObs.getObservationGrid()[(int) avatar.x+1][(int) avatar.y];
		ArrayList<Observation> casillaArriba = stateObs.getObservationGrid()[(int) avatar.x][(int) avatar.y-1];
		ArrayList<Observation> casillaIzquierda = stateObs.getObservationGrid()[(int) avatar.x-1][(int) avatar.y];
		
		if(!casillaArriba.isEmpty()) {
			if(casillaArriba.get(0).itype == 7 || casillaArriba.get(0).itype == 10 || casillaArriba.get(0).itype == 11) {
				if(!casillaDerecha.isEmpty()) {
					if(casillaDerecha.get(0).itype == 7 || casillaDerecha.get(0).itype == 10 || casillaDerecha.get(0).itype == 11) {
						if(!casillaIzquierda.isEmpty()) {
							if(casillaIzquierda.get(0).itype == 7 || casillaIzquierda.get(0).itype == 10 || casillaIzquierda.get(0).itype == 11) {
								estoyAtrapado = true;
								esperar();
								path.clear();
							}
							
							else {
								accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
								accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
							}
						}
						
						else {
							accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
							accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
						}
					}
					
					else {
						accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
						accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
					}
				}
				
				else {
					accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
					accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
				}
			}
			
			else {
				accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
				accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
			}
		}
		
		else {
			accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
			accionesProgramadas.push(Types.ACTIONS.ACTION_UP);
		}	
	}
	
	/**
	 * Añadir acciones al comportamiento reactivo para que el avatar huya si hay peligro arriba
	 * @param stateObs Observación del estado actual
	 * @param avatar Posición del avatar
	 */
	public void huirArriba(StateObservation stateObs, Vector2d avatar){
		ArrayList<Observation> casillaDerecha = stateObs.getObservationGrid()[(int) avatar.x+1][(int) avatar.y];
		ArrayList<Observation> casillaAbajo = stateObs.getObservationGrid()[(int) avatar.x][(int) avatar.y+1];
		ArrayList<Observation> casillaIzquierda = stateObs.getObservationGrid()[(int) avatar.x-1][(int) avatar.y];
		
		if(!casillaAbajo.isEmpty()) {
			if(casillaAbajo.get(0).itype == 7 || casillaAbajo.get(0).itype == 10 || casillaAbajo.get(0).itype == 11) {
				if(!casillaDerecha.isEmpty()) {
					if(casillaDerecha.get(0).itype == 7 || casillaDerecha.get(0).itype == 10 || casillaDerecha.get(0).itype == 11) {
						if(!casillaIzquierda.isEmpty()) {
							if(casillaIzquierda.get(0).itype == 7 || casillaIzquierda.get(0).itype == 10 || casillaIzquierda.get(0).itype == 11) {
								estoyAtrapado = true;
								esperar();
								path.clear();
							}
							
							else {
								accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
								accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
							}
						}
						
						else {
							accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
							accionesProgramadas.push(Types.ACTIONS.ACTION_LEFT);
						}
					}
					
					else {
						accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
						accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
					}
				}
				
				else {
					accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
					accionesProgramadas.push(Types.ACTIONS.ACTION_RIGHT);
				}
			}
			
			else {
				accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
				accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
			}
		}
		
		else {
			accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
			accionesProgramadas.push(Types.ACTIONS.ACTION_DOWN);
		}	
	}
	
	/**
	 * Comprobar si hay enemigos cerca del avatar
	 * @param stateObs Observación del estado actual del mapa
	 * @param avatar Posición del avatar
	 * @return True si hay enemigos cerca del avatar
	 */
	public boolean hayMonstruoCasillaCercana(StateObservation stateObs, Vector2d avatar) {
		ArrayList<Observation> arriba1 = stateObs.getObservationGrid()[(int) avatar.x][(int) avatar.y-1];
		ArrayList<Observation> abajo1 = stateObs.getObservationGrid()[(int) avatar.x][(int) avatar.y+1];
		ArrayList<Observation> izquierda1 = stateObs.getObservationGrid()[(int) avatar.x-1][(int) avatar.y];
		ArrayList<Observation> derecha1 = stateObs.getObservationGrid()[(int) avatar.x+1][(int) avatar.y];
		
		if(!arriba1.isEmpty()) {
			if(arriba1.get(0).itype == 10 || arriba1.get(0).itype == 11) {
				huirArriba(stateObs, avatar);
				return true;
			}
		}
		
		if(!abajo1.isEmpty()) {
			if(abajo1.get(0).itype == 10 || abajo1.get(0).itype == 11) {
				huirAbajo(stateObs, avatar);
				return true;
			}
		}
			
		if(!izquierda1.isEmpty()) {
			if(izquierda1.get(0).itype == 10 || izquierda1.get(0).itype == 11) {
				huirIzquierda(stateObs, avatar);
				return true;
			}
		}
		
		if(!derecha1.isEmpty()) {
			if(derecha1.get(0).itype == 10 || derecha1.get(0).itype == 11) {
				huirDerecha(stateObs, avatar);
				return true;
			}
		}
		
		ArrayList<Observation> arriba2;
		ArrayList<Observation> abajo2;
		ArrayList<Observation> izquierda2;
		ArrayList<Observation> derecha2;
		if(avatar.y-2 >= 0) {
			arriba2 = stateObs.getObservationGrid()[(int) avatar.x][(int) avatar.y-2];
			
			if(!arriba2.isEmpty()) {
				if(arriba2.get(0).itype == 10 || arriba2.get(0).itype == 11)
					if(arriba1.isEmpty()) {
						huirArriba(stateObs, avatar);
						return true;
					}
			}
		}
		
		if(avatar.y+2 <= 12) {
			abajo2 = stateObs.getObservationGrid()[(int) avatar.x][(int) avatar.y+2];
			
			if(!abajo2.isEmpty()) {
				if(abajo2.get(0).itype == 10 || abajo2.get(0).itype == 11)
					if(abajo1.isEmpty()) {
						huirAbajo(stateObs, avatar);
						return true;
					}
			}
		}
		
		if(avatar.x-2 >= 0) {
			izquierda2 = stateObs.getObservationGrid()[(int) avatar.x-2][(int) avatar.y];
			
			if(!izquierda2.isEmpty()) {
				if(izquierda2.get(0).itype == 10 || izquierda2.get(0).itype == 11)
					if(izquierda1.isEmpty()) {
						huirIzquierda(stateObs, avatar);
						return true;
					}
			}
		}
		
		if(avatar.x+2 <= 25) {
			derecha2 = stateObs.getObservationGrid()[(int) avatar.x+2][(int) avatar.y];
			
			if(!derecha2.isEmpty()) {
				if(derecha2.get(0).itype == 10 || derecha2.get(0).itype == 11)
					if(derecha1.isEmpty()) {
						huirDerecha(stateObs, avatar);
						return true;
					}
			}
		}
		
		return false;
	}
	
	/**
	 * Decidir la acción del avatar
	 * @param stateObs Observación del estado actual
	 * @param elapsedTimer Temporizador
	 * @return siguienteAccion Acción que va a realizar el avatar
	 */
	@Override
	public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		
		// Actualizar la observación del estado actual en el path finder
		pf.updateStateObs(stateObs);
		
		// Recalcular las casillas que hacen que se liberen enemigos
		taparCasillasMonstruos(stateObs);
		
		ArrayList<Observation> grid[][] = stateObs.getObservationGrid();
		
		// Obtenemos la posición del avatar
		Vector2d avatar = new Vector2d(stateObs.getAvatarPosition().x / fescala.x, stateObs.getAvatarPosition().y / fescala.y);
	
		// Actualizar el plan de ruta
		if(((avatar.x != ultimaPos.x) || (avatar.y != ultimaPos.y)) && path != null){
			if(!path.isEmpty())
				path.remove(0);
		}
		
		// Calcular el numero de gemas que lleva el avatar
		int nGemas = 0;
		if(!stateObs.getAvatarResources().isEmpty()) {
			nGemas = stateObs.getAvatarResources().get(6);
		}
		
		// Si el avatar no está haciendo un comportamiento reactivo
		if(!esquivarRoca && !picar && !estoyHuyendo) {
			
			// Calcular camino si no hay un camino actual
			if(path == null || path.isEmpty()) {
				path = calcularNuevoCamino(nGemas, avatar, stateObs);
			}
			
			else {
				moviendoPiedra = false;
			}
		}
		
		// Si el avatar está atrapado
		if(estoyAtrapado) {
			
			// Coger la última acción programada en el comportamiento reactivo
			Types.ACTIONS accion = accionesProgramadas.pop();
			
			if(accionesProgramadas.isEmpty()) {
				estoyAtrapado = false;
				path.clear();
			}
			
			ultimaPos = avatar;
			ultimaAccion = accion;
			return accion;
		}
		
		// Si el avatar está esquivando roca
		else if(esquivarRoca) {
			
			// Coger la última acción programada en el comportamiento reactivo
			Types.ACTIONS accion = accionesProgramadas.pop();
			
			if(accionesProgramadas.isEmpty()) {
				esquivarRoca = false;
				path.clear();
			}
			
			ultimaPos = avatar;
			ultimaAccion = accion;
			return accion;
		}
		
		// Si el avatar está picando
		else if(picar) {
			
			// Coger la última acción programada en el comportamiento reactivo
			Types.ACTIONS accion = accionesProgramadas.pop();
			
			if(accionesProgramadas.isEmpty()) {
				picar = false;
				path.clear();
			}
			
			ultimaPos = avatar;
			ultimaAccion = accion;
			return accion;
		}
		
		else if(estoyHuyendo) {
			
			// Coger la última acción programada en el comportamiento reactivo
			Types.ACTIONS accion = accionesProgramadas.pop();
			
			if(accionesProgramadas.isEmpty()) {
				estoyHuyendo = false;
				path.clear();
			}
			
			ultimaPos = avatar;
			ultimaAccion = accion;
			return accion;
		}
		
		// Si hay un plan con un camino
		else if(path != null && !path.isEmpty()) {
	
			Types.ACTIONS siguienteAccion;
			
			// Se coge el primer nodo del camino
			Node siguientePos = path.get(0);
			
			// Casilla en el mapa que corresponde al nodo que se ha cogido
			ArrayList<Observation> casillaSiguiente = grid[(int) siguientePos.position.x][(int) siguientePos.position.y];
			
			// Si hay enemigos cerca del avatar
			if(hayMonstruoCasillaCercana(stateObs, avatar)) {
				
				// Activar comportamiento reactivo para huir
				estoyHuyendo = true;
				path.clear();
				siguienteAccion = Types.ACTIONS.ACTION_NIL;
			}
			
			// Si la posición en el eje x entre la siguiente posición y el avatar NO varía,
			// el avatar va a ir hacia la derecha o izquierda
			else if(siguientePos.position.x != avatar.x) {
				
				// Si el avatar tiene que ir hacia la derecha
				if(siguientePos.position.x > avatar.x) {
					
					// Casilla en el mapa que corresponde a la casilla de arriba del nodo siguientePos
					ArrayList<Observation> casillaArribaSiguiente = grid[(int) siguientePos.position.x][(int) siguientePos.position.y-1];
					
					// Si la casilla siguiente no está vacía
					if(!casillaSiguiente.isEmpty()) {
						
						// Si la siguiente casilla es tierra
						if(casillaSiguiente.get(0).itype == 4) {
							
							// Si la casilla de arriba de la siguiente es una roca
							if(!casillaArribaSiguiente.isEmpty()) {
								if(casillaArribaSiguiente.get(0).itype == 7) {
									
									// Activar comportamiento reactivo para picar
									siguienteAccion = Types.ACTIONS.ACTION_NIL;
									picar = true;
									path.clear();
									
									// Si el avatar no está orientado hacia la derecha
									if(stateObs.getAvatarOrientation().x == 1.0 && stateObs.getAvatarOrientation().y == 0.0) {
										picarRocaDerecha(false);				
									}
									
									else {
										picarRocaDerecha(true);
									}
								}
								
								else {
									siguienteAccion = Types.ACTIONS.ACTION_RIGHT;
								}
							}
							
							else {
								siguienteAccion = Types.ACTIONS.ACTION_RIGHT;
							}
						}
						
						else {
							siguienteAccion = Types.ACTIONS.ACTION_RIGHT;
						}
					}
					
					// Si la casilla siguiente está vacía
					else {
						
						// Si la casilla de arriba de la siguiente tiene una roca
						if(!casillaArribaSiguiente.isEmpty()) {
							if(casillaArribaSiguiente.get(0).itype == 7) {
								siguienteAccion = Types.ACTIONS.ACTION_NIL;
								path.clear();
							}
							
							else {
								siguienteAccion = Types.ACTIONS.ACTION_RIGHT;
							}
						}
						
						else {
							siguienteAccion = Types.ACTIONS.ACTION_RIGHT;
						}
					}	
				}
				
				// Si el avatar tiene que ir hacia la izquierda
				else {
					
					// Casilla en el mapa que corresponde a la casilla de arriba del nodo siguientePos
					ArrayList<Observation> casillaArribaSiguiente = grid[(int) siguientePos.position.x][(int) siguientePos.position.y-1];
					
					// Si la casilla siguiente no está vacía
					if(!casillaSiguiente.isEmpty()) {
				
						// Si la siguiente casilla es tierra
						if(casillaSiguiente.get(0).itype == 4) {
							
							// Si la casilla de arriba de la siguiente es una roca
							if(!casillaArribaSiguiente.isEmpty()) {
								if(casillaArribaSiguiente.get(0).itype == 7) {
									
									// Activar comportamiento reactivo para usar la pala
									siguienteAccion = Types.ACTIONS.ACTION_NIL;
									picar = true;
									path.clear();
									
									// Si el avatar no está orientado hacia la izquierda
									if(stateObs.getAvatarOrientation().x == -1.0 && stateObs.getAvatarOrientation().y == 0.0) {
										picarRocaIzquierda(false);				
									}
									
									else {
										picarRocaIzquierda(true);
									}
								}
								
								else {
									siguienteAccion = Types.ACTIONS.ACTION_LEFT;
								}
							}
							
							else {
								siguienteAccion = Types.ACTIONS.ACTION_LEFT;
							}
						}
						
						else {
							siguienteAccion = Types.ACTIONS.ACTION_LEFT;
						}
					}
					
					// Si la casilla siguiente está vacía
					else {
						
						// Si la casilla de arriba de la siguiente es una roca
						if(!casillaArribaSiguiente.isEmpty()) {
							if(casillaArribaSiguiente.get(0).itype == 7) {
								siguienteAccion = Types.ACTIONS.ACTION_NIL;
								path.clear();
							}
							
							else {
								siguienteAccion = Types.ACTIONS.ACTION_LEFT;
							}
						}
						
						else {
							siguienteAccion = Types.ACTIONS.ACTION_LEFT;
						}
					}
				}
			}
			
			// Si la posición en el eje y entre la siguiente posición y el avatar NO varía,
			// el avatar va a ir hacia arriba o abajo
			else {
				
				// Si el avatar tiene que ir hacia abajo
				if(siguientePos.position.y > avatar.y) {
					siguienteAccion = Types.ACTIONS.ACTION_DOWN;
				}
				
				// Si el avatar tiene que ir hacia arriba
				else {
					
					// Casilla en el mapa que corresponde a la casilla de arriba del nodo siguientePos
					ArrayList<Observation> casillaArribaSiguiente = grid[(int) siguientePos.position.x][(int) siguientePos.position.y-1];

					// Si la casilla siguiente no está vacía
					if(!casillaSiguiente.isEmpty()) {
						
						// Si la casilla siguiente es tierra o una gema
						if(casillaSiguiente.get(0).itype == 4 || casillaSiguiente.get(0).itype == 6) {
							
							// Si la casilla de arriba de la siguiente es una roca
							if(!casillaArribaSiguiente.isEmpty()) {
								if(casillaArribaSiguiente.get(0).itype == 7) {
									
									// Activar comportamiento reactivo para esquivar una roca
									siguienteAccion = Types.ACTIONS.ACTION_UP;
									esquivarRoca = true;
									buscarMejorHuecoParaEsquivar(stateObs, avatar);
									path.clear();
								}

								else {
									siguienteAccion = Types.ACTIONS.ACTION_UP;
								}
							}
							
							else {
								siguienteAccion = Types.ACTIONS.ACTION_UP;
							}
						}
						
						else {
							siguienteAccion = Types.ACTIONS.ACTION_UP;
						}
					}
					
					// Si la casilla siguiente está vacía
					else {
						siguienteAccion = Types.ACTIONS.ACTION_UP;
					}
				}
			}
	
			ultimaAccion = siguienteAccion;
			ultimaPos = avatar;
			return siguienteAccion;
		}
		
		// Acción por defecto
		else {
			ultimaAccion = Types.ACTIONS.ACTION_NIL;
			return Types.ACTIONS.ACTION_NIL;
		}
	}

}
