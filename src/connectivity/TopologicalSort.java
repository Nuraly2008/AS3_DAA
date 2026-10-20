
package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TopologicalSort {

    public static class Result {
        private final List<Integer> order;
        private final boolean hasCycle;
        private final long edgeChecks;

        public Result(List<Integer> order,
                      boolean hasCycle,
                      long edgeChecks) {
            this.order = order;
            this.hasCycle = hasCycle;
            this.edgeChecks = edgeChecks;
        }

        public List<Integer> getOrder() {
            return new ArrayList<>(order);
        }

        public boolean hasCycle() {
            return hasCycle;
        }

        public long getEdgeChecks() {
            return edgeChecks;
        }

        public void printResult() {
            System.out.println("\n=== TOPOLOGICAL SORT ===");

            if (hasCycle) {
                System.out.println(
                        "Cycle detected! Topological sorting is impossible."
                );
            } else {
                System.out.println("Topological order: " + order);
            }

            System.out.println("Edge checks: " + edgeChecks);
        }
    }

    public static Result sort(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Topological sort requires a directed graph"
            );
        }

        int n = graph.getVertices();
        int[] inDegree = new int[n];

        long edgeChecks = 0;

        for (Edge edge : graph.getEdges()) {
            inDegree[edge.getTo()]++;
            edgeChecks++;
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>();

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> order = new ArrayList<>();

        while (!queue.isEmpty()) {
            int current = queue.poll();
            order.add(current);

            for (Edge edge : graph.getNeighbors(current)) {
                edgeChecks++;

                int next = edge.getTo();
                inDegree[next]--;

                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        boolean hasCycle = order.size() != n;

        if (hasCycle) {
            order.clear();
        }

        return new Result(order, hasCycle, edgeChecks);
    }
}
