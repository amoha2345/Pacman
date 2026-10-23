
package com.mycompany.entities;

import com.mycompany.util.Maze;
import java.util.*;

/**
 * AI strategy for Blinky (the red ghost).
 * Implements the Breadth-First Search (BFS) pathfinding algorithm
 * to calculate the true shortest path directly toward Pacman.
 */
public class BlinkyAI implements GhostStrategy {

    // Cardinal movement offsets: Up, Down, Left, Right
    private final int[][] directions = {
        {0, -1}, {0, 1}, {-1, 0}, {1, 0}
    };

    /**
     * Calculates the next optimal move for Blinky toward Pacman using BFS.
     * Explores the maze tile-by-tile using a Queue and propagates the initial step 
     * direction forward until Pacman's coordinates are reached.
     *
     * @param ghost the ghost entity executing the move (Blinky)
     * @param pacman the target Pacman entity
     * @param maze the maze grid representing walls and walkable tiles
     * @return an integer array {dx, dy} representing the optimal direction, or {0, 0} if no path exists
     */
    @Override
    public int[] getNextMove(Ghost ghost, Pacman pacman, Maze maze) {

        int rows = maze.getRows();
        int cols = maze.getCols();

        // Tracks visited tiles to prevent re-processing and infinite loops
        boolean[][] visited = new boolean[rows][cols];

        // Stores the initial movement direction taken from the start for each reached tile
        int[][][] firstMove = new int[rows][cols][2];

        // Queue for BFS traversal
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{ghost.getX(), ghost.getY()});
        visited[ghost.getY()][ghost.getX()] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.remove();

            int x = current[0];
            int y = current[1];

            // Target reached: return the initial step direction that leads here
            if (x == pacman.getX() && y == pacman.getY()) {
                return firstMove[y][x];
            }

            // Explore all 4 adjacent directions
            for (int[] direction : directions) {
                int nx = x + direction[0];
                int ny = y + direction[1];

                // Skip if out of maze boundaries
                if (nx < 0 || nx >= cols || ny < 0 || ny >= rows)
                    continue;

                // Skip if wall or already visited
                if (maze.isWall(nx, ny) || visited[ny][nx])
                    continue;

                visited[ny][nx] = true;

                // Record the first step direction or propagate it from the parent tile
                if (x == ghost.getX() && y == ghost.getY()) {
                    firstMove[ny][nx] = direction;
                } else {
                    firstMove[ny][nx] = firstMove[y][x];
                }

                queue.add(new int[]{nx, ny});
            }
        }

        // Return no movement if Pacman is unreachable
        return new int[]{0, 0};
    }
}