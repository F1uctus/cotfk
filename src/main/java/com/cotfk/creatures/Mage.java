package com.cotfk.creatures;

import com.cotfk.skills.Spell;
import com.crown.common.ObjectCollection;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.Map;
import com.crown.maps.MapIcon;
import com.crown.maps.MapWeight;
import com.crown.maps.Point3D;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Mage extends CreatureBase {
    public ObjectCollection<Spell> knownSpells = new ObjectCollection<>();

    public Mage(String name, Map map, MapIcon<?> mapIcon, Point3D pt) {
        super(
            name,
            map,
            mapIcon,
            MapWeight.OBSTACLE,
            pt
        );
    }

    public ITemplate cast(Spell spell) {
        return cast(spell, null);
    }

    public ITemplate cast(Spell spell, CreatureBase target) {
        if (knownSpells.get(spell.getKeyName()) == null) {
            return I18n.of("mage.dontKnowSpell");
        }
        if (getEnergy() < spell.getEnergyCost()) {
            return I18n.of("mage.lowEnergy");
        }
        var targets = new ArrayList<CreatureBase>();
        if (target != null && pt.getDistance(target.getPt()) <= spell.getAffectedRange()) {
            targets.add(target);
        } else {
            var rangeObjsMatrix = map.getRaw2DArea(
                getPt(),
                spell.getAffectedRange()
            );
            for (var objRow : rangeObjsMatrix) {
                for (var obj : objRow) {
                    if (obj instanceof CreatureBase) {
                        targets.add((CreatureBase) obj);
                    }
                }
            }
        }
        if (targets.size() == 0) {
            return I18n.of("mage.tooFar");
        }

        for (var tgt : targets) {
            spell.apply(tgt);
            changeEnergy(-spell.getEnergyCost());
        }

        return I18n.fmtOf(
            "mage.casted",
            getName(),
            targets
                .stream()
                .map(CreatureBase::getName)
                .map(ITemplate::getLocalized)
                .collect(Collectors.joining(", ")),
            spell.getName()
        );
    }

    public ITemplate learn(Spell spell) {
        if (getEnergy() < spell.getLearnEnergyCost()) {
            return I18n.of("mage.learnLowEnergy");
        }
        changeEnergy(-spell.getLearnEnergyCost());
        knownSpells.add(spell);
        return I18n.fmtOf(
            "mage.learned",
            getName(),
            spell.getName()
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
