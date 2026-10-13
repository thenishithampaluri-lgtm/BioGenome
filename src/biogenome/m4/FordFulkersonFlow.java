package biogenome.m4;

import java.util.*;

/**
 * M4 - Network Flow (CO4)
 * Ford-Fulkerson Algorithm for Laboratory-Resource Flow.
 * Time Complexity: O(E * max_flow) using DFS augmenting paths.
 */
public class FordFulkersonFlow {
    private final int n;
    private final List<String> nodeNames;
    private final int[][] capacity;
    private final List<List<Integer>> adj;

    public FordFulkersonFlow(int numNodes, List<String> nodeNames) {
        this.n = numNodes;
        this.nodeNames = (nodeNames != null) ? new ArrayList<>(nodeNames) : new ArrayList<>();
        if (this.nodeNames.isEmpty()) {
            for (int i = 0; i < numNodes; i++) this.nodeNames.add("Node_" + i);
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

    private int dfsAugment(int u, int sink, int minCap, boolean[] visited, int[][] flowMatrix) {
        if (u == sink) return minCap;
        visited[u] = true;

        for (int v : adj.get(u)) {
            int residual = capacity[u][v] - flowMatrix[u][v];
            if (!visited[v] && residual > 0) {
                int pushed = dfsAugment(v, sink, Math.min(minCap, residual), visited, flowMatrix);
                if (pushed > 0) {
                    flowMatrix[u][v] += pushed;
                    flowMatrix[v][u] -= pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }

    public Map<String, Object> computeMaxFlow(int source, int sink) {
        long t0 = System.nanoTime();
        int[][] flowMatrix = new int[n][n];
        int totalFlow = 0;
        int iterations = 0;

        while (true) {
            boolean[] visited = new boolean[n];
            int pushed = dfsAugment(source, sink, Integer.MAX_VALUE, visited, flowMatrix);
            if (pushed == 0) break;
            totalFlow += pushed;
            iterations++;
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Ford-Fulkerson (DFS Residual Flow)");
        result.put("maxFlow", totalFlow);
        result.put("source", nodeNames.get(source));
        result.put("sink", nodeNames.get(sink));
        result.put("augmentingPaths", iterations);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(E * |max_flow|) time");
        return result;
    }
}
