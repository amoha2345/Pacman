/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.util;


public class Maze {

    private final char[][] layout = {
        "###################".toCharArray(),
        "#.................#".toCharArray(),
        "#.###...#...###...#".toCharArray(),
        "#.................#".toCharArray(),
        "#...##.....##.....#".toCharArray(),
        "#.................#".toCharArray(),
        "#.###...#...###...#".toCharArray(),
        "#.................#".toCharArray(),
        "#...##.....##.....#".toCharArray(),
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