package com.cotfk.objects;

import com.cotfk.maps.MapLevel;
import com.cotfk.ui.MapIcons;
import com.crown.common.utils.Random;
import com.crown.maps.*;

public class Village extends MapObject {
    public static final int size = 4;

    public Village(Map map) {
        super(
            "Village",
            map,
            MapIcons.getIcons().get("village"),
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
