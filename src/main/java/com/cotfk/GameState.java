package com.cotfk;

import com.cotfk.creatures.Mage;
import com.cotfk.creatures.Warrior;
import com.cotfk.maps.MapLevel;
import com.crown.BaseGameState;
import com.crown.common.utils.Random;
import com.crown.creatures.Organism;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.maps.Map;
import com.crown.maps.Point3D;

import java.util.stream.Collectors;

/**
 * Contains game state for current running session.
 */
public class GameState extends BaseGameState {
    public GameState(Map globalMap) {
        super(globalMap);
    }

    public ITemplate addPlayer(String type, String name) {
        if (players.get(name) != null) {
            return I18n.of("player.nameReserved");
        }

        Point3D pt = Random.getFreePoint(getGlobalMap(), new Point3D(-1, -1, MapLevel.ground + 1));

        Organism newPlayer;
        if ("mage".startsWith(type)) {
            newPlayer = new Mage(
                name,
                getGlobalMap(),
                pt
            );
        } else if ("warrior".startsWith(type)) {
            newPlayer = new Warrior(
                name,
                getGlobalMap(),
                pt
            );
        } else {
            return I18n.of("player.invalidType");
        }
        players.add(newPlayer);
        return I18n.okMessage;
    }

    public String playersAsTable() {
        return players.values()
            .stream()
            .map(Organism::getName)
            .map(ITemplate::getLocalized)
            .collect(Collectors.joining("\n"));
    }
}
