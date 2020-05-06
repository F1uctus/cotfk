package com.cotfk.ui;

import com.crown.maps.MapObject;
import com.crown.maps.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.HashSet;

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
            p0 = cp.getPt0().withZ(map.zSize - 1);
        }

        var largeObjs = new HashSet<LargeMapObjectContainer>();
        MapObject[][][] icons = map.getRaw3DArea(p0, radius);
        var relZero = p0.minus(new Point3D(radius, radius, 0));
        int tileSide = getWidth() / icons[0][0].length;
        for (MapObject[][] iconsLayer : icons) {
            for (int relY = 0; relY < iconsLayer.length; relY++) {
                for (int relX = 0; relX < iconsLayer[relY].length; relX++) {
                    MapObject mapObj = iconsLayer[relY][relX];
                    if (mapObj != null) {
                        var largeObj = largeObjs
                            .stream()
                            .filter((c) -> c.obj == mapObj)
                            .findFirst()
                            .orElse(null);
                        if (largeObj != null
                            && largeObj.unfilledCellsCount > 0) {
                            largeObj.unfilledCellsCount--;
                            if (largeObj.unfilledCellsCount == 0) {
                                largeObjs.remove(largeObj);
                            }
                            continue;
                        }
                        var w = mapObj.getWidth();
                        var h = mapObj.getHeight();
                        int relX0 = relX;
                        int relY0 = relY;
                        if (w > 1 || h > 1) {
                            largeObjs.add(new LargeMapObjectContainer(mapObj));
                            var objRelPt0 = mapObj.getPt0().minus(relZero);
                            relX0 = objRelPt0.x;
                            relY0 = objRelPt0.y;
                        }
                        g.drawImage(
                            ImageTools.resize(
                                (BufferedImage) mapObj.getMapIcon().get(),
                                tileSide * h,
                                tileSide * w
                            ),
                            tileSide * relX0,
                            tileSide * relY0,
                            null
                        );
                        if (w > 1) {
                            relX += w - 1;
                        }
                    }
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

    static class LargeMapObjectContainer {
        MapObject obj;
        int unfilledCellsCount;

        LargeMapObjectContainer(MapObject obj) {
            this.obj = obj;
            unfilledCellsCount = obj.getParticles().length - 1;
        }
    }
}
