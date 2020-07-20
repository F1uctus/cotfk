package com.cotfk.objects;

import com.cotfk.ui.MapIcons;
import com.crown.maps.*;

public class Grass extends MapObject {
    public Grass(Map map, Point3D pt) {
        super("Grass", map, MapIcons.getIcons().get("grass"), MapWeight.BLOCKS_LIGHT, pt);
    }

    @Override
    public MapIcon<?> getMapIcon() {
        return MapIcons.getIcons().get(getMapIconId());
    }
}
