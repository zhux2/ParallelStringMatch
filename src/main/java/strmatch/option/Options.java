package strmatch.option;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.PrintStream;
import java.util.Objects;

@CommandLine.Command(name = "Options",
        description = "strmatch options",
        usageHelpWidth = 120
)
public class Options {

    // ---------- specific info options ----------
    @Option(names = {"-h", "--help"},
            description = "Display this help message",
            defaultValue = "false",
            usageHelp = true)
    private boolean printHelp;

    public boolean isPrintHelp() {
        return printHelp;
    }

    public static void printHelp(PrintStream stream) {
        CommandLine cmd = new CommandLine(new Options());
        cmd.setUsageHelpLongOptionsMaxWidth(30);
        cmd.usage(stream);
    }

    public void printHelp() {
        CommandLine cmd = new CommandLine(this);
        cmd.setUsageHelpLongOptionsMaxWidth(30);
        cmd.usage(System.out);
    }

    // ---------- specific problem ----------
    @Parameters(index = "0",
                description = "Specify the problem to solve",
                arity = "1",
                defaultValue = "")
    private String prob;

    public String getProb() {
        return prob;
    }

    // ---------- specific input/output params ----------
    @Parameters(index = "1",
                description = "Input file/path 1",
                arity = "1",
                defaultValue = "")
    private String input1;

    public String getInput1() {
        return input1;
    }

    @Parameters(index = "2",
                description = "Input file/path 2",
                arity = "1",
                defaultValue = "")
    private String input2;

    public String getInput2() {
        return input2;
    }

    @Option(names = {"-o", "--output"},
            description = "Output file",
            defaultValue = "output/out.txt")
    private File output;

    public File getOutput() {
        return output;
    }

    // ---------- specific solver options ----------
    @Option(names = {"-a", "--algo"},
            description = "The string matching algorithm",
            defaultValue = "simple")
    private String algo;

    public String getAlgo() {
        return algo;
    }

    @Option(names = {"--no-para"},
            description = "Run in parallel",
            defaultValue = "true",
            negatable = true)
    private boolean isParallel;

    public boolean isParallel() {
        return isParallel;
    }

    @Option(names = {"-t", "--thread-num"},
            description = "The number of threads",
            defaultValue = "-1")
    private int nrThread;

    public int getNrThread() {
        return nrThread;
    }

    @Option(names = {"-b", "--block-size"},
            description = "The block size of each task (e.g., 16K, 32M)",
            defaultValue = "16K",
            converter = BlockSizeConverter.class)
    private int blockSize;

    public int getBlockSize() {
        return blockSize;
    }

    public static Options parse(String... args) {
        Options options = CommandLine.populateCommand(new Options(), args);
        return postProcess(options);
    }

    private static Options postProcess(Options option) {
        if (option.isPrintHelp()) return option;
        if (option.prob.isBlank()) {
            throw new ConfigException("Missing parameter: " +
                    "problem to solve should be specified");
        }
        if (option.input1.isBlank() || option.input2.isBlank()) {
            throw new ConfigException("Missing parameters: " +
                    "input file/file path should be specified");
        }

        File parentDir = option.output.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                System.out.println("Created output directory: " + parentDir);
            } else {
                throw new ConfigException("Failed to create output directory: " + parentDir);
            }
        }

        if (option.isParallel() && option.nrThread < 0) {
            option.nrThread = Runtime.getRuntime().availableProcessors();
        }
        return option;
    }

    public static class BlockSizeConverter implements CommandLine.ITypeConverter<Integer> {

        @Override
        public Integer convert(String value) throws Exception {
            value = value.trim().toUpperCase();

            int multiplier = 1;
            if (value.endsWith("K")) {
                multiplier = 1024;
                value = value.substring(0, value.length() - 1);
            } else if (value.endsWith("M")) {
                multiplier = 1024 * 1024;
                value = value.substring(0, value.length() - 1);
            }

            try {
                int size = Integer.parseInt(value);
                return size * multiplier;
            } catch (NumberFormatException e) {
                throw new CommandLine.TypeConversionException("Invalid block size format. Expected format like 64M, 16K.");
            }
        }
    }
}
