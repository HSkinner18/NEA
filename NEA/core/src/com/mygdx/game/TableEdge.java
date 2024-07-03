
package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TableEdge {
    private float w,h;
    private float x,y;
    private char plane;
    private Color col;

    public TableEdge(float x, float y, float w, float h, Color c){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        col=c;
    }
    public void draw(ShapeRenderer sr){
        sr.setColor(col);
        sr.rect(x,y,w,h);
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

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }


    public Color getCol() {
        return col;
    }

    public void setCol(Color col) {
        this.col = col;
    }
}
