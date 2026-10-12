
package com.mycompany.entities;

import java.awt.Color;

public class Ghost {

    private int x;
    private int y;
    private Color color;

    public Ghost(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
