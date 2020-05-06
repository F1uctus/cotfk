package com.cotfk;

import com.cotfk.commands.CommandParser;
import com.cotfk.ui.MainWindow;
import com.crown.i18n.I18n;

import java.util.*;

public class Main {
    public static final HashMap<String, ResourceBundle> bundles = new HashMap<>();
    public static final GameState gameState = new GameState();
    public static final Scanner s = new Scanner(System.in);

    private static final CommandParser parser = new CommandParser();
    private static final MainWindow mainWindow = new MainWindow();
    private static boolean lastCmdSuccess = true;

    public static void main(String[] args) {
        bundles.put("ru", ResourceBundle.getBundle("gameMessages", new Locale("ru_RU")));
        bundles.put("en", ResourceBundle.getBundle("gameMessages", new Locale("en_US")));
        I18n.init(bundles);

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
        var result = parser.parse(command);

        if (!silent) {
            var lang = "en";
            if (gameState.getCurrentPlayer() != null) {
                lang = gameState.getCurrentPlayer().lang;
            }
            var localizedResult = result.getLocalized(lang);
            lastCmdSuccess = localizedResult.equals("OK");
            if (!lastCmdSuccess) {
                System.out.println(localizedResult);
            }
        }
        mainWindow.repaint();
    }

    public static void printInputPrefix() {
        String inputPrefix = lastCmdSuccess ? "✓ " : "";
        if (gameState.getCurrentPlayer() != null) {
            inputPrefix += gameState.getCurrentPlayer().getName().getLocalized();
        } else {
            inputPrefix += "#cotfk";
        }
        System.out.print(inputPrefix + "> ");
    }
}
