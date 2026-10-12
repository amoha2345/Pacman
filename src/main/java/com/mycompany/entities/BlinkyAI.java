
package com.mycompany.entities;

import com.mycompany.util.Maze;
import java.util.*;

public class BlinkyAI implements GhostStrategy {

    private final int[][] directions = {
        {0, -1}, {0, 1}, {-1, 0}, {1, 0}
    };

    @Override
    public int[] getNextMove(Ghost ghost, Pacman pacman, Maze maze) {

        int rows = maze.getRows();
        int cols = maze.getCols();

        boolean[][] visited = new boolean[rows][cols];
        int[][][] firstMove = new int[rows][cols][2];

        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{ghost.getX(), ghost.getY()});
        visited[ghost.getY()][ghost.getX()] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.remove();

            int x = current[0];
            int y = current[1];

            if (x == pacman.getX() && y == pacman.getY()) {
                return firstMove[y][x];
            }

            for (int[] direction : directions) {
                int nx = x + direction[0];
                int ny = y + direction[1];

                if (nx < 0 || nx >= cols || ny < 0 || ny >= rows)
                    continue;

                if (maze.isWall(nx, ny) || visited[ny][nx])
                    continue;

                visited[ny][nx] = true;

                if (x == ghost.getX() && y == ghost.getY()) {
                    firstMove[ny][nx] = direction;
                } else {
                    firstMove[ny][nx] = firstMove[y][x];
                }

                queue.add(new int[]{nx, ny});
            }
        }

        return new int[]{0, 0};
    }
}
