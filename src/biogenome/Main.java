package biogenome;

import biogenome.core.BenchmarkEngine;
import biogenome.core.BioGenomeManager;
import biogenome.core.DecisionMatrix;
import biogenome.core.GenomeRecord;
import biogenome.m1.*;
import biogenome.m2.*;
import biogenome.m3.*;
import biogenome.m4.*;
import biogenome.m5.*;
import biogenome.m6.*;
import biogenome.ui.ConsoleView;

import java.util.*;

/**
 * BioGenome – High-Performance Genome Research & Analysis Platform
 * Main Java Application Entry Point with interactive sub-menus for all modules.
 */
public class Main {
    private final BioGenomeManager manager;
    private List<String> availableFiles;
    private String activeFilename;

    public Main() {
        this.manager = new BioGenomeManager();
        this.availableFiles = manager.getRecordNames();
        this.activeFilename = !availableFiles.isEmpty() ? availableFiles.get(0) : null;
    }

    public GenomeRecord getActiveRecord() {
        return manager.getRecord(activeFilename);
    }

    private void pause(Scanner scanner) {
        System.out.print("  " + ConsoleView.DIM + "Press Enter to continue..." + ConsoleView.RESET);
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        System.out.println();
    }

    public void selectDnaFile(Scanner scanner) {
        ConsoleView.printHeader("Select Active DNA Sequence File");
        List<Map<String, Object>> summary = manager.getSummaryTable();
        ConsoleView.printFilesTable(summary, activeFilename);

        System.out.println("  Enter file number to select as active (or press Enter to keep current):");
        System.out.print("  " + ConsoleView.CYAN + "Select [1-" + availableFiles.size() + "]: " + ConsoleView.RESET);
        String choice = scanner.nextLine().trim();
        if (choice.isEmpty()) return;

        try {
            int idx = Integer.parseInt(choice);
            if (idx >= 1 && idx <= availableFiles.size()) {
                activeFilename = availableFiles.get(idx - 1);
                System.out.println("\n  " + ConsoleView.GREEN + "+ Active genome switched to: " + ConsoleView.BOLD + activeFilename + ConsoleView.RESET + "\n");
            } else {
                System.out.println("\n  " + ConsoleView.RED + "Invalid selection." + ConsoleView.RESET + "\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  " + ConsoleView.RED + "Please enter a valid number." + ConsoleView.RESET + "\n");
        }
    }

    // =========================================================================
    // MODULE 1 SUB-MENU
    // =========================================================================
    @SuppressWarnings("unchecked")
    public void handleModule1Menu(Scanner scanner) {
        while (true) {
            ConsoleView.printModule1Menu(activeFilename);
            System.out.print("  " + ConsoleView.CYAN + ConsoleView.BOLD + "Enter Module 1 choice [0-5]: " + ConsoleView.RESET);
            String choice = scanner.hasNextLine() ? scanner.nextLine().trim() : "0";

            if ("0".equals(choice)) {
                System.out.println("\n  Returning to Main Menu...\n");
                return;
            }

            GenomeRecord rec = getActiveRecord();
            String searchWindow = (rec.getLength() > 100_000) ? rec.getSequence().substring(0, 100_000) : rec.getSequence();

            switch (choice) {
                case "1" -> {
                    System.out.print("  Enter target motif to search [Default: GAATTC]: ");
                    String m = scanner.nextLine().trim();
                    if (m.isEmpty()) m = "GAATTC";
                    Map<String, Object> kmp = KMPSearcher.search(searchWindow, m);
                    ConsoleView.printKmpDetails(kmp, m);
                    pause(scanner);
                }
                case "2" -> {
                    System.out.print("  Enter target motif for Z-function [Default: GAATTC]: ");
                    String m = scanner.nextLine().trim();
                    if (m.isEmpty()) m = "GAATTC";
                    Map<String, Object> z = ZAlgorithm.search(searchWindow, m);
                    List<Map<String, Object>> tandem = ZAlgorithm.findTandemRepeats(searchWindow.substring(0, Math.min(5000, searchWindow.length())), 4);
                    ConsoleView.printZFuncDetails(z, tandem, m);
                    pause(scanner);
                }
                case "3" -> {
                    System.out.print("  Enter target motif for Rabin-Karp [Default: GAATTC]: ");
                    String m = scanner.nextLine().trim();
                    if (m.isEmpty()) m = "GAATTC";
                    Map<String, Object> rk = RabinKarpSearcher.search(searchWindow, m);
                    ConsoleView.printRabinKarpDetails(rk, m);
                    pause(scanner);
                }
                case "4" -> {
                    System.out.println("\n  Running Aho-Corasick Multi-Motif Detection (EcoRI, BamHI, HindIII, TATA-Box)...");
                    AhoCorasickSearcher ac = new AhoCorasickSearcher(Arrays.asList("TATAAA", "GAATTC", "GGATCC", "AAGCTT"));
                    Map<String, Object> acRes = ac.search(searchWindow);
                    ConsoleView.printAhoCorasickDetails(acRes);
                    pause(scanner);
                }
                case "5" -> {
                    System.out.println("\n  Running Complete Module 1 Benchmark Pipeline on " + rec.getFilename() + "...");
                    BenchmarkEngine engine = new BenchmarkEngine(rec);
                    Map<String, Object> m1 = engine.runModule1("GAATTC");
                    ConsoleView.printM1Report(m1);
                    pause(scanner);
                }
                default -> System.out.println("\n  " + ConsoleView.RED + "Invalid choice. Please select 0 to 5." + ConsoleView.RESET + "\n");
            }
        }
    }

    // =========================================================================
    // MODULE 2 SUB-MENU
    // =========================================================================
    @SuppressWarnings("unchecked")
    public void handleModule2Menu(Scanner scanner) {
        while (true) {
            ConsoleView.printModule2Menu(activeFilename);
            System.out.print("  " + ConsoleView.CYAN + ConsoleView.BOLD + "Enter Module 2 choice [0-5]: " + ConsoleView.RESET);
            String choice = scanner.hasNextLine() ? scanner.nextLine().trim() : "0";

            if ("0".equals(choice)) {
                System.out.println("\n  Returning to Main Menu...\n");
                return;
            }

            GenomeRecord rec = getActiveRecord();
            int sampleSize = Math.min(rec.getLength(), 15_000);
            String window = rec.getSequence().substring(0, sampleSize);

            switch (choice) {
                case "1" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: SA-IS Linear Suffix Array");
                    long t0 = System.nanoTime();
                    int[] sa = SAIS.buildSuffixArray(window);
                    double ms = (System.nanoTime() - t0) / 1_000_000.0;
                    System.out.printf("  Input Window:        %,d bp\n", sampleSize);
                    System.out.printf("  SA-IS Build Time:    %.2f ms (Linear O(N) Induced Sorting)\n", ms);
                    System.out.println("  Suffix Array Length: " + sa.length);
                    System.out.println("\n  Top 5 Sorted Suffix Loci & Suffix Previews:");
                    for (int i = 0; i < Math.min(5, sa.length); i++) {
                        int pos = sa[i];
                        String preview = window.substring(pos, Math.min(window.length(), pos + 15));
                        System.out.printf("    sa[%d] = %6d -> '%s...'\n", i, pos, preview);
                    }
                    System.out.println();
                    pause(scanner);
                }
                case "2" -> {
                    System.out.print("  Enter pattern to binary search in Suffix Array [Default: TATAAA]: ");
                    String p = scanner.nextLine().trim();
                    if (p.isEmpty()) p = "TATAAA";
                    SuffixArrayIndex saIdx = new SuffixArrayIndex(window);
                    Map<String, Object> res = saIdx.search(p);
                    ConsoleView.printHeader("Algorithm Deep-Dive: Suffix Array Binary Search");
                    System.out.println("  Pattern:             " + ConsoleView.YELLOW + ConsoleView.BOLD + p + ConsoleView.RESET);
                    System.out.println("  Matches Found:       " + ConsoleView.CYAN + ConsoleView.BOLD + res.get("matchCount") + ConsoleView.RESET);
                    System.out.printf("  Query Latency:       %.4f ms (O(M log N))\n", (Double) res.get("elapsedMs"));
                    System.out.println("  Comparisons Made:    " + res.get("comparisons"));
                    System.out.println();
                    pause(scanner);
                }
                case "3" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Kasai's LCP Repeat Analyzer");
                    int[] sa = SAIS.buildSuffixArray(window);
                    Map<String, Object> kasai = KasaiLCP.analyzeRepeatedPatterns(window, sa, 5);
                    System.out.printf("  LCP Array Built:     %,d elements in %.2f ms\n", kasai.get("lcpLength"), (Double) kasai.get("elapsedMs"));
                    System.out.printf("  Maximal Repeat:      '%s' (Length: %s bp)\n", kasai.get("longestRepeatedPattern"), kasai.get("longestLength"));
                    System.out.println("  Repeats Discovered:  " + kasai.get("significantRepeatsFound"));
                    System.out.println();
                    pause(scanner);
                }
                case "4" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Suffix Automaton (SAM / DAWG)");
                    SuffixAutomaton sam = new SuffixAutomaton(window);
                    System.out.println("  Total States:        " + sam.getNumStates());
                    System.out.println("  Contains 'GAATTC':   " + (sam.containsSubstring("GAATTC") ? ConsoleView.GREEN + "YES" : ConsoleView.RED + "NO") + ConsoleView.RESET);
                    Map<String, Object> lcs = sam.longestCommonSubstring("ATGCGATCGTATAAAGCTAGCTA");
                    System.out.printf("  LCS with Probe:      '%s' (Length: %s bp) in %.4f ms\n", lcs.get("lcsString"), lcs.get("lcsLength"), (Double) lcs.get("elapsedMs"));
                    System.out.println();
                    pause(scanner);
                }
                case "5" -> {
                    BenchmarkEngine engine = new BenchmarkEngine(rec);
                    Map<String, Object> m2 = engine.runModule2(15_000);
                    ConsoleView.printM2Report(m2);
                    pause(scanner);
                }
                default -> System.out.println("\n  " + ConsoleView.RED + "Invalid choice. Please select 0 to 5." + ConsoleView.RESET + "\n");
            }
        }
    }

    // =========================================================================
    // MODULE 3 SUB-MENU
    // =========================================================================
    public void handleModule3Menu(Scanner scanner) {
        while (true) {
            ConsoleView.printModule3Menu(activeFilename);
            System.out.print("  " + ConsoleView.CYAN + ConsoleView.BOLD + "Enter Module 3 choice [0-5]: " + ConsoleView.RESET);
            String choice = scanner.hasNextLine() ? scanner.nextLine().trim() : "0";

            if ("0".equals(choice)) {
                System.out.println("\n  Returning to Main Menu...\n");
                return;
            }

            GenomeRecord rec = getActiveRecord();
            int alignLen = Math.min(rec.getLength(), 300);
            String s1 = rec.getSequence().substring(0, alignLen);
            char[] s2Chars = s1.toCharArray();
            for (int i = 5; i < Math.min(s2Chars.length, 30); i += 5) {
                s2Chars[i] = (s2Chars[i] != 'A') ? 'A' : 'T';
            }
            String s2 = new String(s2Chars);

            switch (choice) {
                case "1" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Levenshtein & Damerau-Levenshtein");
                    Map<String, Object> lev = SequenceErrorCorrector.levenshteinDistance(s1, s2);
                    Map<String, Object> dam = SequenceErrorCorrector.damerauLevenshteinDistance(s1, s2);
                    System.out.printf("  Levenshtein Distance:        %s edits (%.1f%% similarity) in %.2f ms\n",
                        lev.get("distance"), ((Double) lev.get("similarityRatio")) * 100.0, (Double) lev.get("elapsedMs"));
                    System.out.printf("  Damerau-Levenshtein Dist:    %s edits (accounting for adjacent transpositions) in %.2f ms\n",
                        dam.get("distance"), (Double) dam.get("elapsedMs"));
                    System.out.println();
                    pause(scanner);
                }
                case "2" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Needleman-Wunsch Global Alignment");
                    NeedlemanWunschAligner nw = new NeedlemanWunschAligner(2, -1, -2);
                    Map<String, Object> res = nw.align(s1, s2);
                    System.out.printf("  Global Score:        %s | Identity: %.1f%% in %.2f ms\n",
                        res.get("score"), (Double) res.get("identityPercentage"), (Double) res.get("elapsedMs"));
                    System.out.println("\n  Alignment Visualization (First 50 bp):");
                    System.out.println("    S1: " + ((String) res.get("alignedSeq1")).substring(0, Math.min(50, ((String) res.get("alignedSeq1")).length())));
                    System.out.println("    AL: " + ((String) res.get("alignmentSymbol")).substring(0, Math.min(50, ((String) res.get("alignmentSymbol")).length())));
                    System.out.println("    S2: " + ((String) res.get("alignedSeq2")).substring(0, Math.min(50, ((String) res.get("alignedSeq2")).length())));
                    System.out.println();
                    pause(scanner);
                }
                case "3" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Smith-Waterman Local Alignment");
                    SmithWatermanAligner sw = new SmithWatermanAligner(3, -2, -2);
                    Map<String, Object> res = sw.align(s1, s2);
                    System.out.printf("  Local Max Score:     %s | Conserved Match: %.1f%% in %.2f ms\n",
                        res.get("maxScore"), (Double) res.get("identityPercentage"), (Double) res.get("elapsedMs"));
                    System.out.printf("  Conserved Domain:    Locus [%s..%s]\n", res.get("localStartSeq1"), res.get("localEndSeq1"));
                    System.out.println("  Extracted Sequence:  " + res.get("alignedSeq1"));
                    System.out.println();
                    pause(scanner);
                }
                case "4" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Bitmask DP Primer Fragment Assembly");
                    List<String> fragments = new ArrayList<>();
                    for (int i = 0; i < 7; i++) {
                        if (i * 12 + 15 <= rec.getLength()) {
                            fragments.add(rec.getSequence().substring(i * 12, i * 12 + 15));
                        }
                    }
                    Map<String, Object> res = BitmaskSequenceOptimizer.assembleFragments(fragments);
                    System.out.printf("  Input PCR Primers:   %s fragments (Total Length: %s bp)\n", res.get("numFragments"), res.get("originalTotalLength"));
                    System.out.printf("  Assembled Length:    %s bp (Saved: %s overlapping bp) in %.2f ms\n", res.get("assembledLength"), res.get("nucleotidesSaved"), (Double) res.get("elapsedMs"));
                    System.out.println("  Assembled Sequence:  " + res.get("assembledSequence"));
                    System.out.println("  Complexity:          " + res.get("complexity"));
                    System.out.println();
                    pause(scanner);
                }
                case "5" -> {
                    BenchmarkEngine engine = new BenchmarkEngine(rec);
                    Map<String, Object> m3 = engine.runModule3(300);
                    ConsoleView.printM3Report(m3);
                    pause(scanner);
                }
                default -> System.out.println("\n  " + ConsoleView.RED + "Invalid choice. Please select 0 to 5." + ConsoleView.RESET + "\n");
            }
        }
    }

    // =========================================================================
    // MODULE 4 SUB-MENU
    // =========================================================================
    @SuppressWarnings("unchecked")
    public void handleModule4Menu(Scanner scanner) {
        while (true) {
            ConsoleView.printModule4Menu(activeFilename);
            System.out.print("  " + ConsoleView.CYAN + ConsoleView.BOLD + "Enter Module 4 choice [0-5]: " + ConsoleView.RESET);
            String choice = scanner.hasNextLine() ? scanner.nextLine().trim() : "0";

            if ("0".equals(choice)) {
                System.out.println("\n  Returning to Main Menu...\n");
                return;
            }

            List<String> nodes = Arrays.asList(
                "BioBank_Intake", "DNA_Extractor_A", "DNA_Extractor_B",
                "Illumina_NovaSeq", "Oxford_Nanopore", "Bio_Archive"
            );
            int[][] edges = {
                {0, 1, 120}, {0, 2, 80},
                {1, 3, 90}, {1, 4, 40},
                {2, 3, 30}, {2, 4, 60},
                {3, 5, 110}, {4, 5, 90}
            };

            switch (choice) {
                case "1" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Ford-Fulkerson (DFS Residual Flow)");
                    FordFulkersonFlow ff = new FordFulkersonFlow(nodes.size(), nodes);
                    for (int[] e : edges) ff.addEdge(e[0], e[1], e[2]);
                    Map<String, Object> res = ff.computeMaxFlow(0, 5);
                    System.out.printf("  Max Laboratory Flow: %s units/hr in %.4f ms\n", res.get("maxFlow"), (Double) res.get("elapsedMs"));
                    System.out.println("  Augmenting Paths:    " + res.get("augmentingPaths"));
                    System.out.println("  Complexity:          " + res.get("complexity"));
                    System.out.println();
                    pause(scanner);
                }
                case "2" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Edmonds-Karp (BFS Shortest Augmenting Path)");
                    EdmondsKarpFlow ek = new EdmondsKarpFlow(nodes.size(), nodes);
                    for (int[] e : edges) ek.addEdge(e[0], e[1], e[2]);
                    Map<String, Object> res = ek.computeMaxFlow(0, 5);
                    System.out.printf("  Sample Distribution: %s samples/day in %.4f ms\n", res.get("maxFlow"), (Double) res.get("elapsedMs"));
                    System.out.println("  Augmenting Paths:    " + res.get("augmentingPaths"));
                    System.out.println("  Complexity:          " + res.get("complexity"));
                    System.out.println();
                    pause(scanner);
                }
                case "3" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Dinic's Algorithm (Level Graph & Blocking Flow)");
                    DinicFlow din = new DinicFlow(nodes.size(), nodes);
                    for (int[] e : edges) din.addEdge(e[0], e[1], e[2]);
                    Map<String, Object> res = din.computeMaxFlow(0, 5);
                    System.out.printf("  Sequencer Net Flow:  %s gb/sec in %.4f ms\n", res.get("maxFlow"), (Double) res.get("elapsedMs"));
                    System.out.println("  Level Graph Phases:  " + res.get("levelPhases"));
                    System.out.println("  Complexity:          " + res.get("complexity"));
                    System.out.println();
                    pause(scanner);
                }
                case "4" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Maximum Bipartite Matching");
                    List<String> researchers = Arrays.asList("Dr. Rosalind (Nanopore)", "Dr. Francis (CRISPR)", "Dr. Barbara (Cytogenetics)", "Dr. Craig (Shotgun)");
                    List<String> experiments = Arrays.asList("Targeted Nanopore Sequencing", "CRISPR Off-target Screen", "Karyotype Analysis", "Whole Genome Assembly");
                    ResearcherExperimentMatcher m = new ResearcherExperimentMatcher(researchers, experiments);
                    m.addCompatibility(0, 0); m.addCompatibility(0, 3);
                    m.addCompatibility(1, 1); m.addCompatibility(2, 2);
                    m.addCompatibility(3, 0); m.addCompatibility(3, 3);
                    Map<String, Object> res = m.computeMatching();
                    System.out.printf("  Matched Protocols:   %s/%s researchers matched in %.4f ms\n\n",
                        res.get("matchedPairsCount"), res.get("totalResearchers"), (Double) res.get("elapsedMs"));
                    List<Map<String, String>> assignments = (List<Map<String, String>>) res.get("assignments");
                    for (Map<String, String> pair : assignments) {
                        System.out.printf("    + %-28s -> %s\n", pair.get("researcher"), pair.get("experiment"));
                    }
                    System.out.println();
                    pause(scanner);
                }
                case "5" -> {
                    BenchmarkEngine engine = new BenchmarkEngine(getActiveRecord());
                    Map<String, Object> m4 = engine.runModule4();
                    ConsoleView.printM4Report(m4);
                    pause(scanner);
                }
                default -> System.out.println("\n  " + ConsoleView.RED + "Invalid choice. Please select 0 to 5." + ConsoleView.RESET + "\n");
            }
        }
    }

    // =========================================================================
    // MODULE 5 SUB-MENU
    // =========================================================================
    @SuppressWarnings("unchecked")
    public void handleModule5Menu(Scanner scanner) {
        while (true) {
            ConsoleView.printModule5Menu(activeFilename);
            System.out.print("  " + ConsoleView.CYAN + ConsoleView.BOLD + "Enter Module 5 choice [0-5]: " + ConsoleView.RESET);
            String choice = scanner.hasNextLine() ? scanner.nextLine().trim() : "0";

            if ("0".equals(choice)) {
                System.out.println("\n  Returning to Main Menu...\n");
                return;
            }

            Map<Integer, String> varNames = new HashMap<>();
            varNames.put(1, "TP53_TumorSuppressor");
            varNames.put(2, "MDM2_Oncogene");
            varNames.put(3, "CDKN1A_CellCycle");
            varNames.put(4, "ATM_Kinase");

            List<List<Integer>> clauses = Arrays.asList(
                Arrays.asList(1, -2, 3),
                Arrays.asList(-1, 2, 4),
                Arrays.asList(-2, -3, 4)
            );

            switch (choice) {
                case "1" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: DPLL 3-SAT Biological Constraints");
                    BiologicalSATSolver sat = new BiologicalSATSolver(4, clauses, varNames);
                    Map<String, Object> res = sat.solve();
                    boolean isSat = (Boolean) res.get("satisfiable");
                    System.out.println("  Status:              " + (isSat ? ConsoleView.GREEN + "SATISFIABLE (Consistent Regulatory Network)" : ConsoleView.RED + "UNSATISFIABLE") + ConsoleView.RESET);
                    System.out.printf("  Execution Time:      %.2f ms\n\n", (Double) res.get("elapsedMs"));
                    System.out.println("  Biological Gene Expression States:");
                    Map<String, String> states = (Map<String, String>) res.get("geneExpressionState");
                    for (String g : states.keySet()) {
                        System.out.printf("    • %-26s -> %s\n", g, states.get(g));
                    }
                    System.out.println();
                    pause(scanner);
                }
                case "2" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: 3-SAT to CLIQUE Reduction");
                    SATToCliqueReducer cr = new SATToCliqueReducer(clauses, varNames);
                    Map<String, Object> res = cr.constructCliqueGraph();
                    System.out.println("  Target Clique Size:  k = " + res.get("targetCliqueSizeK"));
                    System.out.printf("  Graph Constructed:   %s vertices, %s edges in %.2f ms\n",
                        res.get("graphVerticesCount"), res.get("graphEdgesCount"), (Double) res.get("elapsedMs"));
                    System.out.println("  Biological Mapping:  " + res.get("biologicalMapping"));
                    System.out.println();
                    pause(scanner);
                }
                case "3" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: CLIQUE -> INDEPENDENT-SET -> VERTEX-COVER Chain");
                    List<int[]> edges = Arrays.asList(new int[]{0, 1}, new int[]{1, 2}, new int[]{2, 3}, new int[]{3, 0}, new int[]{0, 2});
                    List<String> labels = Arrays.asList(varNames.get(1), varNames.get(2), varNames.get(3), varNames.get(4));
                    GeneticConflictReductionPipeline pipe = new GeneticConflictReductionPipeline(4, edges, labels);
                    Map<String, Object> comp = pipe.computeComplementGraph();
                    Map<String, Object> vc = pipe.reduceIndependentSetToVertexCover(Arrays.asList(0, 3));
                    System.out.printf("  Complement Graph:    %s edges (down from %s original conflict edges)\n", comp.get("complementEdgesCount"), comp.get("originalEdgesCount"));
                    System.out.println("  Independent Set (Max Compatible Mutation Set): " + vc.get("independentSet"));
                    System.out.println("  Vertex Cover (Min Knockout Intervention Set):   " + vc.get("vertexCover"));
                    System.out.println();
                    pause(scanner);
                }
                case "4" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Vertex-Cover 2-Approximation");
                    List<String> ppiNodes = Arrays.asList("BRCA1", "BRCA2", "RAD51", "PALB2", "CHEK2", "ATM");
                    List<int[]> ppiEdges = Arrays.asList(new int[]{0, 1}, new int[]{0, 2}, new int[]{1, 3}, new int[]{2, 4}, new int[]{3, 5}, new int[]{4, 5});
                    VertexCover2Approx vc = new VertexCover2Approx(ppiNodes.size(), ppiEdges, ppiNodes);
                    Map<String, Object> res = vc.computeApproximateCover();
                    System.out.printf("  Network Size:        %s nodes, %s edges\n", res.get("totalProteins"), res.get("totalEdges"));
                    System.out.printf("  Cover Size:          %s probes (Guarantee <= %s) in %.4f ms\n", res.get("coverSize"), res.get("upperBoundGuarantee"), (Double) res.get("elapsedMs"));
                    System.out.println("  Selected Probes:     " + res.get("selectedProbeNodes"));
                    System.out.println();
                    pause(scanner);
                }
                case "5" -> {
                    BenchmarkEngine engine = new BenchmarkEngine(getActiveRecord());
                    Map<String, Object> m5 = engine.runModule5();
                    ConsoleView.printM5Report(m5);
                    pause(scanner);
                }
                default -> System.out.println("\n  " + ConsoleView.RED + "Invalid choice. Please select 0 to 5." + ConsoleView.RESET + "\n");
            }
        }
    }

    // =========================================================================
    // MODULE 6 SUB-MENU
    // =========================================================================
    public void handleModule6Menu(Scanner scanner) {
        while (true) {
            ConsoleView.printModule6Menu(activeFilename);
            System.out.print("  " + ConsoleView.CYAN + ConsoleView.BOLD + "Enter Module 6 choice [0-7]: " + ConsoleView.RESET);
            String choice = scanner.hasNextLine() ? scanner.nextLine().trim() : "0";

            if ("0".equals(choice)) {
                System.out.println("\n  Returning to Main Menu...\n");
                return;
            }

            GenomeRecord rec = getActiveRecord();

            switch (choice) {
                case "1" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Randomized QuickSort");
                    List<RandomizedQuickSort.VariantRecord> raw = Arrays.asList(
                        new RandomizedQuickSort.VariantRecord("chr21", 502014, "G", "A", 98.4, "RUNX1"),
                        new RandomizedQuickSort.VariantRecord("chr21", 124010, "C", "T", 94.1, "APP"),
                        new RandomizedQuickSort.VariantRecord("chr21", 894320, "A", "G", 99.8, "SOD1"),
                        new RandomizedQuickSort.VariantRecord("chr21", 312450, "T", "C", 88.5, "DYRK1A"),
                        new RandomizedQuickSort.VariantRecord("chr21", 215090, "G", "C", 91.2, "ETS2")
                    );
                    List<RandomizedQuickSort.VariantRecord> testRecords = new ArrayList<>();
                    for (int i = 0; i < 200; i++) testRecords.addAll(raw);
                    Map<String, Object> res = RandomizedQuickSort.sort(testRecords);
                    System.out.printf("  Sorted %,d records in %.2f ms (Expected O(N log N))\n", res.get("recordsSortedCount"), (Double) res.get("elapsedMs"));
                    System.out.println();
                    pause(scanner);
                }
                case "2" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Reservoir Sampling (Algorithm R)");
                    ReservoirSampler sampler = new ReservoirSampler(10, 999L);
                    String streamWindow = rec.getSequence().substring(0, Math.min(50_000, rec.getLength()));
                    Map<String, Object> res = sampler.sampleStream(streamWindow);
                    System.out.printf("  Sampled %s bases from %,d stream items in %.2f ms (O(K) Space)\n",
                        res.get("reservoirSizeK"), (Integer) res.get("totalStreamItemsProcessed"), (Double) res.get("elapsedMs"));
                    System.out.println("  Sampled Reservoir:   " + res.get("sampledItems"));
                    System.out.println();
                    pause(scanner);
                }
                case "3" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Miller-Rabin Primality Generator");
                    Map<String, Object> res = MillerRabinPrimality.generateLargePrime(31, 15);
                    System.out.printf("  Certified Prime:     %s (Bit-length: %s) in %.2f ms\n", res.get("prime"), res.get("bitLength"), (Double) res.get("elapsedMs"));
                    System.out.printf("  Rounds Tested:       %s (Error Prob: %s)\n", res.get("roundsTested"), res.get("errorProbability"));
                    System.out.println();
                    pause(scanner);
                }
                case "4" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Blelloch Work-Efficient Parallel Scan");
                    String scanWindow = rec.getSequence().substring(0, Math.min(16_384, rec.getLength()));
                    Map<String, Object> res = BlellochScan.computeCumulativeGCSkew(scanWindow);
                    System.out.printf("  Sequence Window:     %,d bp\n", (Integer) res.get("sequenceLength"));
                    System.out.printf("  Work Operations:     %,d ops | Parallel Depth: %s steps in %.2f ms\n",
                        (Integer) res.get("workOps"), res.get("depthSteps"), (Double) res.get("elapsedMs"));
                    System.out.printf("  Replication Origin:  Candidate oriC located at bp %,d (Min Skew: %s)\n",
                        (Integer) res.get("oriCCandidate"), res.get("minSkew"));
                    System.out.println();
                    pause(scanner);
                }
                case "5" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Parallel Tree Reduction");
                    String reduceWindow = rec.getSequence().substring(0, Math.min(100_000, rec.getLength()));
                    Map<String, Object> res = ParallelGenomeReducer.reduceSequenceStats(reduceWindow, 2000);
                    System.out.printf("  Processed:           %,d bp in %.2f ms\n", (Long) res.get("sequenceLength"), (Double) res.get("elapsedMs"));
                    System.out.printf("  Base Composition:    A=%,d, C=%,d, G=%,d, T=%,d\n", res.get("countA"), res.get("countC"), res.get("countG"), res.get("countT"));
                    System.out.printf("  GC Content:          %.2f%% | Shannon Entropy: %.4f bits\n", (Double) res.get("gcContentPct"), (Double) res.get("shannonEntropy"));
                    System.out.println();
                    pause(scanner);
                }
                case "6" -> {
                    ConsoleView.printHeader("Algorithm Deep-Dive: Brent's Theorem Analyzer");
                    Map<String, Object> res = BrentsTheoremAnalyzer.analyzeParallelScaling(15_000L, 12, null);
                    System.out.printf("  Average Parallelism: %.2fx | Max Speedup: %.2fx\n", (Double) res.get("averageParallelism"), (Double) res.get("maximumSpeedup"));
                    System.out.printf("  Optimal Processors:  %s cores in %.2f ms\n", res.get("optimalProcessorCount"), (Double) res.get("elapsedMs"));
                    System.out.println();
                    pause(scanner);
                }
                case "7" -> {
                    BenchmarkEngine engine = new BenchmarkEngine(rec);
                    Map<String, Object> m6 = engine.runModule6();
                    ConsoleView.printM6Report(m6);
                    pause(scanner);
                }
                default -> System.out.println("\n  " + ConsoleView.RED + "Invalid choice. Please select 0 to 7." + ConsoleView.RESET + "\n");
            }
        }
    }

    // =========================================================================
    // FULL PIPELINE & BATCH MODES
    // =========================================================================
    @SuppressWarnings("unchecked")
    public void runAllModules() {
        GenomeRecord rec = getActiveRecord();
        System.out.println("\n  " + ConsoleView.YELLOW + ConsoleView.BOLD + "Executing Full 6-Module Analytics Pipeline on " +
            rec.getFilename() + " (" + String.format("%,d", rec.getLength()) + " bp)..." + ConsoleView.RESET + "\n");

        BenchmarkEngine engine = new BenchmarkEngine(rec);
        Map<String, Object> pipeline = engine.executeAllModules();

        ConsoleView.printM1Report((Map<String, Object>) pipeline.get("m1"));
        ConsoleView.printM2Report((Map<String, Object>) pipeline.get("m2"));
        ConsoleView.printM3Report((Map<String, Object>) pipeline.get("m3"));
        ConsoleView.printM4Report((Map<String, Object>) pipeline.get("m4"));
        ConsoleView.printM5Report((Map<String, Object>) pipeline.get("m5"));
        ConsoleView.printM6Report((Map<String, Object>) pipeline.get("m6"));

        System.out.printf("  %s%s+ Complete End-to-End Analytics Pipeline finished in %.2f ms.%s\n\n",
            ConsoleView.GREEN, ConsoleView.BOLD, (Double) pipeline.get("totalPipelineTimeMs"), ConsoleView.RESET);
    }

    public void displayBestAlgorithm() {
        GenomeRecord rec = getActiveRecord();
        System.out.println("\n  " + ConsoleView.YELLOW + "Evaluating all algorithms internally on active genome: " +
            ConsoleView.BOLD + rec.getFilename() + ConsoleView.RESET + "...\n");
        DecisionMatrix dm = new DecisionMatrix(rec);
        Map<String, Object> eval = dm.evaluateAndDetermineBest();
        ConsoleView.printBestAlgorithmDashboard(eval);
    }

    @SuppressWarnings("unchecked")
    public void runBatchAllFiles() {
        ConsoleView.printHeader("Batch Analytics Across All 5 Connected DNA Files");
        System.out.printf("  Beginning batch execution across %d genomes (3,000,000 bp total)...\n\n", availableFiles.size());

        List<Map<String, Object>> batchSummary = new ArrayList<>();
        long t0 = System.currentTimeMillis();

        int idx = 1;
        for (String fname : availableFiles) {
            GenomeRecord rec = manager.getRecord(fname);
            System.out.printf("  [%d/5] Processing %-35s (%,9d bp)...", idx++, fname, rec.getLength());
            DecisionMatrix dm = new DecisionMatrix(rec);
            Map<String, Object> res = dm.evaluateAndDetermineBest();
            Map<String, Object> champ = (Map<String, Object>) res.get("overallChampion");

            System.out.printf(" DONE in %7.2f ms | Best: %s\n", (Double) res.get("totalPipelineTimeMs"), champ.get("primaryAlgorithm"));

            Map<String, Object> row = new HashMap<>();
            row.put("filename", fname);
            row.put("length", rec.getLength());
            row.put("gc", String.format("%.1f%%", (Double) rec.getStats().get("gcContentPct")));
            row.put("timeMs", res.get("totalPipelineTimeMs"));
            row.put("bestAlgo", champ.get("primaryAlgorithm"));
            row.put("score", champ.get("mcdaScore"));
            batchSummary.add(row);
        }

        double elapsedSec = (System.currentTimeMillis() - t0) / 1000.0;
        System.out.printf("\n  %s%s+ Batch processing completed in %.2fs.%s\n\n", ConsoleView.GREEN, ConsoleView.BOLD, elapsedSec, ConsoleView.RESET);
        System.out.printf("  %-3s %-35s %-12s %-8s %-12s %-30s %-8s\n", "#", "Filename", "Length", "GC%", "Time (ms)", "Best Algorithm", "Score");
        System.out.println("  " + "-".repeat(115));
        idx = 1;
        for (Map<String, Object> row : batchSummary) {
            System.out.printf("  %-3d %-35s %,12d %-8s %-12.2f %-30s %-8.1f\n",
                idx++, row.get("filename"), (Integer) row.get("length"), row.get("gc"),
                (Double) row.get("timeMs"), row.get("bestAlgo"), (Double) row.get("score"));
        }
        System.out.println("  " + "-".repeat(115));
        System.out.println();
    }

    public void regenerateSequences() {
        manager.generateBenchmarkSequences();
        manager.loadAllRecords();
        availableFiles = manager.getRecordNames();
        System.out.println("\n  " + ConsoleView.GREEN + "+ Successfully verified/regenerated all sequence files." + ConsoleView.RESET + "\n");
    }

    public void runInteractive() {
        ConsoleView.printBanner();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            List<Map<String, Object>> summary = manager.getSummaryTable();
            ConsoleView.printFilesTable(summary, activeFilename);
            ConsoleView.printMainMenu(activeFilename);

            System.out.print("  " + ConsoleView.CYAN + ConsoleView.BOLD + "Enter your choice [0-11]: " + ConsoleView.RESET);
            String choice = "";
            if (scanner.hasNextLine()) {
                choice = scanner.nextLine().trim();
            } else {
                break;
            }

            switch (choice) {
                case "1" -> selectDnaFile(scanner);
                case "2" -> handleModule1Menu(scanner);
                case "3" -> handleModule2Menu(scanner);
                case "4" -> handleModule3Menu(scanner);
                case "5" -> handleModule4Menu(scanner);
                case "6" -> handleModule5Menu(scanner);
                case "7" -> handleModule6Menu(scanner);
                case "8" -> {
                    runAllModules();
                    pause(scanner);
                }
                case "9" -> {
                    displayBestAlgorithm();
                    pause(scanner);
                }
                case "10" -> {
                    runBatchAllFiles();
                    pause(scanner);
                }
                case "11" -> {
                    regenerateSequences();
                    pause(scanner);
                }
                case "0" -> {
                    System.out.println("\n  " + ConsoleView.GREEN + "Thank you for using BioGenome Platform. Goodbye!" + ConsoleView.RESET + "\n");
                    return;
                }
                default -> System.out.println("\n  " + ConsoleView.RED + "Invalid option '" + choice + "'. Please select between 0 and 11." + ConsoleView.RESET + "\n");
            }
        }
    }

    public static void main(String[] args) {
        Main app = new Main();

        if (args.length > 0) {
            String flag = args[0];
            if ("--demo".equalsIgnoreCase(flag)) {
                ConsoleView.printBanner();
                app.runAllModules();
                app.displayBestAlgorithm();
            } else if ("--best".equalsIgnoreCase(flag)) {
                ConsoleView.printBanner();
                app.displayBestAlgorithm();
            } else if ("--batch".equalsIgnoreCase(flag)) {
                ConsoleView.printBanner();
                app.runBatchAllFiles();
            } else {
                app.runInteractive();
            }
        } else {
            app.runInteractive();
        }
    }
}
