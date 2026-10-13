package biogenome.m6;

import java.util.*;

/**
 * M6 - Randomized and Parallel Algorithms (CO6)
 * Brent's Theorem Simulator for Large-Scale Parallel Genome Processing.
 * Principle: T_p <= (W - D) / p + D.
 */
public class BrentsTheoremAnalyzer {

    public static class ScalingRow {
        public int processors;
        public double theoreticalTimeSteps;
        public double speedup;
        public double efficiencyPct;

        public ScalingRow(int p, double t, double s, double e) {
            this.processors = p;
            this.theoreticalTimeSteps = t;
            this.speedup = s;
            this.efficiencyPct = e;
        }
    }

    public static Map<String, Object> analyzeParallelScaling(long workOps, int depthSteps, int[] processorCounts) {
        long t0 = System.nanoTime();
        if (processorCounts == null) {
            processorCounts = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256};
        }

        long W = Math.max(1, workOps);
        int D = Math.max(1, depthSteps);
        double avgParallelism = (double) W / D;

        List<ScalingRow> scalingTable = new ArrayList<>();
        double bestSpeedup = 0.0;
        int optimalP = 1;

        for (int p : processorCounts) {
            double tp = ((double) (W - D) / p) + D;
            double speedup = (double) W / tp;
            double efficiency = (speedup / p) * 100.0;

            if (speedup > bestSpeedup) {
                bestSpeedup = speedup;
                optimalP = p;
            }

            scalingTable.add(new ScalingRow(p, tp, speedup, efficiency));
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Brent's Theorem Parallel Analyzer");
        result.put("totalWorkW", W);
        result.put("criticalPathDepthD", D);
        result.put("averageParallelism", avgParallelism);
        result.put("scalingTable", scalingTable);
        result.put("optimalProcessorCount", optimalP);
        result.put("maximumSpeedup", bestSpeedup);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "Analytical Work-Span Scheduling Model");
        return result;
    }
}
