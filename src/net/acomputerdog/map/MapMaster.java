package net.acomputerdog.map;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.PngWriter;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import net.acomputerdog.map.script.MapScript;
import net.acomputerdog.map.stage.merge.MapMerger;
import net.acomputerdog.map.stage.process.MapOverlay;

import java.io.*;

/**
 * Main class
 */
public class MapMaster {

    private static final String VERSION_STRING = "MapMaster v0.0";
    private static final String USAGE_STRING = "mm <path_to_script>";

    private static File currentDir;

    public static void main(String[] args) {
        System.out.println(VERSION_STRING + "\n");
        if (args.length == 0) {
            System.out.println("Usage: \"" + USAGE_STRING + "\"");
        } else {
            System.out.println("Reading script from \"" + args[0] + "\"...");

            currentDir = new File(args[0]).getParentFile();

            long startTime = System.currentTimeMillis();

            MapScript script = null;
            try {
                script = loadScript(new File(args[0]));
            } catch (FileNotFoundException e) {
                System.out.println("Script file does not exist!");
            } catch (IOException e) {
                System.out.println("IO error reading script file!");
                e.printStackTrace();
            } catch (JsonParseException e) {
                System.out.println("Script file is invalid!");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Unknown exception reading script!");
                e.printStackTrace();
            }

            //not really a better way to do this while keeping separated exception trees
            if (script != null) {
                if (script.getVersion() == MapScript.FORMAT_VERSION) {
                    System.out.println("Read script with " + script.getTileSources().length + " tile and " + script.getOverlaySources().length + " overlay sources.");
                    try {
                        System.out.println("Creating map...");
                        createMap(script);
                        System.out.println("Done.  Map created successfully in " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds.");
                    } catch (Exception e) {
                        System.out.println("Exception creating map!");
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Script version does not match!");
                    System.out.println("Expected version " + MapScript.FORMAT_VERSION + ", got version " + script.getVersion() + "!");
                    System.out.println("The script cannot be loaded, please update it to the version shown and update version field!");
                }
            }
        }
    }

    private static MapScript loadScript(File f) throws IOException {
        try (JsonReader reader = createReader(f)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, MapScript.class);
        }
    }

    private static JsonReader createReader(File f) throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(f));
        reader.setLenient(true);
        return reader;
    }

    public static void createMap(MapScript script) {
        PngWriter map = new PngWriter(createRelativeFile(script.getOutputFile()), new ImageInfo(script.getWidth(), script.getHeight(), 8, false));
        map.setShouldCloseStream(true);
        System.out.println("Preparing overlays...");
        MapOverlay.initOverlays(script);
        System.out.println("Merging tiles...");
        MapMerger.mergeTiles(script, map);
        System.out.println("Saving map...");
        map.end();
    }

    public static File createRelativeFile(String path) {
        return new File(currentDir, path);
    }

}
