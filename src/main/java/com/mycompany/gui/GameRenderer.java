
package com.mycompany.gui;

import com.mycompany.util.Maze;
import com.mycompany.entities.Pacman;
import com.mycompany.entities.Ghost;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
/**
 * Handles all graphical rendering for the Pac-Man game.
 * Responsible for drawing the maze, entities, HUD (score and lives), and the pause overlay.
 */
public class GameRenderer {
/**
     * Renders the complete game state onto the screen.
     * Draws the maze tiles, all ghosts, Pacman (with direction and mouth animation),
     * HUD elements, and the semi-transparent pause overlay when the game is paused.
     *
     * @param g the Graphics context used for drawing
     * @param maze the maze grid containing walls, doors, and dots
     * @param pacman the player entity
     * @param blinky the red ghost
     * @param pinky the pink ghost
     * @param inky the cyan ghost
     * @param clyde the orange ghost
     * @param score the current player score
     * @param lives the number of remaining lives
     * @param mouthOpen true if Pacman's mouth is rendered open, false if closed
     * @param pacmanVisible true if Pacman is visible, false during blinking animation
     * @param paused true if the pause overlay should be displayed, false otherwise
     */
    public void draw(Graphics g, Maze maze, Pacman pacman,
            Ghost blinky, Ghost pinky, Ghost inky, Ghost clyde,
            int score, int lives, boolean mouthOpen,
            boolean pacmanVisible, boolean paused) {

        int tileSize = 30;

        // Draw maze
        for (int y = 0; y < maze.getRows(); y++) {
            for (int x = 0; x < maze.getCols(); x++) {
                int px = x * tileSize;
                int py = y * tileSize;

                if (maze.isDoor(x, y)) {
                    g.setColor(Color.PINK);
                    g.fillRect(px, py + 8, tileSize, 14);
                } else if (maze.isWall(x, y)) {
                    g.setColor(Color.BLUE);
                    g.fillRect(px, py, tileSize, tileSize);
                } else if (maze.hasDot(x, y)) {
                    g.setColor(Color.WHITE);
                    g.fillOval(px + 12, py + 12, 6, 6);
                }
            }
        }

        // Draw ghosts
        drawGhost(g, blinky, tileSize);
        drawGhost(g, pinky, tileSize);
        drawGhost(g, inky, tileSize);
        drawGhost(g, clyde, tileSize);

        // Draw Pac-Man
        if (pacmanVisible) {
            g.setColor(Color.YELLOW);

            int px = pacman.getX() * tileSize + 3;
            int py = pacman.getY() * tileSize + 3;
            int angle = 0;

            if (pacman.getDx() == -1) {
                angle = 180;
            } else if (pacman.getDy() == -1) {
                angle = 90;
            } else if (pacman.getDy() == 1) {
                angle = 270;
            }

            if (mouthOpen) {
                g.fillArc(px, py, 24, 24, angle + 35, 290);
            } else {
                g.fillOval(px, py, 24, 24);
            }
        }

        // Draw score and lives
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 445);
        g.drawString("Lives: " + lives, 150, 445);

        // Draw PAUSED overlay
        if (paused) {
            int panelWidth = maze.getCols() * tileSize;
            int panelHeight = 450;

            // Semi-transparent dark background
            g.setColor(new Color(0, 0, 0, 175));
            g.fillRect(0, 0, panelWidth, panelHeight);

            // Title "PAUSED"
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm = g.getFontMetrics();
            String pauseText = "PAUSED";
            int titleX = (panelWidth - fm.stringWidth(pauseText)) / 2;
            int titleY = panelHeight / 2 - 10;
            g.drawString(pauseText, titleX, titleY);

            // Hint
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            FontMetrics subFm = g.getFontMetrics();
            String subText = "Press 'P' or 'SPACE' to Resume";
            int subX = (panelWidth - subFm.stringWidth(subText)) / 2;
            g.drawString(subText, subX, titleY + 35);
        }
    }
/**
     * Helper method to render an individual ghost entity.
     * Draws the dome head, rectangular body, wavy polygon skirt, and eyes.
     *
     * @param g the Graphics context used for drawing
     * @param ghost the ghost entity to render
     * @param tileSize the size of a grid tile in pixels
     */
    private void drawGhost(Graphics g, Ghost ghost, int tileSize) {
        int x = ghost.getX() * tileSize;
        int y = ghost.getY() * tileSize;

        g.setColor(ghost.getColor());
        g.fillArc(x + 3, y + 2, 24, 25, 0, 180);
        g.fillRect(x + 3, y + 14, 24, 10);

        int[] xPoints = {x + 3, x + 9, x + 15, x + 21, x + 27};
        int[] yPoints = {y + 23, y + 27, y + 23, y + 27, y + 23};
        g.fillPolygon(xPoints, yPoints, xPoints.length);

        g.setColor(Color.WHITE);
        g.fillOval(x + 7, y + 9, 8, 10);
        g.fillOval(x + 17, y + 9, 8, 10);

        g.setColor(new Color(30, 70, 220));
        g.fillOval(x + 10, y + 12, 4, 5);
        g.fillOval(x + 20, y + 12, 4, 5);
    }
}