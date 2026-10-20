
package mst;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PrimMST {

    public static MSTResult findMST(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Prim requires an undirected graph"
            );
        }

        int vertices = graph.getVertices();
        boolean[] visited = new boolean[vertices];

        PriorityQueue<Edge> queue = new PriorityQueue<>(
                Comparator.comparingInt(Edge::getWeight)
                        .thenComparingInt(Edge::getFrom)
                        .thenComparingInt(Edge::getTo)
        );

        List<Edge> mstEdges = new ArrayList<>();

        long totalWeight = 0;
        long edgeChecks = 0;
        long operations = 0;
        int components = 0;

        for (int start = 0; start < vertices; start++) {
            if (visited[start]) {
                continue;
            }

            components++;
            visited[start] = true;

            for (Edge edge : graph.getNeighbors(start)) {
                edgeChecks++;
                queue.offer(edge);
                operations++;
            }

            while (!queue.isEmpty()) {
                Edge current = queue.poll();
                operations++;

                int next = current.getTo();

                if (visited[next]) {
                    continue;
                }

                visited[next] = true;
                mstEdges.add(current);
                totalWeight += current.getWeight();

                for (Edge edge : graph.getNeighbors(next)) {
                    edgeChecks++;

                    if (!visited[edge.getTo()]) {
                        queue.offer(edge);
                        operations++;
                    }
                }
            }
        }

        return new MSTResult(
                mstEdges,
                totalWeight,
                components,
                edgeChecks,
                operations
        );
    }
}

