
package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Pocket {
    private float r;
    private float x,y;
    private char plane;
    private Color col;

    public Pocket(float x, float y, float r, Color c){
        this.x=x;
        this.y=y;
        this.r=r;
        col=c;
    }
    public void draw(ShapeRenderer sr){
        sr.setColor(col);
        sr.circle(x,y,r);
    }
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {

        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public Color getCol() {
        return col;
    }

    public void setCol(Color col) {
        this.col = col;
    }
}
