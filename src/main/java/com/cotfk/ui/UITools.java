package com.cotfk.ui;

import java.awt.*;

public class UITools {
    public static Dimension getTextSize(Graphics g, String text) {
        String longest = "";
        var lines = text.split("\n");
        for (String line : lines) {
            if (line.length() > longest.length()) {
                longest = line;
            }
        }
        return new Dimension(
            (int) (g.getFontMetrics().stringWidth(longest) * 1.1) + 10,
            g.getFontMetrics().getHeight() * lines.length + 10
        );
    }

    public static void drawString(Graphics g, String text, int x, int y) {
        int lineHeight = g.getFontMetrics().getHeight();
        for (String line : text.split("\n")) {
            g.drawString(line, x, y += lineHeight);
        }
    }
}
