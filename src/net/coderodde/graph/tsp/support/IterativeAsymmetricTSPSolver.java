package net.coderodde.graph.tsp.support;

import java.util.ArrayList;
import java.util.List;
import net.coderodde.graph.DirectedGraph;
import net.coderodde.graph.tsp.AsymmetricTSPSolver;

/**
 * This class implements a brute-force nonrecursive algorithm for solving the
 * traveling salesman problem.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 21, 2016)
 */
public class IterativeAsymmetricTSPSolver extends AsymmetricTSPSolver {

    @Override
    public List<Integer> solve(final DirectedGraph graph) {
        final PermutationIterable<Integer> iterable = 
                new PermutationIterable<>(
                        new ArrayList<>(graph.getAllNodes()));
        
        double bestTourCost = Double.POSITIVE_INFINITY;
        List<Integer> bestTour = new ArrayList<>(graph.getAllNodes());
        
        for (final List<Integer> tour : iterable) {
            final double currentTourCost = getTourCost(graph, tour);
            
            if (bestTourCost > currentTourCost) {
                bestTourCost = currentTourCost;
                bestTour = tour;
            }
        }
        
        return bestTour;
    }
    
}
