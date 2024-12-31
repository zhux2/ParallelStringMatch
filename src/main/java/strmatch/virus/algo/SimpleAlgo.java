package strmatch.virus.algo;

import strmatch.util.BitLoader;
import strmatch.util.bitmap.Bitmap;
import strmatch.util.bitmap.SimpleBitmap;
import strmatch.virus.ScanResult;
import strmatch.virus.SimpleScanResult;

import java.io.IOException;
import java.nio.file.Path;import java.util.HashMap;
import java.util.Map;

public class SimpleAlgo implements BinAlgo {
    Map<String, Bitmap> virusMap;

    public SimpleAlgo() {
        virusMap = new HashMap<>();
    }

    @Override
    public void initPattern(String virus, Path file) throws IOException {
        Bitmap bitmap = new SimpleBitmap();
        BitLoader.readFileAsBits(bitmap, file);
        virusMap.put(virus, bitmap);
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

        for (Map.Entry<String, Bitmap> entry : virusMap.entrySet()) {
            String virusName = entry.getKey();
            Bitmap virusSignature = entry.getValue();

            if (contains(str, virusSignature)) {
                result.addMatch(file.toString(), virusName);
            }
        }
        return result;
    }

    private boolean contains(Bitmap str, Bitmap virus) {
        int maxi = str.length() - virus.length();
        for (int i = 0; i <= maxi; ++i) {
            boolean match = true;

            for (int j = 0; j < virus.length(); ++j) {
                if (str.get(i + j) != virus.get(j)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return true;
            }
        }
        return false;
    }
}
