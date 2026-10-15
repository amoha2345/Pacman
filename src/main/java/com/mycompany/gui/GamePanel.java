
package com.mycompany.gui;

import com.mycompany.util.Maze;
import com.mycompany.entities.Pacman;
import com.mycompany.entities.Ghost;
import com.mycompany.entities.BlinkyAI;
import com.mycompany.entities.PinkyAI;
import com.mycompany.entities.InkyAI;
import com.mycompany.entities.ClydeAI;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private Maze maze;
    private Pacman pacman;
    private int score = 0;
    private int lives = 3;

    private Ghost blinky;
    private Ghost pinky;
    private Ghost inky;
    private Ghost clyde;

    private BlinkyAI blinkyAI;
    private PinkyAI pinkyAI;
    private InkyAI inkyAI;
    private ClydeAI clydeAI;

    private Timer pacmanTimer;
    private Timer ghostTimer;
    private Timer pinkyTimer;
    private Timer inkyTimer;
    private Timer clydeTimer;

    private Timer pinkyReleaseTimer;
    private Timer inkyReleaseTimer;
    private Timer clydeReleaseTimer;

    private boolean pinkyReleased = false;
    private boolean inkyReleased = false;
    private boolean clydeReleased = false;

    private boolean gameOver = false;

    public GamePanel() {

        maze = new Maze();
        pacman = new Pacman(1, 1);

        // Blinky starts outside the ghost house.
        // The other three ghosts start side by side inside it.
        blinky = new Ghost(9, 4, Color.RED);
        pinky = new Ghost(8, 6, Color.PINK);
        inky = new Ghost(9, 6, Color.CYAN);
        clyde = new Ghost(10, 6, Color.ORANGE);

        blinkyAI = new BlinkyAI();
        pinkyAI = new PinkyAI();
        inkyAI = new InkyAI();
        clydeAI = new ClydeAI();

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(570, 450));
        setFocusable(true);

        setupKeyBindings();

        pacmanTimer = new Timer(150, e -> movePacman());
        ghostTimer = new Timer(300, e -> moveBlinky());
        pinkyTimer = new Timer(400, e -> movePinky());
        inkyTimer = new Timer(350, e -> moveInky());
        clydeTimer = new Timer(400, e -> moveClyde());

        ghostTimer.start();
        pinkyTimer.start();
        inkyTimer.start();
        clydeTimer.start();

        startPinkyReleaseTimer();
        startInkyReleaseTimer();
        startClydeReleaseTimer();

        pacmanTimer.start();
    }

    private void startPinkyReleaseTimer() {
        pinkyReleaseTimer = new Timer(10000, e -> {
            pinkyReleased = true;
            pinkyReleaseTimer.stop();
        });
        pinkyReleaseTimer.setRepeats(false);
        pinkyReleaseTimer.start();
    }

    private void startInkyReleaseTimer() {
        inkyReleaseTimer = new Timer(20000, e -> {
            inkyReleased = true;
            inkyReleaseTimer.stop();
        });
        inkyReleaseTimer.setRepeats(false);
        inkyReleaseTimer.start();
    }

    private void startClydeReleaseTimer() {
        clydeReleaseTimer = new Timer(30000, e -> {
            clydeReleased = true;
            clydeReleaseTimer.stop();
        });
        clydeReleaseTimer.setRepeats(false);
        clydeReleaseTimer.start();
    }

    private void setupKeyBindings() {
        bindMove("UP", "moveUp", 0, -1);
        bindMove("DOWN", "moveDown", 0, 1);
        bindMove("LEFT", "moveLeft", -1, 0);
        bindMove("RIGHT", "moveRight", 1, 0);
    }

    private void bindMove(String key, String name, int dx, int dy) {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(key), name);

        getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (!gameOver) {
                    pacman.setDirection(dx, dy);
                }
            }
        });
    }

