
package com.mycompany.entities;

import com.mycompany.util.Maze;

public class Pacman {

    private int x;
    private int y;

    private int dx = 0;
    private int dy = 0;

    private int nextDx = 0;
    private int nextDy = 0;

    public Pacman(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public boolean move(Maze maze) {

        // Try to turn in the requested direction.
        int newX = x + nextDx;
        int newY = y + nextDy;

        if ((nextDx != 0 || nextDy != 0)
                && canMove(newX, newY, maze)) {
            dx = nextDx;
            dy = nextDy;
        }

        // If Pac-Man has no direction, it cannot move.
        if (dx == 0 && dy == 0) {
            return false;
        }

        // Continue moving in the current direction.
        newX = x + dx;
        newY = y + dy;

        if (!canMove(newX, newY, maze)) {
            return false;
        }

        // Check whether the next tile contains a dot.
        boolean collectedDot = maze.hasDot(newX, newY);

        x = newX;
        y = newY;

        maze.collectDot(x, y);

        return collectedDot;
    }

    private boolean canMove(int newX, int newY, Maze maze) {
        return newX >= 0 && newX < maze.getCols()
                && newY >= 0 && newY < maze.getRows()
                && !maze.isWallForPacman(newX, newY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDirection(int dx, int dy) {
        nextDx = dx;
        nextDy = dy;
    }
}