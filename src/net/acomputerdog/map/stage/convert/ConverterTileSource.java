package net.acomputerdog.map.stage.convert;

import net.acomputerdog.map.tile.TileSource;

import java.io.File;

public class ConverterTileSource extends TileSource {
    private final File path;

    public ConverterTileSource(Format mapFormat, File path) {
        super(mapFormat, path.getPath());
        this.path = path;
    }

    @Override
    public File getFile() {
        return path;
    }
}
