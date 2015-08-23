package net.acomputerdog.map.overlay;

import ar.com.hjg.pngj.ImageLineInt;
import net.acomputerdog.map.image.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextOverlay extends Overlay {

    private final String text;
    private final BufferedImage image;
    protected Graphics2D graphics;

    private int currRow = 0;

    public TextOverlay(File source) throws IOException {
        super(source);
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(source, "text.txt")))) {
            StringBuilder builder = new StringBuilder();
            while (reader.ready()) {
                builder.append(reader.readLine());
                builder.append('\n');
            }
          this.text = builder.toString();
        }
        this.image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Font font = new Font("Arial", Font.BOLD, 12);
        Color color = Color.BLACK;
        File fontFile = new File(source, "font.txt");
        if (fontFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fontFile))) {
                String fontName = reader.readLine();
                int size = Integer.parseInt(reader.readLine());
                font = new Font(fontName, Font.BOLD, size);
                color = new Color(Integer.parseInt(reader.readLine()));
            } catch (Exception e) {
                System.out.println("Invalid font data!");
                e.printStackTrace();
            }
        }
        graphics = image.createGraphics();
        graphics.setBackground(ImageUtils.COLOR_EMPTY);
        graphics.setColor(color);
        graphics.setFont(font);
        graphics.drawString(text, 3, 3 + font.getSize());
    }

    public String getText() {
        return text;
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
