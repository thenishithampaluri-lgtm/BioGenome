package biogenome.m5;

import java.util.*;

/**
 * M5 - NP-Completeness and Approximation (CO5)
 * Polynomial-Time Reduction: 3-SAT -> CLIQUE.
 * Constructs graph G where a k-clique corresponds to consistent co-expression.
 */
public class SATToCliqueReducer {
    private final List<List<Integer>> clauses;
    private final int k;
    private final Map<Integer, String> varNames;

    public SATToCliqueReducer(List<List<Integer>> clauses, Map<Integer, String> varNames) {
        this.clauses = clauses;
        this.k = clauses.size();
        this.varNames = (varNames != null) ? varNames : new HashMap<>();
    }

    public static class Vertex {
        public int id;
        public int clauseIdx;
        public int literal;
        public String label;

        public Vertex(int id, int clauseIdx, int literal, String label) {
            this.id = id;
            this.clauseIdx = clauseIdx;
            this.literal = literal;
            this.label = label;
        }
    }

    public Map<String, Object> constructCliqueGraph() {
        long t0 = System.nanoTime();
        List<Vertex> vertices = new ArrayList<>();

        for (int cIdx = 0; cIdx < clauses.size(); cIdx++) {
            List<Integer> clause = clauses.get(cIdx);
            for (int lit : clause) {
                int varNum = Math.abs(lit);
                String sign = (lit > 0) ? "+" : "-";
                String name = varNames.getOrDefault(varNum, "x" + varNum);
                String label = "C" + (cIdx + 1) + ":(" + sign + name + ")";
                vertices.add(new Vertex(vertices.size(), cIdx, lit, label));
            }
        }

        int numV = vertices.size();
        List<int[]> edges = new ArrayList<>();

        for (int i = 0; i < numV; i++) {
            for (int j = i + 1; j < numV; j++) {
                Vertex v1 = vertices.get(i);
                Vertex v2 = vertices.get(j);

                // Different clauses and non-contradictory literals
                if (v1.clauseIdx != v2.clauseIdx && v1.literal != -v2.literal) {
                    edges.add(new int[]{i, j});
                }
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "3-SAT to CLIQUE Reduction");
        result.put("formulaClausesCount", k);
        result.put("targetCliqueSizeK", k);
        result.put("graphVerticesCount", numV);
        result.put("graphEdgesCount", edges.size());
        result.put("elapsedMs", elapsedMs);
        result.put("biologicalMapping", "k-Clique in G represents a mutually compatible co-expression cluster satisfying all regulatory constraints.");
        result.put("complexity", "O(|V|^2) polynomial reduction");
        return result;
    }
}
