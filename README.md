# BioGenome – Genome Research & Analysis Platform (Java Edition)

**BioGenome** is a production-grade, modular genomic research platform built in **pure Java** using high-performance Data Structures and Algorithms (DSA). It handles massive DNA sequences (millions of nucleotides), genetic variant annotations, multi-motif detection, genome indexing, global/local sequence alignment, laboratory resource flow optimization, biological regulatory constraint solving, and parallel genomic analytics.

Designed specifically for **Eclipse IDE** and standard Java 17+ / Java 21+ / Java 25 environments.

---

## 🧬 Architectural Overview & DSA Module Mapping

The platform implements all algorithms across 6 modules, mapping directly to Course Outcomes (CO1 to CO6):

| Module | Focus Area | Algorithms Implemented | Biological Application | Complexity |
| :--- | :--- | :--- | :--- | :--- |
| **M1 (CO1)** | **String Algorithms** | • **KMP** (`KMPSearcher`)<br>• **Z-Function** (`ZAlgorithm`)<br>• **Rabin-Karp** (`RabinKarpSearcher`)<br>• **Aho-Corasick** (`AhoCorasickSearcher`) | • Exact DNA motif searching<br>• Repeated nucleotide & tandem repeat detection<br>• Rolling hash DNA sequence matching<br>• Simultaneous multi-motif (restriction enzyme) detection | • KMP: $O(N + M)$<br>• Z-Function: $O(N)$<br>• Rabin-Karp: $O(N + M)$ avg<br>• Aho-Corasick: $O(N + \sum M_i)$ |
| **M2 (CO2)** | **Suffix Structures & Indexing** | • **Suffix Array** (`SuffixArrayIndex`)<br>• **SA-IS** (`SAIS`)<br>• **Kasai's LCP** (`KasaiLCP`)<br>• **Suffix Automaton** (`SuffixAutomaton`) | • Genome indexing with binary search queries<br>• Linear-time suffix array construction<br>• Longest common prefix & maximal repeat discovery<br>• Minimal DAG substring analysis & Longest Common Substring | • SA-IS: $O(N)$ linear time<br>• Suffix Array Search: $O(M \log N)$<br>• Kasai LCP: $O(N)$<br>• SAM: $O(N)$ states & transitions |
| **M3 (CO3)** | **Advanced Dynamic Programming** | • **Levenshtein** (`SequenceErrorCorrector`)<br>• **Damerau-Levenshtein** (`SequenceErrorCorrector`)<br>• **Needleman-Wunsch** (`NeedlemanWunschAligner`)<br>• **Smith-Waterman** (`SmithWatermanAligner`)<br>• **Bitmask DP** (`BitmaskSequenceOptimizer`) | • Single nucleotide error quantification<br>• Sequencing polymerase transposition detection<br>• Global end-to-end DNA sequence alignment<br>• Local conserved catalytic domain alignment<br>• Multiplexed PCR primer fragment superstring assembly | • Levenshtein: $O(NM)$<br>• Needleman-Wunsch: $O(NM)$<br>• Smith-Waterman: $O(NM)$<br>• Bitmask DP: $O(2^K \cdot K^2)$ |
| **M4 (CO4)** | **Network Flow** | • **Ford-Fulkerson** (`FordFulkersonFlow`)<br>• **Edmonds-Karp** (`EdmondsKarpFlow`)<br>• **Dinic's Algorithm** (`DinicFlow`)<br>• **Maximum Bipartite Matching** (`ResearcherExperimentMatcher`) | • Laboratory sample pipeline throughput (DFS paths)<br>• Biological specimen distribution across biobanks (BFS paths)<br>• High-throughput multi-tier sequencing networks (blocking flows)<br>• Matching bioinformaticians to sequencing protocols | • Ford-Fulkerson: $O(E \cdot \|f\|)$<br>• Edmonds-Karp: $O(V E^2)$<br>• Dinic: $O(V^2 E)$<br>• Bipartite Matching: $O(V E)$ |
| **M5 (CO5)** | **NP-Completeness & Approximation** | • **DPLL 3-SAT Solver** (`BiologicalSATSolver`)<br>• **3-SAT $\to$ CLIQUE Reduction** (`SATToCliqueReducer`)<br>• **CLIQUE $\to$ IS $\to$ VC Chain** (`GeneticConflictReductionPipeline`)<br>• **Vertex-Cover 2-Approximation** (`VertexCover2Approx`) | • Boolean gene regulatory network consistency<br>• Co-expression gene cluster modeling<br>• Genetic conflict networks (max non-conflicting mutation set)<br>• Minimum biomarker assay sensor coverage | • DPLL: Backtracking with unit propagation<br>• Reductions: Polynomial-time $O(V^2)$<br>• 2-Approx: $O(V + E)$ linear time ($\le 2 \cdot \text{OPT}$) |
| **M6 (CO6)** | **Randomized & Parallel Algorithms** | • **Randomized QuickSort** (`RandomizedQuickSort`)<br>• **Reservoir Sampling** (`ReservoirSampler`)<br>• **Miller-Rabin Primality** (`MillerRabinPrimality`)<br>• **Blelloch Parallel Scan** (`BlellochScan`)<br>• **Parallel Tree Reduce** (`ParallelGenomeReducer`)<br>• **Brent's Theorem Analyzer** (`BrentsTheoremAnalyzer`) | • Variant call annotation sorting (avoids quadratic coordinate degradation)<br>• Unbiased uniform sampling from unbounded Nanopore read streams<br>• Certified large prime generation for rolling hashes<br>• Work-efficient cumulative GC skew & replication origin (*oriC*) discovery<br>• Multi-threaded sequence nucleotide statistics & Shannon entropy<br>• Multi-core processor scheduling bounds & speedup limits | • Rand QuickSort: $O(N \log N)$ expected<br>• Reservoir: $O(N)$ time, $O(K)$ space<br>• Miller-Rabin: $O(K \log^3 N)$<br>• Blelloch: $O(N)$ work, $O(\log N)$ depth<br>• Reduce: $O(N)$ work, $O(\log N)$ depth<br>• Brent's: $T_p \le (W - D)/p + D$ |

