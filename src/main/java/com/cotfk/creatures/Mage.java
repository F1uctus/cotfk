package com.cotfk.creatures;

import com.cotfk.skills.Spell;
import com.cotfk.ui.MapIcons;
import com.crown.common.ObjectsMap;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.*;
import com.crown.time.Action;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Mage extends Human {
    public final ObjectsMap<Spell> knownSpells = new ObjectsMap<>();

    public Mage(String name, Map map, Point3D pt) {
        super(
            name,
            map,
            MapIcons.getIcons().get("mage"),
            MapWeight.OBSTACLE,
            pt
        );
    }

    public <T extends MapObject> ITemplate cast(Spell<T> spell, @Nullable T target) {
        if (knownSpells.get(spell.getKeyName()) == null) {
            return I18n.of("mage.dontKnowSpell");
        }
        if (getEnergy() < spell.getEnergyCost()) {
            return I18n.of("mage.lowEnergy");
        }
        ArrayList<T> targets;
        if (target != null && getPt0().getDistance(target.getPt0()) <= spell.getAffectedRange()) {
            targets = new ArrayList<>();
            targets.add(target);
        } else {
            targets = getMap().getAll(spell.getTargetClass(), getPt0(), spell.getAffectedRange());
            if (!spell.affectsCaster()) {
                targets.remove(this);
            }
        }
        if (targets.size() == 0) {
            return I18n.of("mage.tooFar");
        }
        return getTimeline().perform(new Action<Human>(this) {
            @Override
            public ITemplate perform() {
                for (var tgt : targets) {
                    spell.perform(tgt);
                    changeEnergy(-spell.getEnergyCost());
                }

                return I18n.fmtOf(
                    "mage.casted",
                    getName(),
                    targets
                        .stream()
                        .map(T::getName)
                        .map(ITemplate::getLocalized)
                        .collect(Collectors.joining(", ")),
                    spell.getName()
                );
            }

            @Override
            public ITemplate rollback() {
                for (var tgt : targets) {
                    spell.rollback(tgt);
                    changeEnergy(-spell.getEnergyCost());
                }
                return I18n.okMessage;
            }
        });
    }

    public ITemplate learn(Spell<?> spell) {
        if (getEnergy() < spell.getLearnEnergyCost()) {
            return I18n.of("mage.learnLowEnergy");
        }
        return getTimeline().perform(new Action<Human>(this) {
            @Override
            public ITemplate perform() {
                changeEnergy(-spell.getLearnEnergyCost());
                knownSpells.add(spell);
                return I18n.fmtOf(
                    "mage.learned",
                    getName(),
                    spell.getName()
                );
            }

            @Override
            public ITemplate rollback() {
                changeEnergy(spell.getLearnEnergyCost());
                knownSpells.remove(spell);
                return I18n.okMessage;
            }
        });
    }
}
