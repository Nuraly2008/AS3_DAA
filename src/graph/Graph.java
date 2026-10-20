
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {
    private final int vertices;
    private final boolean directed;
    private final List<List<Edge>> adjacencyList;
    private final List<Edge> edges;

    public Graph(int vertices, boolean directed) {
        if (vertices <= 0) {
            throw new IllegalArgumentException(
                    "Number of vertices must be positive"
            );
        }

        this.vertices = vertices;
        this.directed = directed;
        this.adjacencyList = new ArrayList<>();
        this.edges = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, int weight) {
        validateVertex(from);
        validateVertex(to);

        Edge edge = new Edge(from, to, weight);

        adjacencyList.get(from).add(edge);
        edges.add(edge);

        if (!directed) {
            adjacencyList.get(to).add(
                    new Edge(to, from, weight)
            );
        }
    }

    public List<Edge> getNeighbors(int vertex) {
        validateVertex(vertex);
        return Collections.unmodifiableList(
                adjacencyList.get(vertex)
        );
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public int getVertices() {
        return vertices;
    }

    public boolean isDirected() {
        return directed;
    }

    public int getEdgeCount() {
        return edges.size();
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= vertices) {
            throw new IllegalArgumentException(
                    "Invalid vertex: " + vertex
            );
        }
    }

    public void printGraph() {
        for (int i = 0; i < vertices; i++) {
            System.out.println(
                    "District " + i + ": " +
                            adjacencyList.get(i)
            );
        }
    }
}

