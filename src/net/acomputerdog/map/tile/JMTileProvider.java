package net.acomputerdog.map.tile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JMTileProvider extends TileProvider {
    protected final Map<TileLoc, Tile> tileMap;
    protected final Map<TileLoc, File> fileMap;

    private TileLoc getter;

    public JMTileProvider(TileSource tileSource) {
        super(tileSource);
        tileMap = new HashMap<>();
        fileMap = new HashMap<>();
        getter = new TileLoc(0, 0);
        loadFiles(tileSource);
    }

    protected void loadFiles(TileSource tileSource) {
        File[] contents = new File(tileSource.getFile(), "/day/").listFiles();
        if (contents == null) {
            throw new IllegalArgumentException("Source file is not a JourneyMap data directory: " + tileSource.getFile().getPath());
        }
        for (File f : contents) {
            TileLoc loc = getLoc(f);
            fileMap.put(loc, f);
            tileMap.put(loc, null);
        }
    }

    protected TileLoc getLoc(File f) {
        String name = f.getName();
        int index = name.indexOf(',');
        try {
            return new TileLoc(Integer.parseInt(name.substring(0, index)), Integer.parseInt(name.substring(index + 1, name.lastIndexOf('.'))));
        } catch (Exception e) {
            throw new IllegalArgumentException("File is not a JM tile: " + name);
        }
    }

    protected char getSeparator() {
        return ',';
    }

    @Override
    public boolean hasRegion(int x, int y) {
        getter.x = x;
        getter.y = y;
        return tileMap.containsKey(getter);
    }

    @Override
    public Tile getRegion(int x, int y) {
        getter.x = x;
        getter.y = y;
        Tile t = tileMap.get(getter);
        if (t == null) {
            try {
                t = new JMTile(super.getTileSource(), fileMap.get(getter), getSeparator());
            } catch (IOException e) {
                throw new RuntimeException("Exception reading region!", e);
            }
        }
        return t;
    }

    public static class TileLoc {
        private int x;
        private int y;

        public TileLoc(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TileLoc)) return false;

            TileLoc tileLoc = (TileLoc) o;

            return x == tileLoc.x && y == tileLoc.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
