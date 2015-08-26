package net.acomputerdog.map.tile;

import net.acomputerdog.map.stage.convert.in.MapImporter;

public class VMTileProvider extends TileProvider {

    private final TileProvider passthrough;

    public VMTileProvider(TileSource tileSource) {
        super(tileSource);
        passthrough = MapImporter.wrapVM(tileSource);
    }

    @Override
    public boolean hasRegion(int x, int y) {
        return passthrough.hasRegion(x, y);
    }

    @Override
    public Tile getRegion(int x, int y) {
        return passthrough.getRegion(x, y);
    }

}
