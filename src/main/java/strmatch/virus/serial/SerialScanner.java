package strmatch.virus.serial;

import strmatch.virus.SimpleScanResult;
import strmatch.virus.VirusScanner;
import strmatch.virus.algo.BinAlgo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class SerialScanner extends VirusScanner {

    public SerialScanner(BinAlgo algo) {
        super(algo);
        result = new SimpleScanResult();
    }

    @Override
    protected void doSolve() {
        try (Stream<Path> paths = Files.walk(dirPath)) {
            paths.filter(Files::isRegularFile)
                    .forEach(file -> result.combine(algo.match(file)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
