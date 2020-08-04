package com.cotfk.maps;

import com.cotfk.ui.ImageTools;
import com.cotfk.ui.MapIcons;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.Direction;
import com.crown.maps.MapIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GraphicalMapIcon extends MapIcon<BufferedImage> {
    private final BufferedImage img;

    public GraphicalMapIcon(String keyName) {
        super(keyName);
        BufferedImage img;
        try {
            // TODO: replace with new ImageIcon(...).getImage() (GIF support)
            img = ImageIO.read(MapIcons.class.getResource("/icons/" + keyName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            img = null;
        }
        this.img = img;
    }

    @Override
    public ITemplate getName() {
        return I18n.empty;
    }

    @Override
    public ITemplate getDescription() {
        return I18n.empty;
    }

    public BufferedImage get() {
        var dir = getDirection();
        if (dir == Direction.east
            || dir == Direction.northEast
            || dir == Direction.southEast) {
            return ImageTools.flipX(img);
        }
        return img;
    }

    @Override
    public void stepAnimation() {

    }
}
