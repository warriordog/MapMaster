package net.acomputerdog.map.stage.merge;

import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import net.acomputerdog.map.image.ImageUtils;
import net.acomputerdog.map.script.MapScript;
import net.acomputerdog.map.stage.process.MapOverlay;
import net.acomputerdog.map.tile.Tile;
import net.acomputerdog.map.tile.TileProvider;
import net.acomputerdog.map.tile.TileSource;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;

public class MapMerger {
    private static final int REGION_SIZE_PIXELS = 16 * 32;

    public static void mergeTiles(MapScript script, PngWriter outImage) {
        //code moved out to avoid recalculating math millions of times in a tight loop
        int startXRegion = getRegionCoord(script.getX1());
        int endXRegion = getRegionCoord(script.getX2());
        int startYRegion = getRegionCoord(script.getY1());
        int endYRegion = getRegionCoord(script.getY2());

        //allocated here to avoid making millions of duplicates
        List<Tile> tileList = new ArrayList<>(script.getTileSources().length);

        //iterates through 32x32 chunk regions in the map image
        //some code sections are oddly placed for performance
        int row = 0;
        for (int yRegion = startYRegion; yRegion < endYRegion; yRegion++) {
            ImageLineInt[] lines = createRegionLines(outImage);
            for (int xRegion = startXRegion; xRegion < endXRegion; xRegion++) {
                getTilesForRegion(script, xRegion, yRegion, tileList); //results are in tileList
                if (tileList.size() > 0) {
                    BufferedImage temp = new BufferedImage(REGION_SIZE_PIXELS, REGION_SIZE_PIXELS, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics = temp.createGraphics();
                    graphics.setComposite(AlphaComposite.SrcOver); //Overwrite existing pixels, unless they are transparent.

                    stackImages(tileList, graphics);
                    copyBufferToScanLine(xRegion, lines, temp, script);

                    tileList.clear();
                }
            }
            writeScanLines(lines, row, outImage);
            row += lines.length;
        }
        int left = outImage.imgInfo.rows - row - 1;
        if (left > 0) {
            System.out.println(left + " PNG lines left, filling with blanks.");
            for (int i = 0; i <= left; i++) {
                outImage.writeRow(new ImageLineInt(outImage.imgInfo));
                row++;
            }
        }

        //System.out.println(row + " / " + (outImage.imgInfo.rows - 1));
    }

    private static void writeScanLines(ImageLineInt[] lines, int row, PngWriter outImage) {
        for (int i = 0; i < lines.length; i++) {
            ImageLineInt line = lines[i];
            MapOverlay.writeIntoLine(row + i, line); //apply overlays
            outImage.writeRow(line);
        }
    }

    private static void stackImages(List<Tile> tileList, Graphics2D graphics) {
        for (Tile tile : tileList) {
            BufferedImage image = tile.getImage();
            graphics.drawImage(image, 0, 0, null);
        }
    }

    private static void copyBufferToScanLine(int xRegion, ImageLineInt[] lines, BufferedImage temp, MapScript script) {
        int xOff = script.worldToImageX(xRegion * REGION_SIZE_PIXELS);
        for (int y = 0; y < REGION_SIZE_PIXELS; y++) {
            ImageUtils.copyImageToPng(temp, y, lines[y], xOff, false);
        }
    }

    private static ImageLineInt[] createRegionLines(PngWriter outImage) {
        ImageLineInt[] lines = new ImageLineInt[REGION_SIZE_PIXELS];
        for (int index = 0; index < REGION_SIZE_PIXELS; index++) {
            lines[index] = new ImageLineInt(outImage.imgInfo);
        }
        return lines;
    }

    private static void getTilesForRegion(MapScript script, int x, int y, List<Tile> tileList) {
        for (TileSource source : script.getTileSources()) {
            TileProvider provider = source.getProvider();
            if (provider.hasRegion(x, y)) {
                tileList.add(provider.getRegion(x, y));
            }
        }
        tileList.sort(Tile::compareAge);
    }

    private static int getRegionCoord(int block) {
        return (int) Math.floor((double) block / (double)REGION_SIZE_PIXELS);
    }

}
