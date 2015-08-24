package net.acomputerdog.map.stage.convert;

import net.acomputerdog.map.image.SourcedImage;
import net.acomputerdog.map.tile.Tile;
import net.acomputerdog.map.tile.TileProvider;
import net.acomputerdog.map.tile.TileSource;

import java.io.File;

public class ConverterTileProvider extends TileProvider {

    private final ConverterCache cache;

    public ConverterTileProvider(TileSource tileSource, ConverterCache cache) {
        super(tileSource);
        this.cache = cache;
    }

    @Override
    public boolean hasRegion(int x, int y) {
        return cache.getImageForRegion(x, y) != null;
    }

    @Override
    public Tile getRegion(int x, int y) {
        try {
            SourcedImage image = cache.getImageForRegion(x, y);
            return new ConverterTile(getTileSource(), image, x, y);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load region " + x + "," + y + " from converter cache!", e);
        }
    }

    private File getFile(int x, int y) {
        return new File(getTileSource().getFile(), getFileName(x, y));
    }

    private String getFileName(int x, int y) {
        return "/" + x + "," + y + ".tmp";
    }
}
