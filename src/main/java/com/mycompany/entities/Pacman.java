package com.mycompany.entities;

import com.mycompany.util.Maze;

/**
 * Represents the player entity (Pac-Man).
 * Handles user-requested direction queuing, movement validation, 
 * dot consumption, and coordinate tracking within the maze.
 */
public class Pacman {

    private int x;
    private int y;

    // Current movement direction
    private int dx = 0;
    private int dy = 0;

    // Buffered direction from player input, applied when a turn becomes possible
    private int nextDx = 0;
    private int nextDy = 0;

    /**
     * Constructs Pac-Man at the specified starting grid coordinates.
     *
     * @param startX initial column coordinate
     * @param startY initial row coordinate
     */
    public Pacman(int startX, int startY) {
        x = startX;
        y = startY;
    }

    /**
     * Attempts to turn in the buffered direction and moves Pac-Man forward.
     * Collects any dot present at the new tile.
     *
     * @param maze the maze grid used for wall collisions and dot collection
     * @return true if Pac-Man consumed a dot during this move, false otherwise
     */
    public boolean move(Maze maze) {

        // Try to turn in the requested direction if the path is clear
        int newX = x + nextDx;
        int newY = y + nextDy;

        if ((nextDx != 0 || nextDy != 0)
                && canMove(newX, newY, maze)) {
            dx = nextDx;
            dy = nextDy;
        }

        // If Pac-Man has no direction, it cannot move
        if (dx == 0 && dy == 0) {
            return false;
        }

        // Continue moving in the current direction
        newX = x + dx;
        newY = y + dy;

        // Stop if blocked by a wall in the current direction
        if (!canMove(newX, newY, maze)) {
            return false;
        }

        // Check whether the destination tile contains a dot
        boolean collectedDot = maze.hasDot(newX, newY);

        x = newX;
        y = newY;

        // Consume the dot from the maze grid
        maze.collectDot(x, y);

        return collectedDot;
    }

    /**
     * Validates whether a tile is within bounds and not blocked by a wall or ghost door.
     *
     * @param newX target column coordinate
     * @param newY target row coordinate
     * @param maze the maze grid to check boundaries and walls
     * @return true if Pac-Man can legally move to this tile, false otherwise
     */
    private boolean canMove(int newX, int newY, Maze maze) {
        return newX >= 0 && newX < maze.getCols()
                && newY >= 0 && newY < maze.getRows()
                && !maze.isWallForPacman(newX, newY);
    }

    /**
     * @return current column coordinate (X)
     */
    public int getX() {
        return x;
    }

    /**
     * @return current row coordinate (Y)
     */
    public int getY() {
        return y;
    }

    /**
     * @return current horizontal movement direction (-1 left, 1 right, 0 still)
     */
    public int getDx() {
        return dx;
    }

    /**
     * @return current vertical movement direction (-1 up, 1 down, 0 still)
     */
    public int getDy() {
        return dy;
    }

    /**
     * Buffers a new movement direction requested by player input.
     *
     * @param dx horizontal step offset
     * @param dy vertical step offset
     */
    public void setDirection(int dx, int dy) {
        nextDx = dx;
        nextDy = dy;
    }
}