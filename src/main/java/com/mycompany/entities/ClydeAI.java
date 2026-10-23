
package com.mycompany.entities;

import com.mycompany.util.Maze;

/**
 * AI strategy for Clyde (the orange ghost).
 * Implements a "coward" behavior based on Manhattan distance:
 * Chases Pacman directly when far away (> 6 tiles), but retreats 
 * to the bottom-left corner of the maze when Pacman gets too close.
 */
public class ClydeAI implements GhostStrategy {

    /**
     * Determines Clyde's next move by calculating the Manhattan distance to Pacman.
     * Selects either Pacman or the bottom-left corner as the target tile,
     * and picks the valid cardinal direction that minimizes distance to that target.
     *
     * @param ghost the ghost entity executing the move (Clyde)
     * @param pacman the target Pacman entity
     * @param maze the maze grid representing walls and walkable tiles
     * @return an integer array {bestX, bestY} representing the chosen movement direction
     */
    @Override
    public int[] getNextMove(Ghost ghost, Pacman pacman, Maze maze) {

        // Calculate Manhattan distance between Clyde and Pacman
        int distance = Math.abs(ghost.getX() - pacman.getX())
                     + Math.abs(ghost.getY() - pacman.getY());

        int targetX;
        int targetY;

        // Switch behavior based on proximity (Coward mechanic)
        if (distance > 6) {
            // Chase Pacman directly when far away
            targetX = pacman.getX();
            targetY = pacman.getY();
        } else {
            // Retreat to the bottom-left corner when within 6 tiles
            targetX = 1;
            targetY = maze.getRows() - 2;
        }

        // Cardinal directions: Right, Down, Left, Up
        int[][] directions = {
            {1, 0}, {0, 1}, {-1, 0}, {0, -1}
        };

        int bestX = 0;
        int bestY = 0;
        int bestDistance = Integer.MAX_VALUE;

        // Greedy search: Test all four directions to find the one closest to the target
        for (int[] direction : directions) {

            int nextX = ghost.getX() + direction[0];
            int nextY = ghost.getY() + direction[1];

            // Skip invalid moves (out of bounds or hitting a wall)
            if (nextX < 0 || nextX >= maze.getCols()
                    || nextY < 0 || nextY >= maze.getRows()
                    || maze.isWall(nextX, nextY)) {
                continue;
            }

            // Calculate distance from this candidate step to the target
            int distanceToTarget =
                    Math.abs(targetX - nextX)
                    + Math.abs(targetY - nextY);

            // Keep track of the direction with the minimum distance
            if (distanceToTarget < bestDistance) {
                bestDistance = distanceToTarget;
                bestX = direction[0];
                bestY = direction[1];
            }
        }

        return new int[]{bestX, bestY};
    }
}