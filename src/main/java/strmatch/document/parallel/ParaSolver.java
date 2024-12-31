package strmatch.document.parallel;


import strmatch.document.DocResult;
import strmatch.document.algo.Algo;
import strmatch.document.DocSearcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class ParaSolver extends DocSearcher {

    private final ExecutorService executor;

    private final int blockSize;

    public ParaSolver(int nrThread, Algo algo, int blockSize) {
        super(algo);
        this.executor = Executors.newFixedThreadPool(nrThread);
        this.blockSize = blockSize;
    }

    @Override
    protected void doSolve() {
        // split tasks
        List<Callable<DocResult>> tasks = new ArrayList<>();
        int nrBlock = (int) ((documentStr.length + blockSize - 1) / blockSize);
        int maxR = documentStr.length;
        for (int i = 0; i < nrBlock; ++i) {
            int finalI = i;
            tasks.add(() -> {
                return algo.match(documentStr, finalI * blockSize, Math.min(((finalI + 1) * blockSize), maxR));
            });
        }

        // combine result
        try {
            List<Future<DocResult>> results = executor.invokeAll(tasks);

            for (Future<DocResult> future : results) {
                result.combine(future.get());
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
