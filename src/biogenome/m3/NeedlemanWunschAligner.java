package biogenome.m3;

import java.util.*;

/**
 * M3 - Advanced Dynamic Programming (CO3)
 * Needleman-Wunsch Algorithm for Global DNA Sequence Alignment.
 * Time Complexity: O(N * M)
 * Auxiliary Space: O(N * M)
 */
public class NeedlemanWunschAligner {
    private final int match;
    private final int mismatch;
    private final int gap;

    public NeedlemanWunschAligner(int match, int mismatch, int gap) {
        this.match = match;
        this.mismatch = mismatch;
        this.gap = gap;
    }

    public Map<String, Object> align(String seq1, String seq2) {
        long t0 = System.nanoTime();
        int n = seq1.length();
        int m = seq2.length();

        int[][] dp = new int[n + 1][m + 1];
        char[][] traceback = new char[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            dp[i][0] = i * gap;
            traceback[i][0] = 'U';
        }
        for (int j = 1; j <= m; j++) {
            dp[0][j] = j * gap;
            traceback[0][j] = 'L';
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int scoreDiag = dp[i - 1][j - 1] + ((seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? match : mismatch);
                int scoreUp = dp[i - 1][j] + gap;
                int scoreLeft = dp[i][j - 1] + gap;

                int best = Math.max(scoreDiag, Math.max(scoreUp, scoreLeft));
                dp[i][j] = best;

                if (best == scoreDiag) {
                    traceback[i][j] = 'D';
                } else if (best == scoreUp) {
                    traceback[i][j] = 'U';
                } else {
                    traceback[i][j] = 'L';
                }
            }
        }

        StringBuilder alignedS1 = new StringBuilder();
        StringBuilder alignedS2 = new StringBuilder();
        StringBuilder matchSymbols = new StringBuilder();
        int i = n, j = m;
        int matches = 0;

        while (i > 0 || j > 0) {
            if (traceback[i][j] == 'D') {
                char c1 = seq1.charAt(i - 1);
                char c2 = seq2.charAt(j - 1);
                alignedS1.append(c1);
                alignedS2.append(c2);
                if (c1 == c2) {
                    matchSymbols.append('|');
                    matches++;
                } else {
                    matchSymbols.append('.');
                }
                i--;
                j--;
            } else if (traceback[i][j] == 'U') {
                alignedS1.append(seq1.charAt(i - 1));
                alignedS2.append('-');
                matchSymbols.append(' ');
                i--;
            } else { // 'L'
                alignedS1.append('-');
                alignedS2.append(seq2.charAt(j - 1));
                matchSymbols.append(' ');
                j--;
            }
        }

        alignedS1.reverse();
        alignedS2.reverse();
        matchSymbols.reverse();

        int alignLen = alignedS1.length();
        double identity = (alignLen > 0) ? ((double) matches / alignLen) * 100.0 : 0.0;
        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;

        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Needleman-Wunsch (Global Alignment)");
        result.put("score", dp[n][m]);
        result.put("alignedSeq1", alignedS1.toString());
        result.put("alignmentSymbol", matchSymbols.toString());
        result.put("alignedSeq2", alignedS2.toString());
        result.put("alignmentLength", alignLen);
        result.put("identicalMatches", matches);
        result.put("identityPercentage", identity);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N * M) time, O(N * M) space");
        return result;
    }
}
