package com.cotfk.creatures;

import com.cotfk.ui.MapIcons;
import com.crown.maps.*;

public class Warrior extends Human {
    public Warrior(String name, Map map, Point3D pt) {
        super(
            name,
            map,
            MapIcons.addIcon("pirate"),
            pt
        );
    }
}
