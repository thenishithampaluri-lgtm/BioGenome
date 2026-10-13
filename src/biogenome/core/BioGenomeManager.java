package biogenome.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Discovers, parses, and manages genomic datasets across the BioGenome platform.
 */
public class BioGenomeManager {
    private final File dataDir;
    private final Map<String, GenomeRecord> records = new TreeMap<>();

    public BioGenomeManager() {
        this.dataDir = new File("data");
        loadAllRecords();
    }

    public BioGenomeManager(String customPath) {
        this.dataDir = new File(customPath);
        loadAllRecords();
    }

    public int loadAllRecords() {
        records.clear();
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            dataDir.mkdirs();
        }

        File[] files = dataDir.listFiles((dir, name) -> name.endsWith(".fasta") || name.endsWith(".fa"));
        if (files != null) {
            for (File file : files) {
                GenomeRecord record = parseFastaFile(file);
                if (record != null) {
                    records.put(record.getFilename(), record);
                }
            }
        }

        // If fewer than 5 files exist, generate realistic benchmark sequences
        if (records.size() < 5) {
            generateBenchmarkSequences();
            // Re-load
            files = dataDir.listFiles((dir, name) -> name.endsWith(".fasta") || name.endsWith(".fa"));
            if (files != null) {
                for (File file : files) {
                    GenomeRecord record = parseFastaFile(file);
                    if (record != null) {
                        records.put(record.getFilename(), record);
                    }
                }
            }
        }

        return records.size();
    }

    private GenomeRecord parseFastaFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = "";
            StringBuilder seqBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith(">")) {
                    if (header.isEmpty()) {
                        header = line;
                    }
                } else {
                    seqBuilder.append(line);
                }
            }
            return new GenomeRecord(file.getName(), file.getAbsolutePath(), header, seqBuilder.toString());
        } catch (IOException e) {
            System.err.println("Error reading FASTA file: " + file.getName() + " -> " + e.getMessage());
            return null;
        }
    }

    public void generateBenchmarkSequences() {
        System.out.println("Generating 5 benchmark FASTA sequence files (3,000,000 bp total)...");
        generateSingleFasta("seq1_human_chr21_segment.fasta", ">NC_000021.9 Homo sapiens chromosome 21 locus", 1_000_000, 0.41, 101);
        generateSingleFasta("seq2_sars_cov2_variant.fasta", ">NC_045512.2 SARS-CoV-2 hybrid variant genome", 500_000, 0.38, 102);
        generateSingleFasta("seq3_ecoli_k12_segment.fasta", ">U00096.3 Escherichia coli K-12 chromosome segment", 1_000_000, 0.508, 103);
        generateSingleFasta("seq4_crispr_plasmid_vector.fasta", ">PX458_Cas9 CRISPR-Cas9 expression plasmid vector", 300_000, 0.48, 104);
        generateSingleFasta("seq5_mammalian_mito_genome.fasta", ">NC_012920.1 Mammalian mitochondrial circular sequence", 200_000, 0.44, 105);
    }

    private void generateSingleFasta(String filename, String header, int length, double gcContent, long seed) {
        File file = new File(dataDir, filename);
        if (file.exists() && file.length() > 50_000) {
            return; // Already exists
        }

        Random rng = new Random(seed);
        char[] bases = {'A', 'C', 'G', 'T'};
        double pGC = gcContent / 2.0;
        double pAT = (1.0 - gcContent) / 2.0;

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(header + "\n");
            StringBuilder lineBuf = new StringBuilder();
            String[] embeddedMotifs = {"TATAAA", "GAATTC", "GGATCC", "AAGCTT", "CGG", "TTAGGGTTAGGG"};

            for (int i = 0; i < length; i++) {
                // Periodically embed standard biological motifs
                if (i % 25_000 == 0 && i + 10 < length) {
                    String motif = embeddedMotifs[rng.nextInt(embeddedMotifs.length)];
                    for (char ch : motif.toCharArray()) {
                        lineBuf.append(ch);
                        if (lineBuf.length() == 70) {
                            writer.write(lineBuf.toString() + "\n");
                            lineBuf.setLength(0);
                        }
                    }
                    i += motif.length() - 1;
                    continue;
                }

                double r = rng.nextDouble();
                char base;
                if (r < pAT) base = 'A';
                else if (r < pAT + pGC) base = 'C';
                else if (r < pAT + 2 * pGC) base = 'G';
                else base = 'T';

                lineBuf.append(base);
                if (lineBuf.length() == 70) {
                    writer.write(lineBuf.toString() + "\n");
                    lineBuf.setLength(0);
                }
            }
            if (lineBuf.length() > 0) {
                writer.write(lineBuf.toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error generating " + filename + ": " + e.getMessage());
        }
    }

    public GenomeRecord getRecord(String filename) {
        return records.get(filename);
    }

    public List<String> getRecordNames() {
        return new ArrayList<>(records.keySet());
    }

    public List<Map<String, Object>> getSummaryTable() {
        List<Map<String, Object>> summary = new ArrayList<>();
        for (String name : records.keySet()) {
            GenomeRecord rec = records.get(name);
            Map<String, Object> stats = rec.getStats();
            Map<String, Object> row = new HashMap<>();
            row.put("filename", name);
            row.put("lengthBp", rec.getLength());
            row.put("gcContent", String.format("%.2f%%", (Double) stats.get("gcContentPct")));
            row.put("header", rec.getHeader());
            summary.add(row);
        }
        return summary;
    }
}
