package net.acomputerdog.map.stage.convert.out;

import ar.com.hjg.pngj.ImageLineInt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PNGExport extends Export {

    private final char separator;

    public PNGExport(File exportFile, char separator) {
        super(exportFile);
        this.separator = separator;
    }

    @Override
    public void exportRegions(ImageLineInt[] lines, int x1, int x2, int rY) {
        int[] temp = new int[4 * 512];
        temp[3] = 255;
        for (int rX = x1; rX < x2; rX++) {
            int scanOff = (rX - x1) * REGION_SIZE_SCAN;
            File outFile = getFile(rX, rY);
            BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < 512; y++) {
                int[] scan = lines[y].getScanline();//rgb
                for (int x = 0; x < 512; x++) {
                    int off = x * 3;
                    temp[off] = scan[scanOff + off];
                    temp[off + 1] = scan[scanOff + off + 1];
                    temp[off + 2] = scan[scanOff + off + 2];
                }
                image.getRaster().setPixels(0, y, 512, 1, temp); //rgba
            }
            try {
                ImageIO.write(image, "PNG", outFile);
            } catch (IOException e) {
                throw new RuntimeException("Exception exporting region at " + rX + "," + rY + "!", e);
            }
        }
    }

    private File getFile(int x, int y) {
        return new File(exportFile, "/" + x + separator + y + ".png");
    }
}
