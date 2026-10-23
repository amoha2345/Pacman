
package com.mycompany.entities;

import com.mycompany.util.Maze;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlinkyAITest {

    private final BlinkyAI ai = new BlinkyAI();

    @Test
    void returnsValidAdjacentMove() {
        Maze maze = new Maze();
        Ghost ghost = new Ghost(9, 4, java.awt.Color.RED);
        Pacman pacman = new Pacman(9, 9);

        int[] move = ai.getNextMove(ghost, pacman, maze);

        assertNotNull(move);
        assertEquals(2, move.length);
        assertEquals(1, Math.abs(move[0]) + Math.abs(move[1]));

        int nextX = ghost.getX() + move[0];
        int nextY = ghost.getY() + move[1];

        assertFalse(maze.isWall(nextX, nextY));
    }

    @Test
    void movesAlongShortestPathTowardPacman() {
        Maze maze = new Maze();

        Ghost ghost = new Ghost(1, 1, java.awt.Color.RED);
        Pacman pacman = new Pacman(5, 1);

        int[] move = ai.getNextMove(ghost, pacman, maze);

        assertArrayEquals(new int[]{1, 0}, move);
    }

    @Test
    void returnsZeroMoveWhenAlreadyOnPacman() {
        Maze maze = new Maze();
        Ghost ghost = new Ghost(5, 3, java.awt.Color.RED);
        Pacman pacman = new Pacman(5, 3);

        int[] move = ai.getNextMove(ghost, pacman, maze);

        assertArrayEquals(new int[]{0, 0}, move);
    }
}
