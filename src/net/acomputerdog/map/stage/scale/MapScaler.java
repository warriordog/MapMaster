package net.acomputerdog.map.stage.scale;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import net.acomputerdog.map.MapMaster;
import net.acomputerdog.map.image.TrackedPngWriter;
import net.acomputerdog.map.script.MapScript;

import java.io.File;

public class MapScaler {

    private static TrackedPngWriter[] writers = new TrackedPngWriter[0];

    public static void saveScaler() {
        for (TrackedPngWriter writer : writers) {
            int left = writer.imgInfo.rows - writer.getCurrentRow() - 1;
            if (left > 0) {
                System.out.println(left + " PNG lines left, filling with blanks.");
                for (int i = 0; i <= left; i++) {
                    writer.writeRow(new ImageLineInt(writer.imgInfo));
                }
            }
            writer.end();
        }
    }

    public static void initScaler(MapScript script, ImageInfo fullInf) {
        int numScales = script.getNumScales() - 1;
        if (numScales > 0) {
            writers = new TrackedPngWriter[numScales];
            for (int index = 0; index < numScales; index++) {
                int scale = (int) Math.pow(2, index + 1);
                ImageInfo inf = new ImageInfo(fullInf.cols / scale, fullInf.rows / scale, fullInf.bitDepth, fullInf.alpha);
                TrackedPngWriter writer = new TrackedPngWriter(getScaleFile(script, index), inf);
                writers[index] = writer;
            }
        }
    }

    private static File getScaleFile(MapScript script, int index) {
        String fullName = script.getOutputFile();
        int split = fullName.lastIndexOf('.');
        if (split == -1) {
            return MapMaster.createRelativeFile(fullName + "." + (index + 1));
        } else {
            return MapMaster.createRelativeFile(fullName.substring(0, split) + "." + (index + 1) + fullName.substring(split, fullName.length()));
        }
    }

    public static void copyScales(ImageLineInt[] lines) {
        for (int index = 0; index < writers.length; index++) {
            int scale = (int) Math.pow(2, index + 1);
            PngWriter writer = writers[index];
            ImageLineInt outLine = new ImageLineInt(writer.imgInfo);
            int[] outScan = outLine.getScanline();
            for (int i = 0; i < lines.length; i += scale) {
                ImageLineInt inLine = lines[i];
                int[] inScan = inLine.getScanline();
                for (int outIndex = 0; outIndex < outScan.length - 3; outIndex += 3) {
                    int inIndex = outIndex * scale;
                    if (inIndex + 3 >= inScan.length) {
                        break; //cut short in case size does not line up exactly.
                    }
                    System.arraycopy(inScan, inIndex, outScan, outIndex, 3);
                }
                writer.writeRow(outLine);
            }
        }
    }
}
