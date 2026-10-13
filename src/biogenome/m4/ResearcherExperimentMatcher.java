package biogenome.m4;

import java.util.*;

/**
 * M4 - Network Flow (CO4)
 * Maximum Bipartite Matching for Assigning Researchers to Experiments.
 * Time Complexity: O(V * E) via augmenting paths.
 */
public class ResearcherExperimentMatcher {
    private final List<String> researchers;
    private final List<String> experiments;
    private final int rLen;
    private final int eLen;
    private final List<List<Integer>> compat;

    public ResearcherExperimentMatcher(List<String> researchers, List<String> experiments) {
        this.researchers = new ArrayList<>(researchers);
        this.experiments = new ArrayList<>(experiments);
        this.rLen = researchers.size();
        this.eLen = experiments.size();
        this.compat = new ArrayList<>();
        for (int i = 0; i < rLen; i++) compat.add(new ArrayList<>());
    }

    public void addCompatibility(int researcherIdx, int experimentIdx) {
        if (researcherIdx >= 0 && researcherIdx < rLen && experimentIdx >= 0 && experimentIdx < eLen) {
            compat.get(researcherIdx).add(experimentIdx);
        }
    }

    private boolean dfsMatch(int u, int[] matchR, boolean[] visited) {
        for (int v : compat.get(u)) {
            if (!visited[v]) {
                visited[v] = true;
                if (matchR[v] < 0 || dfsMatch(matchR[v], matchR, visited)) {
                    matchR[v] = u;
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, Object> computeMatching() {
        long t0 = System.nanoTime();
        int[] matchR = new int[eLen];
        Arrays.fill(matchR, -1);
        int matchedCount = 0;

        for (int u = 0; u < rLen; u++) {
            boolean[] visited = new boolean[eLen];
            if (dfsMatch(u, matchR, visited)) {
                matchedCount++;
            }
        }

        List<Map<String, String>> assignments = new ArrayList<>();
        for (int v = 0; v < eLen; v++) {
            if (matchR[v] != -1) {
                int u = matchR[v];
                Map<String, String> pair = new HashMap<>();
                pair.put("researcher", researchers.get(u));
                pair.put("experiment", experiments.get(v));
                assignments.add(pair);
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Maximum Bipartite Matching");
        result.put("matchedPairsCount", matchedCount);
        result.put("totalResearchers", rLen);
        result.put("totalExperiments", eLen);
        result.put("assignments", assignments);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(V * E) time, O(V + E) space");
        return result;
    }
}
