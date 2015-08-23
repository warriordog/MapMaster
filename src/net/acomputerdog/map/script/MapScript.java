package net.acomputerdog.map.script;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import net.acomputerdog.map.MapMaster;
import net.acomputerdog.map.overlay.Overlay;
import net.acomputerdog.map.tile.TileSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapScript {
    public static final int FORMAT_VERSION = 0;

    private int version;
    private TileSource[] tileSources;
    private String[] overlaySources;
    private transient File[] overlayFiles;
    private String outputFile;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private MapScript() {
        //private constructor for serialization
    }

    public MapScript(TileSource[] tileSources, String[] overlaySources, String outputFile) {
        this.tileSources = tileSources;
        this.overlaySources = overlaySources;
        this.outputFile = outputFile;
    }

    public int getVersion() {
        return version;
    }

    public TileSource[] getTileSources() {
        return tileSources;
    }

    public File[] getOverlaySources() {
        if (overlayFiles == null) {
            overlayFiles = new File[overlaySources.length];
            for (int i = 0; i < overlaySources.length; i++) {
                String str = overlaySources[i];
                overlayFiles[i] = MapMaster.createRelativeFile(str);
            }
        }
        return overlayFiles;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public int getWidth() {
        return x2 - x1;
    }

    public int getHeight() {
        return y2 - y1;
    }

    public int worldToImageX(int x) {
        return x - x1;
    }

    public int worldToImageY(int y) {
        return y - y1;
    }

    /**
     * Creates a template MapScript
     */
    public static void main(String[] args) {
        String name = "script.json";
        if (args.length > 0) {
            name = args[0];
        }

        MapScript script = new MapScript();
        script.outputFile = "./map.png";
        script.x1 = -1000;
        script.y1 = -1000;
        script.x2 = 1000;
        script.y2 = 1000;
        script.tileSources = new TileSource[2];
        script.tileSources[0] = new TileSource(TileSource.Format.JOURNEYMAP, "./source1/");
        script.tileSources[1] = new TileSource(TileSource.Format.JOURNEYMAP, "./source2/");
        script.overlaySources = new String[2];
        script.overlaySources[0] = "./overlays1/";
        script.overlaySources[1] = "./overlays2/";

        Gson gson = new Gson();
        try (JsonWriter writer = createWriter(name)) {
            gson.toJson(script, MapScript.class, writer);
            System.out.println("Created template MapScript.");
        } catch (IOException e) {
            System.out.println("Exception saving json!");
            e.printStackTrace();
        }
    }

    private static JsonWriter createWriter(String name) throws IOException {
        JsonWriter writer = new JsonWriter(new FileWriter(new File(name)));
        writer.setIndent("  ");
        writer.setLenient(true);
        return writer;
    }
}
