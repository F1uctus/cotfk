package com.cotfk.skills;

import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.MapObject;
import com.crown.skills.Skill;
import com.crown.time.Action;

public class Spell<T extends MapObject> extends Skill<T> {
    private final boolean affectsCaster;

    public Spell(
        Class<T> targetClass,
        String keyName,
        Action<T> effect,
        int energyCost,
        int learnEnergyCost,
        int affectedRange,
        boolean affectsCaster
    ) {
        super(
            targetClass,
            keyName,
            effect,
            energyCost,
            learnEnergyCost,
            affectedRange
        );
        this.affectsCaster = affectsCaster;
    }

    public boolean affectsCaster() {
        return affectsCaster;
    }

    public ITemplate getName() {
        return I18n.of("spell." + getKeyName().toLowerCase() + ".name");
    }

    public ITemplate getDescription() {
        return I18n.of("spell." + getKeyName().toLowerCase() + ".description");
    }

    public String getHelp(String lang) {
        String desc = getDescription().getLocalized(lang);
        var descLines = desc.split("\n");
        if (descLines.length > 1) {
            var multilineDesc = new StringBuilder(descLines[0]);
            for (int i = 1; i < descLines.length; i++) {
                // length calculation
                multilineDesc.append("\n").append(" ".repeat(21)).append(descLines[i]);
            }
            desc = multilineDesc.toString();
        }
        return String.format("%-20s %-10s", getName(), desc);
    }

    @Override
    public String toString() {
        return getHelp("en");
    }
}
