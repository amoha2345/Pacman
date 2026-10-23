
package com.mycompany.entities;

import com.mycompany.util.Maze;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClydeAITest {

    private final ClydeAI ai = new ClydeAI();

    @Test
    void chasesPacmanWhenFarAway() {
        Maze maze = new Maze();

        Ghost ghost = new Ghost(1, 1, java.awt.Color.ORANGE);
        Pacman pacman = new Pacman(10, 1);

        int[] move = ai.getNextMove(ghost, pacman, maze);

        assertArrayEquals(new int[]{1, 0}, move);
    }

    @Test
    void movesTowardBottomLeftWhenPacmanIsClose() {
        Maze maze = new Maze();

        Ghost ghost = new Ghost(5, 1, java.awt.Color.ORANGE);
        Pacman pacman = new Pacman(10, 1);

        // Distance is 5, so Clyde targets (1, 13).
        int[] move = ai.getNextMove(ghost, pacman, maze);

        assertArrayEquals(new int[]{0, 1}, move);
    }

    @Test
    void choosesValidAdjacentTile() {
        Maze maze = new Maze();

        Ghost ghost = new Ghost(5, 1, java.awt.Color.ORANGE);
        Pacman pacman = new Pacman(10, 1);

        int[] move = ai.getNextMove(ghost, pacman, maze);

        assertEquals(1, Math.abs(move[0]) + Math.abs(move[1]));

        int nextX = ghost.getX() + move[0];
        int nextY = ghost.getY() + move[1];

        assertFalse(maze.isWall(nextX, nextY));
    }

    @Test
    void doesNotMoveIntoWall() {
        Maze maze = new Maze();

        // At (1, 1), the tile above is a wall.
        Ghost ghost = new Ghost(1, 1, java.awt.Color.ORANGE);
        Pacman pacman = new Pacman(10, 1);

        int[] move = ai.getNextMove(ghost, pacman, maze);

        int nextX = ghost.getX() + move[0];
        int nextY = ghost.getY() + move[1];

        assertFalse(maze.isWall(nextX, nextY));
    }
}
