package com.cotfk.commands;

import com.crown.common.NamedObject;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;

import java.util.function.Function;

public class Command extends NamedObject {
    public final boolean requiresPlayer;

    private final Function<LinkedCaseInsensitiveMap<String>, ITemplate> action;
    private final String[] formalParameters;
    private final String[] optionalParameters;

    public Command(
        String keyName,
        Function<LinkedCaseInsensitiveMap<String>, ITemplate> action,
        boolean requiresPlayer
    ) {
        this(
            keyName,
            action,
            requiresPlayer,
            new String[0],
            new String[0]
        );
    }

    public Command(
        String keyName,
        Function<LinkedCaseInsensitiveMap<String>, ITemplate> action,
        boolean requiresPlayer,
        String[] formalParameters
    ) {
        this(
            keyName,
            action,
            requiresPlayer,
            formalParameters,
            new String[0]
        );
    }

    public Command(
        String keyName,
        Function<LinkedCaseInsensitiveMap<String>, ITemplate> action,
        boolean requiresPlayer,
        String[] formalParameters,
        String[] optionalParameters
    ) {
        super(keyName);
        this.action = action;
        this.requiresPlayer = requiresPlayer;
        this.formalParameters = formalParameters;
        this.optionalParameters = optionalParameters;
    }

    public ITemplate getName() {
        return I18n.of(getKeyName());
    }

    public ITemplate getDescription() {
        return I18n.of("command." + getKeyName() + ".description");
    }

    public String[] getFormalParameters() {
        return formalParameters;
    }

    public String[] getOptionalParameters() {
        return optionalParameters;
    }

    public ITemplate execute(LinkedCaseInsensitiveMap<String> actualParameters) {
        if (actualParameters.size() < formalParameters.length) {
            return I18n.of("Invalid parameters count.");
        }
        return action.apply(actualParameters);
    }

    public String getHelp(String lang) {
        String params = "";
        if (getFormalParameters().length > 0) {
            params += String.join(" ", getFormalParameters());
        }
        if (getOptionalParameters().length > 0) {
            if (!params.equals("")) {
                params += " ";
            }
            params += String.join(" ", getOptionalParameters());
        }
        var desc = getDescription().getLocalized(lang);
        var descLines = desc.split("\n");
        if (descLines.length > 1) {
            StringBuilder multilineDesc = new StringBuilder(descLines[0]);
            for (int i = 1; i < descLines.length; i++) {
                // length calculation
                multilineDesc.append("\n").append(" ".repeat(32)).append(descLines[i]);
            }
            desc = multilineDesc.toString();
        }
        return String.format("%-10s %-20s %-10s", getName(), params, desc);
    }

    @Override
    public String toString() {
        return getHelp("en");
    }
}
