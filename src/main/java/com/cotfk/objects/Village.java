package com.cotfk.objects;


import com.cotfk.maps.GraphicalMapIcon;
import com.crown.common.utils.Random;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.Map;
import com.crown.maps.MapObject;
import com.crown.maps.MapWeight;
import com.crown.maps.Point3D;

public class Village extends MapObject {
    public Village(
        Map map
    ) {
        this(
            map,
            Random.getPoint(map).withZ(1)
        );
    }

    public Village(
        Map map,
        Point3D pt
    ) {
        super(
            "Tree",
            map,
            new GraphicalMapIcon("windmill.png"),
            MapWeight.OBSTACLE,
            pt
        );
    }

    @Override
    public ITemplate getName() {
        return I18n.of("village");
    }

    @Override
    public ITemplate getDescription() {
        return I18n.of("");
    }
}
