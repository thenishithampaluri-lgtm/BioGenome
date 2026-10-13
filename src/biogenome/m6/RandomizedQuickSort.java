package biogenome.m6;

import java.util.*;

/**
 * M6 - Randomized and Parallel Algorithms (CO6)
 * Randomized QuickSort for Genome Annotation & Mutation Records.
 * Expected Time: O(N log N)
 * Auxiliary Space: O(log N)
 */
public class RandomizedQuickSort {

    public static class VariantRecord {
        public String chrom;
        public int position;
        public String ref;
        public String alt;
        public double quality;
        public String gene;

        public VariantRecord(String chrom, int position, String ref, String alt, double quality, String gene) {
            this.chrom = chrom;
            this.position = position;
            this.ref = ref;
            this.alt = alt;
            this.quality = quality;
            this.gene = gene;
        }
    }

    private static final Random rng = new Random();

    private static int partition(List<VariantRecord> arr, int low, int high) {
        int pivotIdx = low + rng.nextInt(high - low + 1);
        Collections.swap(arr, pivotIdx, high);
        int pivotVal = arr.get(high).position;

        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr.get(j).position <= pivotVal) {
                i++;
                Collections.swap(arr, i, j);
            }
        }
        Collections.swap(arr, i + 1, high);
        return i + 1;
    }

    private static void quicksort(List<VariantRecord> arr, int low, int high) {
        while (low < high) {
            int pi = partition(arr, low, high);
            if (pi - low < high - pi) {
                quicksort(arr, low, pi - 1);
                low = pi + 1;
            } else {
                quicksort(arr, pi + 1, high);
                high = pi - 1;
            }
        }
    }

    public static Map<String, Object> sort(List<VariantRecord> records) {
        long t0 = System.nanoTime();
        List<VariantRecord> copy = new ArrayList<>(records);
        if (copy.size() > 1) {
            quicksort(copy, 0, copy.size() - 1);
        }
        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;

        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Randomized QuickSort");
        result.put("recordsSortedCount", copy.size());
        result.put("sortedRecords", copy);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N log N) expected time, O(log N) space");
        return result;
    }
}
