package net.acomputerdog.map.overlay;

import ar.com.hjg.pngj.ImageLineInt;
import net.acomputerdog.map.image.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ShapeOverlay extends Overlay {

    //todo replace with enum
    private static final int RECTANGLE  = 0;
    private static final int OVAL = 1;
    private static final int RECTANGLE_FILLED = 2;
    private static final int OVAL_FILLED = 3;
    private static final int LINE = 4;

    private static final Color COLOR_DEFAULT = Color.BLACK;

    private final BufferedImage image;

    private int currRow = 0;

    public ShapeOverlay(File source) throws IOException {
        super(source);
        int type = getType(source);
        Color color = getColor(source);
        this.image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(color);
        switch (type) {
            case RECTANGLE:
                graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                break;
            case OVAL:
                graphics.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                break;
            case RECTANGLE_FILLED:
                graphics.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
                break;
            case OVAL_FILLED:
                graphics.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                break;
            case LINE:
                graphics.drawLine(0, 0, getWidth() - 1, getHeight() - 1);
                break;
            default:
                throw new RuntimeException("Invalid shape type!");
        }
    }

    private int getType(File source) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(source, "shape.txt")))) {
            String type = reader.readLine().toLowerCase();
            switch (type) {
                case "rectangle":
                    return RECTANGLE;
                case "oval":
                    return OVAL;
                case "rectangle_filled":
                    return RECTANGLE_FILLED;
                case "oval_filled":
                    return OVAL_FILLED;
                case "line":
                    return LINE;
                default:
                    throw new IllegalArgumentException("Unknown shape: " + type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception reading shape type!", e);
        }
    }

    private Color getColor(File source) {
        File colorFile = new File(source, "color.txt");
        if (colorFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(colorFile))) {
                int col = Integer.parseInt(reader.readLine());
                return new Color(col);
            } catch (Exception e) {
                throw new RuntimeException("Exception reading shape color!", e);
            }
        } else {
            return COLOR_DEFAULT;
        }
    }

    @Override
    public void apply(int row, ImageLineInt line) {
        if (currRow >= image.getHeight()) {
            throw new IllegalStateException("Attempted to draw overlay too many times: " + currRow);
        }
        ImageUtils.copyImageToPng(image, currRow, line, getXLoc(), true);
        currRow++;
    }
}
