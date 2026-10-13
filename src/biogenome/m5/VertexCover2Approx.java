package biogenome.m5;

import java.util.*;

/**
 * M5 - NP-Completeness and Approximation (CO5)
 * Vertex-Cover 2-Approximation Algorithm for Biological Network Coverage.
 * Time Complexity: O(V + E) linear time.
 * Approximation Guarantee: <= 2 * OPT.
 */
public class VertexCover2Approx {
    private final int n;
    private final List<int[]> edges;
    private final List<String> nodeNames;

    public VertexCover2Approx(int numNodes, List<int[]> edges, List<String> nodeNames) {
        this.n = numNodes;
        this.edges = new ArrayList<>(edges);
        this.nodeNames = (nodeNames != null) ? new ArrayList<>(nodeNames) : new ArrayList<>();
        if (this.nodeNames.isEmpty()) {
            for (int i = 0; i < numNodes; i++) this.nodeNames.add("Protein_" + i);
        }
    }

    public Map<String, Object> computeApproximateCover() {
        long t0 = System.nanoTime();
        List<int[]> uncovered = new ArrayList<>(edges);
        Set<Integer> coverSet = new TreeSet<>();
        List<int[]> selectedMatching = new ArrayList<>();

        while (!uncovered.isEmpty()) {
            int[] edge = uncovered.remove(0);
            int u = edge[0];
            int v = edge[1];

            if (coverSet.contains(u) || coverSet.contains(v)) {
                continue;
            }

            coverSet.add(u);
            coverSet.add(v);
            selectedMatching.add(new int[]{u, v});

            // Filter out edges incident to u or v
            uncovered.removeIf(e -> e[0] == u || e[0] == v || e[1] == u || e[1] == v);
        }

        List<String> probeNodes = new ArrayList<>();
        for (int idx : coverSet) {
            probeNodes.add(nodeNames.get(idx));
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Vertex-Cover 2-Approximation (Maximal Matching)");
        result.put("totalProteins", n);
        result.put("totalEdges", edges.size());
        result.put("coverSize", coverSet.size());
        result.put("matchingEdgesCount", selectedMatching.size());
        result.put("upperBoundGuarantee", 2 * selectedMatching.size());
        result.put("selectedProbeNodes", probeNodes);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(V + E) time, <= 2 * OPT guarantee");
        return result;
    }
}
