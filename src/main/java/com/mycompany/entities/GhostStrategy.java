
package com.mycompany.entities;

import com.mycompany.util.Maze;

/**
 * Strategy interface for ghost artificial intelligence (Strategy Pattern).
 * Defines the contract that all ghost AI behaviors (Blinky, Pinky, Inky, Clyde)
 * must implement to calculate their next movement.
 */
public interface GhostStrategy {

    /**
     * Calculates the next move direction for a ghost.
     *
     * @param ghost the ghost entity executing the move
     * @param pacman the target Pacman entity
     * @param maze the maze grid representing walls and walkable tiles
     * @return an integer array {dx, dy} representing the next step direction
     */
    int[] getNextMove(Ghost ghost, Pacman pacman, Maze maze);
}