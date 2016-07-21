package net.coderodde.graph.tsp;

import java.util.List;
import net.coderodde.graph.DirectedGraph;

/**
 * This interface defines the API for the algorithms solving asymmetric 
 * traveling salesman problem.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 20, 2016)
 */
public abstract class AsymmetricTSPSolver {

    public abstract List<Integer> solve(final DirectedGraph graph);
    
    public static double getTourCost(final DirectedGraph graph, 
                                     final List<Integer> tour) {
        double cost = 0.0;
        
        for (int i = 0; i < tour.size(); ++i) {
            cost += graph.getEdgeWeight(tour.get(i), 
                                        tour.get((i + 1) % tour.size()));
        }
        
        return cost;
    }
}
