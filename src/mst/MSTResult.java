
package mst;

import graph.Edge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MSTResult {
    private final List<Edge> edges;
    private final long totalWeight;
    private final int components;
    private final long edgeChecks;
    private final long operations;

    public MSTResult(List<Edge> edges, long totalWeight,
                     int components, long edgeChecks,
                     long operations) {
        this.edges = new ArrayList<>(edges);
        this.totalWeight = totalWeight;
        this.components = components;
        this.edgeChecks = edgeChecks;
        this.operations = operations;
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public long getTotalWeight() {
        return totalWeight;
    }

    public int getComponents() {
        return components;
    }

    public boolean isConnected() {
        return components == 1;
    }

    public long getEdgeChecks() {
        return edgeChecks;
    }

    public long getOperations() {
        return operations;
    }

    public void printResult(String algorithm) {
        System.out.println("\n=== " + algorithm + " ===");

        for (Edge edge : edges) {
            System.out.println(edge);
        }

        System.out.println("Total MST weight: " + totalWeight);
        System.out.println("Connected: " + isConnected());
        System.out.println("Components: " + components);
        System.out.println("Edge checks: " + edgeChecks);
        System.out.println("Data structure operations: " + operations);
    }
}

