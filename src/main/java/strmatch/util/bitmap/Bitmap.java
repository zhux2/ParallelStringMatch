package strmatch.util.bitmap;

public interface Bitmap {

    default void init(int length) { }

    int length();

    void push(byte b);

    boolean get(int index);
}
