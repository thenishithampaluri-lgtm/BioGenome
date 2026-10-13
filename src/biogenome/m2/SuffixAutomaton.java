package biogenome.m2;

import java.util.*;

/**
 * M2 - Suffix Structures (CO2)
 * Suffix Automaton (SAM / DAWG) for Substring Analysis and LCS.
 * Time Complexity: O(N) linear time construction and O(M) queries.
 * States: At most 2N - 1 states and 3N - 4 transitions.
 */
public class SuffixAutomaton {

    public static class State {
        public int len;
        public int link;
        public Map<Character, Integer> next = new HashMap<>();
        public int firstPos = -1;
        public boolean isClone = false;

        public State(int len, int link) {
            this.len = len;
            this.link = link;
        }
    }

    private final List<State> states = new ArrayList<>();
    private int last;
    private final double buildTimeMs;

    public SuffixAutomaton(String text) {
        long t0 = System.nanoTime();
        states.add(new State(0, -1));
        last = 0;
        for (int i = 0; i < text.length(); i++) {
            extend(text.charAt(i), i);
        }
        this.buildTimeMs = (System.nanoTime() - t0) / 1_000_000.0;
    }

    public void extend(char ch, int pos) {
        int cur = states.size();
        State curState = new State(states.get(last).len + 1, -1);
        curState.firstPos = pos;
        states.add(curState);

        int p = last;
        while (p != -1 && !states.get(p).next.containsKey(ch)) {
            states.get(p).next.put(ch, cur);
            p = states.get(p).link;
        }

        if (p == -1) {
            states.get(cur).link = 0;
        } else {
            int q = states.get(p).next.get(ch);
            if (states.get(p).len + 1 == states.get(q).len) {
                states.get(cur).link = q;
            } else {
                int clone = states.size();
                State cloneState = new State(states.get(p).len + 1, states.get(q).link);
                cloneState.next.putAll(states.get(q).next);
                cloneState.firstPos = states.get(q).firstPos;
                cloneState.isClone = true;
                states.add(cloneState);

                while (p != -1 && states.get(p).next.get(ch) == q) {
                    states.get(p).next.put(ch, clone);
                    p = states.get(p).link;
                }

                states.get(q).link = clone;
                states.get(cur).link = clone;
            }
        }
        last = cur;
    }

    public boolean containsSubstring(String pattern) {
        int curr = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            if (!states.get(curr).next.containsKey(ch)) {
                return false;
            }
            curr = states.get(curr).next.get(ch);
        }
        return true;
    }

    public Map<String, Object> longestCommonSubstring(String other) {
        long t0 = System.nanoTime();
        int curr = 0;
        int l = 0;
        int bestLen = 0;
        int bestPos = 0;

        for (int i = 0; i < other.length(); i++) {
            char ch = other.charAt(i);
            while (curr > 0 && !states.get(curr).next.containsKey(ch)) {
                curr = states.get(curr).link;
                l = states.get(curr).len;
            }
            if (states.get(curr).next.containsKey(ch)) {
                curr = states.get(curr).next.get(ch);
                l++;
            }
            if (l > bestLen) {
                bestLen = l;
                bestPos = i;
            }
        }

        String lcsStr = (bestLen > 0) ? other.substring(bestPos - bestLen + 1, bestPos + 1) : "";
        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;

        Map<String, Object> res = new HashMap<>();
        res.put("algorithm", "Suffix Automaton (SAM) LCS");
        res.put("lcsString", lcsStr.length() > 60 ? lcsStr.substring(0, 60) + "..." : lcsStr);
        res.put("lcsLength", bestLen);
        res.put("numStates", states.size());
        res.put("elapsedMs", elapsedMs);
        res.put("complexity", "O(N) build, O(M) search");
        return res;
    }

    public int getNumStates() {
        return states.size();
    }
}
