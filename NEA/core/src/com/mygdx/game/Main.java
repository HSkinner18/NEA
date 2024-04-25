


package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	ShapeRenderer sr;




	Table table;
	TableEdge[] tableEdges = new TableEdge[4];

	Ball[] balls = new Ball[15];
	Ball cueBall;
	Cursor cursor = new Cursor(10, 10, 10, Color.RED);

	Pocket[] pockets = new Pocket[6];



	int power = 0;

	@Override
	public void create() {
		sr = new ShapeRenderer();
		table = new Table(200, 300, 800, 400, Color.GREEN);

		tableEdges[0] = new TableEdge(table.getX() - 25, table.getY() - 25, 25, table.getH() + 50, Color.BROWN);  //left
		tableEdges[1] = new TableEdge(table.getX() + table.getW(), table.getY() - 25, 25, table.getH() + 50, Color.BROWN);  //right
		tableEdges[2] = new TableEdge(table.getX(), table.getY() - 25, table.getW(), 25, Color.BROWN);  //bottom
		tableEdges[3] = new TableEdge(table.getX(), table.getY() + table.getH(), table.getW(), 25, Color.BROWN); //top





		int x = 850;
		int y = 435;
		int j = 0;

		for (int i = 0; i < 15; i++) {
			if (i == 5 || i == 9 || i == 12 || i == 14) {
				j = j + 1;
				x = x - 25;
				y = 460 + j * 12;
				balls[i] = new Ball(x, y, 12, 0,0, Color.CYAN);
			} else {
				y = y + 25;
				balls[i] = new Ball(x, y, 12, 0,0, Color.CYAN);
			}
		}

		cueBall = new Ball(300, 500, 12,0,0, Color.WHITE);

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

		for(int tableRender = 0; tableRender < 4; tableRender++){
			tableEdges[tableRender].draw(sr);
		}


		table.draw(sr);

		ballMovement();

		mouseCheck();
		tableEdgeCollision();

		for (int i = 0; i < 15; i++) {
			balls[i].draw(sr);
		}
		cueBall.draw(sr);

		for (int i = 0; i < 6; i++) {
			pockets[i].draw(sr);
		}

		cursor.draw(sr);



		sr.end();
	}



	public boolean checkBallCollision(Ball ball1, Ball ball2) {
		float dx = ball1.getX() - ball2.getX();
		float dy = ball1.getY() - ball2.getY();
		float distance = (float) Math.sqrt(dx * dx + dy * dy);
		return distance <= ball1.getR() + ball2.getR();
	}

	public boolean checkCursorCollision(Ball cueBall, Cursor cursor){
		float dx = cueBall.getX() - cursor.getX();
		float dy = cueBall.getY() - cursor.getY();
		float distance = (float) Math.sqrt(dx * dx + dy * dy);
		return distance <= cueBall.getR() + cursor.getR();

	}

	public void ballMovement() {

		cueBall.setY((cueBall.getY() + cueBall.getyVel()));
		cueBall.setX((cueBall.getX() + cueBall.getxVel()));


		if (cueBall.getxVel() != 0) {
			cueBall.setxVel(cueBall.getxVel() * 0.99f);
		}
		if (cueBall.getyVel() != 0) {
			cueBall.setyVel(cueBall.getyVel() * 0.99f);
		}


		if (Math.abs(cueBall.getxVel()) < 0.2f) {
			cueBall.setxVel(0);

		}
		if (Math.abs(cueBall.getyVel()) < 0.2f) {
			cueBall.setyVel(0);

		}

		if(Math.sqrt ((Math.pow(cueBall.getxVel(), 2) + (Math.pow(cueBall.getyVel(), 2)))) < 0.5f){
			cueBall.setyVel(0);
			cueBall.setxVel(0);
		}

		for(int i = 0; i < 15; i++){
			balls[i].setY(balls[i].getY() + balls[i].getyVel());
			balls[i].setX(balls[i].getX() + balls[i].getxVel());

		}

	}




	public void tableEdgeCollision(){

		if(cueBall.getY() + cueBall.getR() >= tableEdges[3].getY()){
			cueBall.setyVel(-(cueBall.getyVel()));
			cueBall.setY(tableEdges[3].getY() - cueBall.getR());
			cueBall.setxVel((float) (0.8 * cueBall.getxVel()));
			cueBall.setyVel((float) (0.8 * cueBall.getyVel()));
		}

		if(cueBall.getY() - cueBall.getR()<= tableEdges[2].getY() + tableEdges[2].getH()){
			cueBall.setyVel(-(cueBall.getyVel()));
			cueBall.setY(tableEdges[2].getY()+ tableEdges[2].getH() + cueBall.getR());
			cueBall.setxVel((float) (0.8 * cueBall.getxVel()));
			cueBall.setyVel((float) (0.8 * cueBall.getyVel()));
		}

		if(cueBall.getX() + cueBall.getR() >= tableEdges[1].getX()){
			cueBall.setxVel(-(cueBall.getxVel()));
			cueBall.setX(tableEdges[1].getX() - cueBall.getR());
			cueBall.setxVel((float) (0.8 * cueBall.getxVel()));
			cueBall.setyVel((float) (0.8 * cueBall.getyVel()));
		}

		if(cueBall.getX() - cueBall.getR() <= tableEdges[0].getX() + tableEdges[0].getW()){
			cueBall.setxVel(-(cueBall.getxVel()));
			cueBall.setX(tableEdges[0].getX() + tableEdges[0].getW() + cueBall.getR());
			cueBall.setxVel((float) (0.8 * cueBall.getxVel()));
			cueBall.setyVel((float) (0.8 * cueBall.getyVel()));
		}

	}



	public void mouseCheck() {


		if (cueBall.getxVel() == 0 && cueBall.getyVel() == 0) {
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {


				float initialPosX = cueBall.getX();
				float initialPosY = cueBall.getY();

				int finalPosX = cursor.getX();
				int finalPosY = cursor.getY();


				int finalDistance = (int) Math.sqrt((Math.pow(finalPosX - initialPosX, 2) + Math.pow(finalPosY - initialPosY, 2)));

				int newYvel = Math.round((initialPosY - finalPosY) / 10);
				int newXvel = Math.round((initialPosX - finalPosX) / 10);


				int power = Math.round(finalDistance / 10);
				System.out.println(power);

				cueBall.setyVel(newYvel);
				cueBall.setxVel(newXvel);


			}

			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					if (checkBallCollision(balls[i], balls[j])) {
						balls[i].setxVel(balls[j].getxVel());
						balls[i].setyVel(balls[j].getyVel());
					}
				}
			}

			for (int i = 0; i < 15; i++) {
				if (checkBallCollision(cueBall, balls[i])) {
					balls[i].setxVel(cueBall.getxVel());
					balls[i].setyVel(cueBall.getyVel());
					System.out.println("colliding");
				}
			}

			for (int i = 0; i < 15; i++) {
				if(checkCursorCollision(balls[i], cursor)) {
					System.out.println("colliding");
				}


			}


			cursor.setX(Gdx.input.getX());
			cursor.setY(Gdx.graphics.getHeight() - Gdx.input.getY());
			ScreenUtils.clear(0, 0, 0, 1);

		}
	}

	@Override
	public void dispose () {

	}
}
