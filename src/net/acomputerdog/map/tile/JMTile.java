package net.acomputerdog.map.tile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JMTile extends Tile {

    private final File sourceFile;
    private final BufferedImage image;
    private final int startX, startY, endX, endY;
    private final long modified;

    public JMTile(TileSource source, File sourceFile, char separator) throws IOException {
        super(source);

        //get coordinates as well as verify file format
        try {
            String name = sourceFile.getName();
            int split = name.indexOf(separator);
            int x = Integer.parseInt(name.substring(0, split));
            int y = Integer.parseInt(name.substring(split + 1, name.lastIndexOf('.')));
            this.startX = x;
            this.startY = y;
            this.endX = x + 32;
            this.endY = y + 32;
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new IllegalArgumentException("Source file is not a JourneyMap region tile: " + sourceFile.getName());
        }

        this.sourceFile = sourceFile;
        this.image = ImageIO.read(sourceFile);
        this.modified = sourceFile.lastModified();
    }

    public File getSourceFile() {
        return sourceFile;
    }

    @Override
    public int getStartX() {
        return startX;
    }

    @Override
    public int getStartY() {
        return startY;
    }

    @Override
    public int getEndX() {
        return endX;
    }

    @Override
    public int getEndY() {
        return endY;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public long getLastModified() {
        return modified;
    }
}
