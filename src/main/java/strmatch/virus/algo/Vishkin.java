package strmatch.virus.algo;

import strmatch.util.BitLoader;
import strmatch.util.bitmap.Bitmap;
import strmatch.util.bitmap.SimpleBitmap;
import strmatch.virus.ScanResult;
import strmatch.virus.SimpleScanResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a parallel string matching algorithm based on Vishkin's algorithm.
 * This class is adapted for use in a virus scanning context.
 */
public class Vishkin implements BinAlgo {

    private final List<String> virusNames;
    private final List<Bitmap> virusPatterns;
    private final List<int[]> witnessFunc;

    public Vishkin() {
        virusNames = new ArrayList<>();
        virusPatterns = new ArrayList<>();
        witnessFunc = new ArrayList<>();
    }

    @Override
    public void initPattern(String virus, Path file) throws IOException {
        virusNames.add(virus);
        Bitmap pattern = new SimpleBitmap();
        BitLoader.readFileAsBits(pattern, file);
        virusPatterns.add(pattern);
        witnessFunc.add(generateWitnessTable(pattern));
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
        for (int i = 0; i < virusPatterns.size(); i++) {
            Bitmap pattern = virusPatterns.get(i);
            if (parallelSearch(text, pattern, witnessFunc.get(i))) {
                result.addMatch(filename, virusNames.get(i));
            }
        }

        return result;
    }

    /**
     * Performs a parallel search for a pattern in the text using a witness table.
     *
     * @param text    The text to search in.
     * @param pattern The pattern to search for.
     * @return true if the pattern is found in the text, false otherwise.
     */
    private boolean parallelSearch(Bitmap text, Bitmap pattern, int[] witness) {
        int textLength = text.length();
        int patternLength = pattern.length();

//        // Generate the witness table
//        int[] witness = generateWitnessTable(pattern);

        // Search the text for the pattern
        return java.util.stream.IntStream.range(0, textLength - patternLength + 1)
                .parallel() // Enable parallel processing
                .anyMatch(startIdx -> matchesAt(text, pattern, startIdx, witness));
    }

    /**
     * Generates a witness table for the given pattern.
     *
     * @param pattern The pattern for which to generate the witness table.
     * @return The witness table as an array of integers.
     */
    private int[] generateWitnessTable(Bitmap pattern) {
        int length = pattern.length();
        int[] witness = new int[length];

        for (int i = 1; i < length; i++) {
            int shift = 0;
            while (shift < length - i && pattern.get(shift) == pattern.get(shift + i)) {
                shift++;
            }
            witness[i] = shift;
        }

        return witness;
    }

    /**
     * Checks if the pattern matches the text at a specific starting index.
     *
     * @param text     The text to search in.
     * @param pattern  The pattern to search for.
     * @param startIdx The starting index in the text.
     * @param witness  The witness table for the pattern.
     * @return true if the pattern matches at the starting index, false otherwise.
     */
    private boolean matchesAt(Bitmap text, Bitmap pattern, int startIdx, int[] witness) {
        int patternLength = pattern.length();

        for (int i = 0; i < patternLength; i++) {
            if (text.get(startIdx + i) != pattern.get(i)) {
                return false;
            }
        }

        return true;
    }
}
