
package com.mycompany.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MazeTest {

    @Test
    void mazeHasCorrectDimensions() {
        Maze maze = new Maze();

        assertEquals(15, maze.getRows());
        assertEquals(19, maze.getCols());
    }

    @Test
    void detectsWallsCorrectly() {
        Maze maze = new Maze();

        assertTrue(maze.isWall(0, 0));
        assertTrue(maze.isWall(1, 0));
        assertFalse(maze.isWall(1, 1));
    }

    @Test
    void pacmanCannotPassThroughGhostDoor() {
        Maze maze = new Maze();

        assertTrue(maze.isDoor(9, 8));
        assertTrue(maze.isWallForPacman(9, 8));
    }

    @Test
    void ghostDoorIsNotAWallForGhosts() {
        Maze maze = new Maze();

        assertFalse(maze.isWall(9, 8));
        assertTrue(maze.isDoor(9, 8));
    }

    @Test
    void detectsDotsCorrectly() {
        Maze maze = new Maze();

        assertTrue(maze.hasDot(1, 1));
        assertFalse(maze.hasDot(0, 0));
    }

    @Test
    void collectingDotRemovesIt() {
        Maze maze = new Maze();

        assertTrue(maze.hasDot(1, 1));

        maze.collectDot(1, 1);

        assertFalse(maze.hasDot(1, 1));
        assertEquals(' ', maze.getTile(1, 1));
    }

    @Test
    void collectingDotOnEmptyTileChangesNothing() {
        Maze maze = new Maze();

        maze.collectDot(1, 1);
        maze.collectDot(1, 1);

        assertEquals(' ', maze.getTile(1, 1));
    }

    @Test
    void mazeInitiallyHasDotsLeft() {
        Maze maze = new Maze();

        assertTrue(maze.hasDotsLeft());
    }
}
