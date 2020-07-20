package com.cotfk.commands;

import com.cotfk.creatures.Mage;
import com.cotfk.skills.Spell;
import com.cotfk.skills.Spells;
import com.crown.common.ObjectsMap;
import com.crown.i18n.I18n;
import com.crown.time.Timeline;

import java.time.Duration;
import java.time.Instant;

/**
 * Contains all built-in game commands.
 */
public class Commands extends ObjectsMap<Command> {
    private static Commands instance;

    public static Commands getCommands() {
        if (instance == null) {
            synchronized (Commands.class) {
                if (instance == null) {
                    instance = new Commands();
                }
            }
        }
        return instance;
    }

    Commands() {
        // region actions for players

        add(new Command(
            "main",
            args -> Actor.free(),
            true
        ));
        add(new Command(
            "kill",
            args -> Actor.kill(),
            true
        ));
        add(new Command(
            "stats",
            args -> Actor.get().getStats(),
            true
        ));
        add(new Command(
            "time",
            args -> {
                Instant pt =
                    Timeline.getClock()
                        .now()
                        .plus(Duration.ofSeconds(Integer.parseInt(args.get("seconds"))));
                var result = Timeline.move(Actor.get(), pt);
                Actor.pick(result.getValue().getKeyName(), result.getValue().getTimeline());
                return result.getKey();
            },
            true,
            new String[] {
                "seconds"
            }
        ));
        add(new Command(
            "commit",
            args -> {
                return Timeline.commitChanges(Actor.get());
            },
            true
        ));
        add(new Command(
            "rollback",
            args -> {
                var result = Timeline.rollbackChanges(Actor.get());
                Actor.pick(result.getValue().getKeyName(), Timeline.main);
                return result.getKey();
            },
            true
        ));
        add(new Command(
            "move",
            args -> Actor.get().moveBy(
                Integer.parseInt(args.get("x")),
                Integer.parseInt(args.get("y"))
            ),
            true,
            new String[] {
                "x",
                "y"
            }
        ));
        add(new Command(
            "sleep",
            args -> Actor.get().sleep(),
            true
        ));
        add(new Command(
            "wait",
            args -> Actor.get()
                .changeEnergyBy(Integer.parseInt(args.get("time"))),
            true,
            new String[] {
                "time"
            }
        ));
        add(new Command(
            "lang",
            args -> {
                if (!I18n.getLanguages().contains(args.get("name"))) {
                    return I18n.of(
                        "lang.invalid",
                        I18n.fmtOf(
                            "lang.available",
                            String.join(",", I18n.getLanguages())
                        )
                    );
                } else {
                    Actor.get().lang = args.get("name");
                }
                return I18n.okMessage;
            },
            true,
            new String[] {
                "name"
            }
        ));

        // endregion

        // region actions for mages

        add(new Command(
            "cast",
            args -> {
                var cp = Actor.get();
                if (!(cp instanceof Mage)) {
                    return I18n.of("player.notAMage");
                }
                var spell = Spells.getSpells().get(args.get("spell"));
                if (spell == null) {
                    return I18n.of("spell.unknown");
                }
                var target = Actor.getGameState().players.get(args.get("target"));
                return ((Mage) cp).cast(spell, target);
            },
            true,
            new String[] {
                "spell"
            },
            new String[] {
                "target"
            }
        ));
        add(new Command(
            "learn",
            args -> {
                var cp = Actor.get();
                if (!(cp instanceof Mage)) {
                    return I18n.of("player.notAMage");
                }
                var spell = Spells.getSpells().get(args.get("spell"));
                if (spell == null) {
                    return I18n.of("spell.unknown");
                }
                return ((Mage) cp).learn(spell);
            },
            true,
            new String[] {
                "spell"
            }
        ));

        // endregion

        add(new Command(
            "all",
            args -> I18n.of(Actor.getGameState().playersAsTable()),
            false
        ));
        add(new Command(
            "new",
            args -> {
                var result = Actor.getGameState().addPlayer(args.get("type"), args.get("name"));
                Actor.pick(args.get("name"));
                return result;
            },
            false,
            new String[] {
                "type",
                "name"
            }
        ));
        add(new Command(
            "select",
            args -> Actor.pick(args.get("name")),
            false,
            new String[] {
                "name"
            }
        ));
        add(new Command(
            "help",
            args -> {
                var lang = "en";
                var cp = Actor.get();
                if (cp != null) {
                    lang = cp.lang;
                }
                var result = new StringBuilder();
                String subject = args.get("subject");
                if (subject == null) {
                    for (Command cmd : values()) {
                        result.append(cmd.getHelp(lang));
                        result.append("\n");
                    }
                } else if (subject.equals("spells")) {
                    for (Spell spell : Spells.getSpells().values()) {
                        result.append(spell.getHelp(lang));
                        result.append("\n");
                    }
                } else {
                    var cmd = byKeyNameMap.get(args.get("subject"));
                    if (cmd != null) {
                        result.append(cmd.getHelp(lang));
                    }
                }
                return I18n.of(result.toString());
            },
            false,
            new String[0],
            new String[] {
                "subject"
            }
        ));
    }
}
