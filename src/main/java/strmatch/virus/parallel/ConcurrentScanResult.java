package strmatch.virus.parallel;

import strmatch.virus.ScanResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConcurrentScanResult implements ScanResult {

    Map<String, Set<String>> matchResults;

    public ConcurrentScanResult() {
        matchResults = new ConcurrentHashMap<>();
    }

    @Override
    public void addMatch(String file, String virus) {
        matchResults.computeIfAbsent(file, k -> new ConcurrentSkipListSet<>()).add(virus);
    }

    @Override
    public void addAll(String file, Collection<String> virus) {
        matchResults.computeIfAbsent(file, k -> new ConcurrentSkipListSet<>()).addAll(virus);
    }

    @Override
    public Set<String> getMatches(String file) {
        return matchResults.getOrDefault(file, new ConcurrentSkipListSet<>());
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
            matchResults.computeIfAbsent(file, k -> new ConcurrentSkipListSet<>())
                    .addAll(result.getMatches(file));
        }
    }
}
