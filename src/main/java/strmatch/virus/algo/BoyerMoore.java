package strmatch.virus.algo;

import strmatch.util.BitLoader;
import strmatch.util.bitmap.Bitmap;
import strmatch.util.bitmap.SimpleBitmap;
import strmatch.virus.ScanResult;
import strmatch.virus.SimpleScanResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BoyerMoore implements BinAlgo {

    private final Map<String, Bitmap> virusPatterns;
    private final Map<String, int[]> badCharTable;

    public BoyerMoore() {
        virusPatterns = new HashMap<>();
        badCharTable = new HashMap<>();
    }

    @Override
    public void initPattern(String virus, Path file) throws IOException {
        Bitmap pattern = new SimpleBitmap();
        BitLoader.readFileAsBits(pattern, file);
        virusPatterns.put(virus, pattern);
        badCharTable.put(virus, buildBadCharTable(pattern));
    }

    @Override
    public ScanResult match(Path file) {
        ScanResult result = new SimpleScanResult();
        Bitmap text = new SimpleBitmap();

        try {
            BitLoader.readFileAsBits(text, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String filename = file.toString();
        for (Map.Entry<String, Bitmap> entry : virusPatterns.entrySet()) {
            String virusName = entry.getKey();
            Bitmap pattern = entry.getValue();
            int[] badChar = badCharTable.get(virusName);

            if (boyerMooreSearch(text, pattern, badChar)) {
                result.addMatch(filename, virusName);
            }
        }

        return result;
    }

    private int[] buildBadCharTable(Bitmap pattern) {
        int[] table = new int[2]; // Binary alphabet: 0 and 1
        int patternLength = pattern.length();

        for (int i = 0; i < table.length; i++) {
            table[i] = patternLength; // Default shift is the pattern length
        }

        for (int i = 0; i < patternLength - 1; i++) {
            table[pattern.get(i) ? 1 : 0] = patternLength - 1 - i;
        }

        return table;
    }

    private boolean boyerMooreSearch(Bitmap text, Bitmap pattern, int[] badChar) {
        int textLength = text.length();
        int patternLength = pattern.length();
        int skip;

        for (int i = 0; i <= textLength - patternLength; i += skip) {
            skip = 0;
            for (int j = patternLength - 1; j >= 0; j--) {
                if (text.get(i + j) != pattern.get(j)) {
                    skip = Math.max(1, badChar[text.get(i + j) ? 1 : 0] - (patternLength - 1 - j));
                    break;
                }
            }
            if (skip == 0) {
                return true; // Match found
            }
        }

        return false; // No match found
    }
}