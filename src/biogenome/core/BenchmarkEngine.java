package biogenome.core;

import biogenome.m1.*;
import biogenome.m2.*;
import biogenome.m3.*;
import biogenome.m4.*;
import biogenome.m5.*;
import biogenome.m6.*;

import java.util.*;

/**
 * Orchestrates comprehensive algorithmic execution across all 6 modules
 * on a specified GenomeRecord.
 */
public class BenchmarkEngine {
    private final GenomeRecord record;
    private final String seq;
    private final int length;

    public BenchmarkEngine(GenomeRecord record) {
        this.record = record;
        this.seq = record.getSequence();
        this.length = record.getLength();
    }

    // ---------------- MODULE 1: String Algorithms ----------------
    public Map<String, Object> runModule1(String motif) {
        String searchWindow = (length > 100_000) ? seq.substring(0, 100_000) : seq;
        Map<String, Object> kmp = KMPSearcher.search(searchWindow, motif);
        Map<String, Object> z = ZAlgorithm.search(searchWindow, motif);
        Map<String, Object> rk = RabinKarpSearcher.search(searchWindow, motif);

        List<String> multiMotifs = Arrays.asList("TATAAA", "GAATTC", "GGATCC", "AAGCTT");
        AhoCorasickSearcher acSearcher = new AhoCorasickSearcher(multiMotifs);
        Map<String, Object> ac = acSearcher.search(searchWindow);

        List<Map<String, Object>> tandem = ZAlgorithm.findTandemRepeats(searchWindow.substring(0, Math.min(5000, searchWindow.length())), 4);

        Map<String, Object> result = new HashMap<>();
        result.put("module", "M1 - String Algorithms (CO1)");
        result.put("windowSize", searchWindow.length());
        result.put("targetMotif", motif);
        result.put("kmp", kmp);
        result.put("zFunction", z);
        result.put("rabinKarp", rk);
        result.put("ahoCorasick", ac);
        result.put("tandemRepeats", tandem);
        return result;
    }

    // ---------------- MODULE 2: Suffix Structures ----------------
    public Map<String, Object> runModule2(int sampleSize) {
        int actualSample = Math.min(length, sampleSize);
        String window = seq.substring(0, actualSample);

        long t0 = System.nanoTime();
        int[] sa = SAIS.buildSuffixArray(window);
        double saisTime = (System.nanoTime() - t0) / 1_000_000.0;

        SuffixArrayIndex saIndex = new SuffixArrayIndex(window);
        Map<String, Object> saSearch = saIndex.search("TATAAA");

        Map<String, Object> kasai = KasaiLCP.analyzeRepeatedPatterns(window, sa, 5);

        SuffixAutomaton sam = new SuffixAutomaton(window);
        boolean containsGaattc = sam.containsSubstring("GAATTC");
        Map<String, Object> lcs = sam.longestCommonSubstring("ATGCGATCGTATAAAGCTAGCTA");

        Map<String, Object> result = new HashMap<>();
        result.put("module", "M2 - Suffix Structures (CO2)");
        result.put("windowSize", actualSample);
        result.put("saisBuildMs", saisTime);
        result.put("suffixArraySearch", saSearch);
        result.put("kasaiLcp", kasai);
        result.put("suffixAutomatonStates", sam.getNumStates());
        result.put("suffixAutomatonContainsGaattc", containsGaattc);
        result.put("suffixAutomatonLcs", lcs);
        return result;
    }

    // ---------------- MODULE 3: Dynamic Programming ----------------
    public Map<String, Object> runModule3(int alignLen) {
        int actualLen = Math.min(length, alignLen);
        String s1 = seq.substring(0, actualLen);

        char[] s2Chars = s1.toCharArray();
        for (int i = 5; i < Math.min(s2Chars.length, 30); i += 5) {
            s2Chars[i] = (s2Chars[i] != 'A') ? 'A' : 'T';
        }
        String s2 = new String(s2Chars);

        Map<String, Object> lev = SequenceErrorCorrector.levenshteinDistance(s1, s2);
        Map<String, Object> dam = SequenceErrorCorrector.damerauLevenshteinDistance(s1, s2);

        NeedlemanWunschAligner nw = new NeedlemanWunschAligner(2, -1, -2);
        Map<String, Object> nwRes = nw.align(s1, s2);

        SmithWatermanAligner sw = new SmithWatermanAligner(3, -2, -2);
        Map<String, Object> swRes = sw.align(s1, s2);

        List<String> fragments = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (i * 12 + 15 <= length) {
                fragments.add(seq.substring(i * 12, i * 12 + 15));
            }
        }
        Map<String, Object> bitmask = BitmaskSequenceOptimizer.assembleFragments(fragments);

