/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gui;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author amoha
 */
public class GameFrame extends JFrame{
    public GameFrame(){
        setTitle("PACMAN");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);  
        add(createStartScreen());
}
    public JPanel createStartScreen(){
        JPanel panel = new JPanel();
        panel.setBackground(new Color(8,8,25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("PAC-MAN");
        title.setFont(new Font("Monospaced", Font.BOLD, 64));
        title.setForeground(Color.YELLOW);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        JButton startbutton = new JButton("Start Game");
        startbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(startbutton);
        startbutton.addActionListener(e -> {
            getContentPane().removeAll();
            add(new GamePanel());
            revalidate();
            repaint();
        });
        return panel;
    }
            
}
