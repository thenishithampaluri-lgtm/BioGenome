package biogenome.m3;

import java.util.*;

/**
 * M3 - Advanced Dynamic Programming (CO3)
 * Smith-Waterman Algorithm for Local DNA Sequence Alignment.
 * Time Complexity: O(N * M)
 * Auxiliary Space: O(N * M)
 */
public class SmithWatermanAligner {
    private final int match;
    private final int mismatch;
    private final int gap;

    public SmithWatermanAligner(int match, int mismatch, int gap) {
        this.match = match;
        this.mismatch = mismatch;
        this.gap = gap;
    }

    public Map<String, Object> align(String seq1, String seq2) {
        long t0 = System.nanoTime();
        int n = seq1.length();
        int m = seq2.length();

        int[][] H = new int[n + 1][m + 1];
        int[][] traceback = new int[n + 1][m + 1]; // 0: stop, 1: diag, 2: up, 3: left

        int maxScore = 0;
        int maxI = 0, maxJ = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int diag = H[i - 1][j - 1] + ((seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? match : mismatch);
                int up = H[i - 1][j] + gap;
                int left = H[i][j - 1] + gap;

                int best = Math.max(0, Math.max(diag, Math.max(up, left)));
                H[i][j] = best;

                if (best == 0) {
                    traceback[i][j] = 0;
                } else if (best == diag) {
                    traceback[i][j] = 1;
                } else if (best == up) {
                    traceback[i][j] = 2;
                } else {
                    traceback[i][j] = 3;
                }

                if (best > maxScore) {
                    maxScore = best;
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        StringBuilder alignedS1 = new StringBuilder();
        StringBuilder alignedS2 = new StringBuilder();
        StringBuilder matchSymbols = new StringBuilder();
        int i = maxI, j = maxJ;
        int matches = 0;

        while (i > 0 && j > 0 && H[i][j] > 0) {
            int dir = traceback[i][j];
            if (dir == 1) { // Diagonal
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
            } else if (dir == 2) { // Up
                alignedS1.append(seq1.charAt(i - 1));
                alignedS2.append('-');
                matchSymbols.append(' ');
                i--;
            } else if (dir == 3) { // Left
                alignedS1.append('-');
                alignedS2.append(seq2.charAt(j - 1));
                matchSymbols.append(' ');
                j--;
            } else {
                break;
            }
        }

        alignedS1.reverse();
        alignedS2.reverse();
        matchSymbols.reverse();

        int alignLen = alignedS1.length();
        double identity = (alignLen > 0) ? ((double) matches / alignLen) * 100.0 : 0.0;
        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;

        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Smith-Waterman (Local Alignment)");
        result.put("maxScore", maxScore);
        result.put("localStartSeq1", i);
        result.put("localEndSeq1", maxI);
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
