package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

public class Ai {

    double power;
    double rotation;

    public Ai(float x, float y, float r, float yVel, float xVel, boolean potted, Color c){

        this.rotation=rotation;
        this.power=power;
    }


    public double RandomPower(){
        double randomPower = (Math.random() * 75 + 1);
        return randomPower;
    }

    public double RandomRotation(){
        double randomRotation = (Math.random() * 2 * Math.PI);
        return randomRotation;
    }


}