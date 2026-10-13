package biogenome.m1;

import java.util.*;

/**
 * M1 - String Algorithms (CO1)
 * Z-Algorithm for Repeated Nucleotide-Pattern Detection and Tandem Repeats.
 * Time Complexity: O(N) linear time.
 * Auxiliary Space: O(N) for Z-array.
 */
public class ZAlgorithm {

    public static int[] computeZArray(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        return z;
    }

    public static Map<String, Object> search(String text, String pattern) {
        long t0 = System.nanoTime();
        Map<String, Object> result = new HashMap<>();
        List<Integer> matches = new ArrayList<>();

        if (pattern == null || text == null || pattern.isEmpty() || pattern.length() > text.length()) {
            double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
            result.put("algorithm", "Z-Function");
            result.put("matches", matches);
            result.put("matchCount", 0);
            result.put("elapsedMs", elapsedMs);
            result.put("complexity", "O(N + M) time, O(N + M) space");
            return result;
        }

        String concat = pattern + "$" + text;
        int[] z = computeZArray(concat);
        int m = pattern.length();

        for (int i = m + 1; i < concat.length(); i++) {
            if (z[i] == m) {
                matches.add(i - (m + 1));
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        result.put("algorithm", "Z-Function");
        result.put("pattern", pattern);
        result.put("matches", matches);
        result.put("matchCount", matches.size());
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "O(N + M) time, O(N + M) space");
        return result;
    }

    public static List<Map<String, Object>> findTandemRepeats(String sequence, int minRepeatLen) {
        List<Map<String, Object>> repeats = new ArrayList<>();
        int n = sequence.length();
        int[] z = computeZArray(sequence);

        int limit = Math.min(n, 2000);
        for (int i = 1; i < limit; i++) {
            int repeatLen = i;
            if (repeatLen >= minRepeatLen && z[i] >= repeatLen) {
                String unit = sequence.substring(0, repeatLen);
                int copies = (z[i] / repeatLen) + 1;
                Map<String, Object> rep = new HashMap<>();
                rep.put("unit", unit);
                rep.put("unitLength", repeatLen);
                rep.put("copies", copies);
                rep.put("startPos", 0);
                rep.put("spanLength", copies * repeatLen);
                repeats.add(rep);
                if (repeats.size() >= 10) break;
            }
        }
        return repeats;
    }
}
