package net.acomputerdog.map.stage.convert.out;

import ar.com.hjg.pngj.ImageLineInt;

import java.io.File;

public class VMTileExport extends Export {
    public VMTileExport(File exportFile) {
        super(exportFile);
    }

    @Override
    public void exportRegions(ImageLineInt[] lines, int x1, int x2, int y) {
        throw new IllegalStateException("Not implemented!");
    }
}
