package practica_busqueda;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar
{
    public static PriorityQueue<Node> closedList, openList;
    public PathFinder pathfinder;

    public AStar(PathFinder pathfinder)
    {
        this.pathfinder = pathfinder;
    }
    
    /**
	 * Calcular heurística
	 * @param curNode Nodo actual
	 * @param goalNode Nodo meta
	 * @return Distancia de Manhattan
	 */
    private static double heuristicEstimatedCost(Node curNode, Node goalNode)
    {
        //4-way: using Manhattan
        double xDiff = Math.abs(curNode.position.x - goalNode.position.x);
        double yDiff = Math.abs(curNode.position.y - goalNode.position.y);
        return xDiff + yDiff;
    }
    
    /**
	 * Calcular camino que se ha seguido desde un nodo final hasta el nodo inicio
	 * @param node Nodo
	 * @return Camino de nodos
	 */
    private ArrayList<Node> calculatePath(Node node)
    {
        ArrayList<Node> path = new ArrayList<Node>();
        while(node != null)
        {
            if(node.parent != null) //to avoid adding the start node.
            {
                path.add(0,node);
            }
            node = node.parent;
        }
        return path;
    }
    
    /**
	 * Algoritmo A*
	 * @param start Nodo inicial
	 * @param goal Nodo meta
	 * @param modoCalculo Modo de cálculo de vecinos
	 * @param casillasQueAbrenHabitaculos Nodos que liberan enemigos
	 * @return Camino desde el nodo inicio hasta el nodo final
	 */
    public ArrayList<Node> findPath(Node start, Node goal, int modoCalculo, ArrayList<Node> casillasQueAbrenHabitaculos)
    {
    	
        Node node = null;
        
        openList = new PriorityQueue<Node>();
        closedList = new PriorityQueue<Node>();
        
        start.totalCost = 0.0f;

        start.estimatedCost += heuristicEstimatedCost(start, goal);
        
        openList.add(start);
        
        while(openList.size() != 0)
        {
            node = openList.poll();
            closedList.add(node);
            
            if(node.position.equals(goal.position))
                return calculatePath(node);
            
            ArrayList<Node> neighbours = new ArrayList<Node>();
            
            switch (modoCalculo) {
            	case 0:
            		neighbours = pathfinder.getNeighboursPortal(node, casillasQueAbrenHabitaculos);
            		break;
            		
            	case 1:
            		neighbours = pathfinder.getNeighbours(node, casillasQueAbrenHabitaculos);
            		break;
            	
            	case 2:
            		neighbours = pathfinder.getNeighboursHabitaculos(node);
            		break;
            }
            
            for(int i = 0; i < neighbours.size(); ++i)
            {
                Node neighbour = neighbours.get(i);
                double curDistance = neighbour.totalCost;

                if(!openList.contains(neighbour) && !closedList.contains(neighbour))
                {
                    neighbour.totalCost = curDistance + node.totalCost;
                    neighbour.estimatedCost += heuristicEstimatedCost(neighbour, goal);
                    neighbour.parent = node;

                    openList.add(neighbour);

                }else if(curDistance + node.totalCost < neighbour.totalCost)
                {
                    neighbour.totalCost = curDistance + node.totalCost;
                    neighbour.parent = node;

                    if(openList.contains(neighbour))
                        openList.remove(neighbour);

                    if(closedList.contains(neighbour))
                        closedList.remove(neighbour);
                    
                    openList.add(neighbour);
                }
            }
        }

        if(! node.position.equals(goal.position))
            return null;

        return calculatePath(node);

    }
}
