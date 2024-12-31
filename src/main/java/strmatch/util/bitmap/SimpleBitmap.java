package strmatch.util.bitmap;

public class SimpleBitmap implements Bitmap {
    boolean[] bitmap;
    int len = 0;

    @Override
    public void init(int length) {
        bitmap = new boolean[length];
        len = 0;
    }

    @Override
    public int length() {
        return len;
    }

    @Override
    public void push(byte b) {
        for (int i = 7; i >= 0; i--) {
            boolean bit = (b >> i & 1) == 1;
            bitmap[len++] = bit;
        }
    }

    @Override
    public boolean get(int index) {
        return bitmap[index];
    }
}
