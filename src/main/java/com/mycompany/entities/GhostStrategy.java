
package com.mycompany.entities;

import com.mycompany.util.Maze;

public interface GhostStrategy {
    int[] getNextMove(Ghost ghost, Pacman pacman, Maze maze);
}
