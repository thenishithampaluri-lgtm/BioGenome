package biogenome.m2;

import java.util.*;

/**
 * M2 - Suffix Structures (CO2)
 * Suffix Array Indexing & Binary Search Pattern Matching.
 * Query Time: O(M * log N)
 * Construction: O(N) via SA-IS
 */
public class SuffixArrayIndex {
    private final String text;
    private final int n;
    private final int[] sa;
    private final double buildTimeMs;

    public SuffixArrayIndex(String text) {
        this.text = text;
        this.n = text.length();
        long t0 = System.nanoTime();
        this.sa = SAIS.buildSuffixArray(text);
        this.buildTimeMs = (System.nanoTime() - t0) / 1_000_000.0;
    }

    public int[] getSuffixArray() {
        return sa;
    }

    public double getBuildTimeMs() {
        return buildTimeMs;
    }

    public Map<String, Object> search(String pattern) {
        long t0 = System.nanoTime();
        Map<String, Object> result = new HashMap<>();
        List<Integer> matches = new ArrayList<>();
        int m = pattern.length();

        if (m == 0 || n == 0) {
            double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
            result.put("algorithm", "Suffix Array Binary Search");
            result.put("matches", matches);
            result.put("matchCount", 0);
            result.put("elapsedMs", elapsedMs);
            result.put("complexity", "O(M log N) query");
            return result;
        }

        // Lower bound binary search
        int low = 0, high = n - 1;
        int startIdx = n;
        long comparisons = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            int pos = sa[mid];
            int subEnd = Math.min(n, pos + m);
            String suffix = text.substring(pos, subEnd);
            if (suffix.compareTo(pattern) >= 0) {
                startIdx = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        // Upper bound binary search
        low = 0;
        high = n - 1;
        int endIdx = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;
            int pos = sa[mid];
            int subEnd = Math.min(n, pos + m);
            String suffix = text.substring(pos, subEnd);
            if (suffix.compareTo(pattern) <= 0) {
                if (suffix.equals(pattern)) {
                    endIdx = mid;
                }
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (startIdx <= endIdx && startIdx < n && endIdx >= 0) {
            for (int i = startIdx; i <= endIdx; i++) {
                matches.add(sa[i]);
            }
            Collections.sort(matches);
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        result.put("algorithm", "Suffix Array Binary Search");
        result.put("pattern", pattern);
        result.put("matches", matches);
        result.put("matchCount", matches.size());
        result.put("comparisons", comparisons);
        result.put("elapsedMs", elapsedMs);
        result.put("buildTimeMs", buildTimeMs);
        result.put("complexity", "O(M log N) query, O(N) SA-IS build");
        return result;
    }
}
