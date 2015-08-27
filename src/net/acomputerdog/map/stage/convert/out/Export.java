package net.acomputerdog.map.stage.convert.out;

import ar.com.hjg.pngj.ImageLineInt;

import java.io.File;

public abstract class Export {
    public static final int REGION_SIZE_SCAN = 512 * 3;

    protected final File exportFile;

    public Export(File exportFile) {
        this.exportFile = exportFile;
        if (!this.exportFile.exists()) {
            this.exportFile.mkdirs();
        }
    }

    public abstract void exportRegions(ImageLineInt[] lines, int x1, int x2, int y);
}
