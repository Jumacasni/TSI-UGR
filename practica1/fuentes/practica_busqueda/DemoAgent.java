package practica_busqueda;

import core.game.StateObservation;
import ontology.Types;
import tools.ElapsedCpuTimer;

import java.util.ArrayList;
import java.util.Random;

/*  Agente de prueba que usa los métodos de BaseAgent para obtener información
    sobre el entorno y asigna varias probabilidades en función de esta información
    a las posibles acciones.
*/
public class DemoAgent extends BaseAgent{
    private ArrayList<Types.ACTIONS> lista_acciones; // Conjunto de acciones posibles
    private Random generador;
    
    public DemoAgent(StateObservation so, ElapsedCpuTimer elapsedTimer){
        super(so, elapsedTimer);
        generador = new Random();
        
        lista_acciones = new ArrayList();
        lista_acciones.add(Types.ACTIONS.ACTION_UP);
        lista_acciones.add(Types.ACTIONS.ACTION_DOWN);
        lista_acciones.add(Types.ACTIONS.ACTION_RIGHT);
        lista_acciones.add(Types.ACTIONS.ACTION_LEFT);
    }
    
    @Override
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){       
        // Veo todas las acciones que puedo hacer en el turno siguiente (algoritmo greedy)
        // y según lo buena que sea la acción (score) asigno un peso (probabilidad de ser escogida) a la acción.
        // Score
        // Coger gema -> + INF (se escoge siempre)
        // Chocar con enemigo -> - INF (no se escoge nunca esa acción)
        // Estar al lado de un enemigo -> -50
        // Estar al lado de una pared -> -10
        // Estar al lado de una roca -> -20
        // Estar al lado de una gema -> +100
        // Si ya he cogido todas las gemas y estoy en la salida -> + INF
        // Si ya he cogido todas las gemas y estoy al lado de la salida -> +1000
        
        // Scores
        final int cerca_enemigo = -50;
        final int cerca_pared = -10;
        final int cerca_roca = -20;
        final int cerca_gema = 100; // Solo si no he cogido todas las gemas
        final int cerca_salida = 1000; // Solo si ya he cogido todas las gemas

        Types.ACTIONS mejor_accion = Types.ACTIONS.ACTION_NIL;
        
        double score_up = 0;
        double score_down = 0;
        double score_left = 0;
        double score_right = 0;
        
        StateObservation estado_actual;
        PlayerObservation jugador;
        PlayerObservation jugador_antiguo = getPlayer(stateObs);
        ArrayList<Observation> gemas;
        ArrayList<Observation>[] enemigos;
        ArrayList<Observation> paredes = getWallsList(stateObs); // Las paredes no cambian de posición
        ArrayList<Observation> rocas;
        Observation salida = getExit(stateObs); // La salida no cambia de posición
        int score_actual = 0;
        
        // Si me faltan gemas por coger -> veo si puedo coger una
        // Si he cogido todas las necesarias -> veo si puedo abandonar el nivel
        gemas = getGemsList(stateObs);
        
        if (getRemainingGems(stateObs) > 0){
            for (Observation gema_actual : gemas){
                if (jugador_antiguo.getManhattanDistance(gema_actual) == 1){ // Veo si puedo coger esa gema (está a una distancia de 1)

                    if (gema_actual.getX() < jugador_antiguo.getX()) // gema a la izquierda
                        mejor_accion = Types.ACTIONS.ACTION_LEFT;

                    else if (gema_actual.getX() > jugador_antiguo.getX()) // gema a la derecha
                        mejor_accion = Types.ACTIONS.ACTION_RIGHT;

                    else{
                        if (gema_actual.getY() < jugador_antiguo.getY()) // gema arriba
                            mejor_accion = Types.ACTIONS.ACTION_UP;
                        else
                            mejor_accion = Types.ACTIONS.ACTION_DOWN;
                    }

                    return mejor_accion; // Escojo esa acción directamente
                }  
            }
        }
        else{ // Puedo abandonar el nivel
            if (jugador_antiguo.getManhattanDistance(salida) == 1){
                
                    if (salida.getX() < jugador_antiguo.getX()) // salida a la izquierda
                        mejor_accion = Types.ACTIONS.ACTION_LEFT;

                    else if (salida.getX() > jugador_antiguo.getX()) // salida a la derecha
                        mejor_accion = Types.ACTIONS.ACTION_RIGHT;

                    else{
                        if (salida.getY() < jugador_antiguo.getY()) // salida arriba
                            mejor_accion = Types.ACTIONS.ACTION_UP;
                        else
                            mejor_accion = Types.ACTIONS.ACTION_DOWN;
                    }

                    return mejor_accion; // Escojo esa acción directamente
            }
        }
        
        // Recorro todas las acciones
        for (Types.ACTIONS accion_actual : lista_acciones){
            score_actual = 0;
            
            estado_actual = stateObs.copy();
            estado_actual.advance(accion_actual); // Ejecuto una acción y avanzo la simulación en un paso
            
            jugador = getPlayer(estado_actual);
            
            if (jugador.hasDied() || jugador.equals(jugador_antiguo) ){ // El jugador ha muerto o no puede ejecutar esa acción              
                // Hago que esa acción no se ejecute -> score = - inf
                if (accion_actual == Types.ACTIONS.ACTION_UP)
                    score_up = Double.NEGATIVE_INFINITY;
                else if (accion_actual == Types.ACTIONS.ACTION_DOWN)
                    score_down = Double.NEGATIVE_INFINITY;
                else if (accion_actual == Types.ACTIONS.ACTION_RIGHT)
                    score_right = Double.NEGATIVE_INFINITY;
                else
                    score_left = Double.NEGATIVE_INFINITY;           
            }
            
            else{ // Acción válida 
                // Calculo el score de esa acción
                gemas = getGemsList(estado_actual);
                enemigos = getEnemiesList(estado_actual);
                rocas = getBouldersList(estado_actual);
                
                if (getRemainingGems(estado_actual) > 0){
                    for (Observation gema_actual : gemas)
                        if (jugador.getManhattanDistance(gema_actual) == 1)
                            score_actual += cerca_gema;
                }
                else{ // No quedan gemas por coger
                    if (jugador.getManhattanDistance(salida) == 1)
                        score_actual += cerca_salida;
                }
                
                for (int i = 0; i < enemigos.length; i++)
                    for (Observation enemigo_actual : enemigos[i]){
                        if (jugador.getManhattanDistance(enemigo_actual) == 1)
                            score_actual += cerca_enemigo;
                    }
                
                for (Observation pared_actual : paredes){
                    if (jugador.getManhattanDistance(pared_actual) == 1)
                            score_actual += cerca_pared;
                }
                
                for (Observation roca_actual : rocas){
                    if (jugador.getManhattanDistance(roca_actual) == 1)
                            score_actual += cerca_roca;
                }
                
                if (accion_actual == Types.ACTIONS.ACTION_UP)
                    score_up = score_actual;
                else if (accion_actual == Types.ACTIONS.ACTION_DOWN)
                    score_down = score_actual;
                else if (accion_actual == Types.ACTIONS.ACTION_RIGHT)
                    score_right = score_actual;
                else
                    score_left = score_actual;          
            }
        }
        
        // Si no puedo ejecutar ninguna acción (todos los scores valen -inf), devuelvo la acción nula
        if (score_up == Double.NEGATIVE_INFINITY && score_down == Double.NEGATIVE_INFINITY
                && score_right == Double.NEGATIVE_INFINITY && score_left == Double.NEGATIVE_INFINITY)
            return Types.ACTIONS.ACTION_NIL;
        
        // Escojo una acción según su score
        // Para los pesos (probabilidades) calculo el softmax
        double[] pesos = new double[4];
        double score_total_exp = Math.pow(Math.E, score_up) + Math.pow(Math.E, score_down)
                + Math.pow(Math.E, score_right) + Math.pow(Math.E, score_left);
        
        // Peso de action_up
        pesos[0] = Math.pow(Math.E, score_up) / score_total_exp;
        // Peso de action_down
        pesos[1] = Math.pow(Math.E, score_down) / score_total_exp;
        // Peso de action_right
        pesos[2] = Math.pow(Math.E, score_right) / score_total_exp;
        // Peso de action_left
        pesos[3] = Math.pow(Math.E, score_left) / score_total_exp;
        
        double num_pseudoaleatorio = generador.nextDouble();
        
        if (num_pseudoaleatorio < pesos[0])
            mejor_accion = Types.ACTIONS.ACTION_UP;
        else if (num_pseudoaleatorio < pesos[0] + pesos[1])
            mejor_accion = Types.ACTIONS.ACTION_DOWN;
        else if (num_pseudoaleatorio < pesos[0] + pesos[1] + pesos[2])
            mejor_accion = Types.ACTIONS.ACTION_RIGHT;
        else
            mejor_accion = Types.ACTIONS.ACTION_LEFT;
        
        return mejor_accion;
    }
}
