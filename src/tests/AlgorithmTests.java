
package tests;

import graph.*;
import dsu.DSU;
import mst.*;
import connectivity.*;
import shortestpath.Dijkstra;

import java.util.*;

public class AlgorithmTests {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== AUTOMATED TESTS ===");

        run("DSU", AlgorithmTests::testDSU);
        run("Small Graph and MST", AlgorithmTests::testMST);
        run("Disconnected Graph", AlgorithmTests::testDisconnected);
        run("Multiple MSTs and Duplicate Weights", AlgorithmTests::testEqualWeights);
        run("Topological Sort and Cycles", AlgorithmTests::testTopological);
        run("Strongly Connected Components", AlgorithmTests::testSCC);
        run("Dijkstra and Unreachable Vertices", AlgorithmTests::testDijkstra);
        run("Single Vertex", AlgorithmTests::testSingleVertex);

        System.out.println("\nPassed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Tests failed");
        }

        System.out.println("ALL TESTS PASSED!");
    }

    private static void run(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("[PASS] " + name);
        } catch (Exception | AssertionError e) {
            failed++;
            System.out.println("[FAIL] " + name + ": " + e.getMessage());
        }
    }

    private static void check(boolean condition) {
        if (!condition) throw new AssertionError("Incorrect result");
    }

    private static void testDSU() {
        DSU dsu = new DSU(4);
        dsu.union(0, 1);
        dsu.union(1, 2);

        check(dsu.connected(0, 2));
        check(!dsu.connected(0, 3));
        check(dsu.getComponents() == 2);
    }

    private static void testMST() {
        Graph g = new Graph(4, false);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(2, 3, 3);
        g.addEdge(0, 3, 10);

        MSTResult k = KruskalMST.findMST(g);
        MSTResult p = PrimMST.findMST(g);

        check(k.getTotalWeight() == 6);
        check(p.getTotalWeight() == 6);
        check(k.isConnected() && p.isConnected());
        check(k.getEdges().size() == 3);
    }

    private static void testDisconnected() {
        Graph g = new Graph(4, false);
        g.addEdge(0, 1, 2);
        g.addEdge(2, 3, 3);

        MSTResult k = KruskalMST.findMST(g);
        MSTResult p = PrimMST.findMST(g);

        check(!k.isConnected() && !p.isConnected());
        check(k.getComponents() == 2);
        check(p.getComponents() == 2);
        check(k.getTotalWeight() == p.getTotalWeight());
    }

    private static void testEqualWeights() {
        Graph g = new Graph(4, false);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 0, 1);

        check(KruskalMST.findMST(g).getTotalWeight() == 3);
        check(PrimMST.findMST(g).getTotalWeight() == 3);
    }

    private static void testTopological() {
        Graph dag = new Graph(3, true);
        dag.addEdge(0, 1, 1);
        dag.addEdge(1, 2, 1);

        TopologicalSort.Result result = TopologicalSort.sort(dag);
        check(!result.hasCycle());
        check(result.getOrder().equals(Arrays.asList(0, 1, 2)));

        dag.addEdge(2, 0, 1);
        check(TopologicalSort.sort(dag).hasCycle());
    }

    private static void testSCC() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 2, 1);

        List<List<Integer>> components =
                KosarajuSCC.findSCC(g).getComponents();

        check(components.size() == 2);
        check(components.contains(Arrays.asList(0, 1)));
        check(components.contains(Arrays.asList(2, 3)));
    }

    private static void testDijkstra() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 5);
        g.addEdge(0, 2, 2);
        g.addEdge(2, 1, 1);

        Dijkstra.Result result = Dijkstra.shortestPaths(g, 0);

        check(result.getDistance(1) == 3);
        check(result.getPath(1).equals(Arrays.asList(0, 2, 1)));
        check(!result.isReachable(3));

        Graph negative = new Graph(2, true);
        negative.addEdge(0, 1, -1);

        boolean rejected = false;
        try {
            Dijkstra.shortestPaths(negative, 0);
        } catch (IllegalArgumentException e) {
            rejected = true;
        }
        check(rejected);
    }

    private static void testSingleVertex() {
        Graph g = new Graph(1, false);
        check(KruskalMST.findMST(g).getTotalWeight() == 0);
        check(PrimMST.findMST(g).isConnected());

        Graph directed = new Graph(1, true);
        check(!TopologicalSort.sort(directed).hasCycle());
        check(KosarajuSCC.findSCC(directed).getComponentCount() == 1);
        check(Dijkstra.shortestPaths(directed, 0).getDistance(0) == 0);
    }
}

