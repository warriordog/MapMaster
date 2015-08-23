package net.acomputerdog.map.tile;

public abstract class TileProvider {
    private final TileSource tileSource;

    public TileProvider(TileSource tileSource) {
        this.tileSource = tileSource;
    }

    public TileSource getTileSource() {
        return tileSource;
    }

    public abstract boolean hasRegion(int x, int y);
    public abstract Tile getRegion(int x, int y);
}
