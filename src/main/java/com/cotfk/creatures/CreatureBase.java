package com.cotfk.creatures;

import com.crown.creatures.Creature;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.MapIcon;
import com.crown.maps.Map;
import com.crown.maps.MapWeight;
import com.crown.maps.Point3D;

public abstract class CreatureBase extends Creature {
    /**
     * Specifies if creature icon should be flipped or not.
     * All icons are left-sided by default.
     */
    public boolean flipped = false;

    public String lang = "en";

    public CreatureBase(
        String name,
        Map map,
        MapIcon<?> mapIcon,
        MapWeight mapWeight,
        Point3D position
    ) {
        super(name, map, mapIcon, mapWeight, position);
        map.add(this);
    }

    public ITemplate getStats() {
        return I18n.fmtOf(
            String.join(
                "\n",
                "{0}: {1}/{2}",
                "{3}: {4}/{5}",
                "{6}: {7}/{8}",
                "{9}: {10}",
                "{11}: {12}",
                "{13}: {14}",
                "{15}: {16}",
                "{17}: {18}",
                "@ {19}"
            ),
            "stats.hp", getHp(), getMaxHp(),
            "stats.energy", getEnergy(), getMaxEnergy(),
            "stats.speed", getSpeed(), getMaxSpeed(),
            "stats.fov", getFov(),
            "stats.xp", getXp(),
            "stats.level", getLevel(),
            "stats.skillPoints", getSkillPoints(),
            "stats.xp.toNextLevel", getMaxXp(),
            getPt()
        );
    }

    // region HP

    public ITemplate adjustHp(int delta) {
        assert delta > 0;
        if (skillPoints > 0) {
            if (hp + delta > maxHp) {
                hp = maxHp;
                return I18n.of("stats.hp.max");
            }
            skillPoints -= 1;
            return changeHp(delta);
        } else {
            return I18n.of("stats.xp.notEnough");
        }
    }

    public ITemplate changeHp(int delta) {
        maxHp += delta;
        return I18n.changeableOf("stats.hp.{0}", delta);
    }

    // endregion

    // region Energy

    public ITemplate sleep(int delta) {
        assert delta > 0 && energy + delta <= maxEnergy;
        if (energy <= 50) {
            return changeEnergy(delta);
        } else {
            return I18n.of("stats.energy.highEnough");
        }
    }

    public ITemplate changeEnergy(int delta) {
        energy += delta;
        return I18n.changeableOf("stats.energy.{0}", delta);
    }

    // endregion

    // region Speed

    public ITemplate changeSpeed(int delta) {
        speed += delta;
        return I18n.changeableOf("stats.speed.{0}", delta);
    }

    // endregion

    // region FOV

    public ITemplate adjustFov(int delta) {
        assert delta > 0;
        if (skillPoints > 0) {
            if (fov + delta > maxFov) {
                fov = maxFov;
                return I18n.of("stats.fov.max");
            }
            skillPoints -= 1;
            return changeFov(delta);
        } else {
            return I18n.of("stats.xp.notEnough");
        }
    }

    public ITemplate changeFov(int delta) {
        fov += delta;
        return I18n.changeableOf("stats.fov.{0}", delta);
    }

    // endregion

    // region XP

    public ITemplate adjustXp() {
        return changeXp(1);
    }

    public ITemplate changeXp(int delta) {
        assert delta > 0;
        var addedXp = xp + delta;
        while (addedXp >= maxXp) {
            changeLevel();
            addedXp -= maxXp;
        }
        if (addedXp > 0) {
            xp += addedXp;
        }
        return I18n.fmtOf("stats.xp.increased", delta);
    }

    // endregion

    // region Level

    public ITemplate changeLevel() {
        level += 1;
        changeSkillPoints();
        xp = 0;
        maxXp = maxXp + (int) ((float) maxXp / 100 * 30);
        return I18n.of("stats.level.next");
    }

    // endregion

    @Override
    public ITemplate changeSkillPoints() {
        skillPoints++;
        return I18n.of("OK");
    }

    public ITemplate move(Point3D targetPos) {
        var delta = (int) getPt().getDistance(targetPos);
        // TODO insert path finder
        if (!map.contains(targetPos) || map.isObstacle(targetPos.x, targetPos.y)) {
            return I18n.of("Cant move here, there is another object!");
        } else if (getEnergy() >= delta) {
            teleport(targetPos);
            changeEnergy(-delta);
            changeXp(delta);
            return I18n.of("OK");
        } else {
            return I18n.of("stats.energy.low");
        }
    }
}
