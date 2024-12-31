package strmatch;

import strmatch.document.DocSearchWrapper;
import strmatch.option.ConfigException;
import strmatch.option.Options;
import strmatch.util.Timer;
import strmatch.virus.VirusScannerWrapper;

import java.util.Objects;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        Options options = null;
        try {
            options = Options.parse(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Options.printHelp(System.err);
            exit(1);
        }

        if (options.isPrintHelp()) {
            options.printHelp();
            return;
        }

        Solver solver = parseSolver(options);

        runAndCount(solver);
    }

    private static Solver parseSolver(Options option) {
        if (Objects.equals(option.getProb(), "doc")) {
            return new DocSearchWrapper(option);
        }
        else if (Objects.equals(option.getProb(), "virus")) {
            return new VirusScannerWrapper(option);
        }
        else {
            System.err.println("Unknown problem \"" + option.getProb() + "\". " +
                    "Expected: doc|virus");
            exit(1);
        }
        return null;
    }

    private static void runAndCount(Solver solver) {
        try {
            assert solver != null;
            solver.initialize();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            exit(1);
        }
        System.out.println("Solver initialized.");

        Timer timer = new Timer();

        System.out.println("Solver start...");
        timer.start();
        solver.solve();
        timer.stop();

        System.out.println("Solver finishes, elapsed time: " + timer.getDurationSeconds() + "s");
    }
}
//virus data/software_antivirus/opencv-4.10.0 data/software_antivirus/virus -o output/simvirus.txt