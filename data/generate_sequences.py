"""
BioGenome Platform - Sequence Generator
Generates realistic genomic datasets in FASTA format with embedded biological motifs,
tandem repeats, restriction sites, and variable GC content.
"""

import os
import random
import time

MOTIFS = {
    "TATA_BOX": "TATAAA",
    "ECORI_SITE": "GAATTC",
    "BAMHI_SITE": "GGATCC",
    "HINDIII_SITE": "AAGCTT",
    "CRISPR_PAM": "CGG",
    "START_CODON": "ATG",
    "STOP_CODON": "TAA",
    "ZINC_FINGER": "TGCGTTCACTGC",
    "TELOMERE_REPEAT": "TTAGGG"
}

GENOME_SPECS = [
    {
        "filename": "seq1_human_chr21_segment.fasta",
        "header": ">NC_000021.9 Homo sapiens chromosome 21, GRCh38.p14 Primary Assembly segment",
        "length": 1_000_000,
        "gc_content": 0.41,
        "description": "Human Chromosome 21 high-density gene cluster (1 Million bp)"
    },
    {
        "filename": "seq2_sars_cov2_variant.fasta",
        "header": ">NC_045512.2 Severe acute respiratory syndrome coronavirus 2 isolate Wuhan-Hu-1/Omicron hybrid",
        "length": 500_000,
        "gc_content": 0.38,
        "description": "SARS-CoV-2 expanded coronavirus variant genome (500K bp)"
    },
    {
        "filename": "seq3_ecoli_k12_segment.fasta",
        "header": ">U00096.3 Escherichia coli str. K-12 substr. MG1655, complete genome locus",
        "length": 1_000_000,
        "gc_content": 0.508,
        "description": "Escherichia coli K-12 high-GC bacterial chromosome locus (1 Million bp)"
    },
    {
        "filename": "seq4_crispr_plasmid_vector.fasta",
        "header": ">PX458_Cas9_GFP Synthetic CRISPR-Cas9 cloning plasmid expression vector",
        "length": 300_000,
        "gc_content": 0.48,
        "description": "CRISPR-Cas9 gene editing plasmid vector (300K bp)"
    },
    {
        "filename": "seq5_mammalian_mito_genome.fasta",
        "header": ">NC_012920.1 Homo sapiens mitochondrion, complete expanded genome",
        "length": 200_000,
        "gc_content": 0.44,
        "description": "Mammalian mitochondrial circular genome segment (200K bp)"
    }
]


def generate_sequence(length: int, gc_content: float, seed: int = 42) -> str:
    """Generate a realistic DNA sequence with specified GC ratio and embedded biological motifs."""
    rng = random.Random(seed)
    
    # Calculate probabilities
    p_gc = gc_content / 2.0
    p_at = (1.0 - gc_content) / 2.0
    bases = ['A', 'C', 'G', 'T']
    weights = [p_at, p_gc, p_gc, p_at]
    
    # Generate bulk nucleotides in blocks for speed
    chunk_size = 50_000
    seq_parts = []
    generated = 0
    
    while generated < length:
        curr_len = min(chunk_size, length - generated)
        chunk = rng.choices(bases, weights=weights, k=curr_len)
        seq_parts.append("".join(chunk))
        generated += curr_len
        
    full_seq = list("".join(seq_parts))
    
    # Embed biological motifs at strategic locations
    motif_keys = list(MOTIFS.keys())
    num_motifs = max(20, length // 10_000)
    for _ in range(num_motifs):
        motif_name = rng.choice(motif_keys)
        motif_seq = MOTIFS[motif_name]
        pos = rng.randint(0, length - len(motif_seq) - 1)
        full_seq[pos:pos + len(motif_seq)] = list(motif_seq)
        
    # Embed a tandem repeat (e.g. telomeric TTAGGGTTAGGGTTAGGG)
    for _ in range(5):
        repeat = "TTAGGG" * 4
        pos = rng.randint(0, length - len(repeat) - 1)
        full_seq[pos:pos + len(repeat)] = list(repeat)
        
    return "".join(full_seq)


def save_fasta(filepath: str, header: str, sequence: str, line_width: int = 70):
    """Save sequence in standard FASTA format with 70 nucleotides per line."""
    with open(filepath, "w", encoding="utf-8") as f:
        f.write(header + "\n")
        for i in range(0, len(sequence), line_width):
            f.write(sequence[i:i + line_width] + "\n")


def generate_all_sequences(output_dir: str = None):
    """Generates all 5 DNA sequence files."""
    if output_dir is None:
        output_dir = os.path.dirname(os.path.abspath(__file__))
    os.makedirs(output_dir, exist_ok=True)
    
    print("=" * 70)
    print("BioGenome: Generating 5 Benchmark DNA Sequence Files (3,000,000 bp Total)")
    print("=" * 70)
    
    manifest = []
    start_total = time.time()
    for idx, spec in enumerate(GENOME_SPECS, 1):
        filename = spec["filename"]
        filepath = os.path.join(output_dir, filename)
        print(f"[{idx}/5] Generating {filename} ({spec['length']:,} bp, {spec['gc_content']*100:.1f}% GC)...", end="", flush=True)
        
        t0 = time.time()
        seq = generate_sequence(spec["length"], spec["gc_content"], seed=100 + idx * 7)
        save_fasta(filepath, spec["header"], seq)
        file_size_mb = os.path.getsize(filepath) / (1024 * 1024)
        t_elapsed = time.time() - t0
        
        print(f" DONE in {t_elapsed:.2f}s ({file_size_mb:.2f} MB)")
        manifest.append({
            "index": idx,
            "filename": filename,
            "path": filepath,
            "length": spec["length"],
            "size_mb": file_size_mb,
            "description": spec["description"]
        })
        
    print(f"\nAll 5 DNA files generated successfully in {time.time() - start_total:.2f}s")
    print(f"Stored in: {output_dir}")
    return manifest


if __name__ == "__main__":
    generate_all_sequences()
