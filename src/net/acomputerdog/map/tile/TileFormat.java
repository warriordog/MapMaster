package net.acomputerdog.map.tile;

/**
 * Represents a particular map section storage format
 */
public enum TileFormat {
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
