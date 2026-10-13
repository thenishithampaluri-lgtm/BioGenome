package biogenome.m4;

import java.util.*;

/**
 * M4 - Network Flow (CO4)
 * Edmonds-Karp Algorithm for Biological-Sample Distribution.
 * Time Complexity: O(V * E^2) strongly polynomial time.
 * BFS shortest augmenting paths.
 */
public class EdmondsKarpFlow {
    private final int n;
    private final List<String> nodeNames;
    private final int[][] capacity;
    private final List<List<Integer>> adj;

    public EdmondsKarpFlow(int numNodes, List<String> nodeNames) {
        this.n = numNodes;
        this.nodeNames = (nodeNames != null) ? new ArrayList<>(nodeNames) : new ArrayList<>();
        if (this.nodeNames.isEmpty()) {
            for (int i = 0; i < numNodes; i++) this.nodeNames.add("Site_" + i);
        }
        this.capacity = new int[numNodes][numNodes];
        this.adj = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int cap) {
        capacity[u][v] += cap;
        adj.get(u).add(v);
        adj.get(v).add(u);
    }

    public Map<String, Object> computeMaxFlow(int source, int sink) {
        long t0 = System.nanoTime();
        int[][] flowMatrix = new int[n][n];
        int totalFlow = 0;
        int augmentingPaths = 0;

        while (true) {
            int[] parent = new int[n];
            Arrays.fill(parent, -1);
            parent[source] = source;

            Queue<int[]> queue = new ArrayDeque<>();
            queue.add(new int[]{source, Integer.MAX_VALUE});
            int pushed = 0;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int u = curr[0];
                int flow = curr[1];

                if (u == sink) {
                    pushed = flow;
                    break;
                }

                for (int v : adj.get(u)) {
                    int residual = capacity[u][v] - flowMatrix[u][v];
                    if (parent[v] == -1 && residual > 0) {
                        parent[v] = u;
                        queue.add(new int[]{v, Math.min(flow, residual)});
                    }
                }
            }

            if (pushed == 0) break;
            totalFlow += pushed;
            augmentingPaths++;

            int curr = sink;
            while (curr != source) {
                int prev = parent[curr];
                flowMatrix[prev][curr] += pushed;
                flowMatrix[curr][prev] -= pushed;
                curr = prev;
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Edmonds-Karp (BFS Shortest Augmenting Path)");
        result.put("maxFlow", totalFlow);
        result.put("source", nodeNames.get(source));
        result.put("sink", nodeNames.get(sink));
        result.put("augmentingPaths", augmentingPaths);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(V * E^2) strongly polynomial");
        return result;
    }
}
