package net.acomputerdog.map.overlay;

import ar.com.hjg.pngj.ImageLineInt;

import java.io.*;

public abstract class Overlay {
    private final File source;
    private final int xLoc;
    private final int yLoc;
    private final int width;
    private final int height;

    public Overlay(File source) throws IOException {
        this.source = source;
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(source, "coords.txt")))) {
            String loc = reader.readLine();
            String size = reader.readLine();
            int locSplit = loc.indexOf(',');
            int sizeSplit = size.indexOf(',');
            if (locSplit == -1 || sizeSplit == -1) {
                throw new IllegalArgumentException("File is not a text overlay!");
            }
            try {
                xLoc = Integer.parseInt(loc.substring(0, locSplit));
                yLoc = Integer.parseInt(loc.substring(locSplit + 1, loc.length()));
                width = Integer.parseInt(size.substring(0, sizeSplit));
                height = Integer.parseInt(size.substring(sizeSplit + 1, size.length()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Malformed coordinates: " + loc);
            }
        }
    }

    public File getSource() {
        return source;
    }

    public int getXLoc() {
        return xLoc;
    }

    public int getYLoc() {
        return yLoc;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public abstract void apply(int row, ImageLineInt line);
}
