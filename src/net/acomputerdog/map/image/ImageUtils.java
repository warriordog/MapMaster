package net.acomputerdog.map.image;

import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.reflect.Array;
import java.nio.IntBuffer;

public class ImageUtils {
    public static final Color COLOR_EMPTY = new Color(255, 255, 255, 0);

    public static boolean isTransparent(int color) {
        return ((color >> 24) & 0xFF) == 255; //get the first byte "alpha" and bitmask to get rid of everything else
    }

    public static void copyImageToPng(BufferedImage image, int imageY, ImageLineInt png, int pngLoc, boolean skipTransparent) {
        int[] scan = png.getScanline();
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData(); //packed integers

        int pixelsPerRow = image.getWidth();
        int startIndex = pixelsPerRow * imageY;

        for (int offset = 0; offset < pixelsPerRow; offset++) {
            int scanX = (pngLoc + offset) * 3;
            int col = pixels[startIndex + offset];

            if (!skipTransparent || ((col >> 24) & 0xFF) > 0) {
                scan[scanX] = (col >> 16) & 0xFF; //red
                scan[scanX + 1] = (col >> 8) & 0xFF; //green
                scan[scanX + 2] = col & 0xFF; //blue
            }
        }
    }
}
