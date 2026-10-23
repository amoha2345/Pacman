package com.mycompany.util;

/**
 * Represents the 2D grid layout and tile state of the Pac-Man maze.
 * Manages walls, dots, ghost house doors, and dot consumption.
 */
public class Maze {

    // 2D grid layout representing the maze structure:
    // '#' = Wall, '.' = Dot, 'D' = Ghost Door, ' ' = Empty path
    private final char[][] layout = {
        "###################".toCharArray(),
        "#.................#".toCharArray(),
        "#.###...#...###...#".toCharArray(),
        "#.................#".toCharArray(),
        "#...##.....##.....#".toCharArray(),
        "#......#####......#".toCharArray(),
        "#......#...#......#".toCharArray(),
        "#......#...#......#".toCharArray(),
        "#......##D##......#".toCharArray(),
        "#.................#".toCharArray(),
        "#.###...#...###...#".toCharArray(),
        "#.................#".toCharArray(),
        "#.................#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };

    /**
     * Checks if a specified tile is a solid wall ('#').
     * Ghosts use this check so they are permitted to pass through ghost doors ('D').
     *
     * @param x column index
     * @param y row index
     * @return true if the tile contains a wall, false otherwise
     */
    public boolean isWall(int x, int y) {
        return layout[y][x] == '#';
    }

    /**
     * Checks if a specified tile is blocked for Pac-Man.
     * Blocks Pac-Man on both regular walls ('#') and the ghost house door ('D').
     *
     * @param x column index
     * @param y row index
     * @return true if the tile is a wall or door for Pac-Man, false otherwise
     */
    public boolean isWallForPacman(int x, int y) {
        return layout[y][x] == '#' || layout[y][x] == 'D';
    }

    /**
     * Checks if a specified tile is the ghost house door ('D').
     *
     * @param x column index
     * @param y row index
     * @return true if the tile is a door, false otherwise
     */
    public boolean isDoor(int x, int y) {
        return layout[y][x] == 'D';
    }

    /**
     * Checks if a specified tile currently contains an uncollected dot ('.').
     *
     * @param x column index
     * @param y row index
     * @return true if a dot exists at this tile, false otherwise
     */
    public boolean hasDot(int x, int y) {
        return layout[y][x] == '.';
    }

    /**
     * Consumes and removes a dot at the given coordinates, replacing it with an empty space (' ').
     *
     * @param x column index
     * @param y row index
     */
    public void collectDot(int x, int y) {
        if (hasDot(x, y)) {
            layout[y][x] = ' ';
        }
    }

    /**
     * Retrieves the character at a given maze position.
     *
     * @param x column index
     * @param y row index
     * @return the character representing the tile at (x, y)
     */
    public char getTile(int x, int y) {
        return layout[y][x];
    }

    /**
     * @return the total number of rows in the maze grid
     */
    public int getRows() {
        return layout.length;
    }

    /**
     * @return the total number of columns in the maze grid
     */
    public int getCols() {
        return layout[0].length;
    }

    /**
     * Scans the entire maze grid to check if any uncollected dots remain.
     * Used to evaluate the winning condition.
     *
     * @return true if at least one dot is left, false if all dots have been eaten (player wins)
     */
    public boolean hasDotsLeft() {
        for (int y = 0; y < layout.length; y++) {
            for (int x = 0; x < layout[y].length; x++) {
                if (layout[y][x] == '.') {
                    return true;
                }
            }
        }
        return false;
    }
}