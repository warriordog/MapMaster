package net.acomputerdog.map.tile;

import net.acomputerdog.map.image.EmptyingColorModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

public class VMTTile extends Tile {
    private final BufferedImage image;
    private final int startX, startY, endX, endY;
    private final long modified;

    public VMTTile(VMTTileProvider provider, long modified, int x, int y) throws IOException {
        super(provider.getTileSource());

        /*
        byte[] pixels = new byte[512 * 512];
        DataBuffer dataBuffer = new DataBufferByte(pixels, 512*512, 0);
        SampleModel sampleModel = new SinglePixelPackedSampleModel(DataBuffer.TYPE_BYTE, 512, 512, new int[] {(byte)0xf});
        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, null);
        image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB, new EmptyingColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000));
        */
        image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        copyImage(g, provider, x, y, 0, 0);
        copyImage(g, provider, x + 1, y, 256, 0);
        copyImage(g, provider, x, y + 1, 0, 256);
        copyImage(g, provider, x + 1, y + 1, 256, 256);

        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 511, 511);

        this.startX = x;
        this.startY = y;
        this.endX = x + 512;
        this.endY = y + 512;

        this.modified = modified;
    }

    private void copyImage(Graphics2D g, VMTTileProvider provider, int x, int y, int offX, int offY) throws IOException {
        File f = provider.getFile(x, y);
        if (f.exists()) {
            g.drawImage(ImageIO.read(f), offX, offY, null);
        }
    }

    private ColorModel replaceBlack(ColorModel old) {
        EmptyingColorModel cm = new EmptyingColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000);
        return cm;
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
