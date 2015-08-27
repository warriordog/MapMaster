package net.acomputerdog.map.stage.convert.out;

import ar.com.hjg.pngj.ImageLineInt;
import net.acomputerdog.map.MapMaster;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapExporter {

    private static List<Export> preExports;
    private static List<Export> postExports;

    public static void init(ExportType[] types) {
        preExports = new ArrayList<>();
        postExports = new ArrayList<>();
        if (types != null && types.length > 0) {
            for (ExportType type : types) {
                File path = MapMaster.createRelativeFile(type.getOutputPath());
                Export export;
                switch (type.getMapFormat()) {
                    case JOURNEYMAP:
                        export = new PNGExport(path, ',');
                        break;
                    case MAPWRITER:
                        export = new PNGExport(path, '.');
                        break;
                    case VOXELMAP:
                        export = new VMTileExport(path);
                        break;
                    case VOXELMAP_TILE:
                        export = new VMDataExport(path);
                        break;
                    default:
                        System.out.println("Invalid export type: " + type.getMapFormat().name());
                        continue;
                }
                if (type.isIncludeOverlays()) {
                    postExports.add(export);
                } else {
                    preExports.add(export);
                }
            }
        }
    }

    public static void writeRegionsPre(ImageLineInt[] lines, int x1, int x2, int y, boolean changed) {
        if (changed) {
            for (Export export : preExports) {
                export.exportRegions(lines, x1, x2, y);
            }
        }
    }

    public static void writeRegionsPost(ImageLineInt[] lines, int x1, int x2, int y, boolean changed) {
        if (changed) {
            for (Export export : postExports) {
                export.exportRegions(lines, x1, x2, y);
            }
        }
    }
}
