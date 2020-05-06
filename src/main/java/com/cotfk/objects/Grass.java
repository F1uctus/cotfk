package com.cotfk.objects;

import com.cotfk.maps.GraphicalMapIcon;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.*;

public class Grass extends MapObject {
    public Grass(
        Map map,
        Point3D pt
    ) {
        super(
            "Grass",
            map,
            new GraphicalMapIcon("grass.png"),
            MapWeight.BLOCKS_LIGHT,
            pt
        );
    }

    @Override
    public ITemplate getName() {
        return I18n.of("grass");
    }

    @Override
    public ITemplate getDescription() {
        return I18n.of("");
    }
}
