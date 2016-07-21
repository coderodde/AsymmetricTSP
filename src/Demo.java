
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import net.coderodde.graph.DirectedGraph;
import net.coderodde.graph.tsp.AsymmetricTSPSolver;
import static net.coderodde.graph.tsp.AsymmetricTSPSolver.getTourCost;
import net.coderodde.graph.tsp.TSPGraphPreprocessor;
import net.coderodde.graph.tsp.support.DefaultAsymmetricTSPSolver;
import net.coderodde.graph.tsp.support.IterativeAsymmetricTSPSolver;

public class Demo {
    
    private static final int DEMO_NODES = 10;
    private static final int DEMO_ARCS  = 45;
    private static final int WARMUP_ITERATIONS = 50;
    private static final int WARMUP_NODES = 8;
    private static final int WARMUP_ARCS = 35;
    
    public static void main(final String... args) {
        final long seed = System.nanoTime();
        final Random random = new Random(seed);
        System.out.println("Seed = " + seed);
        warmup(random);
        benchmark(random);
    }
    
    private static void warmup(final Random random) {
        System.out.println("[STATUS] Warming up...");
        final AsymmetricTSPSolver solver1 = new DefaultAsymmetricTSPSolver();
        final AsymmetricTSPSolver solver2 = new IterativeAsymmetricTSPSolver();
        
        final DirectedGraph graph = 
                new TSPGraphPreprocessor()
                    .preprocessGraph(
                            createRandomGraph(WARMUP_NODES,
                                              WARMUP_ARCS,
                                              10.0,
                                              random));
        
        for (int i = 0; i < WARMUP_ITERATIONS; ++i) {
            solver1.solve(graph);
            solver2.solve(graph);
        }
        
        System.out.println("[STATUS] Warming up done!");
    }
    
    private static void benchmark(final Random random) {
        final DirectedGraph graph = 
                new TSPGraphPreprocessor()
                    .preprocessGraph(
                            createRandomGraph(DEMO_NODES,
                                              DEMO_ARCS,
                                              10.0,
                                              random));
        final List<Integer> tour1 = 
                benchmark(graph, new DefaultAsymmetricTSPSolver());
        
        final List<Integer> tour2 =
                benchmark(graph, new IterativeAsymmetricTSPSolver());
        
        
    }
    
    private static List<Integer> benchmark(final DirectedGraph graph,
                                  final AsymmetricTSPSolver solver) {
        System.out.println();
        System.out.println("[BENCHMARK] Profiling " + 
                solver.getClass().getSimpleName() + "...");
        
        final long startTime = System.nanoTime();
        final List<Integer> tour = solver.solve(graph);
        final long endTime = System.nanoTime();
        
        System.out.printf("[BENCHMARK] " + solver.getClass().getSimpleName() + 
                           " took %.1f milliseconds.\n",
                          (endTime - startTime) / 1e6);
        
        System.out.println("[BENCHMARK] " + solver.getClass().getSimpleName() +
                           " returned the tour: " + tour + " which has cost " +
                           getTourCost(graph, tour));
        return tour;
    }
    
    private static DirectedGraph createRandomGraph(final int nodes, 
                                                   final int arcs,
                                                   final double maxArcWeight,
                                                   final Random random) {
        final DirectedGraph graph = new DirectedGraph();
        
        IntStream.range(0, nodes).forEach(graph::addNode);
        
        // All possible arcs minus self-loops:
        final int maximumFeasibleArcs = nodes * nodes - nodes;
        final int numberOfArcs = Math.min(arcs, maximumFeasibleArcs);
        final List<Point> arcDescriptorList = new ArrayList<>(numberOfArcs);
        final List<Integer> nodeList = new ArrayList<>(graph.getAllNodes());
        
        for (int i = 0; i < nodeList.size(); ++i) {
            for (int j = 0; j < nodeList.size(); ++j) {
                if (i == j) {
                    // Omit a self loop.
                    continue;
                }
                
                arcDescriptorList.add(new Point(i, j));
            }
        }
        
        Collections.<Point>shuffle(arcDescriptorList, random);
        
        for (int i = 0; i < numberOfArcs; ++i) {
            final Point p = arcDescriptorList.get(i);
            
            graph.addEdge(p.x, p.y, maxArcWeight * random.nextDouble());
        }
        
        return graph;
    }
}
