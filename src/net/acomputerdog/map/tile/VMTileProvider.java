package net.acomputerdog.map.tile;

//todo implement (with converter)
public class VMTileProvider extends TileProvider {
    public VMTileProvider(TileSource tileSource) {
        super(tileSource);
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public boolean hasRegion(int x, int y) {
        return false;
    }

    @Override
    public Tile getRegion(int x, int y) {
        return null;
    }
}
