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
    private BufferedImage bufferedMapImage;
    private Graphics bufferedMapGraphics;

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

        if (bufferedMapImage == null) {
            // Create the buffer
            bufferedMapImage = (BufferedImage) createImage(getWidth(), getHeight());
            bufferedMapGraphics = bufferedMapImage.getGraphics();
        }
        Map map;
        int radius;
        Point3D centerPoint;
        // computing visible bounds
        if (player == null) {
            map = Timeline.main.getGameState().getGlobalMap();
            radius = map.xSize / 2;
            int center = radius % 2 == 0 ? radius + 1 : radius;
            centerPoint = new Point3D(center, center, map.zSize - 1);
        } else {
            map = player.getMap();
            radius = player.getFov();
            centerPoint = player.getPt0().withZ(map.zSize - 1);
        }
        // saving object ids
        MapObject[][][] objects = map.getRaw3DArea(centerPoint, radius);
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
            drawMapToBuffer(bufferedMapGraphics, objects, centerPoint, radius);
        }
        lastMapIds = mapIds;
        g.drawImage(bufferedMapImage, 0, 0, null);

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
        g.setFont(g.getFont().deriveFont(14f));
        Dimension dim = UITools.getTextSize(g, summary);
        g.setColor(new Color(255, 255, 255, 80));
        g.fillRect(0, 0, dim.width, dim.height);
        g.setColor(Color.BLACK);
        UITools.drawString(g, summary, new Point(10, 0));
    }

    private void drawMapToBuffer(Graphics g, MapObject[][][] objects, Point3D centerPoint, int radius) {
        var largeObjs = new HashSet<LargeMapObjectContainer>();
        var relZero = centerPoint.minus(new Point3D(radius, radius, 0));
        int tileSide = getWidth() / (radius * 2 + 1);
        for (MapObject[][] objectsLayer : objects) {
            for (int areaY = 0; areaY < objectsLayer.length; areaY++) {
                for (int areaX = 0; areaX < objectsLayer[areaY].length; areaX++) {
                    MapObject mapObj = objectsLayer[areaY][areaX];
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
                            // skip large object drawing because
                            // it is already drawn from `pt0`.
                            continue;
                        }
                        var w = mapObj.getWidth();
                        var h = mapObj.getHeight();
                        int areaX0 = areaX;
                        int areaY0 = areaY;
                        if (w > 1 || h > 1) {
                            // save large objects to the container
                            largeObjs.add(new LargeMapObjectContainer(mapObj));
                            var objRelPt0 = mapObj.getPt0().minus(relZero);
                            areaX0 = objRelPt0.x;
                            areaY0 = objRelPt0.y;
                        }
                        g.drawImage(
                            ImageTools.resizeTile(
                                (BufferedImage) mapObj.getMapIcon().get(),
                                tileSide * h,
                                tileSide * w
                            ),
                            tileSide * areaX0,
                            tileSide * areaY0,
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
            unfilledCellsCount = obj.getPoints().length - 1;
        }
    }
}
