package strmatch.virus;

import strmatch.Solver;

import strmatch.option.Options;
import strmatch.virus.algo.*;
import strmatch.virus.parallel.ParaScanner;
import strmatch.virus.serial.SerialScanner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class VirusScannerWrapper implements Solver {

    private final Options option;
    private VirusScanner virusScanner;

    public VirusScannerWrapper(Options option) {
        this.option = option;
    }

    @Override
    public void initialize() throws Exception {
        BinAlgo algo = switch (option.getAlgo()) {
            case "simple" -> new SimpleAlgo();
            case "kmp" -> new KMP();
            case "ac" -> new AhoCorasick();
            case "bm" -> new BoyerMoore();
            case "vishkin" -> new Vishkin();
            default -> null;
        };

        if (algo == null) {
            throw new Exception("invalid algorithm, expected [simple|kmp]");
        }

        if (option.isParallel()) {
            virusScanner = new ParaScanner(option.getNrThread(), algo);
        }
        else {
            virusScanner = new SerialScanner(algo);
        }
        printInfo();
    }

    @Override
    public void solve() {
        virusScanner.solve(option.getInput1(), option.getInput2());
        processResult();
    }

    private void processResult() {
        ScanResult result = virusScanner.getResult();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(option.getOutput()))) {
            for (String file : result.fileSet()) {
                writer.write(file);
                Set<String> virus = result.getMatches(file);
                for (String v : virus) {
                    writer.write(' ' + v);
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
//            System.out.println("Block Size: " + Integer.toString(option.getBlockSize() / 1024) + 'K');
        }
        System.out.println("========== ==================== ==========\n");
    }
}
