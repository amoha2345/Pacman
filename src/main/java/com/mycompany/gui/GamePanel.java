
package com.mycompany.gui;

import com.mycompany.util.Maze;
import com.mycompany.entities.Pacman;
import com.mycompany.entities.Ghost;
import com.mycompany.entities.BlinkyAI;
import com.mycompany.entities.PinkyAI;
import com.mycompany.entities.InkyAI;
import com.mycompany.entities.ClydeAI;
import com.mycompany.entities.GhostStrategy;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private Maze maze;
    private Pacman pacman;
    private int score = 0;
    private int lives = 3;

    private Ghost blinky, pinky, inky, clyde;

    private BlinkyAI blinkyAI;
    private PinkyAI pinkyAI;
    private InkyAI inkyAI;
    private ClydeAI clydeAI;

    private Timer pacmanTimer, ghostTimer, pinkyTimer, inkyTimer, clydeTimer;
    private Timer pinkyReleaseTimer, inkyReleaseTimer, clydeReleaseTimer;
    private Timer mouthTimer;

    private boolean pinkyReleased = false;
    private boolean inkyReleased = false;
    private boolean clydeReleased = false;

    private boolean pinkyExited = false;
    private boolean inkyExited = false;
    private boolean clydeExited = false;

    private boolean gameOver = false;
    private boolean mouthOpen = true;
    private boolean pacmanBlinking = false;
    private boolean pacmanVisible = true;

    // The tile outside the ghost house.
    private final int exitX = 9;
    private final int exitY = 9;

    public GamePanel() {
        maze = new Maze();
        pacman = new Pacman(1, 1);

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

        mouthTimer = new Timer(120, e -> {
            if (!pacmanBlinking && !gameOver) {
                mouthOpen = !mouthOpen;
                repaint();
            }
        });
        mouthTimer.start();

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
            repaint();
        });
        pinkyReleaseTimer.setRepeats(false);
        pinkyReleaseTimer.start();
    }

    private void startInkyReleaseTimer() {
        inkyReleaseTimer = new Timer(20000, e -> {
            inkyReleased = true;
            inkyReleaseTimer.stop();
            repaint();
        });
        inkyReleaseTimer.setRepeats(false);
        inkyReleaseTimer.start();
    }

    private void startClydeReleaseTimer() {
        clydeReleaseTimer = new Timer(30000, e -> {
            clydeReleased = true;
            clydeReleaseTimer.stop();
            repaint();
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
                if (!gameOver && !pacmanBlinking) {
                    pacman.setDirection(dx, dy);
                }
            }
        });
    }

    private void movePacman() {
        if (gameOver || pacmanBlinking) return;

        if (pacman.move(maze)) {
            score += 10;
        }

        checkCollision();

        if (gameOver || pacmanBlinking) return;

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
        if (gameOver || pacmanBlinking) return;
        moveUsingAI(blinky, blinkyAI);
    }

    private void movePinky() {
        if (gameOver || pacmanBlinking || !pinkyReleased) return;

        if (!pinkyExited) {
            pinkyExited = moveTowardExit(pinky);
            checkCollision();
            repaint();
            return;
        }

        moveUsingAI(pinky, pinkyAI);
    }

    private void moveInky() {
        if (gameOver || pacmanBlinking || !inkyReleased) return;

        if (!inkyExited) {
            inkyExited = moveTowardExit(inky);
            checkCollision();
            repaint();
            return;
        }

        moveUsingAI(inky, inkyAI);
    }

    private void moveClyde() {
        if (gameOver || pacmanBlinking || !clydeReleased) return;

        if (!clydeExited) {
            clydeExited = moveTowardExit(clyde);
            checkCollision();
            repaint();
            return;
        }

        moveUsingAI(clyde, clydeAI);
    }

    // Move to the centre column, then down through the door.
    private boolean moveTowardExit(Ghost ghost) {
        int x = ghost.getX();
        int y = ghost.getY();

        if (x == exitX && y == exitY) {
            return true;
        }

        int nextX = x;
        int nextY = y;

        if (x < exitX) {
            nextX++;
        } else if (x > exitX) {
            nextX--;
        } else if (y < exitY) {
            nextY++;
        }

        if (!maze.isWall(nextX, nextY) || maze.isDoor(nextX, nextY)) {
            ghost.moveTo(nextX, nextY);
        }

        return ghost.getX() == exitX && ghost.getY() == exitY;
    }

    private void moveUsingAI(Ghost ghost, GhostStrategy ai) {
        int[] move = ai.getNextMove(ghost, pacman, maze);

        if (move == null || move.length < 2) return;

        int nextX = ghost.getX() + move[0];
        int nextY = ghost.getY() + move[1];

        if (move[0] != 0 || move[1] != 0) {
            if (nextX >= 0 && nextX < maze.getCols()
                    && nextY >= 0 && nextY < maze.getRows()
                    && (!maze.isWall(nextX, nextY)
                    || maze.isDoor(nextX, nextY))) {
                ghost.moveTo(nextX, nextY);
            }
        }

        checkCollision();
        repaint();
    }

    private void checkCollision() {
        if (gameOver || pacmanBlinking) return;

        boolean hit =
                (blinky.getX() == pacman.getX()
                && blinky.getY() == pacman.getY())
                || (pinkyReleased
                && pinky.getX() == pacman.getX()
                && pinky.getY() == pacman.getY())
                || (inkyReleased
                && inky.getX() == pacman.getX()
                && inky.getY() == pacman.getY())
                || (clydeReleased
                && clyde.getX() == pacman.getX()
                && clyde.getY() == pacman.getY());

        if (!hit) return;

        stopTimers();
        lives--;

        if (lives <= 0) {
            gameOver = true;
            pacmanVisible = true;
            repaint();
            JOptionPane.showMessageDialog(this, "GAME OVER!");
            closeGame();
            return;
        }

        pacmanBlinking = true;
        pacmanVisible = false;

        Timer blinkTimer = new Timer(150, e -> {
            pacmanVisible = !pacmanVisible;
            repaint();
        });
        blinkTimer.start();

        Timer pauseTimer = new Timer(1200, e -> {
            blinkTimer.stop();

            pacmanBlinking = false;
            pacmanVisible = true;
            pacman = new Pacman(1, 1);

            blinky.moveTo(9, 4);
            pinky.moveTo(8, 6);
            inky.moveTo(9, 6);
            clyde.moveTo(10, 6);

            pinkyReleased = false;
            inkyReleased = false;
            clydeReleased = false;

            pinkyExited = false;
            inkyExited = false;
            clydeExited = false;

            repaint();

            JOptionPane.showMessageDialog(
                    this, "You were caught! Lives left: " + lives
            );

            if (!gameOver) {
                mouthTimer.start();
                pacmanTimer.start();
                ghostTimer.start();
                pinkyTimer.start();
                inkyTimer.start();
                clydeTimer.start();

                startPinkyReleaseTimer();
                startInkyReleaseTimer();
                startClydeReleaseTimer();
            }
        });

        pauseTimer.setRepeats(false);
        pauseTimer.start();
    }

    private void stopTimers() {
        if (pacmanTimer != null) pacmanTimer.stop();
        if (ghostTimer != null) ghostTimer.stop();
        if (pinkyTimer != null) pinkyTimer.stop();
        if (inkyTimer != null) inkyTimer.stop();
        if (clydeTimer != null) clydeTimer.stop();
        if (mouthTimer != null) mouthTimer.stop();

        if (pinkyReleaseTimer != null) pinkyReleaseTimer.stop();
        if (inkyReleaseTimer != null) inkyReleaseTimer.stop();
        if (clydeReleaseTimer != null) clydeReleaseTimer.stop();
    }

    private void closeGame() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) window.dispose();
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

        drawGhost(g, blinky, tileSize);
        drawGhost(g, pinky, tileSize);
        drawGhost(g, inky, tileSize);
        drawGhost(g, clyde, tileSize);

        if (pacmanVisible) {
            g.setColor(Color.YELLOW);

            int px = pacman.getX() * tileSize + 3;
            int py = pacman.getY() * tileSize + 3;

            int angle = 0;

            if (pacman.getDx() == -1) angle = 180;
            else if (pacman.getDy() == -1) angle = 90;
            else if (pacman.getDy() == 1) angle = 270;

            if (mouthOpen) {
                g.fillArc(px, py, 24, 24, angle + 35, 290);
            } else {
                g.fillOval(px, py, 24, 24);
            }
        }

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
