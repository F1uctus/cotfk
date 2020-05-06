package com.cotfk.ui;

import com.crown.maps.MapIcon;
import com.crown.maps.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import static com.cotfk.Main.gameState;
import static java.awt.RenderingHints.*;

public class MainPanel extends JPanel {
    private Point initialClick;

    MainPanel(JFrame wnd) {
        super();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = wnd.getLocation().x;
                int thisY = wnd.getLocation().y;
                int deltaX = e.getX() - initialClick.x;
                int deltaY = e.getY() - initialClick.y;
                wnd.setLocation(thisX + deltaX, thisY + deltaY);
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(
            KEY_ANTIALIASING,
            VALUE_ANTIALIAS_ON
        );
        ((Graphics2D) g).setRenderingHint(
            KEY_RENDERING,
            VALUE_RENDER_QUALITY
        );
        setBackground(Color.BLACK);

        var map = gameState.globalMap;
        int radius = map.xSize / 2;
        Point3D p0 = new Point3D(map.xSize / 2, map.ySize / 2, map.zSize - 1);
        var cp = gameState.getCurrentPlayer();
        if (cp != null) {
            radius = cp.getFov();
            p0 = cp.getPt();
        }

        MapIcon<?>[][][] icons = map.get3DArea(p0, radius);
        int tileWidth = getWidth() / icons[0][0].length;
        for (MapIcon<?>[][] iconDepth : icons) {
            for (int y = 0; y < iconDepth.length; y++) {
                for (int x = 0; x < iconDepth[y].length; x++) {
                    g.drawImage(
                        ImageTools.resize(
                            (BufferedImage) iconDepth[y][x].get(),
                            tileWidth,
                            tileWidth
                        ),
                        tileWidth * x,
                        tileWidth * y,
                        null
                    );
                }
            }
        }

        if (cp != null) {
            var text = cp.getStats().getLocalized(cp.lang);
            g.setFont(g.getFont().deriveFont(14f));
            Dimension dim = UITools.getTextSize(g, text);
            g.setColor(new Color(255, 255, 255, 100));
            g.fillRect(0, 0, dim.width, dim.height);
            g.setColor(Color.BLACK);
            UITools.drawString(g, text, 10, 0);
        }
    }
}
