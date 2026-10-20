
package shortestpath;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {

    private static class Node {
        int vertex;
        long distance;

        Node(int vertex, long distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    public static class Result {
        private final long[] distances;
        private final int[] previous;
        private final int source;
        private final long edgeChecks;
        private final long relaxations;
        private final long queueOperations;

        public Result(long[] distances, int[] previous,
                      int source, long edgeChecks,
                      long relaxations, long queueOperations) {
            this.distances = distances.clone();
            this.previous = previous.clone();
            this.source = source;
            this.edgeChecks = edgeChecks;
            this.relaxations = relaxations;
            this.queueOperations = queueOperations;
        }

        public long getDistance(int vertex) {
            return distances[vertex];
        }

        public long[] getDistances() {
            return distances.clone();
        }

        public boolean isReachable(int vertex) {
            return distances[vertex] != Long.MAX_VALUE;
        }

        public List<Integer> getPath(int target) {
            if (!isReachable(target)) {
                return Collections.emptyList();
            }

            List<Integer> path = new ArrayList<>();

            for (int at = target; at != -1; at = previous[at]) {
                path.add(at);
            }

            Collections.reverse(path);
            return path;
        }

        public long getEdgeChecks() {
            return edgeChecks;
        }

        public long getRelaxations() {
            return relaxations;
        }

        public long getQueueOperations() {
            return queueOperations;
        }

        public void printResult() {
            System.out.println("\n=== DIJKSTRA SHORTEST PATHS ===");
            System.out.println("Source district: " + source);

            for (int i = 0; i < distances.length; i++) {
                System.out.println("\nDistrict " + i + ":");

                if (!isReachable(i)) {
                    System.out.println("Distance: UNREACHABLE");
                    System.out.println("Path: none");
                } else {
                    System.out.println("Distance: " + distances[i]);
                    System.out.println("Path: " + getPath(i));
                }
            }

            System.out.println("\nPerformance Statistics:");
            System.out.println("Edge checks: " + edgeChecks);
            System.out.println("Relaxations: " + relaxations);
            System.out.println("Queue operations: " + queueOperations);
        }
    }

    public static Result shortestPaths(Graph graph, int source) {
        int n = graph.getVertices();

        if (source < 0 || source >= n) {
            throw new IllegalArgumentException(
                    "Invalid source vertex"
            );
        }

        for (Edge edge : graph.getEdges()) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException(
                        "Dijkstra does not support negative weights"
                );
            }
        }

        long[] distances = new long[n];
        int[] previous = new int[n];

        Arrays.fill(distances, Long.MAX_VALUE);
        Arrays.fill(previous, -1);

        distances[source] = 0;

        PriorityQueue<Node> queue = new PriorityQueue<>(
                Comparator.comparingLong((Node node) -> node.distance)
                        .thenComparingInt(node -> node.vertex)
        );

        queue.offer(new Node(source, 0));

        long edgeChecks = 0;
        long relaxations = 0;
        long queueOperations = 1;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            queueOperations++;

            int u = current.vertex;

            if (current.distance != distances[u]) {
                continue;
            }

            for (Edge edge : graph.getNeighbors(u)) {
                edgeChecks++;

                int v = edge.getTo();
                long newDistance = distances[u] + edge.getWeight();

                if (newDistance < distances[v]) {
                    distances[v] = newDistance;
                    previous[v] = u;
                    relaxations++;

                    queue.offer(new Node(v, newDistance));
                    queueOperations++;
                }
            }
        }

        return new Result(
                distances,
                previous,
                source,
                edgeChecks,
                relaxations,
                queueOperations
        );
    }
}

