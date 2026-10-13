package biogenome.m6;

import java.util.*;

/**
 * M6 - Randomized and Parallel Algorithms (CO6)
 * Reservoir Sampling (Algorithm R) for Streaming Biological Observations.
 * Time Complexity: O(N) streaming single pass.
 * Auxiliary Space: O(K) fixed memory reservoir.
 */
public class ReservoirSampler {
    private final int k;
    private final Random rng;
    private final List<Character> reservoir;
    private int itemsSeen;

    public ReservoirSampler(int k, long seed) {
        this.k = k;
        this.rng = new Random(seed);
        this.reservoir = new ArrayList<>();
        this.itemsSeen = 0;
    }

    public void processItem(char item) {
        itemsSeen++;
        if (reservoir.size() < k) {
            reservoir.add(item);
        } else {
            int j = rng.nextInt(itemsSeen);
            if (j < k) {
                reservoir.set(j, item);
            }
        }
    }

    public Map<String, Object> sampleStream(String stream) {
        long t0 = System.nanoTime();
        reservoir.clear();
        itemsSeen = 0;

        for (int i = 0; i < stream.length(); i++) {
            processItem(stream.charAt(i));
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Reservoir Sampling (Algorithm R)");
        result.put("reservoirSizeK", k);
        result.put("totalStreamItemsProcessed", itemsSeen);
        result.put("sampledItems", new ArrayList<>(reservoir));
        result.put("selectionProbability", (double) k / Math.max(1, itemsSeen));
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N) streaming time, O(K) space");
        return result;
    }
}
