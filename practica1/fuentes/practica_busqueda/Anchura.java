package practica_busqueda;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Anchura {
	public static PriorityQueue<Node> closedList, openList;
	public PathFinder pathfinder;
	
	public Anchura(PathFinder pathfinder) {
		this.pathfinder = pathfinder;
	}
	
	/**
	 * Algoritmo búsqueda en anchura
	 * @param start Nodo inicial
	 * @param modoCalculo Modo de cálculo de vecinos
	 */
	public ArrayList<Node> findPath(Node start, int modoCalculo)
    {
        Node node = null;
        
        openList = new PriorityQueue<Node>();
        closedList = new PriorityQueue<Node>();
        
        ArrayList<Node> camino = new ArrayList<Node>();
        
        openList.add(start);
        
        while(openList.size() != 0)
        {
            node = openList.poll();
            closedList.add(node);
            
            ArrayList<Node> neighbours = new ArrayList<Node>();
            
            if(modoCalculo == 0)
            	neighbours = pathfinder.getNeighboursAnchuraCalculo0(node, camino);
            
            else if(modoCalculo == 1) {
            	neighbours = pathfinder.getNeighboursAnchuraCalculo1(node, camino);
            	
            	if(camino.size() >= 1)
            		return camino;
            }
            	
            for(int i = 0; i < neighbours.size(); ++i)
            {
                Node neighbour = neighbours.get(i);

                if(!openList.contains(neighbour) && !closedList.contains(neighbour))
                {
                    openList.add(neighbour);
                }
            }
        }

        return camino;
    }
}
