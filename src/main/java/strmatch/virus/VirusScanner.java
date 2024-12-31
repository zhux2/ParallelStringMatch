package strmatch.virus;

import strmatch.virus.algo.BinAlgo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public abstract class VirusScanner {
    protected BinAlgo algo;

    protected Path dirPath;

    protected ScanResult result;

    public VirusScanner(BinAlgo algo) {
        this.algo = algo;
    }

    public ScanResult getResult() {
        return result;
    }

    public void solve(String scanDir, String virusDir) {
        initialize(Paths.get(scanDir), Paths.get(virusDir));
        doSolve();
    }

    private void initialize(Path scanDir, Path virusDir) {
        dirPath = scanDir;
        try (Stream<Path> paths = Files.walk(virusDir)) {
            paths.filter(Files::isRegularFile)
                    .forEach(this::initFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        algo.initAlgo();

        System.out.println("Scan directory: " + scanDir.toString());
        System.out.println("Virus directory: " + virusDir.toString());
    }

    private void initFile(Path file) {
        try {
            algo.initPattern(file.getFileName().toString(), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void doSolve();
}
