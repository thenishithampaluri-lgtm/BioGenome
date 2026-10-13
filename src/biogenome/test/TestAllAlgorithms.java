package biogenome.test;

import biogenome.m1.*;
import biogenome.m2.*;
import biogenome.m3.*;
import biogenome.m4.*;
import biogenome.m5.*;
import biogenome.m6.*;

import java.util.*;

/**
 * Automated Java Test Suite verifying all algorithms across Modules M1 - M6.
 */
public class TestAllAlgorithms {

    private static void assertTrue(boolean condition, String testName) {
        if (!condition) {
            throw new AssertionError("FAILED: " + testName);
        }
        System.out.println("  [PASS] " + testName);
    }

    private static void assertEquals(Object expected, Object actual, String testName) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("FAILED: " + testName + " | Expected: " + expected + ", Actual: " + actual);
        }
        System.out.println("  [PASS] " + testName);
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("BioGenome: Verifying All Algorithms (Java Suite)");
        System.out.println("=================================================");

        // M1: String Algorithms
        System.out.println("\nTesting Module 1 (CO1: String Algorithms):");
        String text = "ACGTATAAAGCTATAAATGC";
        String motif = "TATAAA";
        Map<String, Object> kmp = KMPSearcher.search(text, motif);
        assertEquals(2, kmp.get("matchCount"), "KMP Match Count");

        Map<String, Object> z = ZAlgorithm.search(text, motif);
        assertEquals(2, z.get("matchCount"), "Z-Algorithm Match Count");

        Map<String, Object> rk = RabinKarpSearcher.search(text, motif);
        assertEquals(2, rk.get("matchCount"), "Rabin-Karp Match Count");

        AhoCorasickSearcher ac = new AhoCorasickSearcher(Arrays.asList("TATAAA", "GAATTC", "CGT"));
        Map<String, Object> acRes = ac.search("ACGTATAAAGAATTCGCT");
        assertEquals(3, acRes.get("totalMatches"), "Aho-Corasick Multi-Motif Count");

        // M2: Suffix Structures
        System.out.println("\nTesting Module 2 (CO2: Suffix Structures):");
        String s = "ACGTACGT";
        int[] sa = SAIS.buildSuffixArray(s);
        assertEquals(s.length(), sa.length, "SA-IS Suffix Array Length");

        SuffixArrayIndex saIdx = new SuffixArrayIndex(s);
        Map<String, Object> saSearch = saIdx.search("CGT");
        assertEquals(2, saSearch.get("matchCount"), "Suffix Array Binary Search Count");

        int[] lcp = KasaiLCP.buildLcpArray(s, sa);
        assertEquals(s.length(), lcp.length, "Kasai LCP Array Length");

        SuffixAutomaton sam = new SuffixAutomaton(s);
        assertTrue(sam.containsSubstring("CGTA"), "Suffix Automaton Substring Existence");
        Map<String, Object> lcs = sam.longestCommonSubstring("TTTACGTTT");
        assertEquals("TACGT", lcs.get("lcsString"), "Suffix Automaton LCS");

        // M3: Dynamic Programming
        System.out.println("\nTesting Module 3 (CO3: Advanced Dynamic Programming):");
        Map<String, Object> lev = SequenceErrorCorrector.levenshteinDistance("ACGTACGT", "ACTTACGT");
        assertEquals(1, lev.get("distance"), "Levenshtein Distance");

        Map<String, Object> dam = SequenceErrorCorrector.damerauLevenshteinDistance("ACGTACGT", "AGCTACGT");
        assertEquals(1, dam.get("distance"), "Damerau-Levenshtein Transposition Distance");

        NeedlemanWunschAligner nw = new NeedlemanWunschAligner(2, -1, -2);
        Map<String, Object> nwRes = nw.align("ACGT", "ACGT");
        assertEquals(8, nwRes.get("score"), "Needleman-Wunsch Global Score");

        SmithWatermanAligner sw = new SmithWatermanAligner(3, -2, -2);
        Map<String, Object> swRes = sw.align("AAAAACGTGGGG", "TTTTACGTCCCC");
        assertEquals("ACGT", swRes.get("alignedSeq1"), "Smith-Waterman Local Alignment Domain");

        Map<String, Object> bitmask = BitmaskSequenceOptimizer.assembleFragments(Arrays.asList("ACGT", "CGTA", "GTAC"));
        assertEquals("ACGTAC", bitmask.get("assembledSequence"), "Bitmask DP Superstring Assembly");

        // M4: Network Flow
        System.out.println("\nTesting Module 4 (CO4: Network Flow):");
        FordFulkersonFlow ff = new FordFulkersonFlow(4, null);
        ff.addEdge(0, 1, 10); ff.addEdge(0, 2, 10); ff.addEdge(1, 3, 5); ff.addEdge(2, 3, 10); ff.addEdge(1, 2, 5);
        assertEquals(15, ff.computeMaxFlow(0, 3).get("maxFlow"), "Ford-Fulkerson Max Flow");

        EdmondsKarpFlow ek = new EdmondsKarpFlow(4, null);
        ek.addEdge(0, 1, 10); ek.addEdge(0, 2, 10); ek.addEdge(1, 3, 5); ek.addEdge(2, 3, 10); ek.addEdge(1, 2, 5);
        assertEquals(15, ek.computeMaxFlow(0, 3).get("maxFlow"), "Edmonds-Karp Max Flow");

        DinicFlow din = new DinicFlow(4, null);
        din.addEdge(0, 1, 10); din.addEdge(0, 2, 10); din.addEdge(1, 3, 5); din.addEdge(2, 3, 10); din.addEdge(1, 2, 5);
        assertEquals(15, din.computeMaxFlow(0, 3).get("maxFlow"), "Dinic Max Flow");

        ResearcherExperimentMatcher matcher = new ResearcherExperimentMatcher(Arrays.asList("Alice", "Bob"), Arrays.asList("Exp1", "Exp2"));
        matcher.addCompatibility(0, 0); matcher.addCompatibility(1, 0); matcher.addCompatibility(1, 1);
        assertEquals(2, matcher.computeMatching().get("matchedPairsCount"), "Bipartite Matching Pairs");

        // M5: NP-Completeness & Approx
        System.out.println("\nTesting Module 5 (CO5: NP-Completeness & Approximation):");
        BiologicalSATSolver sat = new BiologicalSATSolver(2, Arrays.asList(Arrays.asList(1, 2), Arrays.asList(-1, 2)), null);
        assertTrue((Boolean) sat.solve().get("satisfiable"), "DPLL 3-SAT Satisfiability");

        SATToCliqueReducer clique = new SATToCliqueReducer(Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(-1, 2, -3)), null);
        assertEquals(6, clique.constructCliqueGraph().get("graphVerticesCount"), "3-SAT to CLIQUE Graph Vertices");

        VertexCover2Approx vc = new VertexCover2Approx(4, Arrays.asList(new int[]{0, 1}, new int[]{2, 3}), null);
        assertEquals(4, vc.computeApproximateCover().get("coverSize"), "Vertex Cover 2-Approx Cover Size");

        // M6: Randomized & Parallel
        System.out.println("\nTesting Module 6 (CO6: Randomized & Parallel):");
        List<RandomizedQuickSort.VariantRecord> recs = Arrays.asList(
            new RandomizedQuickSort.VariantRecord("chr1", 500, "A", "T", 99.0, "G1"),
            new RandomizedQuickSort.VariantRecord("chr1", 100, "G", "C", 85.0, "G2"),
            new RandomizedQuickSort.VariantRecord("chr1", 300, "C", "T", 90.0, "G3")
        );
        @SuppressWarnings("unchecked")
        List<RandomizedQuickSort.VariantRecord> sorted = (List<RandomizedQuickSort.VariantRecord>) RandomizedQuickSort.sort(recs).get("sortedRecords");
        assertTrue(sorted.get(0).position <= sorted.get(1).position, "Randomized QuickSort Order");
        assertEquals(100, sorted.get(0).position, "Randomized QuickSort First Element");

        ReservoirSampler sampler = new ReservoirSampler(3, 123L);
        Map<String, Object> sampleRes = sampler.sampleStream("ACGTACGTACGT");
        assertEquals(3, ((List<?>) sampleRes.get("sampledItems")).size(), "Reservoir Sample Size");

        assertTrue(MillerRabinPrimality.isPrime(1000000007L, 15), "Miller-Rabin Prime Check (True)");
        assertTrue(!MillerRabinPrimality.isPrime(1000000005L, 15), "Miller-Rabin Composite Check (False)");

        BlellochScan.ScanResult bScan = BlellochScan.inclusiveScan(new int[]{1, 2, 3, 4});
        assertEquals(10, bScan.array[3], "Blelloch Parallel Scan Sum");

        Map<String, Object> red = ParallelGenomeReducer.reduceSequenceStats("ACGTACGT", 4);
        assertEquals(8L, red.get("sequenceLength"), "Parallel Tree Reduce Length");

        Map<String, Object> brent = BrentsTheoremAnalyzer.analyzeParallelScaling(1000L, 10, null);
        assertTrue((Double) brent.get("maximumSpeedup") > 1.0, "Brent's Theorem Speedup");

        System.out.println("\n=================================================");
        System.out.println("SUCCESS: All Algorithm Tests Passed Flawlessly!");
        System.out.println("=================================================");
    }
}
