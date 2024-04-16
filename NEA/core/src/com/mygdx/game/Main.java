package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	ShapeRenderer sr;

	Table table;
	TableEdge tableEdge;

	Ball[] balls = new Ball[15];
	Ball cueBall;

	Pocket[] pockets = new Pocket[6];

	PowerMeter powerMeterFront;
	PowerMeter PowerMeterBack;

	int power = 0;

	@Override
	public void create() {
		sr = new ShapeRenderer();
		table = new Table(200, 300, 800, 400, Color.GREEN);
		tableEdge = new TableEdge(table.getX() - 25, table.getY() - 25, table.getW() + 50, table.getH() + 50, Color.BROWN);

		powerMeterFront = new PowerMeter(200, 100, 800, 15, Color.RED);
		PowerMeterBack = new PowerMeter(200, 100, 800, 15, Color.GREEN);

		int x = 850;
		int y = 435;
		int j = 0;

		for (int i = 0; i < 15; i++) {
			if (i == 5 || i == 9 || i == 12 || i == 14) {
				j = j + 1;
				x = x - 25;
				y = 460 + j * 12;
				balls[i] = new Ball(x, y, 12, 0, Color.CYAN);
			} else {
				y = y + 25;
				balls[i] = new Ball(x, y, 12, 0,Color.CYAN);
			}
		}

		cueBall = new Ball(300, 500, 12,0, Color.WHITE);

		pockets[0] = new Pocket(table.getX() + table.getW(), table.getY() + table.getH(), 15, Color.BLACK);  //top right
		pockets[1] = new Pocket(table.getX() + (table.getW() / 2), table.getY() + table.getH(), 15, Color.BLACK); // top middle
		pockets[2] = new Pocket(table.getX(), table.getY() + table.getH(), 15, Color.BLACK); // top left
		pockets[3] = new Pocket(table.getX(), table.getY(), 15, Color.BLACK);  // bottom right
		pockets[4] = new Pocket(table.getX() + (table.getW() / 2), table.getY(), 15, Color.BLACK); //bottom middle
		pockets[5] = new Pocket(table.getX() + table.getW(), table.getY(), 15, Color.BLACK);  //bottom left


	}

	@Override
	public void render() {
		sr.begin(ShapeRenderer.ShapeType.Filled);
		tableEdge.draw(sr);
		table.draw(sr);

		power();
		ballMovement();
		collision();

		for (int i = 0; i < 15; i++) {
			balls[i].draw(sr);
		}
		cueBall.draw(sr);

		for (int i = 0; i < 6; i++) {
			pockets[i].draw(sr);
		}

		PowerMeterBack.draw(sr);
		powerMeterFront.draw(sr);

		sr.end();
	}


	public int power(){
		int power = 0;
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			if(power < 10){
				power = power+1;
				powerMeterFront.setW(powerMeterFront.getW() - 80);
				System.out.println(power);
			}

		}

		return power;
	}
	public boolean checkBallCollision(Ball ball1, Ball ball2) {
		float dx = ball1.getX() - ball2.getX();
		float dy = ball1.getY() - ball2.getY();
		float distance = (float) Math.sqrt(dx * dx + dy * dy);
		return distance <= ball1.getR() + ball2.getR();
	}

	public void ballMovement(){
		for(int i = 0; i< 15; i++){
			balls[i].setX(balls[i].getX() + balls[i].getV());
		}
		cueBall.setX(cueBall.getX() + cueBall.getV());
	}

	public void collision(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			int queueVel = power * 2;
			float queueMass = 0.5f;
			float ballMass = 0.16f;

			float queueMomentum = queueMass * queueVel;
			float cueballVel = queueMomentum / ballMass;
			cueBall.setV(Math.round(cueballVel));

			for(int k = 0; k<15; k++){
				if(checkBallCollision(cueBall, balls[k])){
					balls[k].setV(cueBall.getV());
				}
			}

			for(int i = 0; i < 15; i++) {
				for(int j = 0; j < 15; j++) {
					if (checkBallCollision(balls[i], balls[j])){
						balls[j].setV(balls[i].getV());
					}
				}
			}
		}
	}

	@Override
	public void dispose () {

	}
}