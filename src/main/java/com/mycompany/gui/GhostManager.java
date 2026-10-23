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

/**
 * Manages the lifecycle, timers, house-release schedules, 
 * and movement updates for all four ghost entities.
 */
public class GhostManager {

    private final Maze maze;
    private Pacman pacman;

    private final Ghost blinky, pinky, inky, clyde;

    private final BlinkyAI blinkyAI = new BlinkyAI();
    private final PinkyAI pinkyAI = new PinkyAI();
    private final InkyAI inkyAI = new InkyAI();
    private final ClydeAI clydeAI = new ClydeAI();

    // Movement speed timers for each individual ghost
    private final Timer ghostTimer, pinkyTimer, inkyTimer, clydeTimer;

    // Countdown timers to release ghosts from the central house
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

    // Target coordinate representing the ghost house exit doorway
    private final int exitX = 9;
    private final int exitY = 9;

    /**
     * Constructs the GhostManager, instantiates the four ghosts, 
     * and sets up their individual movement timers and callbacks.
     *
     * @param maze the maze grid containing walls and doors
     * @param pacman the target Pacman entity
     * @param collisionCheck callback executed after each ghost step to test for collisions
     * @param repaintAction callback executed after each ghost step to refresh the display
     */
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

        // Different update rates (speeds) for each ghost
        ghostTimer = new Timer(300, e -> moveUsingAI(blinky, blinkyAI));
        pinkyTimer = new Timer(400, e -> movePinky());
        inkyTimer = new Timer(350, e -> moveInky());
        clydeTimer = new Timer(400, e -> moveClyde());
    }

    /**
     * Starts all ghost movement timers and initiates the release countdowns.
     */
    public void start() {
        paused = false;

        ghostTimer.start();
        pinkyTimer.start();
        inkyTimer.start();
        clydeTimer.start();

        startReleaseTimers();
    }

    /**
     * Pauses and stops all movement and release timers.
     */
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

    /**
     * Starts one-shot countdown timers to release ghosts from the central house
     * in staggered intervals (Pinky at 10s, Inky at 20s, Clyde at 30s).
     */
    private void startReleaseTimers() {
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

    /**
     * Handles Pinky's movement: navigates toward the exit first, 
     * then switches to PinkyAI once outside.
     */
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

    /**
     * Handles Inky's movement: navigates toward the exit first, 
     * then switches to InkyAI once outside.
     */
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

    /**
     * Handles Clyde's movement: navigates toward the exit first, 
     * then switches to ClydeAI once outside.
     */
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

    /**
     * Steps a ghost entity incrementally toward the house exit door.
     *
     * @param ghost the ghost navigating out of the house
     * @return true if the ghost has reached the exit tile, false otherwise
     */
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

    /**
     * Queries a ghost's AI strategy, validates the returned step against boundaries and walls,
     * updates the ghost's position, and executes collision/repaint callbacks.
     *
     * @param ghost the ghost entity to move
     * @param ai the AI strategy determining the direction
     */
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

    /**
     * Resets all ghosts to their original starting coordinates,
     * relocks the ghost house doors, and resets release states.
     */
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

    /**
     * Updates the reference to the active Pacman entity (used after a respawn).
     *
     * @param pacman the new Pacman instance
     */
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