package com.cotfk.maps;

import com.cotfk.objects.*;
import com.cotfk.ui.MapIcons;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.Map;
import com.crown.maps.Point3D;

public class GlobalMap extends Map {
    public GlobalMap(String name, int xSize, int ySize, int zSize) {
        super(name, xSize, ySize, zSize);
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < ySize; x++) {
                add(new Grass(this, new Point3D(x, y, MapLevel.ground)));
            }
        }
    }

    public GlobalMap flatTerrain() {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < ySize; x++) {
                add(new Grass(this, new Point3D(x, y, MapLevel.ground)));
            }
        }
        return this;
    }

    public GlobalMap generateObjects() {
        for (int i = 0; i < xSize / 2; i++) {
            add(new Tree(this));
        }
        for (int i = 0; i < xSize / 10; i++) {
            add(new House(this));
        }
        for (int i = 0; i < xSize / 15; i++) {
            add(new Village(this));
        }
        return this;
    }

    @Override
    public GraphicalMapIcon getEmptyIcon() {
        return MapIcons.getIcons().get("emptiness");
    }

    @Override
    public ITemplate getName() {
        return I18n.of("map.global.name");
    }

    @Override
    public ITemplate getDescription() {
        return I18n.empty;
    }
}
