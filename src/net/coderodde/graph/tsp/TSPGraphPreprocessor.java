package net.coderodde.graph.tsp;

import java.util.List;
import java.util.Set;
import net.coderodde.graph.DirectedGraph;
import net.coderodde.graph.shortestpath.ShortestPathFinder;
import net.coderodde.graph.shortestpath.support.DijkstraShortestPathFinder;

/**
 * This class provides a method for converting a directed graph into a complete
 * directed graph. For each arc that does not appear in the input graph, this
 * class facilities compute a shortest path between the terminal nodes of the 
 * arc, and adds a missing arc with the weight set to the shortest distance.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 21, 2016)
 */
public class TSPGraphPreprocessor {
   
    public DirectedGraph preprocessGraph(final DirectedGraph graph) {
        final DirectedGraph workGraph = new DirectedGraph();
        final ShortestPathFinder shortestPathFinder = 
                new DijkstraShortestPathFinder();
        
        for (final Integer node : graph.getAllNodes()) {
            workGraph.addNode(node);
        }
        
        final Set<Integer> nodeSet = graph.getAllNodes();
        
        for (final Integer nodeA : nodeSet) {
            for (final Integer nodeB : nodeSet) {
                if (nodeA.equals(nodeB)) {
                    continue;
                }
                
                if (graph.hasEdge(nodeA, nodeB)) {
                    workGraph.addEdge(nodeA, 
                                      nodeB, 
                                      graph.getEdgeWeight(nodeA, 
                                                          nodeB));
                } else {
                    final List<Integer> shortestPath = 
                            shortestPathFinder.findShortestPath(graph,
                                                                nodeA, 
                                                                nodeB);
                    if (shortestPath.isEmpty()) {
                        throw new IllegalArgumentException(
                                "The input graph is not connected.");
                    }
                    
                    workGraph.addEdge(nodeA, 
                                      nodeB, 
                                      getPathCost(graph, shortestPath));
                }
            }
        }
        
        return workGraph;
    }
    
    private double getPathCost(final DirectedGraph graph,
                               final List<Integer> path) {
        double cost = 0.0;
        
        for (int i = 0; i < path.size() - 1; ++i) {
            cost += graph.getEdgeWeight(path.get(i), path.get(i + 1));
        }
        
        return cost;
    }
}
