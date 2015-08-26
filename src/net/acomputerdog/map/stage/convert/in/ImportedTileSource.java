package net.acomputerdog.map.stage.convert.in;

import net.acomputerdog.map.tile.TileSource;

import java.io.File;

public class ImportedTileSource extends TileSource {
    private final File path;

    public ImportedTileSource(Format mapFormat, File path) {
        super(mapFormat, path.getPath());
        this.path = path;
    }

    @Override
    public File getFile() {
        return path;
    }
}
