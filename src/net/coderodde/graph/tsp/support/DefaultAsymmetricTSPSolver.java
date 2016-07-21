package net.coderodde.graph.tsp.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.coderodde.graph.DirectedGraph;
import net.coderodde.graph.tsp.AsymmetricTSPSolver;

/**
 * This class implements a default asymmetric traveling salesman problem solver.
 * It checks to see that the input graph is connected, after which it computes
 * the missing arcs in the graph. Since the graph becomes fully connected, it
 * proceeds to computing the shortest tour over the graph.
 * <p>
 * Note that this class maintains state, so that if you need to run the
 * algorithm in parallel, make sure that each thread constructs its own instance
 * of this class.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 20, 2018)
 */
public final class DefaultAsymmetricTSPSolver extends AsymmetricTSPSolver {

    private DirectedGraph graph;
    private double bestTourLength;
    private double tentativeTourCost;
    private final List<Integer> bestTour      = new ArrayList<>();
    private final Set<Integer> visitedNodeSet = new HashSet<>();
    private final List<Integer> tentativeTour = new ArrayList<>();
    
    @Override
    public List<Integer> solve(final DirectedGraph graph) {
        Objects.requireNonNull(graph, "The input graph is null.");
        
        if (graph.size() == 0) {
            throw new IllegalArgumentException("The input graph is empty.");
        }
        
        return findShortestTour(graph);
    }
    
    private List<Integer> findShortestTour(final DirectedGraph graph) {
        init(graph);
        findShortestTour();
        return new ArrayList<>(bestTour);
    }
    
    private void init(final DirectedGraph graph) {
        this.graph = graph;
        bestTour.clear();
        tentativeTour.clear();
        visitedNodeSet.clear();
        bestTourLength = Double.POSITIVE_INFINITY;
        tentativeTourCost = 0.0;
    }
    
    private void findShortestTour() {
        for (final Integer node : graph.getAllNodes()) {
            if (!visitedNodeSet.contains(node)) {
                double tmp;
                
                if (visitedNodeSet.size() >= 1) {
                    tmp = graph.getEdgeWeight(lastOf(tentativeTour), node);
                } else {
                    tmp = 0.0;
                }
                
                tentativeTourCost += tmp;
                visitedNodeSet.add(node);
                tentativeTour.add(node);
                
                if (visitedNodeSet.size() == graph.size()) {
                    // Add the cost of the last arc that goes from last visited
                    // node to the very first visited node.
                    final double concludingArcWeight = 
                            graph.getEdgeWeight(lastOf(tentativeTour),
                                                firstOf(tentativeTour));
                    
                    tentativeTourCost += concludingArcWeight;
                    
                    if (bestTourLength > tentativeTourCost) {
                        bestTourLength = tentativeTourCost;
                        bestTour.clear();
                        bestTour.addAll(tentativeTour);
                    }
                    
                    tentativeTourCost -= concludingArcWeight;
                } else {
                    findShortestTour();
                }
                
                visitedNodeSet.remove(node);
                removeLast(tentativeTour);
                tentativeTourCost -= tmp;
            }
        }
    }
    
    private static <T> T firstOf(final List<T> list) {
        return list.get(0);
    }
    
    private static <T> T lastOf(final List<T> list) {
        return list.get(list.size() - 1);
    }
    
    private static <T> void removeLast(final List<T> list) {
        list.remove(list.size() - 1);
    }
}
