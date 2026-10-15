
package com.mycompany.entities;

import com.mycompany.util.Maze;
import java.util.LinkedList;
import java.util.Queue;

public class PinkyAI implements GhostStrategy {

    @Override
    public int[] getNextMove(Ghost ghost, Pacman pacman, Maze maze) {

        int cols = maze.getCols();
        int rows = maze.getRows();

        // Target four tiles ahead of Pac-Man.
        int targetX = pacman.getX() + 4 * pacman.getDx();
        int targetY = pacman.getY() + 4 * pacman.getDy();

        targetX = Math.max(0, Math.min(targetX, cols - 1));
        targetY = Math.max(0, Math.min(targetY, rows - 1));

        int startX = ghost.getX();
        int startY = ghost.getY();

        boolean[][] visited = new boolean[cols][rows];
        int[][] previousX = new int[cols][rows];
        int[][] previousY = new int[cols][rows];

        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        int[][] directions = {
            {0, -1}, {0, 1}, {-1, 0}, {1, 0}
        };

        int bestX = startX;
        int bestY = startY;
        int bestDistance = Math.abs(startX - targetX)
                         + Math.abs(startY - targetY);

        // Search the maze using BFS.
        while (!queue.isEmpty()) {

            int[] current = queue.remove();

            int x = current[0];
            int y = current[1];

            int distance = Math.abs(x - targetX)
                         + Math.abs(y - targetY);

            if (distance < bestDistance) {
                bestDistance = distance;
                bestX = x;
                bestY = y;
            }

            for (int[] direction : directions) {

                int nextX = x + direction[0];
                int nextY = y + direction[1];

                if (nextX < 0 || nextX >= cols
                        || nextY < 0 || nextY >= rows) {
                    continue;
                }

                if (visited[nextX][nextY]
                        || maze.isWall(nextX, nextY)) {
                    continue;
                }

                visited[nextX][nextY] = true;
                previousX[nextX][nextY] = x;
                previousY[nextX][nextY] = y;

                queue.add(new int[]{nextX, nextY});
            }
        }

        // No reachable tile is closer to the target.
        if (bestX == startX && bestY == startY) {
            return new int[]{0, 0};
        }

        // Trace the path backwards to find the first step.
        int x = bestX;
        int y = bestY;

        while (previousX[x][y] != startX
                || previousY[x][y] != startY) {

            int oldX = previousX[x][y];
            int oldY = previousY[x][y];

            x = oldX;
            y = oldY;
        }

        return new int[]{
            x - startX,
            y - startY
        };
    }
}
