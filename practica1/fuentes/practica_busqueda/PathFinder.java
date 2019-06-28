package practica_busqueda;

import core.game.Observation;
import core.game.StateObservation;
import tools.Vector2d;

import java.util.ArrayList;

public class PathFinder {

    public AStar astar;
    public Anchura anchura;
    public StateObservation state;
    public ArrayList<Observation> grid[][];


    private static int[] x_arrNeig = null;
    private static int[] y_arrNeig = null;

    public PathFinder()
    {	}

    public void run(StateObservation stateObs)
    {
        this.state = stateObs;
        this.grid = stateObs.getObservationGrid();
        this.astar = new AStar(this);
        this.anchura = new Anchura(this);
        
        init();
    }
    
    public void updateStateObs(StateObservation stateObs) {
    	this.state = stateObs;
    	this.grid = stateObs.getObservationGrid();
    }
    
    private void init()
    {
        x_arrNeig = new int[]{0,    0,    -1,    1};
        y_arrNeig = new int[]{-1,   1,     0,    0};
    }
    
    /**
	 * Cálculo de vecinos para el modo 1 en el algoritmo A*
	 * @param currentNode Nodo actual
	 * @param casillasQueAbrenHabitaculos Casillas que liberan enemigos
	 * @return Vecinos para el nodo currentNode
	 */
    public ArrayList<Node> getNeighbours(Node currentNode, ArrayList<Node> casillasQueAbrenHabitaculos) {
        ArrayList<Node> neighbours = new ArrayList<Node>();
        int posX = (int) (currentNode.position.x);
        int posY = (int) (currentNode.position.y);
        
        for(int i = 0; i < x_arrNeig.length; ++i)
        {   
        	if(casillasQueAbrenHabitaculos.contains(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])))) {
        			Node n = new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i]));
					n.estimatedCost = 10000.0;
					neighbours.add(n);
        	}
        	
        	else if(grid[posX+x_arrNeig[i]][posY+y_arrNeig[i]].isEmpty()) {
        		neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
        	}
        		
        	else {
        		switch (grid[posX+x_arrNeig[i]][posY+y_arrNeig[i]].get(0).itype) {
		 			case 0:				// Muro
		 				break;
		 			
		 			case 1:				// Jugador
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 					
		 			case 4:				// Tierra
		 				if(grid[posX+x_arrNeig[i]][posY-1+y_arrNeig[i]].isEmpty()) {
		 					neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				}
		 					
		 				else if(grid[posX+x_arrNeig[i]][posY-1+y_arrNeig[i]].get(0).itype != 7)
		 					neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				
		 				else {	// Si hay una piedra arriba de este nodo
		 					Node n = new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i]));
		 					n.estimatedCost = 100.0;
		 					neighbours.add(n);
		 				}
		 				
		 				break;
		 			
		 			case 5:				// Portal
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 				
		 			case 6:				// Gema
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 			
		 			case 7:				// Piedra
		 				break;
		 				
		 			case 10:			// Escorpión
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 				
		 			case 11:			// Murciélago
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 		}
        	}
        }

        return neighbours;
    }
    
    /**
	 * Cálculo de vecinos para el modo 0 en el algoritmo A*
	 * @param currentNode Nodo actual
	 * @param casillasQueAbrenHabitaculos Casillas que liberan enemigos
	 * @return Vecinos para el nodo currentNode
	 */
    public ArrayList<Node> getNeighboursPortal(Node currentNode, ArrayList<Node> casillasQueAbrenHabitaculos) {
        ArrayList<Node> neighbours = new ArrayList<Node>();
        int posX = (int) (currentNode.position.x);
        int posY = (int) (currentNode.position.y);
        
        for(int i = 0; i < x_arrNeig.length; ++i)
        {   
        	if(casillasQueAbrenHabitaculos.contains(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])))) {
        			Node n = new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i]));
					n.estimatedCost = 10000.0;
					neighbours.add(n);
        	}
        	
        	else if(grid[posX+x_arrNeig[i]][posY+y_arrNeig[i]].isEmpty()) {
        		neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
        	}
        		
        	else {
        		switch (grid[posX+x_arrNeig[i]][posY+y_arrNeig[i]].get(0).itype) {
		 			case 0:				// Muro
		 				break;
		 			
		 			case 1:				// Jugador
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 					
		 			case 4:				// Tierra
		 				if(grid[posX+x_arrNeig[i]][posY-1+y_arrNeig[i]].isEmpty()) {
		 					neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				}
		 					
		 				else if(grid[posX+x_arrNeig[i]][posY-1+y_arrNeig[i]].get(0).itype != 7)
		 					neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				
		 				else {	// Si hay una piedra arriba de este nodo
		 					Node n = new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i]));
		 					n.estimatedCost = 100.0;
		 					neighbours.add(n);
		 				}
		 				
		 				break;
		 			
		 			case 5:				// Portal
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 				
		 			case 6:				// Gema
		 				break;
		 			
		 			case 7:				// Piedra
		 				break;
		 				
		 			case 10:			// Escorpión
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 				
		 			case 11:			// Murciélago
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 		}
        	}
        }

        return neighbours;
    }
    
    /**
	 * Cálculo de vecinos para el modo 2 en el algoritmo A*
	 * @param currentNode Nodo actual
	 * @return Vecinos para el nodo currentNode
	 */
    public ArrayList<Node> getNeighboursHabitaculos(Node currentNode) {
        ArrayList<Node> neighbours = new ArrayList<Node>();
        int posX = (int) (currentNode.position.x);
        int posY = (int) (currentNode.position.y);

        for(int i = 0; i < x_arrNeig.length; ++i)
        {   
        
        	if(grid[posX+x_arrNeig[i]][posY+y_arrNeig[i]].isEmpty()) 
        		neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
        	
        	else {
        		switch (grid[posX+x_arrNeig[i]][posY+y_arrNeig[i]].get(0).itype) {
		 			case 0:				// Muro
		 				break;
		 			
		 			case 1:				// Jugador
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 					
		 			case 4:				// Tierra
		 				break;
		 			
		 			case 5:				// Portal
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 				
		 			case 6:				// Gema
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 			
		 			case 7:				// Piedra
		 				break;
		 				
		 			case 10:			// Escorpión
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 				
		 			case 11:			// Murciélago
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 		}
        	}
        }

        return neighbours;
    }
    
    /**
	 * Cálculo de vecinos para el modo 0 en el algoritmo búsqueda en anchura
	 * @param currentNode Nodo actual
	 * @param habitaculosEnemigos Nodos que liberan enemigos
	 * @return Vecinos para el nodo currentNode
	 */
    public ArrayList<Node> getNeighboursAnchuraCalculo0(Node currentNode, ArrayList<Node> habitaculosEnemigos) {
        ArrayList<Node> neighbours = new ArrayList<Node>();
        int posX = (int) (currentNode.position.x);
        int posY = (int) (currentNode.position.y);

        for(int i = 0; i < x_arrNeig.length; ++i)
        {   
        	if(grid[posX+x_arrNeig[i]][posY+y_arrNeig[i]].isEmpty()) 
        		neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
        	
        	else {
        		switch (grid[posX+x_arrNeig[i]][posY+y_arrNeig[i]].get(0).itype) {
		 			case 0:				// Muro
		 				break;
		 			
		 			case 1:				// Jugador
		 				break;
		 					
		 			case 4:				// Tierra
		 				habitaculosEnemigos.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 			
		 			case 5:				// Portal
		 				habitaculosEnemigos.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 				
		 			case 6:				// Gema
		 				habitaculosEnemigos.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 			
		 			case 7:				// Piedra
		 				break;
		 				
		 			case 10:			// Escorpión
		 				habitaculosEnemigos.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 				
		 			case 11:			// Murciélago
		 				habitaculosEnemigos.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
		 				break;
		 		}
        	}
        }

        return neighbours;
    }
    
    /**
	 * Cálculo de vecinos para el modo 1 en el algoritmo búsqueda en anchura
	 * @param currentNode Nodo actual
	 * @param caminoGemaAPiedra Rocas que están cerca de la gema y que se pueden desprender
	 * @return Vecinos para el nodo currentNode
	 */
    public ArrayList<Node> getNeighboursAnchuraCalculo1(Node currentNode, ArrayList<Node> caminoGemaAPiedra) {
        ArrayList<Node> neighbours = new ArrayList<Node>();
        int posX = (int) (currentNode.position.x);
        int posY = (int) (currentNode.position.y);

        for(int i = 0; i < x_arrNeig.length; ++i)
        {   
        	if(y_arrNeig[i] != -1) {		// Las posiciones que están más arriba que la gema no son interesantes
        		if(grid[posX+x_arrNeig[i]][posY+y_arrNeig[i]].isEmpty()) 
            		neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
            	
            	else {
            		switch (grid[posX+x_arrNeig[i]][posY+y_arrNeig[i]].get(0).itype) {
    		 			case 0:				// Muro
    		 				break;
    		 			
    		 			case 1:				// Jugador
    		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
    		 				break;
    		 					
    		 			case 4:				// Tierra
    		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
    		 				break;
    		 			
    		 			case 5:				// Portal
    		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
    		 				break;
    		 				
    		 			case 6:				// Gema
    		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
    		 				break;
    		 			
    		 			case 7:				// Piedra
    		 				caminoGemaAPiedra.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
    		 				break;
    		 				
    		 			case 10:			// Escorpión
    		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
    		 				break;
    		 				
    		 			case 11:			// Murciélago
    		 				neighbours.add(new Node(new Vector2d(posX+x_arrNeig[i], posY+y_arrNeig[i])));
    		 				break;
    		 		}
            	}
        	}
        }

        return neighbours;
    }
}