---

## 📁 5 Connected DNA Sequence Files (3,000,000 bp Total)

Located in `data/`:
1. `seq1_human_chr21_segment.fasta` (1,000,000 bp, ~40.9% GC) – Primary assembly locus of Human Chromosome 21.
2. `seq2_sars_cov2_variant.fasta` (500,000 bp, ~38.1% GC) – Expanded SARS-CoV-2 hybrid coronavirus genome.
3. `seq3_ecoli_k12_segment.fasta` (1,000,000 bp, ~50.7% GC) – Escherichia coli K-12 bacterial chromosome locus.
4. `seq4_crispr_plasmid_vector.fasta` (300,000 bp, ~48.0% GC) – Synthetic CRISPR-Cas9 expression vector.
5. `seq5_mammalian_mito_genome.fasta` (200,000 bp, ~44.0% GC) – Mammalian mitochondrial circular sequence.

---

## 🛠️ Running in Eclipse IDE

1. **Refresh Eclipse Workspace:**
   - In Eclipse's **Project Explorer**, right-click `DSA PROJECT-3` and click **Refresh** (or select the project and press **`F5`**).
2. **Launch Application:**
   - In the Project Explorer, expand `src` $\to$ `biogenome` $\to$ open `Main.java`.
   - Right-click `Main.java` $\to$ **Run As** $\to$ **Java Application** (or press the green **Run** button).
   - The interactive BioGenome menu will open directly in Eclipse's **Console** tab!

---

## 💻 Running via Command Line

### Compile:
```powershell
javac -d bin -sourcepath src (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

### Run Interactive Menu:
```powershell
java -cp bin biogenome.Main
```

### Run CLI Shortcut Modes:
* **Display "Best Algorithm" Decision Analysis:**
  ```powershell
  java -cp bin biogenome.Main --best
  ```
* **Run Full Automated Demonstration:**
  ```powershell
  java -cp bin biogenome.Main --demo
  ```
* **Run Batch Benchmark on All 5 DNA Files:**
  ```powershell
  java -cp bin biogenome.Main --batch
  ```

### Run Automated Unit Test Suite:
```powershell
java -cp bin biogenome.test.TestAllAlgorithms
```
*(Verifies all 22 algorithms across all 6 modules).*
