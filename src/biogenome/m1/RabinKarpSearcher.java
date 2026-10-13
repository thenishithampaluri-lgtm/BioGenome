package biogenome.m1;

import java.util.*;

/**
 * M1 - String Algorithms (CO1)
 * Rabin-Karp Rolling Hash Algorithm for DNA Sequence Matching.
 * Average Time: O(N + M)
 * Auxiliary Space: O(1)
 */
public class RabinKarpSearcher {
    private static final int BASE = 256;
    private static final long PRIME = 1_000_000_007L;

    public static Map<String, Object> search(String text, String pattern) {
        long t0 = System.nanoTime();
        Map<String, Object> result = new HashMap<>();
        List<Integer> matches = new ArrayList<>();
        long collisions = 0;

        if (pattern == null || text == null || pattern.isEmpty() || pattern.length() > text.length()) {
            double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
            result.put("algorithm", "Rabin-Karp");
            result.put("matches", matches);
            result.put("matchCount", 0);
            result.put("collisions", 0L);
            result.put("elapsedMs", elapsedMs);
            result.put("complexity", "O(N + M) avg time, O(1) space");
            return result;
        }

        int m = pattern.length();
        int n = text.length();

        long pHash = 0;
        long tHash = 0;
        long h = 1;

        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % PRIME;
        }

        for (int i = 0; i < m; i++) {
            pHash = (BASE * pHash + pattern.charAt(i)) % PRIME;
            tHash = (BASE * tHash + text.charAt(i)) % PRIME;
        }

        for (int i = 0; i <= n - m; i++) {
            if (pHash == tHash) {
                if (text.substring(i, i + m).equals(pattern)) {
                    matches.add(i);
                } else {
                    collisions++;
                }
            }

            if (i < n - m) {
                tHash = (BASE * (tHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME;
                if (tHash < 0) {
                    tHash += PRIME;
                }
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        result.put("algorithm", "Rabin-Karp");
        result.put("pattern", pattern);
        result.put("matches", matches);
        result.put("matchCount", matches.size());
        result.put("collisions", collisions);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N + M) avg time, O(1) space");
        return result;
    }
}
