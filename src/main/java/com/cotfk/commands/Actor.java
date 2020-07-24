package com.cotfk.commands;

import com.cotfk.GameState;
import com.cotfk.creatures.Human;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.time.Timeline;

public class Actor {
    private static Timeline lastUsedTimeline;
    private static Human player;

    public static Human get() {
        return player;
    }

    public static ITemplate pick(String name) {
        if (lastUsedTimeline != Timeline.main
            && player == null
            || player.getTimeline() == null) {
            lastUsedTimeline = Timeline.main;
        } else {
            lastUsedTimeline = player.getTimeline();
        }
        return pick(name, lastUsedTimeline);
    }

    public static ITemplate pick(String name, Timeline tl) {
        var target = tl.getGameState().players.get(name);
        if (target == null) {
            return I18n.of("player.notExists");
        }
        Actor.player = (Human) target;
        return I18n.okMessage;
    }

    public static ITemplate free() {
        player = null;
        return I18n.okMessage;
    }

    public static ITemplate kill() {
        getGameState().players.remove(player);
        free();
        return I18n.okMessage;
    }

    public static GameState getGameState() {
        if (player == null) {
            return (GameState) Timeline.main.getGameState();
        }
        return (GameState) player.getTimeline().getGameState();
    }
}
