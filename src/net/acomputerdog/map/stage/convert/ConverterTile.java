package net.acomputerdog.map.stage.convert;

import net.acomputerdog.map.image.SourcedImage;
import net.acomputerdog.map.tile.Tile;
import net.acomputerdog.map.tile.TileSource;

public class ConverterTile extends Tile {

    private final SourcedImage image;
    private final int startX, startY, endX, endY;
    private final long modified;

    public ConverterTile(TileSource source, SourcedImage image, int x, int y) {
        super(source);
        this.image = image;
        this.startX = x;
        this.startY = y;
        this.endX = x + 512;
        this.endY = y + 512;
        this.modified = image.getSourceFile().lastModified();
    }

    @Override
    public int getStartX() {
        return startX;
    }

    @Override
    public int getStartY() {
        return startY;
    }

    @Override
    public int getEndX() {
        return endX;
    }

    @Override
    public int getEndY() {
        return endY;
    }

    @Override
    public SourcedImage getImage() {
        return image;
    }

    @Override
    public long getLastModified() {
        return modified;
    }
}
