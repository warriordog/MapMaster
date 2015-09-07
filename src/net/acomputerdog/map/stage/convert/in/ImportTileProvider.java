package net.acomputerdog.map.stage.convert.in;

import net.acomputerdog.map.image.SourcedImage;
import net.acomputerdog.map.tile.Tile;
import net.acomputerdog.map.tile.TileProvider;
import net.acomputerdog.map.tile.TileSource;

import java.awt.*;
import java.io.File;

public class ImportTileProvider extends TileProvider {

    private final ImportCache cache;

    public ImportTileProvider(TileSource tileSource, ImportCache cache) {
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
            Graphics2D g = image.createGraphics();
            //Debugging
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, 511, 511);
            return new ImportedTile(getTileSource(), image, x, y);
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
