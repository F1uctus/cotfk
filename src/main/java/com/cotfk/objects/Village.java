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

public class Village extends MapObject {
    public Village(
        Map map
    ) {
        this(
            map,
            // BUG count object size
            Random.getPoint(map).withZ(0)
        );
    }

    public Village(
        Map map,
        Point3D pt
    ) {
        super(
            "Village",
            map,
            new GraphicalMapIcon("windmill.png"),
            MapWeight.OBSTACLE,
            new Point3D[] {
                //  /  \  <- can be passed behind
                pt.plus(new Point3D(0, 0, MapLevel.ground + 2)),
                pt.plus(new Point3D(1, 0, MapLevel.ground + 2)),
                //  |  |  <- on the ground, obstacle
                pt.plus(new Point3D(0, 1, MapLevel.ground + 1)),
                pt.plus(new Point3D(1, 1, MapLevel.ground + 1))
            }
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
