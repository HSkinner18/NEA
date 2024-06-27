
package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

public class Player {
    private Color playerCol;

    public Player(Color c){
        playerCol=c;

    }

    public Color getCol() {
        return playerCol;
    }

    public void setCol(Color playerCol) {
        this.playerCol = playerCol;
    }
}
