package com.cotfk.creatures;

import com.cotfk.ui.MapIcons;
import com.crown.creatures.Creature;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.*;

public abstract class CreatureBase extends Creature {
    public String lang = "en";

    public CreatureBase(
        String name,
        Map map,
        MapIcon<?> mapIcon,
        MapWeight mapWeight,
        Point3D position
    ) {
        super(
            name,
            map,
            mapIcon,
            mapWeight,
            position,
            200,
            100,
            1,
            5,
            1
        );
        xp = 100;
    }

    public ITemplate getStats() {
        return I18n.fmtOf(
            String.join(
                "\n",
                "{0}",
                "{1}: {2}/{3}",
                "{4}: {5}/{6}",
                "{7}: {8}/{9}",
                "{10}: {11}",
                "{12}: {13}",
                "{14}: {15}",
                "{16}: {17}",
                "{18}: {19}",
                "@ {20}"
            ),
            getKeyName(),
            "stats.hp", getHp(), getMaxHp(),
            "stats.energy", getEnergy(), getMaxEnergy(),
            "stats.speed", getSpeed(), getMaxSpeed(),
            "stats.fov", getFov(),
            "stats.level", getLevel(),
            "stats.xp", getXp(),
            "stats.xp.toNextLevel", getXpForLevel(level + 1),
            "stats.skillPoints", getSkillPoints(),
            getPt0()
        );
    }

    @Override
    public MapIcon<?> getMapIcon() {
        return MapIcons.getIcons().get(getMapIconId());
    }
}
