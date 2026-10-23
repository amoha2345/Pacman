
package com.mycompany.entities;

import java.awt.Color;

/**
 * Represents a Ghost entity in the game.
 * Stores the ghost's grid position and display color.
 */
public class Ghost {

    private int x;
    private int y;
    private Color color;

    /**
     * Constructs a Ghost at a specified grid position with a distinct color.
     *
     * @param x initial column coordinate
     * @param y initial row coordinate
     * @param color the display color of the ghost (e.g., Color.RED for Blinky)
     */
    public Ghost(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * @return current column coordinate (X) of the ghost
     */
    public int getX() {
        return x;
    }

    /**
     * @return current row coordinate (Y) of the ghost
     */
    public int getY() {
        return y;
    }

    /**
     * @return display color of the ghost
     */
    public Color getColor() {
        return color;
    }

    /**
     * Updates the ghost's position to new coordinates.
     *
     * @param x the new column coordinate
     * @param y the new row coordinate
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }
}