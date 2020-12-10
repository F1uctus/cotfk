package com.cotfk.ui;

import com.cotfk.Main;
import com.cotfk.commands.Actor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.cotfk.Main.MAP_SIZE;
import static com.cotfk.Main.printInputPrefix;

public class MainWindow extends JFrame {
    private Point initialClick;

    public MainWindow() {
        super("Crown of the Fallen King");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocation(25, 25);
        setSize(new Dimension(10 * MAP_SIZE, 10 * MAP_SIZE));

        var p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new MapPanel(this));
        var cl = new TextField();
        cl.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(cl.getText());
                Main.invoke(cl.getText());
                printInputPrefix();
                MainWindow.this.requestFocusInWindow();
            }
        });
        p.add(cl);
        setContentPane(p);
        setVisible(true);
        requestFocusInWindow();

        addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (Actor.get() != null) {
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
