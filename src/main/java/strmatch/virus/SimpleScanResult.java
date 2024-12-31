package strmatch.virus;

import java.util.*;

public class SimpleScanResult implements ScanResult {

    private final Map<String, Set<String>> matchResults;

    public SimpleScanResult() {
        matchResults = new HashMap<>();
    }

    @Override
    public void addMatch(String file, String virus) {
        matchResults.computeIfAbsent(file, k -> new TreeSet<>()).add(virus);
    }

    @Override
    public void addAll(String file, Collection<String> virus) {
        matchResults.computeIfAbsent(file, k -> new TreeSet<>()).addAll(virus);
    }

    @Override
    public Set<String> getMatches(String file) {
        return matchResults.getOrDefault(file, new TreeSet<>());
    }

    @Override
    public Map<String, Set<String>> getAllMatches() {
        return matchResults;
    }

    @Override
    public Set<String> fileSet() {
        return matchResults.keySet();
    }

    @Override
    public void combine(ScanResult result) {
        if (result == null) return;
        for (String file : result.fileSet()) {
            matchResults.computeIfAbsent(file, k -> new TreeSet<>())
                    .addAll(result.getMatches(file));
        }
    }
}
