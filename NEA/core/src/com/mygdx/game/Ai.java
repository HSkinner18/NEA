package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

public class Ai {

    double power;
    double rotation;
    public Ai(){

    }


    public double RandomPower(){
        double power = (Math.random() * 75 + 1);
        return power;
    }

    public double RandomRotation(){
        double rotation = (Math.random() * 2 * Math.PI);
        return rotation;
    }

}