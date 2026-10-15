
package com.mycompany.entities;

import com.mycompany.util.Maze;

public class ClydeAI implements GhostStrategy {

    @Override
    public int[] getNextMove(Ghost ghost, Pacman pacman, Maze maze) {

        int distance = Math.abs(ghost.getX() - pacman.getX())
                     + Math.abs(ghost.getY() - pacman.getY());

        int targetX;
        int targetY;

        if (distance > 6) {
            targetX = pacman.getX();
            targetY = pacman.getY();
        } else {
            targetX = 1;
            targetY = maze.getRows() - 2;
        }

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

            int distanceToTarget =
                    Math.abs(targetX - nextX)
                    + Math.abs(targetY - nextY);

            if (distanceToTarget < bestDistance) {
                bestDistance = distanceToTarget;
                bestX = direction[0];
                bestY = direction[1];
            }
        }

        return new int[]{bestX, bestY};
    }
}
