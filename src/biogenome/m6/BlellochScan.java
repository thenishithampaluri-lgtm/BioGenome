package biogenome.m6;

import java.util.*;

/**
 * M6 - Randomized and Parallel Algorithms (CO6)
 * Blelloch Work-Efficient Parallel Prefix Scan Algorithm.
 * Work Complexity: O(N) operations.
 * Depth / Span Complexity: O(log N) parallel steps.
 * Identifies the biological replication origin (oriC) via cumulative GC skew.
 */
public class BlellochScan {

    public static class ScanResult {
        public int[] array;
        public int workOps;
        public int depthSteps;

        public ScanResult(int[] array, int workOps, int depthSteps) {
            this.array = array;
            this.workOps = workOps;
            this.depthSteps = depthSteps;
        }
    }

    public static ScanResult exclusiveScan(int[] arr) {
        int n = arr.length;
        if (n == 0) return new ScanResult(new int[0], 0, 0);

        int power = 1;
        while (power < n) power <<= 1;

        int[] tree = new int[power];
        System.arraycopy(arr, 0, tree, 0, n);

        int workOps = 0;
        int depthSteps = 0;

        // Phase 1: Up-Sweep (Reduce Tree)
        for (int d = 1; d < power; d *= 2) {
            depthSteps++;
            for (int k = 0; k < power; k += 2 * d) {
                tree[k + 2 * d - 1] += tree[k + d - 1];
                workOps++;
            }
        }

        // Clear root
        tree[power - 1] = 0;

        // Phase 2: Down-Sweep (Distribution Tree)
        for (int d = power / 2; d > 0; d /= 2) {
            depthSteps++;
            for (int k = 0; k < power; k += 2 * d) {
                int t = tree[k + d - 1];
                tree[k + d - 1] = tree[k + 2 * d - 1];
                tree[k + 2 * d - 1] += t;
                workOps += 2;
            }
        }

        int[] res = new int[n];
        System.arraycopy(tree, 0, res, 0, n);
        return new ScanResult(res, workOps, depthSteps);
    }

    public static ScanResult inclusiveScan(int[] arr) {
        ScanResult excl = exclusiveScan(arr);
        int[] incl = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            incl[i] = excl.array[i] + arr[i];
        }
        return new ScanResult(incl, excl.workOps + arr.length, excl.depthSteps + 1);
    }

    public static Map<String, Object> computeCumulativeGCSkew(String dnaSequence) {
        long t0 = System.nanoTime();
        int n = dnaSequence.length();
        int[] vector = new int[n];

        for (int i = 0; i < n; i++) {
            char ch = dnaSequence.charAt(i);
            if (ch == 'G') vector[i] = 1;
            else if (ch == 'C') vector[i] = -1;
            else vector[i] = 0;
        }

        ScanResult scan = inclusiveScan(vector);
        int[] cumulativeSkew = scan.array;

        int minSkew = 0;
        int minPos = 0;
        int maxSkew = 0;

        for (int i = 0; i < cumulativeSkew.length; i++) {
            int val = cumulativeSkew[i];
            if (val < minSkew) {
                minSkew = val;
                minPos = i;
            }
            if (val > maxSkew) {
                maxSkew = val;
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Blelloch Parallel Prefix Scan");
        result.put("sequenceLength", n);
        result.put("workOps", scan.workOps);
        result.put("depthSteps", scan.depthSteps);
        result.put("minSkew", minSkew);
        result.put("maxSkew", maxSkew);
        result.put("oriCCandidate", minPos);
        result.put("finalNetSkew", (cumulativeSkew.length > 0) ? cumulativeSkew[cumulativeSkew.length - 1] : 0);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N) work, O(log N) depth");
        return result;
    }
}
