package net.acomputerdog.map.tile;

import java.io.File;

public class MWTileProvider extends JMTileProvider {
    public MWTileProvider(TileSource tileSource) {
        super(tileSource);
    }

    /*
     * Only difference is "." instead of "," as separator
     */
    @Override
    protected JMTileProvider.TileLoc getLoc(File f) {
        String name = f.getName();
        int index = name.indexOf('.');
        try {
            return new TileLoc(Integer.parseInt(name.substring(0, index)), Integer.parseInt(name.substring(index + 1, name.lastIndexOf('.'))));
        } catch (Exception e) {
            throw new IllegalArgumentException("File is not a MW tile: " + name);
        }
    }

    @Override
    protected void loadFiles(TileSource tileSource) {
        File[] contents = new File(tileSource.getSource(), "/images/z0/").listFiles();
        if (contents == null) {
            throw new IllegalArgumentException("Source file is not a JourneyMap data directory: " + tileSource.getSource().getPath());
        }
        for (File f : contents) {
            TileLoc loc = getLoc(f);
            fileMap.put(loc, f);
            tileMap.put(loc, null);
        }
    }

    @Override
    protected char getSeparator() {
        return '.';
    }
}
