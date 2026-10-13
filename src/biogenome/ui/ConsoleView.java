package biogenome.ui;

import biogenome.core.DecisionMatrix;

import java.util.*;

/**
 * Terminal UI Console View providing clean spacing, structured sub-menus,
 * tables, module reports, and decision dashboards.
 */
public class ConsoleView {

    public static final String CYAN = "\033[96m";
    public static final String GREEN = "\033[92m";
    public static final String YELLOW = "\033[93m";
    public static final String MAGENTA = "\033[95m";
    public static final String BLUE = "\033[94m";
    public static final String RED = "\033[91m";
    public static final String BOLD = "\033[1m";
    public static final String DIM = "\033[2m";
    public static final String RESET = "\033[0m";

    public static void printBanner() {
        System.out.println();
        System.out.println(CYAN + BOLD);
        System.out.println("  +" + "=".repeat(85) + "+");
        System.out.println("  |   [DNA] BioGenome - High-Performance Genome Research & Analysis Platform          |");
        System.out.println("  |   Algorithms: M1 (String) | M2 (Suffix) | M3 (DP) | M4 (Flow) | M5 (NP) | M6 (Parallel)|");
        System.out.println("  +" + "=".repeat(85) + "+");
        System.out.println(RESET);
    }

    public static void printHeader(String title) {
        System.out.println();
        System.out.println("  " + BLUE + BOLD + "==> " + title.toUpperCase() + " " + RESET);
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    public static void printFilesTable(List<Map<String, Object>> filesSummary, String activeFile) {
        printHeader("Connected DNA Sequence Files (Minimum 5 Linked)");
        System.out.printf("  %s%-3s %-35s %-15s %-10s %-15s%s\n", BOLD, "#", "Filename", "Length (bp)", "GC%", "Status", RESET);
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        int idx = 1;
        for (Map<String, Object> item : filesSummary) {
            String fname = (String) item.get("filename");
            boolean isActive = fname.equals(activeFile);
            String status = isActive ? GREEN + "[ACTIVE]" + RESET : DIM + "[Ready]" + RESET;
            String marker = isActive ? YELLOW + ">>" + RESET : "  ";
            System.out.printf("  %s %-2d %-35s %-15s %-10s %-15s\n",
                marker, idx++, fname, String.format("%,d", (Integer) item.get("lengthBp")),
                item.get("gcContent"), status);
        }
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    public static void printMainMenu(String activeFile) {
        System.out.println("  " + BOLD + CYAN + "=== BioGenome Main Control Panel ===" + RESET);
        System.out.println("  Active Genome File: " + GREEN + BOLD + activeFile + RESET + "\n");
        System.out.println("    " + BOLD + "[1]" + RESET + "  Select Active DNA Sequence File (Switch between 5 FASTA files)\n");
        System.out.println("    " + BOLD + "[2]" + RESET + "  Module 1 (CO1) - String Algorithms [Sub-Menu: KMP, Z-Func, Rabin-Karp, Aho-Corasick]");
        System.out.println("    " + BOLD + "[3]" + RESET + "  Module 2 (CO2) - Suffix Structures & Indexing [Sub-Menu: SA-IS, Suffix Array, Kasai, SAM]");
        System.out.println("    " + BOLD + "[4]" + RESET + "  Module 3 (CO3) - Advanced Dynamic Programming [Sub-Menu: Levenshtein, Needleman, Smith, Bitmask]");
        System.out.println("    " + BOLD + "[5]" + RESET + "  Module 4 (CO4) - Network Flow [Sub-Menu: Ford-Fulkerson, Edmonds-Karp, Dinic, Bipartite]");
        System.out.println("    " + BOLD + "[6]" + RESET + "  Module 5 (CO5) - NP-Completeness & Approx [Sub-Menu: 3-SAT, 3-SAT->Clique, Reductions, 2-Approx]");
        System.out.println("    " + BOLD + "[7]" + RESET + "  Module 6 (CO6) - Randomized & Parallel Algorithms [Sub-Menu: QuickSort, Reservoir, Scan, Reduce]");
        System.out.println();
        System.out.println("    " + BOLD + "[8]" + RESET + "  Execute ALL Algorithms on Current DNA File (Full Sequential Pipeline)");
        System.out.println("    " + BOLD + "[9]" + RESET + "  " + YELLOW + BOLD + "[BEST] Display 'BEST ALGORITHM' Decision Analysis (Internal Benchmark & Winner Report)" + RESET);
        System.out.println("    " + BOLD + "[10]" + RESET + " Run Batch Analytics on All 5 DNA Files Simultaneously");
        System.out.println("    " + BOLD + "[11]" + RESET + " Verify / Regenerate Benchmark DNA Sequences");
        System.out.println();
        System.out.println("    " + BOLD + "[0]" + RESET + "  Exit Platform");
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    // ---------------- SUB-MENUS ----------------

    public static void printModule1Menu(String activeFile) {
        System.out.println();
        System.out.println("  " + BOLD + CYAN + "=== Module 1: String Algorithms (CO1) ===" + RESET);
        System.out.println("  Target Genome: " + GREEN + BOLD + activeFile + RESET + "\n");
        System.out.println("    [1] Knuth-Morris-Pratt (KMP) Exact DNA Motif Search");
        System.out.println("    [2] Z-Function (Repeated Nucleotide Patterns & Tandem Repeats)");
        System.out.println("    [3] Rabin-Karp Rolling Polynomial Hash Matching");
        System.out.println("    [4] Aho-Corasick Simultaneous Multi-Motif Detection");
        System.out.println("    [5] Run All Module 1 Algorithms (Comparative String Benchmark)");
        System.out.println("    [0] Back to Main Menu");
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    public static void printModule2Menu(String activeFile) {
        System.out.println();
        System.out.println("  " + BOLD + CYAN + "=== Module 2: Suffix Structures & Indexing (CO2) ===" + RESET);
        System.out.println("  Target Genome: " + GREEN + BOLD + activeFile + RESET + "\n");
        System.out.println("    [1] SA-IS (Linear-time Suffix Array Induced Sorting)");
        System.out.println("    [2] Suffix Array Binary Search (O(M log N) Query Index)");
        System.out.println("    [3] Kasai's LCP Algorithm (Genomic Maximal Repeats Analyzer)");
        System.out.println("    [4] Suffix Automaton / SAM (DAWG Substring Analysis & LCS)");
        System.out.println("    [5] Run All Module 2 Suffix Structures");
        System.out.println("    [0] Back to Main Menu");
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    public static void printModule3Menu(String activeFile) {
        System.out.println();
        System.out.println("  " + BOLD + CYAN + "=== Module 3: Advanced Dynamic Programming (CO3) ===" + RESET);
        System.out.println("  Target Genome: " + GREEN + BOLD + activeFile + RESET + "\n");
        System.out.println("    [1] Levenshtein & Damerau-Levenshtein (Polymerase Error Correction)");
        System.out.println("    [2] Needleman-Wunsch Global DNA Sequence Alignment");
        System.out.println("    [3] Smith-Waterman Local Conserved Domain Alignment");
        System.out.println("    [4] Bitmask DP (Optimal PCR Primer Superstring Assembly)");
        System.out.println("    [5] Run All Module 3 Dynamic Programming Algorithms");
        System.out.println("    [0] Back to Main Menu");
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    public static void printModule4Menu(String activeFile) {
        System.out.println();
        System.out.println("  " + BOLD + CYAN + "=== Module 4: Network Flow Resource Allocation (CO4) ===" + RESET);
        System.out.println("  Target Genome: " + GREEN + BOLD + activeFile + RESET + "\n");
        System.out.println("    [1] Ford-Fulkerson Algorithm (DFS Residual Laboratory Flow)");
        System.out.println("    [2] Edmonds-Karp Algorithm (BFS Shortest Augmenting Path Distribution)");
        System.out.println("    [3] Dinic's Algorithm (Level Graph & Blocking Flow for Cloud Sequencers)");
        System.out.println("    [4] Maximum Bipartite Matching (Researchers -> Sequencing Protocols)");
        System.out.println("    [5] Run All Module 4 Network Flow Algorithms");
        System.out.println("    [0] Back to Main Menu");
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    public static void printModule5Menu(String activeFile) {
        System.out.println();
        System.out.println("  " + BOLD + CYAN + "=== Module 5: NP-Completeness & Approximation (CO5) ===" + RESET);
        System.out.println("  Target Genome: " + GREEN + BOLD + activeFile + RESET + "\n");
        System.out.println("    [1] DPLL 3-SAT Solver (Biological Regulatory Network Consistency)");
        System.out.println("    [2] 3-SAT -> CLIQUE Reduction (Co-Expression Cluster Modeling)");
        System.out.println("    [3] CLIQUE -> INDEPENDENT-SET -> VERTEX-COVER Reduction Chain");
        System.out.println("    [4] Vertex-Cover 2-Approximation (Minimum Biological Assay Probe Coverage)");
        System.out.println("    [5] Run All Module 5 NP/Approx Algorithms");
        System.out.println("    [0] Back to Main Menu");
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    public static void printModule6Menu(String activeFile) {
        System.out.println();
        System.out.println("  " + BOLD + CYAN + "=== Module 6: Randomized & Parallel Algorithms (CO6) ===" + RESET);
        System.out.println("  Target Genome: " + GREEN + BOLD + activeFile + RESET + "\n");
        System.out.println("    [1] Randomized QuickSort (Genomic Variant Annotation Records)");
        System.out.println("    [2] Reservoir Sampling Algorithm R (Streaming Nanopore Reads)");
        System.out.println("    [3] Miller-Rabin Primality Test (Large Prime Modulus Generation)");
        System.out.println("    [4] Blelloch Work-Efficient Parallel Prefix Scan (OriC Finder)");
        System.out.println("    [5] Parallel Tree Reduction (Sequence Statistics & Shannon Entropy)");
        System.out.println("    [6] Brent's Theorem Analyzer (Parallel Scheduling & Speedup Bounds)");
        System.out.println("    [7] Run All Module 6 Algorithms");
        System.out.println("    [0] Back to Main Menu");
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    // ---------------- INDIVIDUAL ALGORITHM DETAILED REPORTS ----------------

    @SuppressWarnings("unchecked")
    public static void printKmpDetails(Map<String, Object> kmp, String targetMotif) {
        printHeader("Algorithm Deep-Dive: Knuth-Morris-Pratt (KMP)");
        System.out.println("  Algorithm:          " + GREEN + BOLD + kmp.get("algorithm") + RESET);
        System.out.println("  Target Motif:       " + YELLOW + BOLD + targetMotif + RESET);
        System.out.println("  Matches Found:      " + CYAN + BOLD + kmp.get("matchCount") + RESET);
        System.out.println("  Comparisons Made:   " + kmp.get("comparisons"));
        System.out.printf("  Execution Time:     %.4f ms\n", (Double) kmp.get("elapsedMs"));
        System.out.println("  Complexity:         " + MAGENTA + kmp.get("complexity") + RESET);
        List<Integer> matches = (List<Integer>) kmp.get("matches");
        if (!matches.isEmpty()) {
            System.out.print("  First 10 Match Loci: ");
            for (int i = 0; i < Math.min(10, matches.size()); i++) {
                System.out.print("bp " + matches.get(i) + (i < Math.min(10, matches.size()) - 1 ? ", " : ""));
            }
            if (matches.size() > 10) System.out.print(" ... (+ " + (matches.size() - 10) + " more)");
            System.out.println();
        }
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printZFuncDetails(Map<String, Object> z, List<Map<String, Object>> tandem, String motif) {
        printHeader("Algorithm Deep-Dive: Z-Algorithm & Tandem Repeats");
        System.out.println("  Algorithm:          " + GREEN + BOLD + z.get("algorithm") + RESET);
        System.out.println("  Target Motif:       " + YELLOW + BOLD + motif + RESET);
        System.out.println("  Matches Found:      " + CYAN + BOLD + z.get("matchCount") + RESET);
        System.out.printf("  Execution Time:     %.4f ms\n", (Double) z.get("elapsedMs"));
        System.out.println("  Complexity:         " + MAGENTA + z.get("complexity") + RESET);
        System.out.println("\n  " + BOLD + "Tandem Repeat & Periodic Structure Detection:" + RESET);
        if (tandem != null && !tandem.isEmpty()) {
            for (int i = 0; i < Math.min(5, tandem.size()); i++) {
                Map<String, Object> r = tandem.get(i);
                System.out.printf("    [%d] Repeat Unit: '%s' (Length: %s bp) | Copies: %s | Total Span: %s bp\n",
                    i + 1, r.get("unit"), r.get("unitLength"), r.get("copies"), r.get("spanLength"));
            }
        } else {
            System.out.println("    No significant tandem repeats in sampled window.");
        }
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printRabinKarpDetails(Map<String, Object> rk, String motif) {
        printHeader("Algorithm Deep-Dive: Rabin-Karp Rolling Hash");
        System.out.println("  Algorithm:          " + GREEN + BOLD + rk.get("algorithm") + RESET);
        System.out.println("  Target Motif:       " + YELLOW + BOLD + motif + RESET);
        System.out.println("  Matches Found:      " + CYAN + BOLD + rk.get("matchCount") + RESET);
        System.out.println("  Spurious Collisions:" + GREEN + " " + rk.get("collisions") + " (Zero false hits due to large 64-bit prime)" + RESET);
        System.out.printf("  Execution Time:     %.4f ms\n", (Double) rk.get("elapsedMs"));
        System.out.println("  Complexity:         " + MAGENTA + rk.get("complexity") + RESET);
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printAhoCorasickDetails(Map<String, Object> ac) {
        printHeader("Algorithm Deep-Dive: Aho-Corasick Multi-Motif Automaton");
        System.out.println("  Algorithm:          " + GREEN + BOLD + ac.get("algorithm") + RESET);
        System.out.println("  Total Matches:      " + CYAN + BOLD + ac.get("totalMatches") + RESET);
        System.out.printf("  Execution Time:     %.4f ms (Single Linear Pass!)\n", (Double) ac.get("elapsedMs"));
        System.out.println("  Complexity:         " + MAGENTA + ac.get("complexity") + RESET);
        System.out.println("\n  " + BOLD + "Breakdown per Restriction Enzyme / Biological Motif:" + RESET);
        Map<String, List<Integer>> res = (Map<String, List<Integer>>) ac.get("results");
        for (String pat : res.keySet()) {
            List<Integer> list = res.get(pat);
            System.out.printf("    • %-10s -> %4d hits\n", pat, list.size());
        }
        System.out.println();
    }

    // ---------------- SUMMARY MODULE REPORTS ----------------

    @SuppressWarnings("unchecked")
    public static void printM1Report(Map<String, Object> res) {
        printHeader("Module 1 (CO1): String Algorithms Benchmark");
        System.out.printf("  Target Motif: %s%s%s%s | Evaluated Window: %,d bp\n\n",
            YELLOW, BOLD, res.get("targetMotif"), RESET, (Integer) res.get("windowSize"));
        System.out.printf("  %s%-28s %-16s %-18s %-22s%s\n", BOLD, "Algorithm", "Matches Found", "Execution Time", "Complexity", RESET);
        System.out.println("  " + DIM + "-".repeat(85) + RESET);

        Map<String, Object> kmp = (Map<String, Object>) res.get("kmp");
        Map<String, Object> z = (Map<String, Object>) res.get("zFunction");
        Map<String, Object> rk = (Map<String, Object>) res.get("rabinKarp");
        Map<String, Object> ac = (Map<String, Object>) res.get("ahoCorasick");

        System.out.printf("  %-28s %-16s %-18.4f ms %-22s\n", "KMP", kmp.get("matchCount"), (Double) kmp.get("elapsedMs"), kmp.get("complexity"));
        System.out.printf("  %-28s %-16s %-18.4f ms %-22s\n", "Z-Function", z.get("matchCount"), (Double) z.get("elapsedMs"), z.get("complexity"));
        System.out.printf("  %-28s %-16s %-18.4f ms %-22s\n", "Rabin-Karp", rk.get("matchCount"), (Double) rk.get("elapsedMs"), rk.get("complexity"));
        System.out.printf("  %-28s %-16s %-18.4f ms %-22s\n", "Aho-Corasick (4 Motifs)", ac.get("totalMatches"), (Double) ac.get("elapsedMs"), ac.get("complexity"));
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printM2Report(Map<String, Object> res) {
        printHeader("Module 2 (CO2): Suffix Structures & Indexing");
        System.out.printf("  Evaluated Substring Window: %,d bp\n\n", (Integer) res.get("windowSize"));
        System.out.printf("  %s-> SA-IS Linear Suffix Array Construction:%s %.2f ms (Linear O(N))\n",
            GREEN, RESET, (Double) res.get("saisBuildMs"));

        Map<String, Object> saSearch = (Map<String, Object>) res.get("suffixArraySearch");
        System.out.printf("  %s-> Suffix Array Binary Search ('TATAAA'):%s %s hits in %.4f ms (O(M log N))\n",
            GREEN, RESET, saSearch.get("matchCount"), (Double) saSearch.get("elapsedMs"));

        Map<String, Object> kasai = (Map<String, Object>) res.get("kasaiLcp");
        System.out.printf("  %s-> Kasai's LCP Repeat Analyzer:%s Longest Repeat = %s bp ('%s') in %.2f ms\n",
            GREEN, RESET, kasai.get("longestLength"), kasai.get("longestRepeatedPattern"), (Double) kasai.get("elapsedMs"));

        Map<String, Object> lcs = (Map<String, Object>) res.get("suffixAutomatonLcs");
        System.out.printf("  %s-> Suffix Automaton (SAM):%s %,d states | LCS with probe = '%s' (len %s) in %.2f ms\n",
            GREEN, RESET, (Integer) res.get("suffixAutomatonStates"), lcs.get("lcsString"), lcs.get("lcsLength"), (Double) lcs.get("elapsedMs"));
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printM3Report(Map<String, Object> res) {
        printHeader("Module 3 (CO3): Advanced Dynamic Programming");
        System.out.printf("  Alignment Window: %d bp\n\n", (Integer) res.get("alignWindow"));

        Map<String, Object> lev = (Map<String, Object>) res.get("levenshtein");
        Map<String, Object> dam = (Map<String, Object>) res.get("damerauLevenshtein");
        System.out.printf("  %s-> Levenshtein Distance:%s %s edits (%.1f%% identity) in %.2f ms\n",
            CYAN, RESET, lev.get("distance"), ((Double) lev.get("similarityRatio")) * 100.0, (Double) lev.get("elapsedMs"));
        System.out.printf("  %s-> Damerau-Levenshtein:%s %s edits (with Polymerase Transpositions) in %.2f ms\n",
            CYAN, RESET, dam.get("distance"), (Double) dam.get("elapsedMs"));

        Map<String, Object> nw = (Map<String, Object>) res.get("needlemanWunsch");
        System.out.printf("\n  %s-> Needleman-Wunsch (Global Alignment):%s Score: %s | Identity: %.1f%% in %.2f ms\n",
            GREEN, RESET, nw.get("score"), (Double) nw.get("identityPercentage"), (Double) nw.get("elapsedMs"));

        Map<String, Object> sw = (Map<String, Object>) res.get("smithWaterman");
        System.out.printf("\n  %s-> Smith-Waterman (Local Alignment):%s Max Score: %s | Match: %.1f%% in %.2f ms\n",
            GREEN, RESET, sw.get("maxScore"), (Double) sw.get("identityPercentage"), (Double) sw.get("elapsedMs"));

        Map<String, Object> bm = (Map<String, Object>) res.get("bitmaskDp");
        System.out.printf("\n  %s-> Bitmask DP Fragment Assembly:%s Assembled %s primers from %s bp into %s bp (Saved: %s bp) in %.2f ms (O(2^N * N^2))\n",
            MAGENTA, RESET, bm.get("numFragments"), bm.get("originalTotalLength"), bm.get("assembledLength"), bm.get("nucleotidesSaved"), (Double) bm.get("elapsedMs"));
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printM4Report(Map<String, Object> res) {
        printHeader("Module 4 (CO4): Network Flow Laboratory Resource Allocation");
        System.out.printf("  %s%-30s %-22s %-18s %-18s%s\n", BOLD, "Algorithm", "Max Flow Throughput", "Elapsed Time", "Complexity", RESET);
        System.out.println("  " + DIM + "-".repeat(85) + RESET);

        Map<String, Object> ff = (Map<String, Object>) res.get("fordFulkerson");
        Map<String, Object> ek = (Map<String, Object>) res.get("edmondsKarp");
        Map<String, Object> din = (Map<String, Object>) res.get("dinic");

        System.out.printf("  %-30s %-22s %-18.4f ms %-18s\n", "Ford-Fulkerson", ff.get("maxFlow") + " units/hr", (Double) ff.get("elapsedMs"), ff.get("complexity"));
        System.out.printf("  %-30s %-22s %-18.4f ms %-18s\n", "Edmonds-Karp", ek.get("maxFlow") + " samples/day", (Double) ek.get("elapsedMs"), ek.get("complexity"));
        System.out.printf("  %-30s %-22s %-18.4f ms %-18s\n", "Dinic's Algorithm", din.get("maxFlow") + " gb/sec", (Double) din.get("elapsedMs"), din.get("complexity"));
        System.out.println("  " + DIM + "-".repeat(85) + RESET);

        Map<String, Object> bp = (Map<String, Object>) res.get("bipartiteMatching");
        System.out.printf("\n  %s-> Maximum Bipartite Matching (Researchers -> Protocols):%s\n", GREEN, RESET);
        System.out.printf("    Matched: %s/%s researchers to protocols in %.4f ms\n",
            bp.get("matchedPairsCount"), bp.get("totalResearchers"), (Double) bp.get("elapsedMs"));
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printM5Report(Map<String, Object> res) {
        printHeader("Module 5 (CO5): NP-Completeness & Approximation");
        Map<String, Object> sat = (Map<String, Object>) res.get("dpllSat");
        boolean isSat = (Boolean) sat.get("satisfiable");
        String status = isSat ? GREEN + "SATISFIABLE (Consistent Expression)" + RESET : RED + "UNSATISFIABLE" + RESET;
        System.out.printf("  %s-> DPLL 3-SAT Biological Constraints:%s Status = %s (%.2f ms)\n",
            CYAN, RESET, status, (Double) sat.get("elapsedMs"));

        Map<String, Object> clique = (Map<String, Object>) res.get("satToClique");
        System.out.printf("\n  %s-> 3-SAT -> CLIQUE Reduction:%s Constructed G=(V,E) with %s vertices, %s edges in %.2f ms\n",
            MAGENTA, RESET, clique.get("graphVerticesCount"), clique.get("graphEdgesCount"), (Double) clique.get("elapsedMs"));

        Map<String, Object> vc = (Map<String, Object>) res.get("reductionVc");
        System.out.printf("\n  %s-> CLIQUE -> INDEPENDENT SET -> VERTEX COVER Chain:%s\n", MAGENTA, RESET);
        System.out.printf("    Max Compatible Mutation Set: %s\n", vc.get("independentSet"));
        System.out.printf("    Min Knockout Intervention Set: %s\n", vc.get("vertexCover"));

        Map<String, Object> approx = (Map<String, Object>) res.get("vertexCover2approx");
        System.out.printf("\n  %s-> Vertex-Cover 2-Approximation (Maximal Matching):%s\n", YELLOW, RESET);
        System.out.printf("    Cover Size: %s nodes (Guarantee <= %s) in %.4f ms\n",
            approx.get("coverSize"), approx.get("upperBoundGuarantee"), (Double) approx.get("elapsedMs"));
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printM6Report(Map<String, Object> res) {
        printHeader("Module 6 (CO6): Randomized & Parallel Algorithms");
        Map<String, Object> qs = (Map<String, Object>) res.get("randomizedQuicksort");
        System.out.printf("  %s-> Randomized QuickSort:%s Sorted %,d genomic records in %.2f ms (Expected O(N log N))\n",
            CYAN, RESET, (Integer) qs.get("recordsSortedCount"), (Double) qs.get("elapsedMs"));

        Map<String, Object> rs = (Map<String, Object>) res.get("reservoirSampling");
        System.out.printf("  %s-> Reservoir Sampling (Algorithm R):%s Sampled %s items from %,d stream bases in %.2f ms\n",
            CYAN, RESET, rs.get("reservoirSizeK"), (Integer) rs.get("totalStreamItemsProcessed"), (Double) rs.get("elapsedMs"));

        Map<String, Object> mr = (Map<String, Object>) res.get("millerRabin");
        System.out.printf("  %s-> Miller-Rabin Primality:%s Certified %s-bit prime (%s) in %.2f ms\n",
            CYAN, RESET, mr.get("bitLength"), mr.get("prime"), (Double) mr.get("elapsedMs"));

        Map<String, Object> bs = (Map<String, Object>) res.get("blellochScan");
        System.out.printf("\n  %s-> Blelloch Work-Efficient Parallel Prefix Scan (OriC Finder):%s\n", GREEN, RESET);
        System.out.printf("    Evaluated %,d bp | Work: %,d ops | Depth: %s steps | OriC: bp %,d in %.2f ms\n",
            (Integer) bs.get("sequenceLength"), (Integer) bs.get("workOps"), bs.get("depthSteps"), (Integer) bs.get("oriCCandidate"), (Double) bs.get("elapsedMs"));

        Map<String, Object> pr = (Map<String, Object>) res.get("parallelReduce");
        System.out.printf("\n  %s-> Parallel Tree Reduction:%s %,d bp | GC Content: %.2f%% | Shannon Entropy: %.4f bits in %.2f ms\n",
            GREEN, RESET, (Long) pr.get("sequenceLength"), (Double) pr.get("gcContentPct"), (Double) pr.get("shannonEntropy"), (Double) pr.get("elapsedMs"));

        Map<String, Object> bt = (Map<String, Object>) res.get("brentsTheorem");
        System.out.printf("\n  %s-> Brent's Theorem Bounds (T_p <= (W - D)/p + D):%s Max Speedup = %.2fx in %.2f ms\n",
            MAGENTA, RESET, (Double) bt.get("maximumSpeedup"), (Double) bt.get("elapsedMs"));
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    public static void printBestAlgorithmDashboard(Map<String, Object> evalData) {
        printHeader("[DECISION MATRIX] DEFINITIVE 'BEST ALGORITHM' ANALYSIS");

        Map<String, Object> champ = (Map<String, Object>) evalData.get("overallChampion");
        System.out.println("  " + YELLOW + BOLD + "=".repeat(85));
        System.out.printf("    OVERALL CHAMPION ALGORITHM FOR '%s'\n", evalData.get("selectedGenome"));
        System.out.printf("    [#1 WINNER] %s\n", champ.get("name"));
        System.out.println("  " + "=".repeat(85) + RESET);
        System.out.printf("    * Multi-Criteria Score:    %s%s%.1f / 100%s\n", GREEN, BOLD, (Double) champ.get("mcdaScore"), RESET);
        System.out.printf("    * Execution Latency:       %s%.4f ms%s\n", CYAN, (Double) champ.get("executionTimeMs"), RESET);
        System.out.printf("    * Theoretical Complexity:  %s%s%s\n", MAGENTA, champ.get("theoreticalComplexity"), RESET);
        System.out.printf("    * Auxiliary Space:         %s%s%s\n", BLUE, champ.get("auxiliarySpace"), RESET);
        System.out.printf("\n    %sScientific & Empirical Justification:%s\n", BOLD, RESET);
        System.out.printf("    %s\n\n", champ.get("scientificRationale"));

        System.out.printf("  %s%-28s %-12s %-10s %-10s %-12s %-15s%s\n", BOLD, "Search Candidate", "Time (ms)", "Space", "Latency", "MCDA Score", "Verdict", RESET);
        System.out.println("  " + DIM + "-".repeat(85) + RESET);

        List<DecisionMatrix.CandidateScore> list = (List<DecisionMatrix.CandidateScore>) evalData.get("searchBenchmarkTable");
        DecisionMatrix.CandidateScore best = (DecisionMatrix.CandidateScore) evalData.get("bestSearchAlgorithm");

        for (DecisionMatrix.CandidateScore c : list) {
            boolean isWinner = c.name.equals(best.name);
            String verdict = isWinner ? GREEN + "[WINNER]" + RESET : DIM + "Sub-optimal" + RESET;
            String marker = isWinner ? YELLOW + ">>" + RESET : "  ";
            System.out.printf("  %s %-26s %-12.4f %-10s %-10d %-12.1f %s\n",
                marker, c.name, c.timeMs, c.spaceComplexity, c.latencyScore, c.mcdaScore, verdict);
        }
        System.out.println("  " + DIM + "-".repeat(85) + RESET);

        System.out.println("\n  " + BOLD + CYAN + "Category-by-Category Champions Matrix:" + RESET);
        System.out.printf("  %s%-32s %-32s %-25s%s\n", BOLD, "Category", "Champion Algorithm", "Deciding Factor", RESET);
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.printf("  %-32s %-32s %-25s\n", "Exact Single Motif Search", best.name, "O(N+M) or O(M log N) query");
        System.out.printf("  %-32s %-32s %-25s\n", "Multi-Motif Batch Search", "Aho-Corasick Automaton", "Single-pass O(N + sum(M_i))");
        System.out.printf("  %-32s %-32s %-25s\n", "Genome Indexing & Repeat", "SA-IS + Kasai LCP", "Linear O(N) indexing");
        System.out.printf("  %-32s %-32s %-25s\n", "Sequence Domain Alignment", evalData.get("bestDpAlgorithm"), "Conserved local domains");
        System.out.printf("  %-32s %-32s %-25s\n", "Resource Network Flow", evalData.get("bestFlowAlgorithm"), "Blocking flow O(V^2 E)");
        System.out.printf("  %-32s %-32s %-25s\n", "NP Combinatorial Coverage", "Vertex-Cover 2-Approx", "Guaranteed <= 2*OPT");
        System.out.printf("  %-32s %-32s %-25s\n", "Parallel Big Data Aggregator", evalData.get("bestParallelAlgorithm"), "Work-efficient O(N)");
        System.out.println("  " + DIM + "-".repeat(85) + RESET);
        System.out.println();
    }
}
