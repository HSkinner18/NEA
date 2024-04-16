
package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Pocket {
    private int r;
    private int x,y;
    private char plane;
    private Color col;

    public Pocket(int x, int y, int r, Color c){
        this.x=x;
        this.y=y;
        this.r=r;
        col=c;
    }
    public void draw(ShapeRenderer sr){
        sr.setColor(col);
        sr.circle(x,y,r);
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {

        this.y = y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public Color getCol() {
        return col;
    }

    public void setCol(Color col) {
        this.col = col;
    }
}
