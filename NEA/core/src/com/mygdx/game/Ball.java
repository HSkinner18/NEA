


package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ball {
    private int r;
    private float x,y;
    private float yVel, xVel;
    private char plane;

    private boolean potted;
    private Color col;

    public Ball(int x, int y, int r, float yVel, float xVel, boolean potted, Color c){
        this.x=x;
        this.y=y;
        this.r=r;
        this.yVel=yVel;
        this.xVel=xVel;
        this.potted=potted;
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

    public float getyVel() {
        return yVel;
    }

    public void setyVel(float yVel) {
        this.yVel = yVel;
    }

    public float getxVel() {
        return xVel;
    }

    public void setxVel(float xVel) {
        this.xVel = xVel;
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

    public boolean getPotted(){
        return potted;
    }

    public void setPotted(boolean potted){
        this.potted = potted;
    }

    public void setCol(Color col) {
        this.col = col;
    }
}
