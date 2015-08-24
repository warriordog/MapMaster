package net.acomputerdog.map.stage.convert;

import net.acomputerdog.map.image.SourcedImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConverterCache {
    private final Map<RegionLoc, SourcedImage> cache;

    private RegionLoc temp;

    public ConverterCache() {
        cache = new HashMap<>();
        temp = new RegionLoc(0, 0);
    }

    public SourcedImage getOrCreateImage(int x, int y, File source) {
        temp.x = x;
        temp.y = y;

        SourcedImage image = cache.get(temp);
        if (image == null) {
            image = new SourcedImage(512, 512, BufferedImage.TYPE_INT_ARGB, source);
            cache.put(new RegionLoc(x, y), image);
        }
        return image;
    }

    public SourcedImage getImageForRegion(int x, int y) {
        temp.x = x;
        temp.y = y;

        return cache.get(temp);
    }

    public void saveCache(File outDir) throws IOException {
        for (Map.Entry<RegionLoc, SourcedImage> entry : cache.entrySet()) {
            File out = new File(outDir, "/" + entry.getKey().x + "," + entry.getKey().y + ".tmp");
            ImageIO.write(entry.getValue(), "PNG", out);
        }
    }

    private static class RegionLoc {
        private int x;
        private int y;

        public RegionLoc(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RegionLoc)) return false;

            RegionLoc regionLoc = (RegionLoc) o;

            return x == regionLoc.x && y == regionLoc.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
