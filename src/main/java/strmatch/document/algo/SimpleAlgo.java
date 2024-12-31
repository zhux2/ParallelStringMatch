package strmatch.document.algo;

import strmatch.document.DocResult;

import java.util.List;

public class SimpleAlgo implements Algo{
    private List<String> patterns;
    private int maxR;

    @Override
    public void init(List<String> patterns, int maxR) {
        this.patterns = patterns;
        this.maxR = maxR;
    }

    @Override
    public DocResult match(char[] str, int l, int r) {
        DocResult result = new DocResult();

        for (String pattern : patterns) {
            int patternLen = pattern.length();
            for (int i = l; i < r; ++i) {
                boolean matchFlag = true;
                for (int j = 0; j < patternLen && i + j < maxR; ++j) {
                    if (str[i + j] != pattern.charAt(j)) {
                        matchFlag = false;
                        break;
                    }
                }

                if (matchFlag) {
                    result.addMatch(pattern, i);
                }
            }
        }
        return result;
    }
}
