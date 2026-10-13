package biogenome.m2;

import java.util.*;

/**
 * M2 - Suffix Structures (CO2)
 * SA-IS (Suffix Array Induced Sorting) Algorithm.
 * Linear Time: O(N) worst-case time complexity.
 * State-of-the-art linear-time suffix array construction (Nong, Zhang, Chan 2009).
 */
public class SAIS {

    private static void induceSort(int[] s, int[] sa, boolean[] types, int k, int[] bucket) {
        int n = sa.length;

        // Place L-type suffixes
        int[] count = new int[k + 1];
        int[] head = new int[k + 1];
        int cur = 0;
        for (int i = 0; i <= k; i++) {
            head[i] = cur;
            cur += bucket[i];
        }

        for (int i = 0; i < n; i++) {
            int j = sa[i] - 1;
            if (j >= 0 && !types[j]) { // L-type
                int c = s[j];
                sa[head[c] + count[c]] = j;
                count[c]++;
            }
        }

        // Place S-type suffixes
        Arrays.fill(count, 0);
        int[] tail = new int[k + 1];
        cur = 0;
        for (int i = 0; i <= k; i++) {
            cur += bucket[i];
            tail[i] = cur - 1;
        }

        for (int i = n - 1; i >= 0; i--) {
            int j = sa[i] - 1;
            if (j >= 0 && types[j]) { // S-type
                int c = s[j];
                sa[tail[c] - count[c]] = j;
                count[c]++;
            }
        }
    }

    public static int[] saisAlgorithm(int[] s, int k) {
        int n = s.length;
        if (n == 1) return new int[]{0};
        if (n == 2) {
            return (s[0] < s[1]) ? new int[]{0, 1} : new int[]{1, 0};
        }

        boolean[] types = new boolean[n]; // true for S-type, false for L-type
        types[n - 1] = true; // Sentinel is S-type
        for (int i = n - 2; i >= 0; i--) {
            if (s[i] < s[i + 1]) {
                types[i] = true;
            } else if (s[i] > s[i + 1]) {
                types[i] = false;
            } else {
                types[i] = types[i + 1];
            }
        }

        List<Integer> lmsList = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            if (types[i] && !types[i - 1]) {
                lmsList.add(i);
            }
        }
        int[] lms = lmsList.stream().mapToInt(Integer::intValue).toArray();

        int[] bucket = new int[k + 1];
        for (int c : s) {
            bucket[c]++;
        }

        int[] sa = new int[n];
        Arrays.fill(sa, -1);

        int[] count = new int[k + 1];
        int[] tail = new int[k + 1];
        int cur = 0;
        for (int i = 0; i <= k; i++) {
            cur += bucket[i];
            tail[i] = cur - 1;
        }

        for (int i : lms) {
            int c = s[i];
            sa[tail[c] - count[c]] = i;
            count[c]++;
        }

        induceSort(s, sa, types, k, bucket);

        // Compact LMS suffixes into sorted order
        List<Integer> lmsOrderList = new ArrayList<>();
        Set<Integer> lmsSet = new HashSet<>(lmsList);
        for (int pos : sa) {
            if (lmsSet.contains(pos)) {
                lmsOrderList.add(pos);
            }
        }
        int[] lmsOrder = lmsOrderList.stream().mapToInt(Integer::intValue).toArray();

        int name = 0;
        int[] names = new int[n];
        Arrays.fill(names, -1);
        names[lmsOrder[0]] = name;

        for (int i = 1; i < lmsOrder.length; i++) {
            int prev = lmsOrder[i - 1];
            int current = lmsOrder[i];
            if (!isLmsEqual(s, types, n, prev, current)) {
                name++;
            }
            names[current] = name;
        }

        int[] s1 = new int[lms.length];
        for (int i = 0; i < lms.length; i++) {
            s1[i] = names[lms[i]];
        }
        int k1 = name;

        int[] sa1;
        if (k1 == lms.length - 1) {
            sa1 = new int[lms.length];
            for (int i = 0; i < s1.length; i++) {
                sa1[s1[i]] = i;
            }
        } else {
            sa1 = saisAlgorithm(s1, k1);
        }

        int[] sortedLms = new int[sa1.length];
        for (int i = 0; i < sa1.length; i++) {
            sortedLms[i] = lms[sa1[i]];
        }

        Arrays.fill(sa, -1);
        Arrays.fill(count, 0);
        for (int i = sortedLms.length - 1; i >= 0; i--) {
            int pos = sortedLms[i];
            int c = s[pos];
            sa[tail[c] - count[c]] = pos;
            count[c]++;
        }

        induceSort(s, sa, types, k, bucket);
        return sa;
    }

    private static boolean isLmsEqual(int[] s, boolean[] types, int n, int p1, int p2) {
        if (p1 == n - 1 || p2 == n - 1) return p1 == p2;
        int d = 0;
        while (true) {
            int c1 = s[p1 + d];
            int c2 = s[p2 + d];
            boolean t1 = types[p1 + d];
            boolean t2 = types[p2 + d];
            if (c1 != c2 || t1 != t2) return false;
            if (d > 0 && ((t1 && !types[p1 + d - 1]) || (t2 && !types[p2 + d - 1]))) {
                return (t1 && !types[p1 + d - 1]) == (t2 && !types[p2 + d - 1]);
            }
            d++;
        }
    }

    public static int[] buildSuffixArray(String text) {
        if (text == null || text.isEmpty()) return new int[0];
        TreeSet<Character> charSet = new TreeSet<>();
        for (char c : text.toCharArray()) charSet.add(c);

        Map<Character, Integer> charMap = new HashMap<>();
        int idx = 1;
        for (char c : charSet) {
            charMap.put(c, idx++);
        }

        int[] s = new int[text.length() + 1];
        for (int i = 0; i < text.length(); i++) {
            s[i] = charMap.get(text.charAt(i));
        }
        s[text.length()] = 0; // Sentinel

        int k = charSet.size() + 1;
        int[] sa = saisAlgorithm(s, k);

        // Exclude sentinel (first element)
        int[] result = new int[text.length()];
        System.arraycopy(sa, 1, result, 0, text.length());
        return result;
    }
}
