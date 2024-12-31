package strmatch.document;

import strmatch.Solver;
import strmatch.document.algo.Algo;
import strmatch.document.algo.KMP;
import strmatch.document.algo.SimpleAlgo;
import strmatch.document.parallel.ParaSolver;
import strmatch.document.serial.SerialSolver;
import strmatch.option.Options;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class DocSearchWrapper implements Solver {
    private final Options option;
    private DocSearcher docSearcher;

    public DocSearchWrapper(Options option) {
        this.option = option;
    }

    @Override
    public void initialize() throws Exception {
        Algo algo = switch (option.getAlgo()) {
            case "simple" -> new SimpleAlgo();
            case "kmp" -> new KMP();
            default -> null;
        };

        if (algo == null) {
            throw new Exception("invalid algorithm, expected [simple|kmp]");
        }

        if (option.isParallel()) {
            docSearcher = new ParaSolver(option.getNrThread(), algo, option.getBlockSize());
        }
        else {
            docSearcher = new SerialSolver(algo);
        }
        printInfo();
    }

    @Override
    public void solve() {
        docSearcher.solve(option.getInput1(), option.getInput2());
        processResult();
    }

    private void processResult() {
        DocResult result = docSearcher.getResult();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(option.getOutput()))){
            for (String pattern : docSearcher.getPatternStr()) {
                Set<Integer> matches = result.getMatches(pattern);
                writer.write(Integer.toString(matches.size()));
                for (Integer p : matches) {
                    writer.write(" " + p.toString());
                }
                writer.write('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Output saved to " + option.getOutput().toString());
    }

    private void printInfo() {
        System.out.println("========== Document Search Info ==========");
        System.out.println("Mode: " + (option.isParallel() ? "Parallel" : "Serial"));
        System.out.println("Algo: " + option.getAlgo());

        if (option.isParallel()) {
            System.out.println("Thread Num: " + Integer.toString(option.getNrThread()));
            System.out.println("Block Size: " + Integer.toString(option.getBlockSize() / 1024) + 'K');
        }
        System.out.println("========== ==================== ==========\n");
    }
}
