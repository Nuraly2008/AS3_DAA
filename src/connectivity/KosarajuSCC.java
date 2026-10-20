
package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KosarajuSCC {

    private static long edgeChecks;

    public static class Result {
        private final List<List<Integer>> components;
        private final long edgeChecks;

        public Result(List<List<Integer>> components,
                      long edgeChecks) {
            this.components = components;
            this.edgeChecks = edgeChecks;
        }

        public List<List<Integer>> getComponents() {
            List<List<Integer>> copy = new ArrayList<>();

            for (List<Integer> component : components) {
                copy.add(new ArrayList<>(component));
            }

            return copy;
        }

        public int getComponentCount() {
            return components.size();
        }

        public long getEdgeChecks() {
            return edgeChecks;
        }

        public void printResult() {
            System.out.println("\n=== KOSARAJU SCC ===");
            System.out.println(
                    "Total SCCs: " + components.size()
            );

            for (int i = 0; i < components.size(); i++) {
                System.out.println(
                        "Component " + (i + 1) + ": " +
                                components.get(i)
                );
            }

            System.out.println("Edge checks: " + edgeChecks);
        }
    }

    public static Result findSCC(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kosaraju requires a directed graph"
            );
        }

        edgeChecks = 0;

        int n = graph.getVertices();
        boolean[] visited = new boolean[n];

        List<Integer> order = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs1(graph, i, visited, order);
            }
        }

        List<List<Integer>> reversed = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            reversed.add(new ArrayList<>());
        }

        for (Edge edge : graph.getEdges()) {
            reversed.get(edge.getTo()).add(edge.getFrom());
            edgeChecks++;
        }

        visited = new boolean[n];

        Collections.reverse(order);

        List<List<Integer>> components = new ArrayList<>();

        for (int vertex : order) {
            if (!visited[vertex]) {
                List<Integer> component = new ArrayList<>();

                dfs2(vertex, reversed, visited, component);

                Collections.sort(component);
                components.add(component);
            }
        }

        components.sort(
                Comparator.comparingInt(component -> component.get(0))
        );

        return new Result(components, edgeChecks);
    }

    private static void dfs1(Graph graph,
                             int vertex,
                             boolean[] visited,
                             List<Integer> order) {

        visited[vertex] = true;

        for (Edge edge : graph.getNeighbors(vertex)) {
            edgeChecks++;

            int next = edge.getTo();

            if (!visited[next]) {
                dfs1(graph, next, visited, order);
            }
        }

        order.add(vertex);
    }

    private static void dfs2(int vertex,
                             List<List<Integer>> reversed,
                             boolean[] visited,
                             List<Integer> component) {

        visited[vertex] = true;
        component.add(vertex);

        for (int next : reversed.get(vertex)) {
            edgeChecks++;

            if (!visited[next]) {
                dfs2(next, reversed, visited, component);
            }
        }
    }
}

