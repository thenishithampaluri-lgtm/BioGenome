package biogenome.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a parsed genomic FASTA record with sequence metadata and cached statistics.
 */
public class GenomeRecord {
    private final String filename;
    private final String filepath;
    private final String header;
    private final String sequence;
    private final int length;
    private Map<String, Object> statsCache = null;

    public GenomeRecord(String filename, String filepath, String header, String sequence) {
        this.filename = filename;
        this.filepath = filepath;
        this.header = header;
        this.sequence = sequence.toUpperCase();
        this.length = this.sequence.length();
    }

    public String getFilename() {
        return filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getHeader() {
        return header;
    }

    public String getSequence() {
        return sequence;
    }

    public int getLength() {
        return length;
    }

    public synchronized Map<String, Object> getStats() {
        if (statsCache != null) {
            return statsCache;
        }

        long countA = 0, countC = 0, countG = 0, countT = 0, countN = 0;
        for (int i = 0; i < length; i++) {
            char ch = sequence.charAt(i);
            switch (ch) {
                case 'A' -> countA++;
                case 'C' -> countC++;
                case 'G' -> countG++;
                case 'T' -> countT++;
                default -> countN++;
            }
        }

        long total = Math.max(1, length);
        double gcPct = ((double) (countC + countG) / total) * 100.0;
        double atPct = 100.0 - gcPct;

        statsCache = new HashMap<>();
        statsCache.put("length", length);
        statsCache.put("countA", countA);
        statsCache.put("countC", countC);
        statsCache.put("countG", countG);
        statsCache.put("countT", countT);
        statsCache.put("countN", countN);
        statsCache.put("gcContentPct", gcPct);
        statsCache.put("atContentPct", atPct);

        return statsCache;
    }

    public String getWindow(int start, int windowLength) {
        int safeStart = Math.max(0, Math.min(start, length - 1));
        int safeEnd = Math.min(length, safeStart + windowLength);
        return sequence.substring(safeStart, safeEnd);
    }
}
