
package com.mycompany.entities;

import com.mycompany.util.Maze;

public class InkyAI implements GhostStrategy {

    @Override
    public int[] getNextMove(Ghost ghost, Pacman pacman, Maze maze) {

        int targetX = pacman.getX() + 2 * pacman.getDx();
        int targetY = pacman.getY() + 2 * pacman.getDy();

        targetX = Math.max(0, Math.min(targetX, maze.getCols() - 1));
        targetY = Math.max(0, Math.min(targetY, maze.getRows() - 1));

        int[][] directions = {
            {1, 0}, {0, 1}, {-1, 0}, {0, -1}
        };

        int bestX = 0;
        int bestY = 0;
        int bestDistance = Integer.MAX_VALUE;

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

