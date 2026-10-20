
package experiments;

import graph.Graph;
import mst.*;
import connectivity.*;
import shortestpath.Dijkstra;

import java.util.Locale;

public class Benchmark {

    private static final int[] V = {10, 100, 1000};
    private static final int[] E = {30, 500, 5000};
    private static final int REPEATS = 5;

    private interface Experiment {
        long run();
    }

    private static void measure(String name, int v, int e,
                                Experiment experiment) {
        long totalTime = 0;
        long totalOperations = 0;

        experiment.run();

        for (int i = 0; i < REPEATS; i++) {
            long start = System.nanoTime();
            long operations = experiment.run();
            long end = System.nanoTime();

            totalTime += end - start;
            totalOperations += operations;
        }

        System.out.printf(Locale.US,
                "%s,%d,%d,%.6f,%.1f%n",
                name, v, e,
                totalTime / (REPEATS * 1_000_000.0),
                totalOperations / (double) REPEATS);
    }

    public static void main(String[] args) {
        System.out.println("Algorithm,Vertices,Edges,AvgTimeMs,AvgOperations");

        for (int i = 0; i < V.length; i++) {
            int v = V[i];
            int e = E[i];

            Graph undirected = GraphGenerator.undirected(v, e);
            Graph dag = GraphGenerator.directed(v, e, true);
            Graph directed = GraphGenerator.directed(v, e, false);

            measure("Kruskal", v, e, () -> {
                MSTResult r = KruskalMST.findMST(undirected);
                return r.getEdgeChecks() + r.getOperations();
            });

            measure("Prim", v, e, () -> {
                MSTResult r = PrimMST.findMST(undirected);
                return r.getEdgeChecks() + r.getOperations();
            });

            measure("Topological_DAG", v, e, () ->
                    TopologicalSort.sort(dag).getEdgeChecks());

            measure("Topological_Cyclic", v, e, () ->
                    TopologicalSort.sort(directed).getEdgeChecks());

            measure("Kosaraju", v, e, () ->
                    KosarajuSCC.findSCC(directed).getEdgeChecks());

            measure("Dijkstra", v, e, () -> {
                Dijkstra.Result r = Dijkstra.shortestPaths(directed, 0);
                return r.getEdgeChecks() + r.getQueueOperations();
            });
        }
    }
}

