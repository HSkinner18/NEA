

package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private float r;
    private float x,y;
    private float xVel, yVel;
    private char plane;
    private Color col;
    Vector2 previousPosition;
    

    public Player(float x, float y, float r, float xVel, float yVel, Color c){
        previousPosition = new Vector2(getX(), getY());
        this.x=x;
        this.y=y;
        this.r=r;
        this.yVel=yVel;
        this.xVel=xVel;
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

    public float getYvel() {
        return yVel;
    }

    public void setYvel(float yvel) {

        this.yVel = yvel;
    }

    public float getXvel() {
        return xVel;
    }

    public void setXvel(float Xvel) {

        this.xVel = Xvel;
    }

    public boolean hasMoved(){
        if(previousPosition.x != getX() || previousPosition.y != getY()){
            previousPosition.x = getX();
            previousPosition.y = getY();
            return true;
        }
        return false;
    }
}
