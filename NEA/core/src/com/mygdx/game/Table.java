package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Table {
    private int w,h;
    private int x,y;
    private char plane;
    private Color col;
    
    TableEdge[] tableEdges = new TableEdge[4];

    public Table(int x, int y, int w, int h, Color c){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        col=c;
        tableEdges[0] = new TableEdge(x - 25, y - 25, 25, h + 50, Color.BROWN);  //left
        tableEdges[1] = new TableEdge(x + w, y - 25, 25, h + 50, Color.BROWN);  //right
        tableEdges[2] = new TableEdge(x, y - 25, w, 25, Color.BROWN);  //bottom
        tableEdges[3] = new TableEdge(x, y + h, w, 25, Color.BROWN); //top

    }
    public void draw(ShapeRenderer sr){
        sr.setColor(col);
        sr.rect(x,y,w,h);

        for(int i = 0; i< 4; i++){
            tableEdges[i].draw(sr);
        }

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

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }


    public Color getCol() {
        return col;
    }

    public void setCol(Color col) {
        this.col = col;
    }
    
    public float getTableEdge0Y(){
        return tableEdges[0].getY();
    }

    public float getTableEdge0X(){
        return tableEdges[0].getX();
    }

    public float getTableEdge1Y(){
        return tableEdges[1].getY();
    }

    public float getTableEdge1X(){
        return tableEdges[1].getX();
    }

    public float getTableEdge2Y(){
        return tableEdges[2].getY();
    }

    public float getTableEdge2X(){
        return tableEdges[2].getX();
    }

    public float getTableEdge3Y(){
        return tableEdges[3].getY();
    }

    public float getTableEdge3X(){
        return tableEdges[3].getX();
    }
    
    public float getTableEdge0H(){
        return tableEdges[0].getH();
    }

    public float getTableEdge1H(){
        return tableEdges[1].getH();
    }
    public float getTableEdge2H(){
        return tableEdges[2].getH();
    }
    public float getTableEdge3H(){
        return tableEdges[3].getH();
    }


    public float getTableEdge0W(){
        return tableEdges[0].getW();
    }

    public float getTableEdge1W(){
        return tableEdges[1].getW();
    }
    public float getTableEdge2W(){
        return tableEdges[2].getW();
    }
    public float getTableEdge3W(){
        return tableEdges[3].getW();
    }
}
