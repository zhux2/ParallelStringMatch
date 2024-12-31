package strmatch.document.algo;

import strmatch.document.DocResult;

import java.util.List;

public interface Algo {
    void init(List<String> patterns, int maxR);

    /**
     * should be thread-safe
     * @param str the entire document string
     * @param l   the start index, inclusive
     * @param r   the end index, exclusive
     */
    DocResult match(char[] str, int l, int r);
}
