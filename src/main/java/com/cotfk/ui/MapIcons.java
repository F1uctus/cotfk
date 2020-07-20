package com.cotfk.ui;

import com.cotfk.maps.GraphicalMapIcon;
import com.crown.common.ObjectsMap;

public class MapIcons extends ObjectsMap<GraphicalMapIcon> {
    private static final String[] imageNames = {
        "emptiness",
        "grass",
        "house1",
        "mage",
        "pirate",
        "tree",
        "village",
        "windmill"
    };

    private static MapIcons instance;

    public static MapIcons getIcons() {
        if (instance == null) {
            synchronized (MapIcons.class) {
                if (instance == null) {
                    instance = new MapIcons();
                }
            }
        }
        return instance;
    }

    MapIcons() {
        for (String name : imageNames) {
            add(new GraphicalMapIcon(name));
        }
    }
}
