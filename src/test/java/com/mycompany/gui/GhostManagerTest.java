
package com.mycompany.gui;

import com.mycompany.entities.Ghost;
import com.mycompany.entities.Pacman;
import com.mycompany.util.Maze;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GhostManagerTest {

    private GhostManager createManager() {
        return new GhostManager(
                new Maze(),
                new Pacman(1, 1),
                () -> {},
                () -> {}
        );
    }

    @Test
    void ghostsStartAtCorrectPositions() {
        GhostManager manager = createManager();

        assertPosition(manager.getBlinky(), 9, 4);
        assertPosition(manager.getPinky(), 8, 6);
        assertPosition(manager.getInky(), 9, 6);
        assertPosition(manager.getClyde(), 10, 6);
    }

    @Test
    void ghostsAreInitiallyNotReleased() {
        GhostManager manager = createManager();

        assertFalse(manager.isPinkyReleased());
        assertFalse(manager.isInkyReleased());
        assertFalse(manager.isClydeReleased());
    }

    @Test
    void resetRestoresGhostPositions() {
        GhostManager manager = createManager();

        manager.getBlinky().moveTo(1, 1);
        manager.getPinky().moveTo(2, 1);
        manager.getInky().moveTo(3, 1);
        manager.getClyde().moveTo(4, 1);

        manager.resetPositions();

        assertPosition(manager.getBlinky(), 9, 4);
        assertPosition(manager.getPinky(), 8, 6);
        assertPosition(manager.getInky(), 9, 6);
        assertPosition(manager.getClyde(), 10, 6);
    }

    @Test
    void resetRestoresReleaseStates() {
        GhostManager manager = createManager();

        manager.resetPositions();

        assertFalse(manager.isPinkyReleased());
        assertFalse(manager.isInkyReleased());
        assertFalse(manager.isClydeReleased());
    }

    private void assertPosition(
            Ghost ghost, int expectedX, int expectedY) {

        assertEquals(expectedX, ghost.getX());
        assertEquals(expectedY, ghost.getY());
    }
}
