package strmatch.document;

import java.util.*;

public class DocResult {

    private final Map<String, Set<Integer>> matchResults;

    public DocResult() {
        this.matchResults = new HashMap<>();
    }

    public void addMatch(String pattern, int position) {
        Set<Integer> positions = matchResults.computeIfAbsent(pattern, k -> new TreeSet<>());
        positions.add(position);
    }

    public void addAll(String pattern, Collection<Integer> positions) {
        Set<Integer> matchSet = matchResults.computeIfAbsent(pattern, k -> new TreeSet<>());
        matchSet.addAll(positions);
    }

    public Set<Integer> getMatches(String pattern) {
        return matchResults.getOrDefault(pattern, new TreeSet<>());
    }

    public Map<String, Set<Integer>> getAllMatches() {
        return matchResults;
    }

    public Set<String> patternSet() {
        return matchResults.keySet();
    }

    public void combine(DocResult result) {
        for (String pattern : result.patternSet()) {
            matchResults.computeIfAbsent(pattern, k -> new TreeSet<>())
                        .addAll(result.getMatches(pattern));
        }
    }
}
