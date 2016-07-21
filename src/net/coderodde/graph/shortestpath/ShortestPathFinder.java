package net.coderodde.graph.shortestpath;

import java.util.List;
import net.coderodde.graph.DirectedGraph;

/**
 * This interface defines the API for directed shortest path algorithms.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 20, 2016)
 */
public interface ShortestPathFinder {
   
    /**
     * Searches for the shortest path in the graph {@code graph} starting from
     * the node {@code source} and aiming to reach the node {@code target}.
     * 
     * @param graph  the graph to search.
     * @param source the source node.
     * @param target the target node.
     * @return the list of nodes on a shortest path or an empty list if target
     *         is not reachable from source.
     */
    public List<Integer> findShortestPath(final DirectedGraph graph,
                                          final Integer source,
                                          final Integer target);
}
