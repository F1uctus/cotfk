package com.cotfk.ui;

import com.cotfk.commands.Actor;
import com.crown.maps.Map;
import com.crown.maps.*;
import com.crown.time.Timeline;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.awt.RenderingHints.*;

public class MapPanel extends JPanel {
    private final DateTimeFormatter timeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withLocale(Locale.UK)
            .withZone(ZoneOffset.UTC);

    MapPanel(JFrame parentWindow) {
        super();
        setPreferredSize(
            new Dimension(
                parentWindow.getWidth(),
                parentWindow.getWidth()
            )
        );
    }

    // Double buffering
    private BufferedImage dbImage;
    private Graphics dbg;

    private UUID[][] lastMapIds;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (Timeline.main == null || !isValid()) {
            return;
        }

        ((Graphics2D) g).setRenderingHint(
            KEY_ANTIALIASING,
            VALUE_ANTIALIAS_ON
        );
        ((Graphics2D) g).setRenderingHint(
            KEY_RENDERING,
            VALUE_RENDER_QUALITY
        );

        var player = Actor.get();

        if (dbImage == null) {
            // Create the buffer
            dbImage = (BufferedImage) createImage(getWidth(), getHeight());
            dbg = dbImage.getGraphics();
        }
        Map map;
        int radius;
        Point3D centerPoint;
        if (player == null) {
            map = Timeline.main.getGameState().getGlobalMap();
            radius = map.xSize / 2;
            centerPoint = new Point3D(map.xSize / 2, map.ySize / 2, map.zSize - 1);
        } else {
            map = player.getMap();
            radius = player.getFov();
            centerPoint = player.getPt0().withZ(map.zSize - 1);
        }
        MapObject[][][] objects = map.getRaw3DArea(centerPoint, radius);
        int maxZ = objects.length;
        int maxY = objects[0].length;
        int maxX = objects[0][0].length;
        var mapIds = new UUID[maxY][maxX];
        for (MapObject[][] object : objects) {
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < maxX; x++) {
                    var mapObj = object[y][x];
                    if (mapObj != null) {
                        mapIds[y][x] = mapObj.getId();
                    }
                }
            }
        }
        if (lastMapIds == null
            || !(maxY == lastMapIds.length && maxX == lastMapIds[0].length)
            || !Arrays.deepEquals(mapIds, lastMapIds)) {
            drawMapToBuffer(dbg, objects, centerPoint, radius);
        }
        lastMapIds = mapIds;
        g.drawImage(dbImage, 0, 0, null);

        var nowTime = timeFormatter.format(Timeline.getClock().now());
        var summary = nowTime + "\n";
        if (player != null) {
            var tl = player.getTimeline();
            if (tl != Timeline.main && tl != null) {
                summary += " (- " + player.getTimeline().getOffsetToMain() + ")";
            }
            summary += String.join(
                "\n",
                player.getStats().getLocalized(player.lang)
            );
        }
        g.setFont(g.getFont().

            deriveFont(14f));
        Dimension dim = UITools.getTextSize(g, summary);
        g.setColor(new

            Color(255, 255, 255, 80));
        g.fillRect(0, 0, dim.width, dim.height);
        g.setColor(Color.BLACK);
        UITools.drawString(g, summary, 10, 0);
    }

    private void drawMapToBuffer(Graphics g, MapObject[][][] objects, Point3D centerPoint, int radius) {
        var largeObjs = new HashSet<LargeMapObjectContainer>();
        var relZero = centerPoint.minus(new Point3D(radius, radius, 0));
        int tileSide = getWidth() / (radius * 2 + 1);
        for (MapObject[][] objectsLayer : objects) {
            for (int relY = 0; relY < objectsLayer.length; relY++) {
                for (int relX = 0; relX < objectsLayer[relY].length; relX++) {
                    MapObject mapObj = objectsLayer[relY][relX];
                    if (mapObj != null && mapObj.getMap() != null) {
                        var largeObj = largeObjs
                            .stream()
                            .filter(c -> c.obj == mapObj)
                            .findFirst()
                            .orElse(null);
                        if (largeObj != null
                            && largeObj.unfilledCellsCount > 0) {
                            largeObj.unfilledCellsCount--;
                            if (largeObj.unfilledCellsCount == 0) {
                                // if all parts of large object have been drawn
                                largeObjs.remove(largeObj);
                            }
                            continue;
                        }
                        var w = mapObj.getWidth();
                        var h = mapObj.getHeight();
                        int relX0 = relX;
                        int relY0 = relY;
                        if (w > 1 || h > 1) {
                            // save large objects to the container
                            largeObjs.add(new LargeMapObjectContainer(mapObj));
                            var objRelPt0 = mapObj.getPt0().minus(relZero);
                            relX0 = objRelPt0.x;
                            relY0 = objRelPt0.y;
                        }
                        g.drawImage(
                            ImageTools.resizeTile(
                                (BufferedImage) mapObj.getMapIcon().get(),
                                tileSide * h,
                                tileSide * w
                            ),
                            tileSide * relX0,
                            tileSide * relY0,
                            null
                        );
                    }
                }
            }
        }
    }

    static class LargeMapObjectContainer {
        final MapObject obj;
        int unfilledCellsCount;

        LargeMapObjectContainer(MapObject obj) {
            this.obj = obj;
            unfilledCellsCount = obj.getParticles().length - 1;
        }
    }
}
