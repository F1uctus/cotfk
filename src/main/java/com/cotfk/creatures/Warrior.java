package com.cotfk.creatures;

import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.MapIcon;
import com.crown.maps.Map;
import com.crown.maps.MapWeight;
import com.crown.maps.Point3D;

public class Warrior extends CreatureBase {
    public Warrior(String name, Map map, MapIcon<?> mapIcon, Point3D pt) {
        super(
            name,
            map,
            mapIcon,
            MapWeight.OBSTACLE,
            pt
        );
    }

    @Override
    public ITemplate getName() {
        return I18n.of(getKeyName());
    }

    @Override
    public ITemplate getDescription() {
        // TODO add descriptions for named objects
        return I18n.of("");
    }
}
