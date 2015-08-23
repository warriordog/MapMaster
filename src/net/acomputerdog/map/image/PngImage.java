package net.acomputerdog.map.image;

public class PngImage {
    public static final int ALPHA = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 3;

    private final int[][][] image;
    private final int width;
    private final int height;

    public PngImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.image = new int[width][height][4];
    }

    public int[] getPixel(int x, int y) {
        return image[x][y];
    }

    public void setPixel(int x, int y, int[] pixel) {
        image[x][y] = pixel;
    }

    public void setARGB(int x, int y, int color) {
        int[] pixel = new int[4];
        pixel[ALPHA] = (color & 0xFF000000) >> 24;
        pixel[RED] = (color & 0x00FF0000) >> 16;
        pixel[GREEN] = (color & 0x0000FF00) >> 8;
        pixel[BLUE] = color & 0x000000FF;
        image[x][y] = pixel;
    }
    public int getARGB(int x, int y) {
        int[] pixel = image[x][y];
        return pixel[ALPHA]<<24 + pixel[RED]<<16 + pixel[GREEN]<<8 + pixel[BLUE];
    }

    public int getRed(int x, int y) {
        return image[x][y][RED];
    }
    public int getGreen(int x, int y) {
        return image[x][y][GREEN];
    }
    public int getBlue(int x, int y) {
        return image[x][y][BLUE];
    }
    public int getAlpha(int x, int y) {
        return image[x][y][ALPHA];
    }

    public void setRed(int x, int y, int val) {
        image[x][y][RED] = val;
    }
    public void setGreen(int x, int y, int val) {
        image[x][y][GREEN] = val;
    }
    public void setBlue(int x, int y, int val) {
        image[x][y][BLUE] = val;
    }
    public void setAlpha(int x, int y, int val) {
        image[x][y][ALPHA] = val;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
