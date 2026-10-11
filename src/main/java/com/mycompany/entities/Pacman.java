/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.entities;

import com.mycompany.util.Maze;

public class Pacman {

    private int x;
    private int y;

    public Pacman(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void move(int dx, int dy, Maze maze) {
        int newX = x + dx;
        int newY = y + dy;

        if (newX >= 0 && newX < maze.getCols()
                && newY >= 0 && newY < maze.getRows()
                && !maze.isWall(newX, newY)) {

            x = newX;
            y = newY;
            maze.collectDot(x, y);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}