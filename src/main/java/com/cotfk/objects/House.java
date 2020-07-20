package com.cotfk.objects;

import com.cotfk.maps.MapLevel;
import com.cotfk.ui.MapIcons;
import com.crown.common.utils.Random;
import com.crown.maps.*;

public class House extends MapObject {
    public static final int size = 3;

    public House(Map map) {
        super(
            "House",
            map,
            MapIcons.getIcons().get("house1"),
            MapWeight.OBSTACLE,
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
