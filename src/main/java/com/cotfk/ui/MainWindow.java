package com.cotfk.ui;

import com.cotfk.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Crown of the Fallen King");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        add(new MainPanel(this));
        setLocation(25, 25);
        setMinimumSize(new Dimension(650, 650));
        setVisible(true);

        addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                var cp = Main.gameState.getCurrentPlayer();
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
    }
}
