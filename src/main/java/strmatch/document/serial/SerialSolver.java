package strmatch.document.serial;

import strmatch.document.DocSearcher;
import strmatch.document.algo.Algo;

public class SerialSolver extends DocSearcher {

    public SerialSolver(Algo algo) {
        super(algo);
    }

    @Override
    protected void doSolve() {
        result = algo.match(documentStr, 0, documentStr.length);
    }
}
