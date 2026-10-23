package com.mycompany.gui;

import com.mycompany.util.Maze;
import com.mycompany.entities.Pacman;
import com.mycompany.entities.Ghost;

import javax.swing.*;
import java.awt.*;
/**
 * Main gameplay panel for the Pac-Man game.
 * Manages game state, user input, timers, collision detection, and rendering.
 */
public class GamePanel extends JPanel {

    private Maze maze;
    private Pacman pacman;

    private int score = 0;
    private int lives = 3;

    private GhostManager ghostManager;
    private final GameRenderer renderer = new GameRenderer();

    private Timer pacmanTimer;
    private Timer mouthTimer;

    private boolean gameOver = false;
    private boolean mouthOpen = true;
    private boolean pacmanBlinking = false;
    private boolean pacmanVisible = true;
    private boolean paused = false; // Track pause state
     /**
     * Constructs the GamePanel, initializes the maze, Pacman, ghosts,
     * key bindings, and starts the gameplay timers.
     */

    public GamePanel() {
        maze = new Maze();
        pacman = new Pacman(1, 1);

        ghostManager = new GhostManager(
                maze, pacman, this::checkCollision, this::repaint
        );

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(570, 450));
        setFocusable(true);

        setupKeyBindings();

        pacmanTimer = new Timer(150, e -> movePacman());

        mouthTimer = new Timer(120, e -> {
            if (!pacmanBlinking && !gameOver && !paused) {
                mouthOpen = !mouthOpen;
                repaint();
            }
        });

        mouthTimer.start();
        ghostManager.start();
        pacmanTimer.start();
    }
/**
     * Sets up keyboard bindings for arrow movement and pause keys (P and SPACE).
     */
    private void setupKeyBindings() {
        bindMove("UP", "moveUp", 0, -1);
        bindMove("DOWN", "moveDown", 0, 1);
        bindMove("LEFT", "moveLeft", -1, 0);
        bindMove("RIGHT", "moveRight", 1, 0);

        // Bind 'P' and 'SPACE' to pause/resume
        bindPause("P", "pauseKeyP");
        bindPause("SPACE", "pauseKeySpace");
    }
/**
     * Binds a directional key stroke to Pacman's movement direction.
     *
     * @param key the keyboard key name (e.g., "UP", "DOWN")
     * @param name unique identifier for the action map
     * @param dx horizontal direction delta (-1 for left, 1 for right, 0 for none)
     * @param dy vertical direction delta (-1 for up, 1 for down, 0 for none)
     */
    private void bindMove(String key, String name, int dx, int dy) {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(key), name);

        getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Ignore input while paused
                if (!gameOver && !pacmanBlinking && !paused) {
                    pacman.setDirection(dx, dy);
                }
            }
        });
    }
 /**
     * Binds a key stroke to the pause/resume functionality.
     *
     * @param key the keyboard key name (e.g., "P", "SPACE")
     * @param name unique identifier for the pause action
     */
    private void bindPause(String key, String name) {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(key), name);

        getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                togglePause();
            }
        });
    }
/**
     * Toggles the paused state of the game.
     * Freezes gameplay timers when paused and resumes them when unpaused.
     */
    private void togglePause() {
        if (gameOver || pacmanBlinking) return;

        paused = !paused;

        if (paused) {
            stopTimers();
        } else {
            pacmanTimer.start();
            mouthTimer.start();
            ghostManager.start();
        }

        repaint();
    }
/**
     * Moves Pacman in his current direction, updates the score if a dot is eaten,
     * triggers collision checks, and verifies win conditions.
     */
    private void movePacman() {
        if (gameOver || pacmanBlinking || paused) return;

        if (pacman.move(maze)) {
            score += 10;
        }

        checkCollision();

        if (gameOver || pacmanBlinking) return;

        if (!maze.hasDotsLeft()) {
            gameOver = true;
            stopTimers();
            JOptionPane.showMessageDialog(this, "YOU WIN!\nFinal Score: " + score);
            closeGame();
            return;
        }

        repaint();
    }
/**
     * Checks if Pacman collides with any released ghost.
     * Decreases lives, triggers the blink animation, or ends the game if lives reach zero.
     */
    private void checkCollision() {
        if (gameOver || pacmanBlinking || paused || ghostManager == null) return;

        Ghost blinky = ghostManager.getBlinky();
        Ghost pinky = ghostManager.getPinky();
        Ghost inky = ghostManager.getInky();
        Ghost clyde = ghostManager.getClyde();

        boolean hit =
                samePosition(blinky)
                || (ghostManager.isPinkyReleased() && samePosition(pinky))
                || (ghostManager.isInkyReleased() && samePosition(inky))
                || (ghostManager.isClydeReleased() && samePosition(clyde));

        if (!hit) return;

        stopTimers();
        lives--;

        if (lives <= 0) {
            gameOver = true;
            pacmanVisible = true;
            repaint();
            // Displays final score along with GAME OVER
            JOptionPane.showMessageDialog(this, "GAME OVER!\nFinal Score: " + score);
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

            ghostManager.setPacman(pacman);
            ghostManager.resetPositions();

            repaint();

            JOptionPane.showMessageDialog(
                    this, "You were caught! Lives left: " + lives
            );

            if (!gameOver && !paused) {
                mouthTimer.start();
                pacmanTimer.start();
                ghostManager.start();
            }
        });

        pauseTimer.setRepeats(false);
        pauseTimer.start();
    }
/**
     * Checks whether a ghost occupies the exact same tile as Pacman.
     *
     * @param ghost the ghost entity to check against Pacman
     * @return true if both occupy the same (x, y) coordinates, false otherwise
     */
    private boolean samePosition(Ghost ghost) {
        return ghost.getX() == pacman.getX()
                && ghost.getY() == pacman.getY();
    }
/**
     * Stops all active gameplay timers (Pacman, mouth animation, and ghosts).
     */
    private void stopTimers() {
        if (pacmanTimer != null) pacmanTimer.stop();
        if (mouthTimer != null) mouthTimer.stop();

        if (ghostManager != null) {
            ghostManager.stop();
        }
    }
 /**
     * Disposes and closes the parent window when the game finishes.
     */
    private void closeGame() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
/**
     * Paints the game screen components including the maze, entities, and UI overlay.
     *
     * @param g the Graphics context used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        renderer.draw(g, maze, pacman,
                ghostManager.getBlinky(),
                ghostManager.getPinky(),
                ghostManager.getInky(),
                ghostManager.getClyde(),
                score, lives, mouthOpen, pacmanVisible, paused);
    }
}