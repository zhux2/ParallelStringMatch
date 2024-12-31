package strmatch.util;

import strmatch.util.bitmap.Bitmap;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BitLoader {

    private BitLoader() {}

    public static void readFileAsBits(Bitmap emptyBitmap, Path file) throws IOException {
        assert emptyBitmap.length() == 0;
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file))) {
            int byteRead;
            long fileLength = Files.size(file);
            emptyBitmap.init((int) (fileLength * 8));

            while ((byteRead = bis.read()) != -1) {
                emptyBitmap.push((byte) byteRead);
            }
        }
    }

    public static boolean[] readBinaryFile(Path file) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file))) {
            int byteRead;
            long fileLength = Files.size(file);
            boolean[] bitmap = new boolean[(int) (fileLength * 8)];
            int id = 0;

            while ((byteRead = bis.read()) != -1) {
                for (int i = 7; i >= 0; i--) {
                    boolean bit = (byteRead >> i & 1) == 1;
                    bitmap[id++] = bit;
                }
            }
            return bitmap;
        }
    }
}
