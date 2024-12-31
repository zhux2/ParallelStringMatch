package strmatch.virus.algo;

import strmatch.util.BitLoader;
import strmatch.util.bitmap.Bitmap;
import strmatch.util.bitmap.BitsetBaseBitmap;
import strmatch.util.bitmap.ListBaseBitmap;
import strmatch.util.bitmap.SimpleBitmap;
import strmatch.virus.ScanResult;
import strmatch.virus.SimpleScanResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class KMP implements BinAlgo {

    private final List<String> virusName;
    private final List<Bitmap> virusStr;
    private final List<int[]> failMatch;

    public KMP() {
        virusName   = new ArrayList<>();
        virusStr    = new ArrayList<>();
        failMatch   = new ArrayList<>();
    }

    @Override
    public void initPattern(String virus, Path file) throws IOException {
        virusName.add(virus);
        Bitmap pattern = new SimpleBitmap();
        BitLoader.readFileAsBits(pattern, file);
        virusStr.add(pattern);
        failMatch.add(processPattern(pattern));
    }

    @Override
    public ScanResult match(Path file) {
        ScanResult result = new SimpleScanResult();
        Bitmap str = new SimpleBitmap();

        try {
            BitLoader.readFileAsBits(str, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String filename = file.toString();
        for (int i = 0; i < virusStr.size(); i += 1) {
            if (kmpSearch(str, virusStr.get(i), failMatch.get(i))) {
                result.addMatch(filename, virusName.get(i));
            }
        }
        return result;
    }

    /**
     * @return failMatch array of pattern
     */
    private int[] processPattern(Bitmap pattern) {
        int[] table = new int[pattern.length() + 1];
        int j = 0;
        table[0] = -1;
        table[1] = 0;
        int len = pattern.length();
        for (int i = 1; i < len; i += 1) {
            while (j > 0 && (pattern.get(i) != pattern.get(j))) {
                j = table[j];
            }

            if (pattern.get(i) == pattern.get(j)) {
                j += 1;
            }

            table[i + 1] = j;
        }

        return table;
    }

    /**
     * @param fail fail function table
     * @return true if str contains pattern
     */
    private boolean kmpSearch(Bitmap str, Bitmap pattern, int[] fail) {
        int i = 0;
        int j = 0;
        int strLen = str.length();
        int patternLen = pattern.length();
        while (i < strLen) {
            if (str.get(i) == pattern.get(j)) {
                i += 1;
                j += 1;
                if (j == patternLen) {
                    return true;
                }
            }
            else {
                if (j > 0) {
                    j = fail[j];
                } else {
                    i += 1;
                }
            }
        }
        return false;
    }
}
