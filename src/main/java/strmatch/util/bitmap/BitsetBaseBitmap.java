package strmatch.util.bitmap;

import java.util.BitSet;

public class BitsetBaseBitmap implements Bitmap {
    private int length = 0;
    private final BitSet bitSet;

    public BitsetBaseBitmap() {
        bitSet = new BitSet();
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public void push(byte b) {
        for (int i = 7; i >= 0; i--) {
            boolean bit = (b >> i & 1) == 1;
            bitSet.set(length++, bit);
        }
    }

    @Override
    public boolean get(int index) {
        assert index < length;
        return bitSet.get(index);
    }
}
