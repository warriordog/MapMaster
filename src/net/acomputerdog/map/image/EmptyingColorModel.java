package net.acomputerdog.map.image;

import java.awt.color.ColorSpace;
import java.awt.image.DirectColorModel;

public class EmptyingColorModel extends DirectColorModel {
    public EmptyingColorModel(int bits, int rmask, int gmask, int bmask) {
        super(bits, rmask, gmask, bmask);
    }

    public EmptyingColorModel(int bits, int rmask, int gmask, int bmask, int amask) {
        super(bits, rmask, gmask, bmask, amask);
    }

    public EmptyingColorModel(ColorSpace space, int bits, int rmask, int gmask, int bmask, int amask, boolean isAlphaPremultiplied, int transferType) {
        super(space, bits, rmask, gmask, bmask, amask, isAlphaPremultiplied, transferType);
    }

    @Override
    public int getRGB(Object inData) {
        int color = ((int[]) inData)[0];
        if (((color >> 16) & 0xFF) == 255 && ((color >> 16) & 0xFF) == 255 && ((color >> 16) & 0xFF) == 255) {
            return ImageUtils.COLOR_EMPTY_INT;
        } else {
            return super.getRGB(inData);
        }
    }
}
