package net.acomputerdog.map.overlay;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LabelOverlay extends TextOverlay {
    public LabelOverlay(File source) throws IOException {
        super(source);
        graphics.drawRect(0,0,getWidth()-1, getHeight()-1);
    }
}
