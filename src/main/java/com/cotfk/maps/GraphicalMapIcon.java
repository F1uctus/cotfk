package com.cotfk.maps;

import com.cotfk.ui.ImageTools;
import com.crown.maps.MapIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GraphicalMapIcon extends MapIcon<BufferedImage> {
    private final BufferedImage img;
    private final BufferedImage imgFlipped;

    public GraphicalMapIcon(String imagePath) {
        BufferedImage v;
        try {
            v = ImageIO.read(getClass().getResource("/icons/" + imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            v = null;
        }
        this.img = v;
        imgFlipped = ImageTools.flipX(v);
    }

    public GraphicalMapIcon(BufferedImage value) {
        this.img = value;
        imgFlipped = ImageTools.flipX(value);
    }

    public BufferedImage get() {
        if (flipped) return imgFlipped;
        return img;
    }
}
