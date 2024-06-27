

package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ShotLine {
    private float x1,x2;
    private float y1,y2;
    private float w;
    private Color col;

    public ShotLine(float x1, float y1, float x2, float y2, float w, Color c){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        this.w=w;
        col=c;
    }
    public void draw(ShapeRenderer sr){
        sr.setColor(col);
        sr.rectLine(x1,y1,x2,y2,w);
    }
    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public float getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public Color getCol() {
        return col;
    }

    public void setCol(Color col) {
        this.col = col;
    }

}
