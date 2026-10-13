package biogenome.m1;

import java.util.*;

/**
 * M1 - String Algorithms (CO1)
 * Knuth-Morris-Pratt (KMP) Algorithm for Exact DNA Motif Searching.
 * Time Complexity: O(N + M)
 * Auxiliary Space: O(M)
 */
public class KMPSearcher {

    public static int[] computePrefixFunction(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];
        int k = 0;
        for (int i = 1; i < m; i++) {
            while (k > 0 && pattern.charAt(k) != pattern.charAt(i)) {
                k = pi[k - 1];
            }
            if (pattern.charAt(k) == pattern.charAt(i)) {
                k++;
            }
            pi[i] = k;
        }
        return pi;
    }

    public static Map<String, Object> search(String text, String pattern) {
        long t0 = System.nanoTime();
        Map<String, Object> result = new HashMap<>();
        List<Integer> matches = new ArrayList<>();
        long comparisons = 0;

        if (pattern == null || text == null || pattern.isEmpty() || pattern.length() > text.length()) {
            double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
            result.put("algorithm", "Knuth-Morris-Pratt (KMP)");
            result.put("matches", matches);
            result.put("matchCount", 0);
            result.put("comparisons", 0L);
            result.put("elapsedMs", elapsedMs);
            result.put("complexity", "O(N + M) time, O(M) space");
            return result;
        }

        int[] pi = computePrefixFunction(pattern);
        int m = pattern.length();
        int n = text.length();
        int q = 0;

        for (int i = 0; i < n; i++) {
            while (q > 0 && pattern.charAt(q) != text.charAt(i)) {
                comparisons++;
                q = pi[q - 1];
            }
            comparisons++;
            if (pattern.charAt(q) == text.charAt(i)) {
                q++;
            }
            if (q == m) {
                matches.add(i - m + 1);
                q = pi[q - 1];
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        result.put("algorithm", "Knuth-Morris-Pratt (KMP)");
        result.put("pattern", pattern);
        result.put("matches", matches);
        result.put("matchCount", matches.size());
        result.put("comparisons", comparisons);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N + M) time, O(M) space");
        return result;
    }
}
