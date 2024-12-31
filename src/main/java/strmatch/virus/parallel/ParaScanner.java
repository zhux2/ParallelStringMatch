package strmatch.virus.parallel;

import strmatch.virus.VirusScanner;
import strmatch.virus.algo.BinAlgo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ParaScanner extends VirusScanner {

    private final ExecutorService executor;

    public ParaScanner(int nrThread, BinAlgo algo) {
        super(algo);
        long keepAliveTime = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        this.executor = new ThreadPoolExecutor(
                nrThread,
                nrThread,
                keepAliveTime,
                unit,
                new ArrayBlockingQueue<>(nrThread * 5),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        result = new ConcurrentScanResult();
    }

    @Override
    protected void doSolve() {
        List<CompletableFuture<Void>> futures;
        try (Stream<Path> paths = Files.walk(dirPath)) {
            futures = paths.filter(Files::isRegularFile)
                    .map(this::processFile).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
    }

    private CompletableFuture<Void> processFile(Path file) {
        return CompletableFuture.supplyAsync(() -> algo.match(file), executor)
                .exceptionally(e -> {
                    System.err.println("Error in scanning file " + file.toString() + ": " + e.getMessage());
                    return null;
                })
                .thenAccept(scanResult -> result.combine(scanResult));
    }

}
