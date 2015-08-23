package net.acomputerdog.map.stage.process;

import ar.com.hjg.pngj.ImageLineInt;
import net.acomputerdog.map.overlay.LabelOverlay;
import net.acomputerdog.map.overlay.Overlay;
import net.acomputerdog.map.overlay.ShapeOverlay;
import net.acomputerdog.map.overlay.TextOverlay;
import net.acomputerdog.map.script.MapScript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class MapOverlay {

    private static MapScript script;
    private static Map<Integer, List<Overlay>> rowMap = new HashMap<>();

    public static void initOverlays(MapScript mapScript) {
        script = mapScript;
        for (File dir : mapScript.getOverlaySources()) {
            File[] contents = dir.listFiles();
            if (contents == null) {
                System.out.println("Overlay source \"" + dir.getName() + "\" is not a directory!");
            } else {
                for (File f : contents) {
                    if (f.isDirectory()) {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(new File(f, "type.txt")));
                            String line = reader.readLine().toLowerCase();
                            Overlay overlay = null;
                            switch (line) {
                                case "text":
                                    overlay = new TextOverlay(f);
                                    break;
                                case "shape":
                                    overlay = new ShapeOverlay(f);
                                    break;
                                case "label":
                                    overlay = new LabelOverlay(f);
                                    break;
                                default:
                                    System.out.println("Unsupported overlay type: " + line);
                            }
                            if (overlay != null) {
                                for (int index = overlay.getYLoc(); index < overlay.getYLoc() + overlay.getHeight(); index++) {
                                    getOverlays(index).add(overlay);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Exception loading overlay: " + dir.getName());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static List<Overlay> getOverlays(int index) {
        List<Overlay> overlays = rowMap.get(index);
        if (overlays == null) {
            overlays = new ArrayList<>();
            rowMap.put(index, overlays);
        }
        return overlays;
    }

    public static void writeIntoLine(int row, ImageLineInt line) {
        List<Overlay> overlays = rowMap.get(row);
        if (overlays != null && overlays.size() > 0) {
            for (Overlay overlay : overlays) {
                overlay.apply(row, line);
            }
        }
    }
}
