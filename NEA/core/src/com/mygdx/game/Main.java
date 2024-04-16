package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	ShapeRenderer sr;

	Table table;
	TableEdge tableEdge;

	Ball[] balls = new Ball[15];
	Pocket[] pockets = new Pocket[6];
	
	@Override
	public void create () {
		sr = new ShapeRenderer();
		table = new Table(200, 300, 800, 400, Color.GREEN);
		tableEdge = new TableEdge(table.getX() - 25, table.getY() - 25, table.getW() + 50, table.getH() + 50, Color.BROWN);


		int x = 850;
		int y = 435;
		int j = 0;

		for (int i = 0; i < 15; i++) {
			if(i == 5 || i == 9 || i == 12 || i == 14){
				j = j +1;
				x = x - 25;
				y = 460 + j*12;
				balls[i] = new Ball(x, y, 12, Color.CYAN);
			}
			else{
				y = y+25;
				balls[i] = new Ball(x, y, 12, Color.CYAN);
			}
		}

		pockets[0] = new Pocket(table.getX() + table.getW(), table.getY() + table.getH(), 15, Color.BLACK );
		pockets[1] = new Pocket(table.getX() + (table.getW() / 2), table.getY() + table.getH(), 15, Color.BLACK );
		pockets[2] = new Pocket(table.getX(), table.getY() + table.getH(), 15, Color.BLACK );
		pockets[3] = new Pocket(table.getX(), table.getY() + table.getH(), 15, Color.BLACK );
		pockets[4] = new Pocket(table.getX(), table.getY() + table.getH(), 15, Color.BLACK );
		pockets[5] = new Pocket(table.getX(), table.getY() + table.getH(), 15, Color.BLACK );


	}

	@Override
	public void render () {
		sr.begin(ShapeRenderer.ShapeType.Filled);
		tableEdge.draw(sr);
		table.draw(sr);

		for(int i = 0; i < 15; i++){
			balls[i].draw(sr);
		}


		sr.end();
	}
	
	@Override
	public void dispose () {

	}
}
