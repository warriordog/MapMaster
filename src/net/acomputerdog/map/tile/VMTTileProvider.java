package net.acomputerdog.map.tile;

import java.io.File;
import java.io.IOException;

public class VMTTileProvider extends TileProvider {
    public VMTTileProvider(TileSource tileSource) {
        super(tileSource);
    }

    @Override
    public boolean hasRegion(int x, int y) {
        return getFile((x * 2), ((y * 2) + 1)).exists();
    }

    @Override
    public Tile getRegion(int x, int y) {
        int locX = x * 2;
        int locY = (y * 2);// + 1;
        long age = getFile(x, y).lastModified();
        try {
            return new VMTTile(this, age, locX, locY);
        } catch (IOException e) {
            throw new RuntimeException("Exception loading region at " + x + "," + y + "!", e);
        }
    }

    public File getFile(int x, int y) {
        return new File(getTileSource().getFile(), "/" + x + "," + y + ".png"); //lots of numerical adjustments
    }
}
