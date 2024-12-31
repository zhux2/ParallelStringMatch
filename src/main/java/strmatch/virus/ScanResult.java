package strmatch.virus;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ScanResult {
    void addMatch(String file, String virus);

    void addAll(String file, Collection<String> virus);

    Set<String> getMatches(String file);

    Map<String, Set<String>> getAllMatches();

    Set<String> fileSet();

    void combine(ScanResult result);
}
