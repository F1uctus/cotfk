package com.cotfk.objects;

import com.cotfk.maps.MapLevel;
import com.cotfk.ui.MapIcons;
import com.crown.common.utils.Random;
import com.crown.maps.*;

public class Tree extends MapObject {
    public static final int size = 2;

    public Tree(Map map) {
        super(
            "Tree",
            map,
            MapIcons.getIcons().get("tree"),
            LargeObjectTemplates.getSquareLinearZTemplate(
                Random.getPoint(map, size, size).withZ(MapLevel.ground + 1),
                size
            )
        );
    }

    @Override
    public MapIcon<?> getMapIcon() {
        return MapIcons.getIcons().get(getMapIconId());
    }
}
