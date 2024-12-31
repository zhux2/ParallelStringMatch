package strmatch.document.algo;

import strmatch.document.DocResult;

import java.util.ArrayList;
import java.util.List;

public class KMP implements Algo {

    private List<String> patterns;
    private List<int[]> failMatch;
    private int maxR;

    @Override
    public void init(List<String> patterns, int maxR) {
        this.patterns = patterns;
        this.failMatch = new ArrayList<>();

        for (String pattern : patterns) {
            int[] fail = processPattern(pattern);
            failMatch.add(fail);
        }

        this.maxR = maxR;
    }

    @Override
    public DocResult match(char[] str, int l, int r) {
        DocResult docResult = new DocResult();

        for (int i = 0; i < patterns.size(); i++) {
            String pattern = patterns.get(i);
            int[] table = failMatch.get(i);
            docResult.addAll(pattern, kmpSearch(str, l, Math.min(maxR, r - 1 + pattern.length()), pattern.toCharArray(), table));
        }

        return docResult;
    }

    /**
     * @return failMatch array of pattern
     */
    private int[] processPattern(String pattern) {
        int[] table = new int[pattern.length() + 1];
        int j = 0;
        table[0] = -1;
        table[1] = 0;
        for (int i = 1; i < pattern.length(); i += 1) {
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = table[j];
            }

            if (pattern.charAt(i) == pattern.charAt(j)) {
                j += 1;
            }

            table[i + 1] = j;
        }

        return table;
    }

    /**
     * @param l the start index, inclusive
     * @param r the end index, exclusive
     * @param fail fail function table
     * @return list of all match position
     */
    private List<Integer> kmpSearch(char[] str, int l, int r, char[] pattern, int[] fail) {
        int i = l;
        int j = 0;
        List<Integer> matches = new ArrayList<>();

        while (i < r) {
            if (str[i] == pattern[j]) {
                i += 1;
                j += 1;
                if (j == pattern.length) {
                    matches.add(i - j);
                    j = fail[j];
                }
            }
            else {
                if (j > 0) {
                    j = fail[j];
                } else {
                    i += 1;
                }
            }
        }

        return matches;
    }
}
