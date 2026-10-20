
package dsu;

public class DSU {
    private final int[] parent;
    private final int[] size;

    private int components;
    private long findCalls;
    private long unionCalls;
    private long parentTraversals;

    public DSU(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    "Size must be positive"
            );
        }

        parent = new int[n];
        size = new int[n];
        components = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        findCalls++;

        int root = x;

        while (parent[root] != root) {
            root = parent[root];
            parentTraversals++;
        }

        while (parent[x] != x) {
            int next = parent[x];
            parent[x] = root;
            x = next;
            parentTraversals++;
        }

        return root;
    }

    public boolean union(int a, int b) {
        unionCalls++;

        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) {
            return false;
        }

        if (size[rootA] < size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }

        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        components--;

        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    public int getComponents() {
        return components;
    }

    public long getFindCalls() {
        return findCalls;
    }

    public long getUnionCalls() {
        return unionCalls;
    }

    public long getParentTraversals() {
        return parentTraversals;
    }
}
