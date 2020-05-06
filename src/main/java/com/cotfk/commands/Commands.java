package com.cotfk.commands;

import com.cotfk.creatures.Mage;
import com.cotfk.skills.Spell;
import com.crown.common.ObjectCollection;
import com.crown.i18n.I18n;
import com.crown.maps.Point3D;

import static com.cotfk.Main.gameState;

/**
 * Contains all built-in game commands.
 */
public class Commands extends ObjectCollection<Command> {
    public Commands fromBuiltIn() {
        // region actions for selected player

        add(new Command(
            "main",
            (args) -> gameState.unselectPlayer(),
            true
        ));
        add(new Command(
            "kill",
            (args) -> gameState.removePlayer(),
            true
        ));
        add(new Command(
            "stats",
            (args) -> gameState.getCurrentPlayer().getStats(),
            true
        ));
        add(new Command(
            "move",
            (args) -> gameState.getCurrentPlayer().move(
                gameState.getCurrentPlayer().getPt().plus(
                    new Point3D(
                        Integer.parseInt(args.get("x")),
                        Integer.parseInt(args.get("y")),
                        0
                    )
                )),
            true,
            new String[] {
                "x",
                "y"
            }
        ));
        add(new Command(
            "sleep",
            (args) -> gameState.getCurrentPlayer().sleep(),
            true
        ));
        add(new Command(
            "wait",
            (args) -> gameState
                .getCurrentPlayer()
                .sleep(Integer.parseInt(args.get("time"))),
            true,
            new String[] {
                "time"
            }
        ));

        // endregion

        // region actions for selected mage

        add(new Command(
            "cast",
            (args) -> {
                var cp = gameState.getCurrentPlayer();
                if (!(cp instanceof Mage)) {
                    return (I18n.of("player.notAMage"));
                }
                var spell = gameState.spells.get(args.get("spell"));
                if (spell == null) {
                    return (I18n.of("spell.unknown"));
                }
                var target = gameState.players.get(args.get("target"));
                return ((Mage) cp).cast(spell, target);
            },
            true,
            new String[] {
                "spell"
            },
            new String[] {
                "target?"
            }
        ));
        add(new Command(
            "learn",
            (args) -> {
                var cp = gameState.getCurrentPlayer();
                if (!(cp instanceof Mage)) {
                    return (I18n.of("player.notAMage"));
                }
                var spell = gameState.spells.get(args.get("spell"));
                if (spell == null) {
                    return (I18n.of("spell.unknown"));
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
            (args) -> I18n.of(gameState.playersAsTable()),
            false
        ));
        add(new Command(
            "new",
            (args) -> gameState.addPlayer(args.get("type"), args.get("name")),
            false,
            new String[] {
                "type",
                "name"
            }
        ));
        add(new Command(
            "select",
            (args) -> gameState.selectPlayer(args.get("name")),
            false,
            new String[] {
                "name"
            }
        ));
        add(new Command(
            "lang",
            (args) -> {
                if (!I18n.getLanguages().contains(args.get("name"))) {
                    return I18n.of(
                        "lang.invalid",
                        I18n.fmtOf(
                            "lang.available",
                            String.join(",", I18n.getLanguages())
                        )
                    );
                } else {
                    gameState.getCurrentPlayer().lang = args.get("name");
                }
                return I18n.of("OK");
            },
            true,
            new String[] {
                "name"
            }
        ));
        add(new Command(
            "help",
            (args) -> {
                var lang = "en";
                if (gameState.getCurrentPlayer() != null) {
                    lang = gameState.getCurrentPlayer().lang;
                }
                var result = new StringBuilder();
                String subject = args.get("subject");
                if (subject == null) {
                    for (Command cmd : all.values()) {
                        result.append(cmd.getHelp(lang));
                        result.append("\n");
                    }
                } else if (subject.equals("spells")) {
                    for (Spell spell : gameState.spells.values()) {
                        result.append(spell.getHelp(lang));
                        result.append("\n");
                    }
                } else {
                    var cmd = all.get(args.get("subject"));
                    if (cmd != null) {
                        result.append(cmd.getHelp(lang));
                    }
                }
                return I18n.of(result.toString());
            },
            false,
            new String[0],
            new String[] {
                "subject?"
            }
        ));

        return this;
    }
}
