package com.mycompany.gui;

import com.mycompany.util.Maze;
import com.mycompany.entities.Pacman;
import com.mycompany.entities.Ghost;
import com.mycompany.entities.BlinkyAI;
import com.mycompany.entities.PinkyAI;
import com.mycompany.entities.InkyAI;
import com.mycompany.entities.ClydeAI;
import com.mycompany.entities.GhostStrategy;

import javax.swing.Timer;
import java.awt.Color;

public class GhostManager {

    private final Maze maze;
    private Pacman pacman;

    private final Ghost blinky, pinky, inky, clyde;

    private final BlinkyAI blinkyAI = new BlinkyAI();
    private final PinkyAI pinkyAI = new PinkyAI();
    private final InkyAI inkyAI = new InkyAI();
    private final ClydeAI clydeAI = new ClydeAI();

    private final Timer ghostTimer, pinkyTimer, inkyTimer, clydeTimer;

    private Timer pinkyReleaseTimer, inkyReleaseTimer, clydeReleaseTimer;

    private boolean pinkyReleased = false;
    private boolean inkyReleased = false;
    private boolean clydeReleased = false;

    private boolean pinkyExited = false;
    private boolean inkyExited = false;
    private boolean clydeExited = false;

    private boolean paused = false;

    private final Runnable collisionCheck;
    private final Runnable repaintAction;

    private final int exitX = 9;
    private final int exitY = 9;

    public GhostManager(Maze maze, Pacman pacman,
            Runnable collisionCheck, Runnable repaintAction) {

        this.maze = maze;
        this.pacman = pacman;
        this.collisionCheck = collisionCheck;
        this.repaintAction = repaintAction;

        blinky = new Ghost(9, 4, Color.RED);
        pinky = new Ghost(8, 6, Color.PINK);
        inky = new Ghost(9, 6, Color.CYAN);
        clyde = new Ghost(10, 6, Color.ORANGE);

        ghostTimer = new Timer(300, e -> moveUsingAI(blinky, blinkyAI));
        pinkyTimer = new Timer(400, e -> movePinky());
        inkyTimer = new Timer(350, e -> moveInky());
        clydeTimer = new Timer(400, e -> moveClyde());
    }

    public void start() {
        paused = false;

        ghostTimer.start();
        pinkyTimer.start();
        inkyTimer.start();
        clydeTimer.start();

        startReleaseTimers();
    }

    public void stop() {
        paused = true;

        ghostTimer.stop();
        pinkyTimer.stop();
        inkyTimer.stop();
        clydeTimer.stop();

        if (pinkyReleaseTimer != null) pinkyReleaseTimer.stop();
        if (inkyReleaseTimer != null) inkyReleaseTimer.stop();
        if (clydeReleaseTimer != null) clydeReleaseTimer.stop();
    }

    private void startReleaseTimers() {
        // Do not restart release timers for ghosts already released.
        if (!pinkyReleased) {
            pinkyReleaseTimer = new Timer(10000, e -> {
                pinkyReleased = true;
                repaintAction.run();
            });
            pinkyReleaseTimer.setRepeats(false);
            pinkyReleaseTimer.start();
        }

        if (!inkyReleased) {
            inkyReleaseTimer = new Timer(20000, e -> {
                inkyReleased = true;
                repaintAction.run();
            });
            inkyReleaseTimer.setRepeats(false);
            inkyReleaseTimer.start();
        }

        if (!clydeReleased) {
            clydeReleaseTimer = new Timer(30000, e -> {
                clydeReleased = true;
                repaintAction.run();
            });
            clydeReleaseTimer.setRepeats(false);
            clydeReleaseTimer.start();
        }
    }

    private void movePinky() {
        if (paused || !pinkyReleased) return;

        if (!pinkyExited) {
            pinkyExited = moveTowardExit(pinky);
            collisionCheck.run();
            repaintAction.run();
            return;
        }

        moveUsingAI(pinky, pinkyAI);
    }

    private void moveInky() {
        if (paused || !inkyReleased) return;

        if (!inkyExited) {
            inkyExited = moveTowardExit(inky);
            collisionCheck.run();
            repaintAction.run();
            return;
        }

        moveUsingAI(inky, inkyAI);
    }

    private void moveClyde() {
        if (paused || !clydeReleased) return;

        if (!clydeExited) {
            clydeExited = moveTowardExit(clyde);
            collisionCheck.run();
            repaintAction.run();
            return;
        }

        moveUsingAI(clyde, clydeAI);
    }

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
        if (paused) return;

        int[] move = ai.getNextMove(ghost, pacman, maze);

        if (move != null && move.length >= 2
                && (move[0] != 0 || move[1] != 0)) {

            int nextX = ghost.getX() + move[0];
            int nextY = ghost.getY() + move[1];

            if (nextX >= 0 && nextX < maze.getCols()
                    && nextY >= 0 && nextY < maze.getRows()
                    && (!maze.isWall(nextX, nextY)
                    || maze.isDoor(nextX, nextY))) {

                ghost.moveTo(nextX, nextY);
            }
        }

        collisionCheck.run();
        repaintAction.run();
    }

    public void resetPositions() {
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
    }

    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    public Ghost getBlinky() { return blinky; }
    public Ghost getPinky() { return pinky; }
    public Ghost getInky() { return inky; }
    public Ghost getClyde() { return clyde; }

    public boolean isPinkyReleased() { return pinkyReleased; }
    public boolean isInkyReleased() { return inkyReleased; }
    public boolean isClydeReleased() { return clydeReleased; }
}