private void movePacman() {
    if (gameOver) {
        return;
    }

    // Move Pac-Man and award points if he collects a dot.
    if (pacman.move(maze)) {
        score += 10;
    }

    checkCollision();

    if (gameOver) {
        return;
    }

    // Check whether all dots have been collected.
    if (!maze.hasDotsLeft()) {
        gameOver = true;
        stopTimers();
        JOptionPane.showMessageDialog(this, "YOU WIN!");
        closeGame();
        return;
    }

    repaint();
}


    private void moveBlinky() {
        if (gameOver) {
            return;
        }

        int[] move = blinkyAI.getNextMove(blinky, pacman, maze);
        blinky.moveTo(
                blinky.getX() + move[0],
                blinky.getY() + move[1]
        );

        checkCollision();
        repaint();
    }

    private void movePinky() {
        if (gameOver || !pinkyReleased) {
            return;
        }

        int[] move = pinkyAI.getNextMove(pinky, pacman, maze);
        pinky.moveTo(
                pinky.getX() + move[0],
                pinky.getY() + move[1]
        );

        checkCollision();
        repaint();
    }

    private void moveInky() {
        if (gameOver || !inkyReleased) {
            return;
        }

        int[] move = inkyAI.getNextMove(inky, pacman, maze);
        inky.moveTo(
                inky.getX() + move[0],
                inky.getY() + move[1]
        );

        checkCollision();
        repaint();
    }

    private void moveClyde() {
        if (gameOver || !clydeReleased) {
            return;
        }

        int[] move = clydeAI.getNextMove(clyde, pacman, maze);
        clyde.moveTo(
                clyde.getX() + move[0],
                clyde.getY() + move[1]
        );

        checkCollision();
        repaint();
    }

    private void checkCollision() {
        if (gameOver) {
            return;
        }

        boolean blinkyHit =
                blinky.getX() == pacman.getX()
                && blinky.getY() == pacman.getY();

        boolean pinkyHit = pinkyReleased
                && pinky.getX() == pacman.getX()
                && pinky.getY() == pacman.getY();

        boolean inkyHit = inkyReleased
                && inky.getX() == pacman.getX()
                && inky.getY() == pacman.getY();

        boolean clydeHit = clydeReleased
                && clyde.getX() == pacman.getX()
                && clyde.getY() == pacman.getY();

        if (!blinkyHit && !pinkyHit && !inkyHit && !clydeHit) {
            return;
        }

        stopTimers();
        lives--;

        if (lives <= 0) {
            gameOver = true;
            repaint();
            JOptionPane.showMessageDialog(this, "GAME OVER!");
            closeGame();
            return;
        }

        // Reset positions after Pac-Man loses a life.
        pacman = new Pacman(1, 1);

        blinky.moveTo(9, 4);
        pinky.moveTo(8, 6);
        inky.moveTo(9, 6);
        clyde.moveTo(10, 6);

        pinkyReleased = false;
        inkyReleased = false;
        clydeReleased = false;

        repaint();

        JOptionPane.showMessageDialog(
                this, "You were caught! Lives left: " + lives
        );

        if (!gameOver) {
            pacmanTimer.start();
            ghostTimer.start();
            pinkyTimer.start();
            inkyTimer.start();
            clydeTimer.start();

            startPinkyReleaseTimer();
            startInkyReleaseTimer();
            startClydeReleaseTimer();
        }
    }

    private void stopTimers() {
        if (pacmanTimer != null) pacmanTimer.stop();
        if (ghostTimer != null) ghostTimer.stop();
        if (pinkyTimer != null) pinkyTimer.stop();
        if (inkyTimer != null) inkyTimer.stop();
        if (clydeTimer != null) clydeTimer.stop();

        if (pinkyReleaseTimer != null) pinkyReleaseTimer.stop();
        if (inkyReleaseTimer != null) inkyReleaseTimer.stop();
        if (clydeReleaseTimer != null) clydeReleaseTimer.stop();
    }

    private void closeGame() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int tileSize = 30;

        for (int y = 0; y < maze.getRows(); y++) {
            for (int x = 0; x < maze.getCols(); x++) {
                int px = x * tileSize;
                int py = y * tileSize;

                if (maze.isDoor(x, y)) {
                    // Ghost-only door.
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

        // Pac-Man
        g.setColor(Color.YELLOW);
        g.fillOval(
                pacman.getX() * tileSize + 3,
                pacman.getY() * tileSize + 3,
                24, 24
        );

        // Ghosts
        drawGhost(g, blinky, tileSize);
        drawGhost(g, pinky, tileSize);
        drawGhost(g, inky, tileSize);
        drawGhost(g, clyde, tileSize);

        // Score and lives
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 445);
        g.drawString("Lives: " + lives, 150, 445);
    }

    private void drawGhost(Graphics g, Ghost ghost, int tileSize) {
        g.setColor(ghost.getColor());
        g.fillOval(
                ghost.getX() * tileSize + 3,
                ghost.getY() * tileSize + 3,
                24, 24
        );
    }
}
