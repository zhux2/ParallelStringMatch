package strmatch.util.bitmap;

public class ByteArrBitmap implements Bitmap {
    int len;
    byte[] byteArr;

    @Override
    public void init(int length) {
        byteArr = new byte[length / 8];
        this.len = 0;
    }

    @Override
    public int length() {
        return len * 8;
    }

    @Override
    public void push(byte b) {
        byteArr[len++] = b;
    }

    @Override
    public boolean get(int index) {
        byte bt = byteArr[index / 8];
        return (bt >> (8 - index % 8) & 1) == 1;
    }
}
