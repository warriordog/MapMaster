package net.acomputerdog.map.image;

import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {
    public static final Color COLOR_EMPTY = new Color(255, 255, 255, 0);

    public static boolean isTransparent(int color) {
        return ((color >> 24) & 0xFF) == 255; //get the first byte "alpha" and bitmask to get rid of everything else
    }

    public static void copyImageToPng(BufferedImage image, int imageY, ImageLineInt png, int pngLoc, boolean skipTransparent) {
        int[] scan = png.getScanline();
        for (int imageX = 0; imageX < image.getWidth(); imageX++) {
            int col = image.getRGB(imageX, imageY);
            if (!skipTransparent || ((col >> 24) & 0xFF) == 255) { //is transparency allowed, or color is opaque
                int scanX = (pngLoc * 3) + (imageX * 3);
                scan[scanX] = (col >> 16) & 0xFF; //red
                scan[scanX + 1] = (col >> 8) & 0xFF; //green
                scan[scanX + 2] = col & 0xFF; //blue
            }
        }
    }
}
