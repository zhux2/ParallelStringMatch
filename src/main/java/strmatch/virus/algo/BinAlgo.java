package strmatch.virus.algo;

import strmatch.virus.ScanResult;

import java.io.IOException;
import java.nio.file.Path;

public interface BinAlgo {
    void initPattern(String virus, Path file) throws IOException;

    default void initAlgo() {};
    /**
     * should be thread-safe
     */
    ScanResult match(Path file);
}
