package com.cotfk;

import com.cotfk.creatures.CreatureBase;
import com.cotfk.creatures.Mage;
import com.cotfk.creatures.Warrior;
import com.cotfk.maps.GlobalMap;
import com.cotfk.maps.GraphicalMapIcon;
import com.cotfk.skills.Spell;
import com.crown.common.ObjectCollection;
import com.crown.common.utils.Random;
import com.crown.creatures.Creature;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.MapWeight;
import com.crown.maps.Point3D;

import java.util.stream.Collectors;

import static com.cotfk.Main.gameState;

/**
 * Contains game state for current running session.
 */
public class GameState {
    public final ObjectCollection<Spell> spells = new ObjectCollection<>();
    public final ObjectCollection<CreatureBase> players = new ObjectCollection<>();

    private CreatureBase currentPlayer;

    public final GlobalMap globalMap = new GlobalMap(
        "Global map",
        25,
        25,
        2,
        new GraphicalMapIcon("emptiness.png")
    );

    public GameState() {
        initBuiltInSpells();
    }

    private void initBuiltInSpells() {
        spells.add(new Spell("Fatigue", (target) -> {
            if (target instanceof Creature) {
                Creature c = (Creature) target;
                for (int i = 0; i < 5; i++) {
                    c.changeEnergy(-10);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }, 15, 10, 5));
        spells.add(new Spell("Snare", (target) -> {
            if (target instanceof Creature) {
                Creature c = (Creature) target;
                c.changeSpeed(-1);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignored) {
                }
                c.changeSpeed(1);
            }
        }, 15, 10, 5));
    }

    public CreatureBase getCurrentPlayer() {
        return currentPlayer;
    }

    public ITemplate removePlayer() {
        gameState.players.remove(currentPlayer.getKeyName());
        unselectPlayer();
        return I18n.of("OK");
    }

    public ITemplate unselectPlayer() {
        currentPlayer = null;
        return I18n.of("OK");
    }

    public ITemplate selectPlayer(String name) {
        var target = players.get(name);
        if (target == null) {
            return I18n.of("player.notExists");
        }
        currentPlayer = target;
        return I18n.of("OK");
    }

    public ITemplate addPlayer(String type, String name) {
        if (players.get(name) != null) {
            return I18n.of("player.nameReserved");
        }

        Point3D pt;
        do {
            pt = Random.getPoint(globalMap);
        } while (globalMap.get(pt) != null
                 && globalMap.get(pt).getMapWeight() == MapWeight.OBSTACLE);
        pt = pt.withZ(1);

        CreatureBase newPlayer;
        switch (type) {
        case "m":
        case "mage":
            newPlayer = new Mage(
                name,
                globalMap,
                new GraphicalMapIcon("tree.png"),
                pt
            );
            break;
        case "w":
        case "warrior":
            newPlayer = new Warrior(
                name,
                globalMap,
                new GraphicalMapIcon("player.png"),
                pt
            );
            break;
        default:
            return I18n.of("player.invalidType");
        }
        players.add(newPlayer);
        currentPlayer = newPlayer;
        return I18n.of("OK");
    }

    public String playersAsTable() {
        return players.values()
                      .stream()
                      .map(CreatureBase::getName)
                      .map(ITemplate::getLocalized)
                      .collect(Collectors.joining("\n"));
    }
}