        Map<String, Object> result = new HashMap<>();
        result.put("module", "M3 - Dynamic Programming (CO3)");
        result.put("alignWindow", actualLen);
        result.put("levenshtein", lev);
        result.put("damerauLevenshtein", dam);
        result.put("needlemanWunsch", nwRes);
        result.put("smithWaterman", swRes);
        result.put("bitmaskDp", bitmask);
        return result;
    }

    // ---------------- MODULE 4: Network Flow ----------------
    public Map<String, Object> runModule4() {
        List<String> nodeNames = Arrays.asList(
            "BioBank_Intake", "DNA_Extractor_A", "DNA_Extractor_B",
            "Illumina_NovaSeq", "Oxford_Nanopore", "Bio_Archive"
        );

        int[][] edges = {
            {0, 1, 120}, {0, 2, 80},
            {1, 3, 90}, {1, 4, 40},
            {2, 3, 30}, {2, 4, 60},
            {3, 5, 110}, {4, 5, 90}
        };

        FordFulkersonFlow ff = new FordFulkersonFlow(nodeNames.size(), nodeNames);
        EdmondsKarpFlow ek = new EdmondsKarpFlow(nodeNames.size(), nodeNames);
        DinicFlow din = new DinicFlow(nodeNames.size(), nodeNames);

        for (int[] e : edges) {
            ff.addEdge(e[0], e[1], e[2]);
            ek.addEdge(e[0], e[1], e[2]);
            din.addEdge(e[0], e[1], e[2]);
        }

        Map<String, Object> ffRes = ff.computeMaxFlow(0, 5);
        Map<String, Object> ekRes = ek.computeMaxFlow(0, 5);
        Map<String, Object> dinRes = din.computeMaxFlow(0, 5);

        List<String> researchers = Arrays.asList(
            "Dr. Rosalind (Nanopore)", "Dr. Francis (CRISPR)", "Dr. Barbara (Cytogenetics)", "Dr. Craig (Shotgun)"
        );
        List<String> experiments = Arrays.asList(
            "Targeted Nanopore Sequencing", "CRISPR Off-target Screen", "Karyotype Analysis", "Whole Genome Assembly"
        );
        ResearcherExperimentMatcher matcher = new ResearcherExperimentMatcher(researchers, experiments);
        matcher.addCompatibility(0, 0);
        matcher.addCompatibility(0, 3);
        matcher.addCompatibility(1, 1);
        matcher.addCompatibility(2, 2);
        matcher.addCompatibility(3, 0);
        matcher.addCompatibility(3, 3);
        Map<String, Object> bpRes = matcher.computeMatching();

        Map<String, Object> result = new HashMap<>();
        result.put("module", "M4 - Network Flow (CO4)");
        result.put("fordFulkerson", ffRes);
        result.put("edmondsKarp", ekRes);
        result.put("dinic", dinRes);
        result.put("bipartiteMatching", bpRes);
        return result;
    }

    // ---------------- MODULE 5: NP-Completeness & Approx ----------------
    public Map<String, Object> runModule5() {
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

        BiologicalSATSolver sat = new BiologicalSATSolver(4, clauses, varNames);
        Map<String, Object> satRes = sat.solve();

        SATToCliqueReducer clique = new SATToCliqueReducer(clauses, varNames);
        Map<String, Object> cliqueRes = clique.constructCliqueGraph();

        List<int[]> conflictEdges = Arrays.asList(
            new int[]{0, 1}, new int[]{1, 2}, new int[]{2, 3}, new int[]{3, 0}, new int[]{0, 2}
        );
        List<String> labels = Arrays.asList(varNames.get(1), varNames.get(2), varNames.get(3), varNames.get(4));
        GeneticConflictReductionPipeline pipeline = new GeneticConflictReductionPipeline(4, conflictEdges, labels);
        Map<String, Object> compGraph = pipeline.computeComplementGraph();
        Map<String, Object> vcReduction = pipeline.reduceIndependentSetToVertexCover(Arrays.asList(0, 3));

        List<String> ppiNodes = Arrays.asList("BRCA1", "BRCA2", "RAD51", "PALB2", "CHEK2", "ATM");
        List<int[]> ppiEdges = Arrays.asList(
            new int[]{0, 1}, new int[]{0, 2}, new int[]{1, 3}, new int[]{2, 4}, new int[]{3, 5}, new int[]{4, 5}
        );
        VertexCover2Approx vcApprox = new VertexCover2Approx(ppiNodes.size(), ppiEdges, ppiNodes);
        Map<String, Object> vcRes = vcApprox.computeApproximateCover();

        Map<String, Object> result = new HashMap<>();
        result.put("module", "M5 - NP-Completeness (CO5)");
        result.put("dpllSat", satRes);
        result.put("satToClique", cliqueRes);
        result.put("complementGraph", compGraph);
        result.put("reductionVc", vcReduction);
        result.put("vertexCover2approx", vcRes);
        return result;
    }

    // ---------------- MODULE 6: Randomized & Parallel ----------------
    public Map<String, Object> runModule6() {
        List<RandomizedQuickSort.VariantRecord> raw = Arrays.asList(
            new RandomizedQuickSort.VariantRecord("chr21", 502014, "G", "A", 98.4, "RUNX1"),
            new RandomizedQuickSort.VariantRecord("chr21", 124010, "C", "T", 94.1, "APP"),
            new RandomizedQuickSort.VariantRecord("chr21", 894320, "A", "G", 99.8, "SOD1"),
            new RandomizedQuickSort.VariantRecord("chr21", 312450, "T", "C", 88.5, "DYRK1A"),
            new RandomizedQuickSort.VariantRecord("chr21", 215090, "G", "C", 91.2, "ETS2")
        );
        List<RandomizedQuickSort.VariantRecord> testRecords = new ArrayList<>();
        for (int i = 0; i < 200; i++) testRecords.addAll(raw);

        Map<String, Object> sortRes = RandomizedQuickSort.sort(testRecords);

        ReservoirSampler sampler = new ReservoirSampler(10, 999L);
        String streamWindow = seq.substring(0, Math.min(50_000, length));
        Map<String, Object> sampleRes = sampler.sampleStream(streamWindow);

        Map<String, Object> primeRes = MillerRabinPrimality.generateLargePrime(31, 15);

        String scanWindow = seq.substring(0, Math.min(16_384, length));
        Map<String, Object> scanRes = BlellochScan.computeCumulativeGCSkew(scanWindow);

        String reduceWindow = seq.substring(0, Math.min(100_000, length));
        Map<String, Object> reduceRes = ParallelGenomeReducer.reduceSequenceStats(reduceWindow, 2000);

        long workOps = (Integer) reduceRes.get("reductionOperations") + 10_000L;
        int depthSteps = (Integer) reduceRes.get("treeReductionDepth") + 5;
        Map<String, Object> brentRes = BrentsTheoremAnalyzer.analyzeParallelScaling(workOps, depthSteps, null);

        Map<String, Object> result = new HashMap<>();
        result.put("module", "M6 - Randomized & Parallel (CO6)");
        result.put("randomizedQuicksort", sortRes);
        result.put("reservoirSampling", sampleRes);
        result.put("millerRabin", primeRes);
        result.put("blellochScan", scanRes);
        result.put("parallelReduce", reduceRes);
        result.put("brentsTheorem", brentRes);
        return result;
    }

    public Map<String, Object> executeAllModules() {
        long t0 = System.nanoTime();
        Map<String, Object> m1 = runModule1("GAATTC");
        Map<String, Object> m2 = runModule2(15_000);
        Map<String, Object> m3 = runModule3(300);
        Map<String, Object> m4 = runModule4();
        Map<String, Object> m5 = runModule5();
        Map<String, Object> m6 = runModule6();

        double totalMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> pipeline = new HashMap<>();
        pipeline.put("genomeFile", record.getFilename());
        pipeline.put("genomeLength", length);
        pipeline.put("totalPipelineTimeMs", totalMs);
        pipeline.put("m1", m1);
        pipeline.put("m2", m2);
        pipeline.put("m3", m3);
        pipeline.put("m4", m4);
        pipeline.put("m5", m5);
        pipeline.put("m6", m6);
        return pipeline;
    }
}
