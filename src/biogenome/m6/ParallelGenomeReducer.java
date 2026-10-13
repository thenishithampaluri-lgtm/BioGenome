package biogenome.m6;

import java.util.*;
import java.util.concurrent.*;

/**
 * M6 - Randomized and Parallel Algorithms (CO6)
 * Parallel Tree-Based Reduction for Large Genomic Sequence Statistics.
 * Work Complexity: O(N) operations.
 * Depth / Span Complexity: O(log N) parallel steps.
 */
public class ParallelGenomeReducer {

    public static class NucleotideStats {
        public long a = 0, c = 0, g = 0, t = 0, n = 0;

        public NucleotideStats() {}

        public NucleotideStats(long a, long c, long g, long t, long n) {
            this.a = a;
            this.c = c;
            this.g = g;
            this.t = t;
            this.n = n;
        }

        public static NucleotideStats combine(NucleotideStats s1, NucleotideStats s2) {
            return new NucleotideStats(
                s1.a + s2.a,
                s1.c + s2.c,
                s1.g + s2.g,
                s1.t + s2.t,
                s1.n + s2.n
            );
        }
    }

    public static Map<String, Object> reduceSequenceStats(String sequence, int blockSize) {
        long t0 = System.nanoTime();
        int len = sequence.length();
        if (len == 0) {
            Map<String, Object> empty = new HashMap<>();
            empty.put("sequenceLength", 0);
            empty.put("gcContentPct", 0.0);
            return empty;
        }

        List<NucleotideStats> leaves = new ArrayList<>();
        for (int i = 0; i < len; i += blockSize) {
            int end = Math.min(len, i + blockSize);
            String chunk = sequence.substring(i, end);
            long ca = 0, cc = 0, cg = 0, ct = 0, cn = 0;
            for (int k = 0; k < chunk.length(); k++) {
                char ch = chunk.charAt(k);
                switch (ch) {
                    case 'A' -> ca++;
                    case 'C' -> cc++;
                    case 'G' -> cg++;
                    case 'T' -> ct++;
                    default -> cn++;
                }
            }
            leaves.add(new NucleotideStats(ca, cc, cg, ct, cn));
        }

        List<NucleotideStats> currentLevel = leaves;
        int depthSteps = 0;
        int totalOps = 0;

        while (currentLevel.size() > 1) {
            depthSteps++;
            List<NucleotideStats> nextLevel = new ArrayList<>();
            for (int i = 0; i < currentLevel.size(); i += 2) {
                if (i + 1 < currentLevel.size()) {
                    nextLevel.add(NucleotideStats.combine(currentLevel.get(i), currentLevel.get(i + 1)));
                    totalOps += 5;
                } else {
                    nextLevel.add(currentLevel.get(i));
                }
            }
            currentLevel = nextLevel;
        }

        NucleotideStats finalStats = currentLevel.get(0);
        long totalBases = finalStats.a + finalStats.c + finalStats.g + finalStats.t + finalStats.n;
        double gcPct = ((double) (finalStats.c + finalStats.g) / Math.max(1, totalBases)) * 100.0;

        // Shannon entropy
        double entropy = 0.0;
        long[] counts = {finalStats.a, finalStats.c, finalStats.g, finalStats.t};
        for (long cnt : counts) {
            if (cnt > 0) {
                double p = (double) cnt / totalBases;
                entropy -= p * (Math.log(p) / Math.log(2));
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Parallel Tree Reduction");
        result.put("sequenceLength", totalBases);
        result.put("simulatedLeafBlocks", leaves.size());
        result.put("treeReductionDepth", depthSteps);
        result.put("reductionOperations", totalOps);
        result.put("countA", finalStats.a);
        result.put("countC", finalStats.c);
        result.put("countG", finalStats.g);
        result.put("countT", finalStats.t);
        result.put("countN", finalStats.n);
        result.put("gcContentPct", gcPct);
        result.put("shannonEntropy", entropy);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N) work, O(log N) depth");
        return result;
    }
}
