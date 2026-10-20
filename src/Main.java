
import graph.Graph;
import mst.KruskalMST;
import mst.PrimMST;
import mst.MSTResult;
import connectivity.TopologicalSort;
import connectivity.KosarajuSCC;
import shortestpath.Dijkstra;

public class Main {

    public static void main(String[] args) {

        System.out.println("SMART CITY INFRASTRUCTURE");
        System.out.println("================================");

        System.out.println("\nTASK 1: MINIMUM SPANNING TREE");

        Graph city = new Graph(5, false);

        city.addEdge(0, 1, 4);
        city.addEdge(0, 2, 2);
        city.addEdge(1, 2, 1);
        city.addEdge(1, 3, 5);
        city.addEdge(2, 3, 8);
        city.addEdge(2, 4, 10);
        city.addEdge(3, 4, 2);
        city.addEdge(1, 4, 6);

        System.out.println("\nCity Graph:");
        city.printGraph();

        MSTResult kruskal = KruskalMST.findMST(city);
        MSTResult prim = PrimMST.findMST(city);

        kruskal.printResult("KRUSKAL");
        prim.printResult("PRIM");

        System.out.println("\n=== MST COMPARISON ===");

        System.out.println(
                "Kruskal weight: " + kruskal.getTotalWeight()
        );

        System.out.println(
                "Prim weight: " + prim.getTotalWeight()
        );

        System.out.println(
                "Results match: " +
                        (kruskal.getTotalWeight() == prim.getTotalWeight())
        );

        System.out.println("\n================================");
        System.out.println("TASK 2: CONNECTIVITY ANALYSIS");

        Graph dag = new Graph(6, true);

        dag.addEdge(5, 2, 1);
        dag.addEdge(5, 0, 1);
        dag.addEdge(4, 0, 1);
        dag.addEdge(4, 1, 1);
        dag.addEdge(2, 3, 1);
        dag.addEdge(3, 1, 1);

        System.out.println("\nTesting DAG:");

        TopologicalSort.Result topoResult =
                TopologicalSort.sort(dag);

        topoResult.printResult();

        Graph cyclic = new Graph(3, true);

        cyclic.addEdge(0, 1, 1);
        cyclic.addEdge(1, 2, 1);
        cyclic.addEdge(2, 0, 1);

        System.out.println("\nTesting cyclic graph:");

        TopologicalSort.Result cycleResult =
                TopologicalSort.sort(cyclic);

        cycleResult.printResult();

        Graph directed = new Graph(7, true);

        directed.addEdge(0, 1, 1);
        directed.addEdge(1, 2, 1);
        directed.addEdge(2, 0, 1);
        directed.addEdge(2, 3, 1);
        directed.addEdge(3, 4, 1);
        directed.addEdge(4, 5, 1);
        directed.addEdge(5, 3, 1);
        directed.addEdge(5, 6, 1);

        KosarajuSCC.Result sccResult =
                KosarajuSCC.findSCC(directed);

        sccResult.printResult();

        System.out.println("\n================================");
        System.out.println("TASK 3: SHORTEST PATHS");

        Graph roads = new Graph(7, true);

        roads.addEdge(0, 1, 4);
        roads.addEdge(0, 2, 2);
        roads.addEdge(2, 1, 1);
        roads.addEdge(1, 3, 5);
        roads.addEdge(2, 3, 8);
        roads.addEdge(2, 4, 10);
        roads.addEdge(3, 4, 2);
        roads.addEdge(4, 5, 3);
        roads.addEdge(1, 5, 20);

        System.out.println("\nDirected Road Network:");
        roads.printGraph();

        Dijkstra.Result dijkstraResult =
                Dijkstra.shortestPaths(roads, 0);

        dijkstraResult.printResult();

        System.out.println("\nTesting negative weights:");

        Graph negativeGraph = new Graph(2, true);
        negativeGraph.addEdge(0, 1, -5);

        try {
            Dijkstra.shortestPaths(negativeGraph, 0);
            System.out.println("Negative weight test: FAILED");
        } catch (IllegalArgumentException e) {
            System.out.println("Negative weight test: PASSED");
            System.out.println(e.getMessage());
        }

        System.out.println("\n================================");
        System.out.println("TASK 1, TASK 2 AND TASK 3 COMPLETED");
    }
}
