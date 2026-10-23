
package com.mycompany.entities;

import com.mycompany.util.Maze;
import java.util.LinkedList;
import java.util.Queue;

/**
 * AI strategy for Pinky (the pink ghost).
 * Targets 4 tiles ahead of Pacman to stage an ambush.
 * Uses Breadth-First Search (BFS) to find the tile closest to that ambush point,
 * and traces the path backwards using parent pointers (previousX/previousY) 
 * to determine the immediate first step.
 */
public class PinkyAI implements GhostStrategy {

    /**
     * Calculates Pinky's next move toward an ambush target 4 tiles ahead of Pacman.
     * Uses BFS exploration and backtracks from the best reachable tile to determine the first step.
     *
     * @param ghost the ghost entity executing the move (Pinky)
     * @param pacman the target Pacman entity
     * @param maze the maze grid representing walls and walkable tiles
     * @return an integer array {dx, dy} representing the next move direction, or {0, 0} if trapped
     */
    @Override
    public int[] getNextMove(Ghost ghost, Pacman pacman, Maze maze) {

        int cols = maze.getCols();
        int rows = maze.getRows();

        // Ambush target: 4 tiles ahead in Pacman's current facing direction
        int targetX = pacman.getX() + 4 * pacman.getDx();
        int targetY = pacman.getY() + 4 * pacman.getDy();

        // Clamp target coordinates within the maze boundaries
        targetX = Math.max(0, Math.min(targetX, cols - 1));
        targetY = Math.max(0, Math.min(targetY, rows - 1));

        int startX = ghost.getX();
        int startY = ghost.getY();

        // BFS tracking structures
        boolean[][] visited = new boolean[cols][rows];
        int[][] previousX = new int[cols][rows]; // Parent X pointers for path reconstruction
        int[][] previousY = new int[cols][rows]; // Parent Y pointers for path reconstruction

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

        // Search the maze using BFS to find the tile closest to the ambush target
        while (!queue.isEmpty()) {

            int[] current = queue.remove();

            int x = current[0];
            int y = current[1];

            int distance = Math.abs(x - targetX)
                         + Math.abs(y - targetY);

            // Update the closest tile found so far
            if (distance < bestDistance) {
                bestDistance = distance;
                bestX = x;
                bestY = y;
            }

            // Explore adjacent tiles
            for (int[] direction : directions) {

                int nextX = x + direction[0];
                int nextY = y + direction[1];

                // Skip out-of-bounds tiles
                if (nextX < 0 || nextX >= cols
                        || nextY < 0 || nextY >= rows) {
                    continue;
                }

                // Skip visited tiles and walls
                if (visited[nextX][nextY]
                        || maze.isWall(nextX, nextY)) {
                    continue;
                }

                visited[nextX][nextY] = true;
                previousX[nextX][nextY] = x; // Record parent tile
                previousY[nextX][nextY] = y;

                queue.add(new int[]{nextX, nextY});
            }
        }

        // Return {0, 0} if no reachable tile is closer to the target than current position
        if (bestX == startX && bestY == startY) {
            return new int[]{0, 0};
        }

        // Trace the path backwards from the destination tile to find the very first step
        int x = bestX;
        int y = bestY;

        while (previousX[x][y] != startX
                || previousY[x][y] != startY) {

            int oldX = previousX[x][y];
            int oldY = previousY[x][y];

            x = oldX;
            y = oldY;
        }

        // Return the directional delta of the first step from start
        return new int[]{
            x - startX,
            y - startY
        };
    }
}