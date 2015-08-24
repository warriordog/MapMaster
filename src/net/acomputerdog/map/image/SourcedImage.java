package net.acomputerdog.map.image;

import java.awt.image.BufferedImage;
import java.io.File;

public class SourcedImage extends BufferedImage {

    private final File source;

    public SourcedImage(int width, int height, int imageType, File source) {
        super(width, height, imageType);
        this.source = source;
    }

    public File getSourceFile() {
        return source;
    }
}
