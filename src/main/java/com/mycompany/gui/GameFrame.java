
package com.mycompany.gui;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("PACMAN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(createStartScreen());

        pack();
        setLocationRelativeTo(null);
    }

    public JPanel createStartScreen() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(8, 8, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("PAC-MAN");
        title.setFont(new Font("Monospaced", Font.BOLD, 64));
        title.setForeground(Color.YELLOW);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(100));
        panel.add(title);
        panel.add(Box.createVerticalStrut(30));

        JButton startbutton = new JButton("Start Game");
        startbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(startbutton);

        panel.add(Box.createVerticalStrut(100));

        startbutton.addActionListener(e -> {
            getContentPane().removeAll();

            GamePanel gamePanel = new GamePanel();
            add(gamePanel);

            pack();
            setLocationRelativeTo(null);

            revalidate();
            repaint();

            gamePanel.requestFocusInWindow();
        });

        return panel;
    }
}
