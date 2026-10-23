
package com.mycompany.entities;

import com.mycompany.util.Maze;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PacmanTest {

    @Test
    void pacmanStartsAtSpecifiedPosition() {
        Pacman pacman = new Pacman(1, 1);

        assertEquals(1, pacman.getX());
        assertEquals(1, pacman.getY());
        assertEquals(0, pacman.getDx());
        assertEquals(0, pacman.getDy());
    }

    @Test
    void pacmanMovesInRequestedDirection() {
        Maze maze = new Maze();
        Pacman pacman = new Pacman(1, 1);

        pacman.setDirection(1, 0);
        boolean collectedDot = pacman.move(maze);

        assertEquals(2, pacman.getX());
        assertEquals(1, pacman.getY());
        assertTrue(collectedDot);
    }

    @Test
    void pacmanCollectsDotOnlyOnce() {
        Maze maze = new Maze();
        Pacman pacman = new Pacman(1, 1);

        pacman.setDirection(1, 0);

        assertTrue(pacman.move(maze));

        // The next tile also contains a dot.
        assertTrue(pacman.move(maze));

        // Move back to the tile already visited.
        pacman.setDirection(-1, 0);
        assertFalse(pacman.move(maze));

        assertEquals(2, pacman.getX());
        assertEquals(1, pacman.getY());
    }

    @Test
    void pacmanCannotMoveIntoWall() {
        Maze maze = new Maze();
        Pacman pacman = new Pacman(1, 1);

        pacman.setDirection(0, -1);

        boolean collectedDot = pacman.move(maze);

        assertFalse(collectedDot);
        assertEquals(1, pacman.getX());
        assertEquals(1, pacman.getY());
    }

    @Test
    void pacmanCannotMoveThroughGhostDoor() {
        Maze maze = new Maze();
        Pacman pacman = new Pacman(9, 9);

        pacman.setDirection(0, -1);
        pacman.move(maze);

        assertEquals(9, pacman.getX());
        assertEquals(9, pacman.getY());
    }

    @Test
    void pacmanContinuesMovingWhenRequestedTurnIsBlocked() {
        Maze maze = new Maze();
        Pacman pacman = new Pacman(1, 1);

        pacman.setDirection(1, 0);
        pacman.move(maze);

        // Up is blocked by the wall at (2, 0).
        pacman.setDirection(0, -1);
        pacman.move(maze);

        // Pac-Man should continue moving right.
        assertEquals(3, pacman.getX());
        assertEquals(1, pacman.getY());
        assertEquals(1, pacman.getDx());
        assertEquals(0, pacman.getDy());
    }

    @Test
    void pacmanDoesNotMoveWithoutDirection() {
        Maze maze = new Maze();
        Pacman pacman = new Pacman(1, 1);

        boolean collectedDot = pacman.move(maze);

        assertFalse(collectedDot);
        assertEquals(1, pacman.getX());
        assertEquals(1, pacman.getY());
    }
}
