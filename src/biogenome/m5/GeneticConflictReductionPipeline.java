package biogenome.m5;

import java.util.*;

/**
 * M5 - NP-Completeness and Approximation (CO5)
 * Reduction Chain: CLIQUE -> INDEPENDENT-SET -> VERTEX-COVER.
 * Models genetic conflict structures and mutation compatibility.
 */
public class GeneticConflictReductionPipeline {
    private final int n;
    private final List<int[]> edges;
    private final List<String> nodeLabels;

    public GeneticConflictReductionPipeline(int numNodes, List<int[]> edges, List<String> nodeLabels) {
        this.n = numNodes;
        this.edges = edges;
        this.nodeLabels = (nodeLabels != null) ? nodeLabels : new ArrayList<>();
        if (this.nodeLabels.isEmpty()) {
            for (int i = 0; i < numNodes; i++) this.nodeLabels.add("GeneMut_" + i);
        }
    }

    public Map<String, Object> computeComplementGraph() {
        long t0 = System.nanoTime();
        Set<String> edgeSet = new HashSet<>();
        for (int[] e : edges) {
            int u = Math.min(e[0], e[1]);
            int v = Math.max(e[0], e[1]);
            edgeSet.add(u + "-" + v);
        }

        List<int[]> compEdges = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (!edgeSet.contains(u + "-" + v)) {
                    compEdges.add(new int[]{u, v});
                }
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("transformation", "CLIQUE -> INDEPENDENT SET (Complement Graph)");
        result.put("originalEdgesCount", edges.size());
        result.put("complementEdgesCount", compEdges.size());
        result.put("elapsedMs", elapsedMs);
        result.put("property", "A k-Clique in G corresponds directly to an Independent Set of size k in G_bar.");
        return result;
    }

    public Map<String, Object> reduceIndependentSetToVertexCover(List<Integer> indSet) {
        long t0 = System.nanoTime();
        Set<Integer> indSetNodes = new HashSet<>(indSet);
        List<String> indSetLabels = new ArrayList<>();
        List<String> vertexCoverLabels = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (indSetNodes.contains(i)) {
                indSetLabels.add(nodeLabels.get(i));
            } else {
                vertexCoverLabels.add(nodeLabels.get(i));
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("transformation", "INDEPENDENT SET -> VERTEX COVER");
        result.put("independentSetSizeK", indSet.size());
        result.put("independentSet", indSetLabels);
        result.put("vertexCoverSize", vertexCoverLabels.size());
        result.put("vertexCover", vertexCoverLabels);
        result.put("elapsedMs", elapsedMs);
        result.put("biologicalSignificance", "Independent set = max compatible mutation profile. Vertex Cover = minimum intervention set to disrupt all conflicts.");
        return result;
    }
}
