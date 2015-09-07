package net.acomputerdog.map.tile;

import java.awt.image.BufferedImage;

public abstract class Tile {

    public final TileSource source;

    public Tile(TileSource source) {
        this.source = source;
    }

    public abstract int getStartX();
    public abstract int getStartY();
    public abstract int getEndX();
    public abstract int getEndY();

    public abstract BufferedImage getImage();

    public int getWidth() {
        return getEndX() - getStartX();
    }

    public int getHeight() {
        return getEndY() - getStartY();
    }

    public abstract long getLastModified();

    public int compareAge(Tile other) {
        //reversed so that oldest are first
        return -1 * (int)(this.getLastModified() - other.getLastModified());
    }
}
