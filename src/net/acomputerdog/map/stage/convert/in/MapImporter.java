package net.acomputerdog.map.stage.convert.in;

import net.acomputerdog.map.tile.TileProvider;
import net.acomputerdog.map.tile.TileSource;
import net.acomputerdog.map.tool.ColorCalc;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MapImporter {
    private static int[] colors;
    private static int[] vmBuffer = new int[256 * 256 * 17];

    public static void initConverter() {
        colors = loadColors();
    }

    public static TileProvider wrapVM(TileSource source) {
        File sourceFile = source.getFile();
        try {
            ImportCache converter = new ImportCache();
            convertVMTiles(sourceFile, converter);
            return new ImportTileProvider(source, converter);
        } catch (Exception e) {
            throw new RuntimeException("Exception converting VM terrain!", e);
        }
    }

    private static void convertVMTiles(File source, ImportCache converter) throws IOException {
        File sourceFile = new File(source, "/Overworld (dimension 0)/");
        File[] contents = sourceFile.listFiles();
        if (contents != null && contents.length > 0) {
            for (File f : contents) {
                String fName = f.getName();
                if (fName.endsWith(".zip")) {
                    String loc = fName.substring(0, fName.length() - 4);
                    String[] parts = loc.split(",");
                    if (parts.length == 2) {
                        try {
                            int part1 = Integer.parseInt(parts[0]);
                            int part2 = Integer.parseInt(parts[1]);
                            int x = part1 / 2; //VM uses 256x256 sub-regions
                            int y = part2 / 2;
                            BufferedImage image = converter.getOrCreateImage(x, y, f);
                            writeVMSection(image, f, part1, part2);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
        } else {
            System.out.println("VM source \"" + source.getPath() + "\" is invalid!");
        }
    }

    private static void writeVMSection(BufferedImage image, File f, int part1, int part2) {
        int offX = (part1 % 2 == 0) ? 0 : 256;//256 * (Math.abs(part1) % 2); //0 if even, 256 if odd
        int offY = (part2 % 2 == 0) ? 0 : 256;//256 * (Math.abs(part2 + 1) % 2);

        try {
            int[] data = readVMSection(f);
            int index = 0;
            for (int y = offY; y < offY + 256; y++) {
                for (int x = offX; x < offX + 256; x++) {
                    image.setRGB(x, y, getVMColor(data, index, x - offX, y - offY));
                    index += 17;
                }
            }
        } catch (IOException e) {
            System.err.println("Exception converting VM section at " + part1 + "," + part2 + "!");
            e.printStackTrace();
        }
    }

    private static int getVMColor(int[] data, int index, int x, int y) {
        int color = colors[data[index + 2]];
        int height = data[index];
        boolean darkened = false;
        boolean lightened = false;
        if (y < 255) {
            int otherH = getVMHeight(data, x, y + 1);
            if (otherH > height && !darkened) {
                color = darken(color);
                darkened = true;
            } else if (otherH < height && !lightened) {
                color = lighten(color);
                lightened = true;
            }
        }
        if (x < 255) {
            int otherH = getVMHeight(data, x + 1, y);
            if (otherH > height && !darkened) {
                color = darken(color);
                darkened = true;
            } else if (otherH < height && !lightened) {
                color = lighten(color);
                lightened = true;
            }
        }
        if (y > 0) {
            int otherH = getVMHeight(data, x, y - 1);
            if (otherH > height && !darkened) {
                color = darken(color);
                darkened = true;
            } else if (otherH < height && !lightened) {
                color = lighten(color);
                lightened = true;
            }
        }
        if (x > 0) {
            int otherH = getVMHeight(data, x - 1, y);
            if (otherH > height && !darkened) {
                color = darken(color);
                darkened = true;
            } else if (otherH < height && !lightened) {
                color = lighten(color);
                lightened = true;
            }
        }
        return color;
    }

    private static int darken(int color) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        if (r > 10) {
            r -= 10;
        } else {
            r = 0;
        }
        if (g > 10) {
            g -= 10;
        } else {
            g = 0;
        }
        if (b > 10) {
            b -= 10;
        } else {
            b = 0;
        }
        return ColorCalc.getColor(a, r, g, b);
    }

    private static int lighten(int color) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        if (r < 245) {
            r += 10;
        } else {
            r = 255;
        }
        if (g < 245) {
            g += 10;
        } else {
            g = 255;
        }
        if (b < 245) {
            b += 10;
        } else {
            b = 255;
        }
        return ColorCalc.getColor(a, r, g, b);
    }

    private static int getVMHeight(int[] data, int x, int y) {
        int yOff = y * 256;
        int xOff = yOff + x;
        int index = xOff * 17;
        try {
            return data[index];
        } catch (Exception e) {
            System.err.println(x + " / " + y);
            System.err.println(yOff + " / " + xOff + " / " + index);
            throw e;
        }
    }

    private static int[] readVMSection(File f) throws IOException {
        try (ZipFile zip = new ZipFile(f)) {
            ZipEntry entry = null;
            while (zip.entries().hasMoreElements()) {
                ZipEntry currEntry = zip.entries().nextElement();
                if (currEntry.getName().equals("data")) {
                    entry = currEntry;
                    break;
                }
            }
            if (entry == null) {
                throw new IllegalArgumentException("Zip file is missing data section!");
            } else {
                InputStream in = zip.getInputStream(entry);
                int index = 0;
                while (in.available() > 0) {
                    vmBuffer[index] = in.read();
                    index++;
                }
                if (index != vmBuffer.length) {
                    System.out.println("Did not read enough bytes: " + index + "/" + vmBuffer.length + " (" + f.getName() + ")");
                }
                return vmBuffer;
            }
        }
    }

    private static int[] loadColors() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(MapImporter.class.getResourceAsStream("/VMTiles.cfg")))) {
            List<Integer> colorList = new ArrayList<>();
            while (reader.ready()) {
                String fullLine = reader.readLine().trim();
                if (!(fullLine.isEmpty() || fullLine.startsWith("#"))) {
                    String[] line = fullLine.split("=");
                    String[] parts = line[1].split(",");
                    int id = Integer.parseInt(line[0]);
                    int red = Integer.parseInt(parts[0]);
                    int green = Integer.parseInt(parts[1]);
                    int blue = Integer.parseInt(parts[2]);
                    int alpha = Integer.parseInt(parts[3]);
                    int mixed = ColorCalc.getColor(alpha, red, green, blue);
                    while (id >= colorList.size()) {
                        colorList.add(0);
                    }
                    colorList.set(id, mixed);
                }
            }
            int[] colors = new int[colorList.size()];
            for (int index = 0; index < colorList.size(); index++) {
                Integer i = colorList.get(index);
                colors[index] = i;
            }
            return colors;
        } catch (Exception e) {
            throw new RuntimeException("Exception loading VM tile mappings!", e);
        }
    }
}
