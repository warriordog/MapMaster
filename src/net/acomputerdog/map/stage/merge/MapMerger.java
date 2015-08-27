package net.acomputerdog.map.stage.merge;

import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import net.acomputerdog.map.image.ImageUtils;
import net.acomputerdog.map.script.MapScript;
import net.acomputerdog.map.stage.convert.out.MapExporter;
import net.acomputerdog.map.stage.process.MapOverlay;
import net.acomputerdog.map.stage.scale.MapScaler;
import net.acomputerdog.map.tile.Tile;
import net.acomputerdog.map.tile.TileProvider;
import net.acomputerdog.map.tile.TileSource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MapMerger {
    private static final int REGION_SIZE_PIXELS = 512;
    private static final int REGION_SIZE_SHIFT = 9;

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
            boolean changed = false;
            for (int xRegion = startXRegion; xRegion < endXRegion; xRegion++) {
                getTilesForRegion(script, xRegion, yRegion, tileList); //results are in tileList
                if (tileList.size() > 0) {
                    changed = true;
                    for (Tile tile : tileList) {
                        copyBufferToScanLine(xRegion, lines, tile.getImage(), script);
                    }

                    tileList.clear();
                }
            }
            writeScanLines(lines, startXRegion, endXRegion, startYRegion, row, outImage, changed);
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
    }

    private static void writeScanLines(ImageLineInt[] lines, int x1, int x2, int y1, int row, PngWriter outImage, boolean changed) {
        int rY = y1 + (row / 512);
        MapExporter.writeRegionsPre(lines, x1, x2, rY, changed);
        for (int i = 0; i < lines.length; i++) {
            ImageLineInt line = lines[i];
            MapOverlay.writeIntoLine(row + i, line); //apply overlays
            outImage.writeRow(line);
        }
        MapExporter.writeRegionsPost(lines, x1, x2, rY, changed);
        MapScaler.copyScales(lines);
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
            ImageUtils.copyImageToPng(temp, y, lines[y], xOff, true);
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
        return block >> 9; //shift by nine is same as dividing by 512 and flooring
    }

}
