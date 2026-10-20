package mst;
import dsu.DSU;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KruskalMST {

    public static MSTResult findMST(Graph graph) {
        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kruskal requires an undirected graph"
            );
        }

        int vertices = graph.getVertices();

        List<Edge> edges = new ArrayList<>(graph.getEdges());
        edges.sort(
                Comparator.comparingInt(Edge::getWeight)
                        .thenComparingInt(Edge::getFrom)
                        .thenComparingInt(Edge::getTo)
        );

        DSU dsu = new DSU(vertices);
        List<Edge> mstEdges = new ArrayList<>();

        long totalWeight = 0;
        long edgeChecks = 0;

        for (Edge edge : edges) {
            edgeChecks++;

            if (dsu.union(edge.getFrom(), edge.getTo())) {
                mstEdges.add(edge);
                totalWeight += edge.getWeight();
            }

            if (mstEdges.size() == vertices - 1) {
                break;
            }
        }

        long operations = dsu.getFindCalls()
                + dsu.getUnionCalls();

        return new MSTResult(
                mstEdges,
                totalWeight,
                dsu.getComponents(),
                edgeChecks,
                operations
        );
    }
}

