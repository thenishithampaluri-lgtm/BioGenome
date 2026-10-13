package biogenome.m5;

import java.util.*;

/**
 * M5 - NP-Completeness and Approximation (CO5)
 * DPLL 3-SAT Solver for Biological Regulatory Constraints.
 * Models Boolean gene expression switches and activation/inhibition pathways.
 */
public class BiologicalSATSolver {
    private final int numVars;
    private final List<List<Integer>> clauses;
    private final Map<Integer, String> varNames;

    public BiologicalSATSolver(int numVars, List<List<Integer>> clauses, Map<Integer, String> varNames) {
        this.numVars = numVars;
        this.clauses = clauses;
        this.varNames = (varNames != null) ? varNames : new HashMap<>();
        for (int i = 1; i <= numVars; i++) {
            this.varNames.putIfAbsent(i, "Gene_" + i);
        }
    }

    private static List<List<Integer>> simplifyClauses(List<List<Integer>> clauses, Map<Integer, Boolean> assignment) {
        List<List<Integer>> simplified = new ArrayList<>();
        for (List<Integer> clause : clauses) {
            boolean satisfied = false;
            List<Integer> newClause = new ArrayList<>();
            for (int lit : clause) {
                int var = Math.abs(lit);
                boolean sign = lit > 0;
                if (assignment.containsKey(var)) {
                    if (assignment.get(var) == sign) {
                        satisfied = true;
                        break;
                    }
                } else {
                    newClause.add(lit);
                }
            }
            if (!satisfied) {
                if (newClause.isEmpty()) return null; // Contradiction
                simplified.add(newClause);
            }
        }
        return simplified;
    }

    private static Map<Integer, Boolean> dpll(List<List<Integer>> clauses, Map<Integer, Boolean> assignment, Set<Integer> allVars) {
        // Unit propagation
        boolean changed = true;
        while (changed) {
            changed = false;
            List<Integer> unitLits = new ArrayList<>();
            for (List<Integer> c : clauses) {
                if (c.size() == 1) unitLits.add(c.get(0));
            }
            for (int lit : unitLits) {
                int var = Math.abs(lit);
                boolean val = lit > 0;
                if (assignment.containsKey(var) && assignment.get(var) != val) return null;
                if (!assignment.containsKey(var)) {
                    assignment.put(var, val);
                    changed = true;
                }
            }
            if (changed) {
                List<List<Integer>> simp = simplifyClauses(clauses, assignment);
                if (simp == null) return null;
                clauses = simp;
            }
        }

        if (clauses.isEmpty()) {
            for (int v : allVars) assignment.putIfAbsent(v, true);
            return assignment;
        }

        for (List<Integer> c : clauses) {
            if (c.isEmpty()) return null;
        }

        // Branching: choose most frequent unassigned variable
        Map<Integer, Integer> freq = new HashMap<>();
        for (List<Integer> c : clauses) {
            for (int lit : c) {
                int v = Math.abs(lit);
                if (!assignment.containsKey(v)) {
                    freq.put(v, freq.getOrDefault(v, 0) + 1);
                }
            }
        }
        if (freq.isEmpty()) return assignment;

        int chosenVar = Collections.max(freq.entrySet(), Map.Entry.comparingByValue()).getKey();

        // Branch 1: True
        Map<Integer, Boolean> assignTrue = new HashMap<>(assignment);
        assignTrue.put(chosenVar, true);
        List<List<Integer>> cTrue = simplifyClauses(clauses, assignTrue);
        if (cTrue != null) {
            Map<Integer, Boolean> sol = dpll(cTrue, assignTrue, allVars);
            if (sol != null) return sol;
        }

        // Branch 2: False
        Map<Integer, Boolean> assignFalse = new HashMap<>(assignment);
        assignFalse.put(chosenVar, false);
        List<List<Integer>> cFalse = simplifyClauses(clauses, assignFalse);
        if (cFalse != null) {
            Map<Integer, Boolean> sol = dpll(cFalse, assignFalse, allVars);
            if (sol != null) return sol;
        }

        return null;
    }

    public Map<String, Object> solve() {
        long t0 = System.nanoTime();
        Set<Integer> allVars = new HashSet<>();
        for (int i = 1; i <= numVars; i++) allVars.add(i);

        Map<Integer, Boolean> initial = new HashMap<>();
        List<List<Integer>> simplified = simplifyClauses(clauses, initial);

        Map<Integer, Boolean> solution = null;
        boolean sat = false;
        if (simplified != null) {
            solution = dpll(simplified, initial, allVars);
            sat = (solution != null);
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, String> geneStates = new HashMap<>();
        if (solution != null) {
            for (Map.Entry<Integer, Boolean> entry : solution.entrySet()) {
                String name = varNames.getOrDefault(entry.getKey(), "Gene_" + entry.getKey());
                geneStates.put(name, entry.getValue() ? "EXPRESSED (Active)" : "REPRESSED (Inactive)");
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "DPLL 3-SAT Solver (Biological Constraints)");
        result.put("satisfiable", sat);
        result.put("numGenes", numVars);
        result.put("numClauses", clauses.size());
        result.put("geneExpressionState", geneStates);
        result.put("elapsedMs", elapsedMs);
        result.put("complexity", "NP-Complete (DPLL pruning)");
        return result;
    }
}
