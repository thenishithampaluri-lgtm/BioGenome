package biogenome.m2;

import java.util.*;

/**
 * M2 - Suffix Structures (CO2)
 * Kasai's Algorithm for Longest Common Prefix (LCP) Array Construction.
 * Time Complexity: O(N) linear time.
 * Discovers maximal repeats and repeated genomic patterns.
 */
public class KasaiLCP {

    public static int[] buildLcpArray(String text, int[] sa) {
        int n = text.length();
        if (n == 0 || sa.length != n) return new int[0];

        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        int[] lcp = new int[n];
        int k = 0;

        for (int i = 0; i < n; i++) {
            if (rank[i] == 0) {
                k = 0;
                continue;
            }

            int j = sa[rank[i] - 1];
            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }

            lcp[rank[i]] = k;
            if (k > 0) {
                k--;
            }
        }

        return lcp;
    }

    public static Map<String, Object> analyzeRepeatedPatterns(String text, int[] sa, int minLength) {
        long t0 = System.nanoTime();
        int[] lcp = buildLcpArray(text, sa);
        int n = text.length();

        int maxLcp = 0;
        int maxIdx = -1;
        List<Map<String, Object>> repeats = new ArrayList<>();

        for (int i = 1; i < n; i++) {
            int val = lcp[i];
            if (val >= minLength) {
                String pattern = text.substring(sa[i], sa[i] + val);
                Map<String, Object> rep = new HashMap<>();
                rep.put("pattern", pattern);
                rep.put("length", val);
                rep.put("pos1", sa[i - 1]);
                rep.put("pos2", sa[i]);
                repeats.add(rep);
            }
            if (val > maxLcp) {
                maxLcp = val;
                maxIdx = i;
            }
        }

        String longestRepeat = "";
        int longestPos1 = -1, longestPos2 = -1;
        if (maxIdx != -1) {
            longestRepeat = text.substring(sa[maxIdx], sa[maxIdx] + maxLcp);
            longestPos1 = sa[maxIdx - 1];
            longestPos2 = sa[maxIdx];
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Kasai LCP Pattern Analyzer");
        result.put("lcpLength", lcp.length);
        result.put("maxLcp", maxLcp);
        result.put("longestRepeatedPattern", longestRepeat.length() > 60 ? longestRepeat.substring(0, 60) + "..." : longestRepeat);
        result.put("longestLength", maxLcp);
        result.put("longestOccurrences", Arrays.asList(longestPos1, longestPos2));
        result.put("significantRepeatsFound", repeats.size());
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N) time, O(N) space");
        return result;
    }
}
