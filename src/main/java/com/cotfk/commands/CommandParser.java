package com.cotfk.commands;

import com.cotfk.Main;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandParser {
    public static String clear(String input) {
        return input.strip().toLowerCase();
    }

    public static ITemplate parse(String input) {
        input = clear(input);
        if (input.isBlank()) {
            return I18n.okMessage;
        }
        final ArrayList<String> inputParts =
            new ArrayList<>(Arrays.asList(clear(input)
                .replaceAll("\\s+", " ")
                .split(" ")));

        // validating input
        if (inputParts.size() == 0) {
            return I18n.of("command.unknown");
        }

        var cmd = Commands.getCommands().get(inputParts.get(0));
        if (cmd == null) {
            return I18n.of("command.unknown", "\n", "welcome.help");
        }

        if (cmd.requiresPlayer && Actor.get() == null) {
            return I18n.of("player.notSelected");
        }

        int minArgs = cmd.getFormalParameters().length;
        int maxArgs = minArgs + cmd.getOptionalParameters().length;
        if (inputParts.size() - 1 > maxArgs) {
            // too much of args provided
            return I18n.of(
                I18n.fmtOf(
                    "command.argsExpected",
                    minArgs,
                    maxArgs
                ),
                "\n",
                I18n.fmtOf(
                    "help.aboutCommand",
                    inputParts.get(0)
                )
            );
        }
        if (inputParts.size() - 1 < minArgs) {
            // less than required args provided, request more
            System.out.println(I18n.of("command.tooFewArguments"));
            while (inputParts.size() - 1 < maxArgs) {
                if (inputParts.size() - 1 > minArgs) {
                    System.out.print(cmd.getOptionalParameters()[inputParts.size() - 1] + "> ");
                } else {
                    System.out.print(cmd.getFormalParameters()[inputParts.size() - 1] + "> ");
                }
                var param = clear(Main.s.nextLine());
                if (param.equals("!x")) {
                    return I18n.okMessage;
                }
                inputParts.add(param);
            }
        }

        var parameters = new LinkedCaseInsensitiveMap<String>();
        for (int i = 0; i < cmd.getFormalParameters().length; i++) {
            parameters.put(cmd.getFormalParameters()[i], inputParts.get(i + 1));
        }
        for (int i = 0; i < cmd.getOptionalParameters().length && i < inputParts.size() - 1; i++) {
            parameters.put(
                cmd.getOptionalParameters()[i],
                inputParts.get(parameters.size() + i + 1)
            );
        }
        return cmd.execute(parameters);
    }
}
