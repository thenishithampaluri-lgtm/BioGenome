package biogenome.m4;

import java.util.*;

/**
 * M4 - Network Flow (CO4)
 * Dinic's Algorithm for High-Throughput Genomic Resource Networks.
 * Time Complexity: O(V^2 * E) general, O(E * sqrt(V)) unit networks.
 * Uses BFS Level Graphs and DFS Blocking Flows with current-arc pointer.
 */
public class DinicFlow {

    static class Edge {
        int to;
        int rev;
        int cap;
        int flow;

        Edge(int to, int rev, int cap) {
            this.to = to;
            this.rev = rev;
            this.cap = cap;
            this.flow = 0;
        }
    }

    private final int n;
    private final List<String> nodeNames;
    private final List<List<Edge>> graph;
    private final int[] level;
    private final int[] ptr;

    public DinicFlow(int numNodes, List<String> nodeNames) {
        this.n = numNodes;
        this.nodeNames = (nodeNames != null) ? new ArrayList<>(nodeNames) : new ArrayList<>();
        if (this.nodeNames.isEmpty()) {
            for (int i = 0; i < numNodes; i++) this.nodeNames.add("Unit_" + i);
        }
        this.graph = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) graph.add(new ArrayList<>());
        this.level = new int[numNodes];
        this.ptr = new int[numNodes];
    }

    public void addEdge(int u, int v, int cap) {
        Edge forward = new Edge(v, graph.get(v).size(), cap);
        Edge backward = new Edge(u, graph.get(u).size(), 0);
        graph.get(u).add(forward);
        graph.get(v).add(backward);
    }

    private boolean bfsLevelGraph(int source, int sink) {
        Arrays.fill(level, -1);
        level[source] = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (Edge edge : graph.get(u)) {
                if (edge.cap - edge.flow > 0 && level[edge.to] == -1) {
                    level[edge.to] = level[u] + 1;
                    queue.add(edge.to);
                }
            }
        }
        return level[sink] != -1;
    }

    private int dfsBlockingFlow(int u, int sink, int pushed) {
        if (pushed == 0 || u == sink) return pushed;

        for (int cid = ptr[u]; cid < graph.get(u).size(); cid++) {
            ptr[u] = cid;
            Edge edge = graph.get(u).get(cid);
            int tr = edge.to;

            if (level[u] + 1 != level[tr] || edge.cap - edge.flow <= 0) {
                continue;
            }

            int pushable = Math.min(pushed, edge.cap - edge.flow);
            int flow = dfsBlockingFlow(tr, sink, pushable);
            if (flow == 0) continue;

            edge.flow += flow;
            graph.get(tr).get(edge.rev).flow -= flow;
            return flow;
        }
        return 0;
    }

    public Map<String, Object> computeMaxFlow(int source, int sink) {
        long t0 = System.nanoTime();
        int totalFlow = 0;
        int phases = 0;

        while (bfsLevelGraph(source, sink)) {
            Arrays.fill(ptr, 0);
            phases++;
            while (true) {
                int pushed = dfsBlockingFlow(source, sink, Integer.MAX_VALUE);
                if (pushed == 0) break;
                totalFlow += pushed;
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Dinic's Algorithm (Level Graph & Blocking Flow)");
        result.put("maxFlow", totalFlow);
        result.put("source", nodeNames.get(source));
        result.put("sink", nodeNames.get(sink));
        result.put("levelPhases", phases);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(V^2 * E) time, O(V + E) space");
        return result;
    }
}
