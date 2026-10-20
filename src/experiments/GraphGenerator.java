
package experiments;

import graph.Graph;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GraphGenerator {

    private static final long SEED = 42L;

    public static Graph undirected(int vertices, int edges) {
        if (vertices < 1 || edges < vertices - 1 ||
                edges > (long) vertices * (vertices - 1) / 2) {
            throw new IllegalArgumentException("Invalid graph size");
        }

        Graph graph = new Graph(vertices, false);
        Random random = new Random(SEED + vertices);
        Set<Long> used = new HashSet<>();

        for (int i = 1; i < vertices; i++) {
            int parent = random.nextInt(i);
            addUndirected(graph, used, parent, i,
                    random.nextInt(100) + 1, vertices);
        }

        while (graph.getEdgeCount() < edges) {
            int a = random.nextInt(vertices);
            int b = random.nextInt(vertices);

            if (a != b) {
                addUndirected(graph, used, a, b,
                        random.nextInt(100) + 1, vertices);
            }
        }

        return graph;
    }

    private static void addUndirected(Graph graph, Set<Long> used,
                                      int a, int b, int weight, int n) {
        int x = Math.min(a, b);
        int y = Math.max(a, b);
        long key = (long) x * n + y;

        if (used.add(key)) {
            graph.addEdge(a, b, weight);
        }
    }


    public static Graph directed(int vertices, int edges, boolean dag) {
        long max = dag
                ? (long) vertices * (vertices - 1) / 2
                : (long) vertices * (vertices - 1);

        if (vertices < 1 || edges < 0 || edges > max) {
            throw new IllegalArgumentException("Invalid graph size");
        }

        if (!dag && vertices >= 3 && edges < 3) {
            throw new IllegalArgumentException(
                    "At least 3 edges are required for a cycle"
            );
        }

        Graph graph = new Graph(vertices, true);
        Random random = new Random(SEED + vertices + (dag ? 1000 : 2000));
        Set<Long> used = new HashSet<>();

        if (!dag && vertices >= 3) {
            int[][] cycle = {{0, 1}, {1, 2}, {2, 0}};

            for (int[] edge : cycle) {
                int a = edge[0];
                int b = edge[1];
                graph.addEdge(a, b, random.nextInt(100) + 1);
                used.add((long) a * vertices + b);
            }
        }

        while (graph.getEdgeCount() < edges) {
            int a = random.nextInt(vertices);
            int b = random.nextInt(vertices);

            if (a == b) continue;

            if (dag && a > b) {
                int temp = a;
                a = b;
                b = temp;
            }

            long key = (long) a * vertices + b;

            if (used.add(key)) {
                graph.addEdge(a, b, random.nextInt(100) + 1);
            }
        }

        return graph;
    }
}
