package net.coderodde.graph.shortestpath.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.coderodde.graph.DirectedGraph;
import net.coderodde.graph.shortestpath.ShortestPathFinder;
import net.coderodde.util.PriorityQueue;
import net.coderodde.util.support.DaryHeap;

/**
 *
 * @author Rodion "rodde" 
 */
public class DijkstraShortestPathFinder implements ShortestPathFinder {

    @Override
    public List<Integer> findShortestPath(final DirectedGraph graph, 
                                          final Integer source, 
                                          final Integer target) {
        Objects.requireNonNull(graph, "The input graph is null.");
        Objects.requireNonNull(source, "The source node is null.");
        Objects.requireNonNull(target, "The target node is null.");
        
        if (!graph.hasNode(source)) {
            throw new IllegalArgumentException(
                    "The source node does not appear in the input graph.");
        }
        
        if (!graph.hasNode(target)) {
            throw new IllegalArgumentException(
                    "The target node does not appear in the input graph.");
        }
        
        final int size = graph.size();
        final PriorityQueue<Integer> OPEN = new DaryHeap<>();
        final Set<Integer> CLOSED = new HashSet<>(size);
        final Map<Integer, Integer> PARENTS = new HashMap<>(size);
        final Map<Integer, Double> DISTANCE = new HashMap<>(size);
        
        OPEN.add(source, 0.0);
        PARENTS.put(source, null);
        DISTANCE.put(source, 0.0);
        
        while (!OPEN.isEmpty()) {
            final Integer current = OPEN.extractMinimum();
            
            if (current.equals(target)) {
                return tracebackPath(target, PARENTS);
            }
            
            for (final Integer child : graph.getChildrenOf(current)) {
                if (CLOSED.contains(child)) {
                    continue;
                }
                
                final double tentativeWeight = DISTANCE.get(current) +
                                               graph.getEdgeWeight(current, 
                                                                   child);
                
                if (!DISTANCE.containsKey(child)
                        || DISTANCE.get(child) > tentativeWeight) {
                    OPEN.add(child, tentativeWeight);
                    PARENTS.put(child, current);
                    DISTANCE.put(child, tentativeWeight);
                }
            }
        }
        
        return Collections.<Integer>emptyList();
    }
     
    private List<Integer> tracebackPath(final Integer target, 
                                        final Map<Integer, Integer> PARENTS) {
        List<Integer> nodeList = new ArrayList<>();
        Integer current = target;
        
        while (current != null) {
            nodeList.add(current);
            current = PARENTS.get(current);
        }
        
        Collections.<Integer>reverse(nodeList);
        return nodeList;
    }
}
