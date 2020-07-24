package com.cotfk.ui;

import com.cotfk.Main;
import com.cotfk.commands.Actor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.cotfk.Main.MAP_SIZE;

public class MainWindow extends JFrame {
    private Point initialClick;

    public MainWindow() {
        super("Crown of the Fallen King");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocation(25, 25);
        setMinimumSize(new Dimension(MAP_SIZE * 5, MAP_SIZE * 5));
        setContentPane(new MapPanel(this));
        setVisible(true);

        addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                var cp = Actor.get();
                if (cp != null) {
                    if (key == KeyEvent.VK_LEFT) {
                        Main.invoke("move -1 0", true);
                    } else if (key == KeyEvent.VK_RIGHT) {
                        Main.invoke("move 1 0", true);
                    } else if (key == KeyEvent.VK_UP) {
                        Main.invoke("move 0 -1", true);
                    } else if (key == KeyEvent.VK_DOWN) {
                        Main.invoke("move 0 1", true);
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int deltaX = e.getX() - initialClick.x;
                int deltaY = e.getY() - initialClick.y;
                setLocation(thisX + deltaX, thisY + deltaY);
            }
        });
    }
}
