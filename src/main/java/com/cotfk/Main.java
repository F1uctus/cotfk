package com.cotfk;

import com.cotfk.commands.Actor;
import com.cotfk.commands.CommandParser;
import com.cotfk.maps.GlobalMap;
import com.cotfk.maps.MapLevel;
import com.cotfk.ui.MainWindow;
import com.crown.i18n.I18n;
import com.crown.time.Timeline;
import com.crown.time.VirtualClock;

import java.awt.*;
import java.util.*;

public class Main {
    public static final HashMap<String, ResourceBundle> bundles = new HashMap<>();
    public static final Scanner s = new Scanner(System.in);

    private static MainWindow mainWindow;
    private static boolean lastCmdSuccess = true;

    public static final int MAP_SIZE = 51;
    public static final int TILE_SIZE = 50;

    public static void main(String[] args) {
        bundles.put("ru", ResourceBundle.getBundle("gameMessages", new Locale("ru_RU")));
        bundles.put("en", ResourceBundle.getBundle("gameMessages", new Locale("en_US")));
        I18n.init(bundles);

        EventQueue.invokeLater(() -> {
            mainWindow = new MainWindow();
            Timeline.setMain(
                new VirtualClock(
                    24,
                    mainWindow::repaint
                ).startAtRnd(),
                new GameState(
                    new GlobalMap(
                        "Global map",
                        MAP_SIZE,
                        MAP_SIZE,
                        MapLevel.height
                    ).flatTerrain().generateObjects()
                )
            );
        });

        System.out.println(I18n.of("welcome"));
        System.out.println(I18n.of("welcome.help"));

        new Thread(() -> {
            while (true) {
                printInputPrefix();
                String input = s.nextLine();
                if (input.equals("")) {
                    continue;
                }
                if (input.equals("exit") || input.equals("quit")) {
                    break;
                }
                invoke(input);
            }
        }).start();
    }

    public static void invoke(String command) {
        invoke(command, false);
    }

    public static void invoke(String command, boolean silent) {
        var result = CommandParser.parse(command);

        if (!silent) {
            var lang = "en";
            var cp = Actor.get();
            if (cp != null) {
                lang = cp.lang;
            }
            lastCmdSuccess = result.equals(I18n.okMessage);
            if (!lastCmdSuccess) {
                System.out.println(result.getLocalized(lang));
            }
        }
        mainWindow.repaint();
    }

    public static void printInputPrefix() {
        String inputPrefix = lastCmdSuccess ? "✓ " : "";
        var cp = Actor.get();
        if (cp != null) {
            inputPrefix += cp.getName().getLocalized();
        } else {
            inputPrefix += "#cotfk";
        }
        System.out.print(inputPrefix + "> ");
    }
}