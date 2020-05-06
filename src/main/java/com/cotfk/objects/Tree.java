package com.cotfk.objects;


import com.cotfk.maps.GraphicalMapIcon;
import com.cotfk.maps.MapLevel;
import com.crown.common.utils.Random;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.Map;
import com.crown.maps.MapObject;
import com.crown.maps.MapWeight;
import com.crown.maps.Point3D;

public class Tree extends MapObject {
    public Tree(
        Map map
    ) {
        this(
            map,
            Random.getPoint(map).withZ(MapLevel.ground + 1)
        );
    }

    public Tree(
        Map map,
        Point3D pt
    ) {
        super(
            "Tree",
            map,
            new GraphicalMapIcon("tree.png"),
            MapWeight.OBSTACLE,
            pt
        );
    }

    @Override
    public ITemplate getName() {
        return I18n.of("tree");
    }

    @Override
    public ITemplate getDescription() {
        return I18n.of("");
    }
}
