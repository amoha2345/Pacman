package com.mycompany.gui;

import javax.swing.SwingUtilities;

/**
 * Entry point for the Pac-Man application.
 * Launches the graphical user interface on the Swing Event Dispatch Thread (EDT).
 */
public class Main {

    /**
     * Main method that initializes and displays the game window.
     *
     * @param args command-line arguments (not utilized)
     */
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}