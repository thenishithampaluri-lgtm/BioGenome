package biogenome.m3;

import java.util.*;

/**
 * M3 - Advanced Dynamic Programming (CO3)
 * Levenshtein & Damerau-Levenshtein Distance for Genomic Sequence-Error Correction.
 * Time Complexity: O(N * M)
 * Auxiliary Space: O(N * M)
 */
public class SequenceErrorCorrector {

    public static Map<String, Object> levenshteinDistance(String seq1, String seq2) {
        long t0 = System.nanoTime();
        int n = seq1.length();
        int m = seq2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                    dp[i - 1][j] + 1,       // Deletion
                    Math.min(
                        dp[i][j - 1] + 1,   // Insertion
                        dp[i - 1][j - 1] + cost // Substitution
                    )
                );
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        int dist = dp[n][m];
        double ratio = 1.0 - ((double) dist / Math.max(1, Math.max(n, m)));

        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Levenshtein Distance");
        result.put("distance", dist);
        result.put("similarityRatio", ratio);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N * M) time, O(N * M) space");
        return result;
    }

    public static Map<String, Object> damerauLevenshteinDistance(String seq1, String seq2) {
        long t0 = System.nanoTime();
        int n = seq1.length();
        int m = seq2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                    dp[i - 1][j] + 1,
                    Math.min(
                        dp[i][j - 1] + 1,
                        dp[i - 1][j - 1] + cost
                    )
                );
                // Adjacent transposition check (polymerase slip)
                if (i > 1 && j > 1 &&
                    seq1.charAt(i - 1) == seq2.charAt(j - 2) &&
                    seq1.charAt(i - 2) == seq2.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                }
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        int dist = dp[n][m];
        double ratio = 1.0 - ((double) dist / Math.max(1, Math.max(n, m)));

        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Damerau-Levenshtein (with Transposition)");
        result.put("distance", dist);
        result.put("similarityRatio", ratio);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N * M) time, O(N * M) space");
        return result;
    }
}
