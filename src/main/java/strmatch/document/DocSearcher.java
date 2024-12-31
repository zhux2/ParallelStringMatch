package strmatch.document;

import strmatch.document.algo.Algo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class DocSearcher {

    protected final Algo algo;

    protected DocResult result;

    protected char[] documentStr;

    protected List<String> patternStr;

    public DocSearcher(Algo algo) {
        this.algo = algo;
    }

    public DocResult getResult() {
        return result;
    }

    public List<String> getPatternStr() {
        return patternStr;
    }

    public void solve(String document, String patterns) {
        initialize(document, patterns);
        doSolve();
    }

    protected void initialize(String document, String patterns) {
        loadDocument(document);
        loadPatterns(patterns);
        algo.init(patternStr, documentStr.length);
        result = new DocResult();
    }

    private void loadDocument(String path) {
        File file = new File(path);
        System.out.println("Reading document from: " + path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            long length = file.length();
//            if (length > Integer.MAX_VALUE) {
//                throw new IOException("File is too large to fit in a char array");
//            }
//
//            documentStr = new char[(int) length];
//            int readChars = reader.read(documentStr);
//            assert readChars == length;
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (!builder.isEmpty()) {
                    builder.append('\n');
                }
                builder.append(line);
            }

            documentStr = builder.toString().toCharArray();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPatterns(String path) {
        patternStr = new ArrayList<>();
        System.out.println("Reading patterns from: " + path);
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                patternStr.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void doSolve();
}
