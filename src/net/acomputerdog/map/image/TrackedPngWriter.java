package net.acomputerdog.map.image;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.PngWriter;

import java.io.File;
import java.io.OutputStream;

public class TrackedPngWriter extends PngWriter {
    public TrackedPngWriter(File file, ImageInfo imgInfo, boolean allowoverwrite) {
        super(file, imgInfo, allowoverwrite);
    }

    public TrackedPngWriter(File file, ImageInfo imgInfo) {
        super(file, imgInfo);
    }

    public TrackedPngWriter(OutputStream outputStream, ImageInfo imgInfo) {
        super(outputStream, imgInfo);
    }

    public int getCurrentRow() {
        return rowNum;
    }
}
