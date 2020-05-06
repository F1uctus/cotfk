package com.cotfk.skills;

import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.MapObject;
import com.crown.skills.Skill;

import java.util.function.Consumer;

public class Spell extends Skill {
    public Spell(
        String keyName,
        Consumer<MapObject> effect,
        int energyCost,
        int learnEnergyCost,
        int affectedRange
    ) {
        super(keyName, effect, energyCost, learnEnergyCost, affectedRange);
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
