package strmatch.util.bitmap;

import java.util.ArrayList;
import java.util.List;

public class ListBaseBitmap implements Bitmap {
    private final List<Boolean> bits;

    public ListBaseBitmap() {
        bits = new ArrayList<>();
    }

    @Override
    public int length() {
        return bits.size();
    }

    @Override
    public void push(byte b) {
        for (int i = 7; i >= 0; i--) {
            boolean bit = (b >> i & 1) == 1;
            bits.add(bit);
        }
    }

    @Override
    public boolean get(int index) {
        return bits.get(index);
    }
}
