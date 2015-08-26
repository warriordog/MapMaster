package net.acomputerdog.map.image;

import ar.com.hjg.pngj.ImageLineInt;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class ImageUtils {
    public static final Color COLOR_EMPTY = new Color(255, 255, 255, 0);
    public static final int COLOR_EMPTY_INT = COLOR_EMPTY.getRGB();

    public static boolean isTransparent(int color) {
        //checks if alpha is 255 or red,green, and blue are all 255
        return ((color >> 24) & 0xFF) == 255 || (color | 0xFF000000) == 0xFFFFFFFF; //get the first byte "alpha" and bitmask to get rid of everything else
    }

    public static void copyImageToPng(BufferedImage image, int imageY, ImageLineInt png, int pngLoc, boolean skipTransparent) {
        int[] scan = png.getScanline();
        DataBuffer buffer = image.getRaster().getDataBuffer();

        int scanX = pngLoc * 3;

        if (buffer instanceof DataBufferInt) {
            int pixelsPerRow = image.getWidth();
            int startIndex = pixelsPerRow * imageY;
            int endIndex = startIndex + pixelsPerRow;

            int[] pixels = ((DataBufferInt) buffer).getData(); //argb packed integers

            for (int offset = startIndex; offset < endIndex; offset++) {
                int col = pixels[offset];
                scanX = copyData(scan, scanX, skipTransparent, (col >> 24) & 0xFF, (col >> 16) & 0xFF, (col >> 8) & 0xFF, col & 0xFF);
            }
        } else {
            byte[] pixels = ((DataBufferByte) buffer).getData();//array of abgr bytes

            int rowWidth = image.getWidth() * 4;
            int startIndex = rowWidth * imageY; //4 bytes in place of one int
            int endIndex = startIndex + rowWidth; //4 bytes in place of one int

            for (int offset = startIndex; offset < endIndex; offset += 4) {
                int alpha = pixels[offset] & 0xFF;
                int red = pixels[offset + 3] & 0xFF;
                int green = pixels[offset + 2] & 0xFF;
                int blue = pixels[offset + 1] & 0xFF;

                scanX = copyData(scan, scanX, skipTransparent, alpha, red, green, blue);
            }
        }
    }

    private static int copyData(int[] scan, int scanX, boolean skipTransparent, int alpha, int red, int green, int blue) {
        if (!skipTransparent || alpha > 0) { //if allow transparent or pixel is transparent
            //increment happens AFTER array value is calculated
            scan[scanX++] = red; //scanX
            scan[scanX++] = green; //scanX + 1
            scan[scanX++] = blue; //scanX + 2
            return scanX;
        } else {
            return scanX + 3;
        }
    }
}
