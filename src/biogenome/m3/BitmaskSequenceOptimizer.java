package biogenome.m3;

import java.util.*;

/**
 * M3 - Advanced Dynamic Programming (CO3)
 * Bitmask Dynamic Programming for Small-Scale Sequence Combinations.
 * Time Complexity: O(2^N * N^2)
 * Space Complexity: O(2^N * N)
 * Solves Shortest Common Superstring problem for multiplexed PCR primer assembly.
 */
public class BitmaskSequenceOptimizer {

    private static int computeOverlap(String a, String b) {
        int maxOv = 0;
        int limit = Math.min(a.length(), b.length());
        for (int k = 1; k <= limit; k++) {
            if (a.substring(a.length() - k).equals(b.substring(0, k))) {
                maxOv = k;
            }
        }
        return maxOv;
    }

    public static Map<String, Object> assembleFragments(List<String> fragments) {
        long t0 = System.nanoTime();
        int n = fragments.size();
        Map<String, Object> result = new HashMap<>();

        if (n == 0) {
            result.put("assembledLength", 0);
            result.put("assembledSequence", "");
            return result;
        }
        if (n == 1) {
            result.put("assembledLength", fragments.get(0).length());
            result.put("assembledSequence", fragments.get(0));
            return result;
        }

        int[][] overlap = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    overlap[i][j] = computeOverlap(fragments.get(i), fragments.get(j));
                }
            }
        }

        int numMasks = 1 << n;
        int INF = 1_000_000_000;
        int[][] dp = new int[numMasks][n];
        int[][] parent = new int[numMasks][n];

        for (int[] row : dp) Arrays.fill(row, INF);
        for (int[] row : parent) Arrays.fill(row, -1);

        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = fragments.get(i).length();
        }

        for (int mask = 1; mask < numMasks; mask++) {
            for (int last = 0; last < n; last++) {
                if ((mask & (1 << last)) == 0 || dp[mask][last] == INF) continue;
                for (int nxt = 0; nxt < n; nxt++) {
                    if ((mask & (1 << nxt)) == 0) {
                        int nxtMask = mask | (1 << nxt);
                        int cost = dp[mask][last] + fragments.get(nxt).length() - overlap[last][nxt];
                        if (cost < dp[nxtMask][nxt]) {
                            dp[nxtMask][nxt] = cost;
                            parent[nxtMask][nxt] = last;
                        }
                    }
                }
            }
        }

        int fullMask = numMasks - 1;
        int bestCost = INF;
        int bestLast = -1;

        for (int i = 0; i < n; i++) {
            if (dp[fullMask][i] < bestCost) {
                bestCost = dp[fullMask][i];
                bestLast = i;
            }
        }

        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        int currMask = fullMask;
        int currNode = bestLast;

        while (currNode != -1) {
            path.add(currNode);
            int prev = parent[currMask][currNode];
            currMask ^= (1 << currNode);
            currNode = prev;
        }
        Collections.reverse(path);

        StringBuilder assembled = new StringBuilder(fragments.get(path.get(0)));
        for (int idx = 1; idx < path.size(); idx++) {
            String currFrag = fragments.get(path.get(idx));
            int ov = overlap[path.get(idx - 1)][path.get(idx)];
            assembled.append(currFrag.substring(ov));
        }

        int origLen = 0;
        for (String f : fragments) origLen += f.length();

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        result.put("algorithm", "Bitmask Dynamic Programming (Fragment Superstring)");
        result.put("numFragments", n);
        result.put("assembledSequence", assembled.toString());
        result.put("assembledLength", assembled.length());
        result.put("originalTotalLength", origLen);
        result.put("nucleotidesSaved", origLen - assembled.length());
        result.put("optimalOrder", path);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(2^N * N^2) time, O(2^N * N) space");
        return result;
    }
}
