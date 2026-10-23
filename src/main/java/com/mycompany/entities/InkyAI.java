
package com.mycompany.entities;

import com.mycompany.util.Maze;

/**
 * AI strategy for Inky (the cyan ghost).
 * Implements an "ambush" behavior by targeting 2 tiles ahead of Pacman's current direction,
 * attempting to cut off Pacman's escape route.
 */
public class InkyAI implements GhostStrategy {

    /**
     * Determines Inky's next move by calculating an ambush target 2 tiles ahead of Pacman,
     * clamping that target within maze bounds, and picking the valid move closest to it.
     *
     * @param ghost the ghost entity executing the move (Inky)
     * @param pacman the target Pacman entity
     * @param maze the maze grid with walls and walkable tiles
     * @return an integer array {bestX, bestY} representing the next move direction
     */
    @Override
    public int[] getNextMove(Ghost ghost, Pacman pacman, Maze maze) {

        // Ambush target: 2 tiles ahead of Pacman's current movement direction
        int targetX = pacman.getX() + 2 * pacman.getDx();
        int targetY = pacman.getY() + 2 * pacman.getDy();

        // Clamp target coordinates within the maze boundaries to avoid out-of-bounds targets
        targetX = Math.max(0, Math.min(targetX, maze.getCols() - 1));
        targetY = Math.max(0, Math.min(targetY, maze.getRows() - 1));

        int[][] directions = {
            {1, 0}, {0, 1}, {-1, 0}, {0, -1}
        };

        int bestX = 0;
        int bestY = 0;
        int bestDistance = Integer.MAX_VALUE;

        // Find the valid cardinal move that minimizes Manhattan distance to the ambush target
        for (int[] direction : directions) {

            int nextX = ghost.getX() + direction[0];
            int nextY = ghost.getY() + direction[1];

            if (nextX < 0 || nextX >= maze.getCols()
                    || nextY < 0 || nextY >= maze.getRows()
                    || maze.isWall(nextX, nextY)) {
                continue;
            }

            int distance = Math.abs(targetX - nextX)
                         + Math.abs(targetY - nextY);

            if (distance < bestDistance) {
                bestDistance = distance;
                bestX = direction[0];
                bestY = direction[1];
            }
        }

        return new int[]{bestX, bestY};
    }
}
