package net.acomputerdog.map.stage.convert.out;

import net.acomputerdog.map.tile.TileFormat;

public class ExportType {
    private String outputPath;
    private TileFormat mapFormat;
    private boolean includeOverlays = false;

    public ExportType() {
        //serialization
    }

    public ExportType(String outputPath, TileFormat mapFormat, boolean includeOverlays) {
        this.outputPath = outputPath;
        this.mapFormat = mapFormat;
        this.includeOverlays = includeOverlays;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public TileFormat getMapFormat() {
        return mapFormat;
    }

    public boolean isIncludeOverlays() {
        return includeOverlays;
    }
}
