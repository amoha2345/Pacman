
package com.mycompany.entities;

import com.mycompany.util.Maze;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PinkyAITest {

    private final PinkyAI ai = new PinkyAI();

    @Test
    void movesTowardTargetAheadOfPacman() {
        Maze maze = new Maze();

        Ghost ghost = new Ghost(5, 1, java.awt.Color.PINK);
        Pacman pacman = new Pacman(3, 1);

        pacman.setDirection(1, 0);
        pacman.move(maze);

        int[] move = ai.getNextMove(ghost, pacman, maze);

        assertArrayEquals(new int[]{1, 0}, move);
    }

    @Test
    void returnsAdjacentValidMove() {
        Maze maze = new Maze();

        Ghost ghost = new Ghost(5, 1, java.awt.Color.PINK);
        Pacman pacman = new Pacman(10, 1);

        int[] move = ai.getNextMove(ghost, pacman, maze);

        assertEquals(1, Math.abs(move[0]) + Math.abs(move[1]));

        int nextX = ghost.getX() + move[0];
        int nextY = ghost.getY() + move[1];

        assertFalse(maze.isWall(nextX, nextY));
    }

    @Test
    void returnsZeroMoveWhenNoCloserTileExists() {
        Maze maze = new Maze();

        Ghost ghost = new Ghost(1, 1, java.awt.Color.PINK);
        Pacman pacman = new Pacman(1, 1);

        int[] move = ai.getNextMove(ghost, pacman, maze);

        assertArrayEquals(new int[]{0, 0}, move);
    }

    @Test
    void targetDoesNotExceedMazeBounds() {
        Maze maze = new Maze();

        Ghost ghost = new Ghost(17, 1, java.awt.Color.PINK);
        Pacman pacman = new Pacman(17, 1);

        pacman.setDirection(1, 0);

        // Pac-Man moves right, then the target is clamped
        // to the right edge of the maze.
        pacman.move(maze);

        int[] move = ai.getNextMove(ghost, pacman, maze);

        assertNotNull(move);

        int nextX = ghost.getX() + move[0];
        int nextY = ghost.getY() + move[1];

        assertTrue(nextX >= 0 && nextX < maze.getCols());
        assertTrue(nextY >= 0 && nextY < maze.getRows());
    }
}
