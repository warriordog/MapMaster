package net.acomputerdog.map.tile;

import net.acomputerdog.map.MapMaster;

import java.io.File;

/**
 * A source of map tiles.  Contains a File pointing to the source and the format.
 */
public class TileSource {

    private Format mapFormat;
    private String path;
    private transient File source;
    //transient so no serialization
    private transient TileProvider provider;

    private TileSource() {
        //private constructor for serialization
    }

    public TileSource(Format mapFormat, String path) {
        this.mapFormat = mapFormat;
        this.path = path;
    }

    public Format getMapFormat() {
        return mapFormat;
    }

    public File getFile() {
        if (source == null) {
            source = MapMaster.createRelativeFile(path);
        }
        return source;
    }

    public TileProvider getProvider() {
        if (provider == null) {
            switch (mapFormat) {
                case JOURNEYMAP:
                    provider = new JMTileProvider(this);
                    break;
                case VOXELMAP:
                    provider = new VMTileProvider(this);
                    break;
                case MAPWRITER:
                    provider = new MWTileProvider(this);
                    break;
                case VOXELMAP_TILE:
                    provider = new VMTTileProvider(this);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown map format!");
            }
        }
        return provider;
    }

    /**
     * Represents a particular map section storage format
     */
    public enum Format {
        /**
         * Journeymap format
         */
        JOURNEYMAP,

        /**
         * Voxelmap in data mode (default)
         */
        VOXELMAP,

        /**
         * MapWriter format
         */
        MAPWRITER,

        /**
         * VoxelMap in Tile mode
         */
        VOXELMAP_TILE
    }
}
