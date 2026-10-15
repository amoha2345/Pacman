
package com.mycompany.util;

public class Maze {

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

    public boolean isWall(int x, int y) {
        return layout[y][x] == '#';
    }

    // Pac-Man cannot pass through the ghost door.
    public boolean isWallForPacman(int x, int y) {
        return layout[y][x] == '#' || layout[y][x] == 'D';
    }

    public boolean isDoor(int x, int y) {
        return layout[y][x] == 'D';
    }

    public boolean hasDot(int x, int y) {
        return layout[y][x] == '.';
    }

    public void collectDot(int x, int y) {
        if (hasDot(x, y)) {
            layout[y][x] = ' ';
        }
    }

    public char getTile(int x, int y) {
        return layout[y][x];
    }

    public int getRows() {
        return layout.length;
    }

    public int getCols() {
        return layout[0].length;
    }

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
