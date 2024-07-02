





package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main extends ApplicationAdapter {
	ShapeRenderer sr;
	ShapeRenderer sr2;
	Table table;

	Table[] Sim = new Table[15];


	Ball[] balls = new Ball[15];

	Ball[] ballSimSet1 = new Ball[15];
	Ball[] ballSimSet2 = new Ball[15];
	Ball[] ballSimSet3 = new Ball[15];
	Ball[] ballSimSet4 = new Ball[15];
	Ball[] ballSimSet5 = new Ball[15];
	Ball[] ballSimSet6 = new Ball[15];
	Ball[] ballSimSet7 = new Ball[15];
	Ball[] ballSimSet8 = new Ball[15];
	Ball[] ballSimSet9 = new Ball[15];
	Ball[] ballSimSet10 = new Ball[15];
	Ball[] ballSimSet11 = new Ball[15];
	Ball[] ballSimSet12 = new Ball[15];
	Ball[] ballSimSet13 = new Ball[15];
	Ball[] ballSimSet14 = new Ball[15];
	Ball[] ballSimSet15 = new Ball[15];

	ArrayList<Ball[]> ballSimSets = new ArrayList<>();
	
	Pocket[] pocketsSimSet1 = new Pocket[6];
	Pocket[] pocketsSimSet2 = new Pocket[6];
	Pocket[] pocketsSimSet3 = new Pocket[6];
	Pocket[] pocketsSimSet4 = new Pocket[6];
	Pocket[] pocketsSimSet5 = new Pocket[6];
	Pocket[] pocketsSimSet6 = new Pocket[6];
	Pocket[] pocketsSimSet7 = new Pocket[6];
	Pocket[] pocketsSimSet8 = new Pocket[6];
	Pocket[] pocketsSimSet9 = new Pocket[6];
	Pocket[] pocketsSimSet10 = new Pocket[6];
	Pocket[] pocketsSimSet11 = new Pocket[6];
	Pocket[] pocketsSimSet12 = new Pocket[6];
	Pocket[] pocketsSimSet13 = new Pocket[6];
	Pocket[] pocketsSimSet14 = new Pocket[6];
	Pocket[] pocketsSimSet15 = new Pocket[6];
	
	

	ArrayList<Pocket[]> simPockets = new ArrayList<>();
	
	
	Ball cueBallSim1;
	Ball cueBallSim2;
	Ball cueBallSim3;
	Ball cueBallSim4;
	Ball cueBallSim5;
	Ball cueBallSim6;
	Ball cueBallSim7;
	Ball cueBallSim8;
	Ball cueBallSim9;
	Ball cueBallSim10;
	Ball cueBallSim11;
	Ball cueBallSim12;
	Ball cueBallSim13;
	Ball cueBallSim14;
	Ball cueBallSim15;

	ArrayList<Ball> cueBalls = new ArrayList<>();




	Ball cueBall;
	Cursor cursor = new Cursor(10, 10, 10, Color.BROWN);

	ArrayList<Ball> pocketedBalls = new ArrayList<>();


	ShotLine shotLine;
	ShotLine reboundLine;
	Player player;

	Ai ai;


	int AiBallDistanceBonus = 2;



	@Override
	public void create() {

		int row1x = 45;
		int row1Y = 45;
		int row2Y = 270;
		int row3Y = row2Y + (row2Y - row1Y);
		int row4Y = row3Y + (row2Y - row1Y);


		sr = new ShapeRenderer();
		sr2 = new ShapeRenderer();
		table = new Table(row1x + (375*3), row3Y + (row2Y - row1Y), 300, 150, Color.GREEN);


		int x = 850;
		int y = 435;
		int j = 0;

		for (int i = 0; i < 15; i++) {
			if (i == 5 || i == 9 || i == 12 || i == 14) {
				j = j+1;
				x = x - 25;
				y = 460 + j * 12;
				if (i % 2 == 0) {
					balls[i] = new Ball(x, y, 9, 0, 0, false, Color.YELLOW);
				} else {
					balls[i] = new Ball(x, y, 9, 0, 0, false, Color.RED);
				}
			} else {
				y = y + 25;
				if (i % 2 == 0) {
					balls[i] = new Ball(x, y, 9, 0, 0, false, Color.YELLOW);
				} else {
					balls[i] = new Ball(x, y, 9, 0, 0, false, Color.RED);
				}
			}
		}

		balls[0].setCol(Color.RED);
		balls[7].setCol(Color.BLACK);


		cueBall = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);



		shotLine = new ShotLine(0, 0, 0, 0, 3, Color.BLUE);
		reboundLine = new ShotLine(0, 0, 0, 0, 3, Color.BLUE);

		player = new Player(Color.BLACK);
		ai = new Ai();


		ballSimSets.add(ballSimSet1);
		ballSimSets.add(ballSimSet2);
		ballSimSets.add(ballSimSet3);
		ballSimSets.add(ballSimSet4);
		ballSimSets.add(ballSimSet5);
		ballSimSets.add(ballSimSet6);
		ballSimSets.add(ballSimSet7);
		ballSimSets.add(ballSimSet8);
		ballSimSets.add(ballSimSet9);
		ballSimSets.add(ballSimSet10);
		ballSimSets.add(ballSimSet11);
		ballSimSets.add(ballSimSet12);
		ballSimSets.add(ballSimSet13);
		ballSimSets.add(ballSimSet14);
		ballSimSets.add(ballSimSet15);


		simPockets.add(pocketsSimSet1);
		simPockets.add(pocketsSimSet2);
		simPockets.add(pocketsSimSet3);
		simPockets.add(pocketsSimSet4);
		simPockets.add(pocketsSimSet5);
		simPockets.add(pocketsSimSet6);
		simPockets.add(pocketsSimSet7);
		simPockets.add(pocketsSimSet8);
		simPockets.add(pocketsSimSet9);
		simPockets.add(pocketsSimSet10);
		simPockets.add(pocketsSimSet11);
		simPockets.add(pocketsSimSet12);
		simPockets.add(pocketsSimSet13);
		simPockets.add(pocketsSimSet14);
		simPockets.add(pocketsSimSet15);


		cueBallSim1 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim2 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim3 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim4 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim5 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim6 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim7 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim8 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim9 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim10 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim11 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim12 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim13 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim14 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		cueBallSim15 = new Ball(300, 500, 12, 0, 0, false, Color.WHITE);
		
		cueBalls.add(cueBallSim1);
		cueBalls.add(cueBallSim2);
		cueBalls.add(cueBallSim3);
		cueBalls.add(cueBallSim4);
		cueBalls.add(cueBallSim5);
		cueBalls.add(cueBallSim6);
		cueBalls.add(cueBallSim7);
		cueBalls.add(cueBallSim8);
		cueBalls.add(cueBallSim9);
		cueBalls.add(cueBallSim10);
		cueBalls.add(cueBallSim11);
		cueBalls.add(cueBallSim12);
		cueBalls.add(cueBallSim13);
		cueBalls.add(cueBallSim14);
		cueBalls.add(cueBallSim15);



		for (int i = 0; i < 4; i++) {
			Sim[i] = new Table(row1x, row1Y, 300, 150, Color.GREEN);
			row1x = row1x + 375;
		}

		row1x = 45;
		for (int i = 4; i < 8; i++) {
			Sim[i] = new Table(row1x, row2Y, 300, 150, Color.GREEN);
			row1x = row1x + 375;
		}

		row1x = 45;
		for (int i = 8; i < 12; i++) {
			Sim[i] = new Table(row1x, row3Y, 300, 150, Color.GREEN);
			row1x = row1x + 375;
		}

		row1x = 45;
		for (int i = 12; i < 15; i++) {
			Sim[i] = new Table(row1x, row4Y, 300, 150, Color.GREEN);
			row1x = row1x + 375;
		}

		for(int i = 0; i<15; i++){
			initializeBallsForTable(Sim[i], ballSimSets.get(i));
		}

		for(int i = 0; i<15; i++){
			initialisePockets(Sim[i], simPockets.get(i));
		}

		for (int i = 0; i<15; i++){
			initialiseCueBalls(Sim[i], cueBalls.get(i));
		}



	}

	@Override
	public void render() {

		ScreenUtils.clear(0, 0, 0, 1);

		sr.begin(ShapeRenderer.ShapeType.Filled);



		table.draw(sr);


		for (int i = 0; i < 15; i++) {
			Sim[i].draw(sr);
		}




		CueballMovement();
		BallMovement();
		AiShot(ai.RandomRotation(), ai.RandomPower());
		tableEdgeCollision();
		shotPredictor();
		foul();
		//potBall();


		for (Ball pocketedBall : pocketedBalls) {
			pocketedBall.setxVel(0);
			pocketedBall.setyVel(0);
			pocketedBall.setX(4000);
			pocketedBall.setY(4000);
		}


		chooseColour();


		for (int i = 0; i < 15; i++) {
			balls[i].draw(sr);
		}
		cueBall.draw(sr);

		for(int j = 0; j<15; j++){
			for(int i = 0; i<15; i++) {
				ballSimSets.get(i)[j].draw(sr);
			}
		}

		for (int i = 0; i < 15; i++) {
			cueBalls.get(i).draw(sr);
		}

		for(int j = 0; j<6; j++){
			for(int i = 0; i<15; i++) {
				simPockets.get(i)[j].draw(sr);
			}
		}

		cursor.draw(sr);

		shotLine.draw(sr);
		reboundLine.draw(sr);

		sr.end();
	}




	/*public void initializeTables(int row1x, int row1Y, int row2Y, int row3Y, int row4Y) {
		for (int i = 0; i < 4; i++) {
			Sim[i] = new Table(row1x, row1Y, 300, 150, Color.GREEN);
			row1x = row1x + 375;
		}

		row1x = 45;
		for (int i = 4; i < 8; i++) {
			Sim[i] = new Table(row1x, row2Y, 300, 150, Color.GREEN);
			row1x = row1x + 375;
		}

		row1x = 45;
		for (int i = 8; i < 12; i++) {
			Sim[i] = new Table(row1x, row3Y, 300, 150, Color.GREEN);
			row1x = row1x + 375;
		}

		row1x = 45;
		for (int i = 12; i < 15; i++) {
			Sim[i] = new Table(row1x, row4Y, 300, 150, Color.GREEN);
			row1x = row1x + 375;
		}
	}

	 */


	public boolean checkBallCollision(Ball ball1, Ball ball2) {
		float dx = ball1.getX() - ball2.getX();
		float dy = ball1.getY() - ball2.getY();
		float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
		return distance <= ball1.getR() + ball2.getR();
	}


	public void CueballMovement() {

		cueBall.setY((cueBall.getY() + cueBall.getyVel()));
		cueBall.setX((cueBall.getX() + cueBall.getxVel()));


		if (cueBall.getxVel() != 0) {
			cueBall.setxVel(cueBall.getxVel() * 0.98f);
		}
		if (cueBall.getyVel() != 0) {
			cueBall.setyVel(cueBall.getyVel() * 0.98f);
		}


		if (Math.abs(cueBall.getxVel()) < 0.2f) {
			cueBall.setxVel(0);

		}
		if (Math.abs(cueBall.getyVel()) < 0.2f) {
			cueBall.setyVel(0);

		}

		if (Math.sqrt((Math.pow(cueBall.getxVel(), 2) + (Math.pow(cueBall.getyVel(), 2)))) < 0.5f) {
			cueBall.setyVel(0);
			cueBall.setxVel(0);
		}

	}


	public void BallMovement() {

		for (int i = 0; i < 15; i++) {

			balls[i].setY((balls[i].getY() + balls[i].getyVel()));
			balls[i].setX((balls[i].getX() + balls[i].getxVel()));


			if (balls[i].getxVel() != 0) {
				balls[i].setxVel(balls[i].getxVel() * 0.99f);
			}
			if (balls[i].getyVel() != 0) {
				balls[i].setyVel(balls[i].getyVel() * 0.99f);
			}

			if (Math.abs(balls[i].getxVel()) < 0.2f) {
				balls[i].setxVel(0);

			}
			if (Math.abs(balls[i].getyVel()) < 0.2f) {
				balls[i].setyVel(0);

			}

			if (Math.sqrt((Math.pow(balls[i].getxVel(), 2) + (Math.pow(balls[i].getyVel(), 2)))) < 0.5f) {
				balls[i].setyVel(0);
				balls[i].setxVel(0);
			}
		}

	}


	/*public void Shot() {

		if (cueBall.getxVel() == 0 && cueBall.getyVel() == 0 && checkBallsStationary()) {
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {


				float initialPosX = cueBall.getX();
				float initialPosY = cueBall.getY();

				int finalPosX = cursor.getX();
				int finalPosY = cursor.getY();


				int finalDistance = (int) Math.sqrt((Math.pow(finalPosX - initialPosX, 2) + Math.pow(finalPosY - initialPosY, 2)));

				int newYvel = Math.round((initialPosY - finalPosY) / 10);
				int newXvel = Math.round((initialPosX - finalPosX) / 10);


				int power = Math.round((float) finalDistance / 10);
				System.out.println(power);

				cueBall.setyVel(newYvel);
				cueBall.setxVel(newXvel);


			}
		}

		for (int i = 0; i < 15; i++) {
			for (int j = i+1; j < 15; j++) {
				if (checkBallCollision(balls[i], balls[j])) {

					float relativeVelX = balls[j].getxVel() - balls[i].getxVel();
					float relativeVelY = balls[j].getyVel() - balls[i].getyVel();

					float collisionNormalX = balls[j].getX() - balls[i].getX();
					float collisionNormalY = balls[j].getY() - balls[i].getY();
					float collisionNormalLength = (float) Math.sqrt(collisionNormalX * collisionNormalX + collisionNormalY * collisionNormalY);
					collisionNormalX /= collisionNormalLength;
					collisionNormalY /= collisionNormalLength;


					float dotProduct = relativeVelX * collisionNormalX + relativeVelY * collisionNormalY;
					float impulse = dotProduct; // Assuming same mass for both balls

					balls[i].setxVel(balls[i].getxVel() + impulse * collisionNormalX);
					balls[i].setyVel(balls[i].getyVel() + impulse * collisionNormalY);
					balls[i].setX(balls[i].getX()-collisionNormalX);
					balls[i].setY(balls[i].getY()-collisionNormalY);
					balls[j].setxVel(balls[j].getxVel() - impulse * collisionNormalX);
					balls[j].setyVel(balls[j].getyVel() - impulse * collisionNormalY);
					balls[j].setX(balls[j].getX()+collisionNormalX);
					balls[j].setY(balls[j].getY()+collisionNormalY);
				}
			}
		}


		for (int i = 0; i < 15; i++) {
			if (checkBallCollision(balls[i], cueBall)) {

				float relativeVelX = cueBall.getxVel() - balls[i].getxVel();
				float relativeVelY = cueBall.getyVel() - balls[i].getyVel();


				float collisionNormalX = cueBall.getX() - balls[i].getX();
				float collisionNormalY = cueBall.getY() - balls[i].getY();
				float collisionNormalLength = (float) Math.sqrt(collisionNormalX * collisionNormalX + collisionNormalY * collisionNormalY);
				collisionNormalX /= collisionNormalLength;
				collisionNormalY /= collisionNormalLength;


				float dotProduct = relativeVelX * collisionNormalX + relativeVelY * collisionNormalY;
				float impulse = 2 * dotProduct / (2+1);


				balls[i].setxVel(balls[i].getxVel() + impulse * collisionNormalX);
				balls[i].setyVel(balls[i].getyVel() + impulse * collisionNormalY);
				balls[i].setX(balls[i].getX()-collisionNormalX);
				balls[i].setY(balls[i].getY()-collisionNormalY);
				cueBall.setxVel(cueBall.getxVel() - impulse * collisionNormalX);
				cueBall.setyVel(cueBall.getyVel() - impulse * collisionNormalY);
				cueBall.setX(cueBall.getX()+collisionNormalX);
				cueBall.setY(cueBall.getY()+collisionNormalY);
			}
		}

		cursor.setX(Gdx.input.getX());
		cursor.setY(Gdx.graphics.getHeight() - Gdx.input.getY());
		ScreenUtils.clear(0, 0, 0, 1);



	}
	*/


	public void tableEdgeCollision() {

		if (cueBall.getY() + cueBall.getR() >= table.getTableEdge3Y()) {
			cueBall.setyVel(-(cueBall.getyVel()));
			cueBall.setY(table.getTableEdge3Y() - cueBall.getR());
			cueBall.setxVel((float) (0.85 * cueBall.getxVel()));
			cueBall.setyVel((float) (0.85 * cueBall.getyVel()));
		}

		if (cueBall.getY() - cueBall.getR() <= table.getTableEdge2Y() + table.getTableEdge2H()) {
			cueBall.setyVel(-(cueBall.getyVel()));
			cueBall.setY(table.getTableEdge2Y() + table.getTableEdge2H() + cueBall.getR());
			cueBall.setxVel((float) (0.85 * cueBall.getxVel()));
			cueBall.setyVel((float) (0.85 * cueBall.getyVel()));
		}

		if (cueBall.getX() + cueBall.getR() >= table.getTableEdge1X()) {
			cueBall.setxVel(-(cueBall.getxVel()));
			cueBall.setX(table.getTableEdge1X() - cueBall.getR());
			cueBall.setxVel((float) (0.85 * cueBall.getxVel()));
			cueBall.setyVel((float) (0.85 * cueBall.getyVel()));
		}

		if (cueBall.getX() - cueBall.getR() <= table.getTableEdge0X() + table.getTableEdge0W()) {
			cueBall.setxVel(-(cueBall.getxVel()));
			cueBall.setX(table.getTableEdge0X() + table.getTableEdge0W() + cueBall.getR());
			cueBall.setxVel((float) (0.85 * cueBall.getxVel()));
			cueBall.setyVel((float) (0.85 * cueBall.getyVel()));
		}


		for (int i = 0; i < 15; i++) {
			if (balls[i].getY() + balls[i].getR() >= table.getTableEdge3Y()) {
				balls[i].setyVel(-(balls[i].getyVel()));
				balls[i].setY(table.getTableEdge3Y() - balls[i].getR());
				balls[i].setxVel((float) (0.85 * balls[i].getxVel()));
				balls[i].setyVel((float) (0.85 * balls[i].getyVel()));
			}

			if (balls[i].getY() - balls[i].getR() <= table.getTableEdge2Y() + table.getTableEdge2H()) {
				balls[i].setyVel(-(balls[i].getyVel()));
				balls[i].setY(table.getTableEdge2Y() + table.getTableEdge2H() + balls[i].getR());
				balls[i].setxVel((float) (0.85 * balls[i].getxVel()));
				balls[i].setyVel((float) (0.85 * balls[i].getyVel()));
			}

			if (balls[i].getX() + balls[i].getR() >=  table.getTableEdge1X()) {
				balls[i].setxVel(-(balls[i].getxVel()));
				balls[i].setX( table.getTableEdge1X() - balls[i].getR());
				balls[i].setxVel((float) (0.85 * balls[i].getxVel()));
				balls[i].setyVel((float) (0.85 * balls[i].getyVel()));
			}

			if (balls[i].getX() - balls[i].getR() <= table.getTableEdge0X() + table.getTableEdge0W()) {
				balls[i].setxVel(-(balls[i].getxVel()));
				balls[i].setX(table.getTableEdge0X() + table.getTableEdge0W() + balls[i].getR());
				balls[i].setxVel((float) (0.85 * balls[i].getxVel()));
				balls[i].setyVel((float) (0.85 * balls[i].getyVel()));
			}
		}

	}


	public boolean checkPocketCollision(Pocket pocket, Ball ball) {
		float dx = pocket.getX() - ball.getX();
		float dy = pocket.getY() - ball.getY();
		float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
		return distance <= pocket.getR() + ball.getR();
	}

	/*public void potBall() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 15; j++) {
				if (checkPocketCollision(table.gettablepockets[i], balls[j])) {
					pocketedBalls.add(balls[j]);
				}
			}
			if (checkPocketCollision(pockets[i], cueBall)) {
				cueBall.setX(300);
				cueBall.setY(500);
				cueBall.setxVel(0);
				cueBall.setyVel(0);
			}
		}

	}

	 */

	public boolean checkBallsStationary() {
		for(int i = 0; i < balls.length; i++){
			if(balls[i].getxVel() > 0 || balls[i].getyVel() > 0){
				return false;
			}
		}
		if(cueBall.getyVel() > 0 || cueBall.getxVel() > 0){
			return false;
		}

		else {

			return true;
		}

	}

	public void shotPredictor() {
		float cueBallX = cueBall.getX();
		float cueBallY = cueBall.getY();
		float cursorX = cursor.getX();
		float cursorY = cursor.getY();


		float gradLine = (cursorY - cueBallY) / (cursorX - cueBallX);


		float C = cueBallY - gradLine * cueBallX;


		float topY = table.getY() + table.getH();
		float topX = (topY - C) / gradLine;


		float bottomY = table.getY();
		float bottomX = (bottomY - C) / gradLine;

		float leftX = table.getX();
		float leftY = (leftX * gradLine) + C;

		float rightX = table.getX() + table.getW();
		float rightY = (rightX * gradLine) + C;


		if (cueBallY > cursorY) {
			shotLine.setX2(topX);
			shotLine.setY2(topY);

		} else {
			shotLine.setX2(bottomX);
			shotLine.setY2(bottomY);
		}


		if (cueBallY > cursorY) {
			if (topX < rightX || topX > leftX) {
				reboundLine.setX2(topX + (topX - cueBallX));
				reboundLine.setY2(cueBallY);
				reboundLine.setX1(topX);
				reboundLine.setY1(topY);
			}


		}


		if (cueBallX > cursorX) {
			shotLine.setX2(rightX);
			shotLine.setY2(rightY);

			if (rightY < topY || rightY > bottomY) {
				reboundLine.setX2(cueBallX);
				reboundLine.setY2(rightY + (rightY - cueBallY));
				reboundLine.setX1(rightX);
				reboundLine.setY1(rightY);
			}

		} else {
			shotLine.setX2(leftX);
			shotLine.setY2(leftY);

			if (leftY < bottomY || leftY > bottomY) {
				reboundLine.setX2(cueBallX);
				reboundLine.setY2(leftY + (leftY - cueBallY));
				reboundLine.setX1(leftX);
				reboundLine.setY1(leftY);
			}
		}

		shotLine.setX1(cueBallX);
		shotLine.setY1(cueBallY);

		if (cueBallY > cursorY) {
			if (topX < rightX || topX > leftX) {
				reboundLine.setX2(topX + (topX - cueBallX));
				reboundLine.setY2(cueBallY);
				reboundLine.setX1(topX);
				reboundLine.setY1(topY);
			}


		} else {
			if (bottomX < rightX || bottomX > leftX) {
				reboundLine.setX2(bottomX + (bottomX - cueBallX));
				reboundLine.setY2(cueBallY);
				reboundLine.setX1(bottomX);
				reboundLine.setY1(bottomY);
			}
		}

	}


	public void chooseColour() {
		if (!pocketedBalls.isEmpty()) {
			player.setCol(pocketedBalls.get(0).getCol());
			if (player.getCol() == Color.RED) {
				System.out.println("red");
			}
			if (player.getCol() == Color.YELLOW) {
				System.out.println("yellow");
			}
		}
	}

	public boolean foul() {
		for (int i = 0; i < 15; i++) {
			if (checkBallCollision(cueBall, balls[i])) {
				if (player.getCol() == Color.YELLOW || player.getCol() == Color.RED) {
					if (balls[i].getCol() != player.getCol()) {
						System.out.println("Foul");
						return true;
					}
				}
			}
		}
		return false;
	}

	public void AiShot(double angle, double power) {


		ScreenUtils.clear(0, 0, 0, 1);

		if(checkBallsStationary()) {

			cueBall.setxVel(-(float) (power * Math.cos(angle)));
			cueBall.setyVel(-(float) (power * Math.sin(angle)));

		}

		for (int i = 0; i < 15; i++) {
			for (int j = i + 1; j < 15; j++) {
				if (checkBallCollision(balls[i], balls[j])) {

					float relativeVelX = balls[j].getxVel() - balls[i].getxVel();
					float relativeVelY = balls[j].getyVel() - balls[i].getyVel();

					float collisionNormalX = balls[j].getX() - balls[i].getX();
					float collisionNormalY = balls[j].getY() - balls[i].getY();
					float collisionNormalLength = (float) Math.sqrt(collisionNormalX * collisionNormalX + collisionNormalY * collisionNormalY);
					collisionNormalX /= collisionNormalLength;
					collisionNormalY /= collisionNormalLength;


					float dotProduct = relativeVelX * collisionNormalX + relativeVelY * collisionNormalY;
					float impulse = dotProduct; // Assuming same mass for both balls

					balls[i].setxVel(balls[i].getxVel() + impulse * collisionNormalX);
					balls[i].setyVel(balls[i].getyVel() + impulse * collisionNormalY);
					balls[i].setX(balls[i].getX() - collisionNormalX);
					balls[i].setY(balls[i].getY() - collisionNormalY);
					balls[j].setxVel(balls[j].getxVel() - impulse * collisionNormalX);
					balls[j].setyVel(balls[j].getyVel() - impulse * collisionNormalY);
					balls[j].setX(balls[j].getX() + collisionNormalX);
					balls[j].setY(balls[j].getY() + collisionNormalY);
				}
			}
		}


		for (int i = 0; i < 15; i++) {
			if (checkBallCollision(balls[i], cueBall)) {

				float relativeVelX = cueBall.getxVel() - balls[i].getxVel();
				float relativeVelY = cueBall.getyVel() - balls[i].getyVel();


				float collisionNormalX = cueBall.getX() - balls[i].getX();
				float collisionNormalY = cueBall.getY() - balls[i].getY();
				float collisionNormalLength = (float) Math.sqrt(collisionNormalX * collisionNormalX + collisionNormalY * collisionNormalY);
				collisionNormalX /= collisionNormalLength;
				collisionNormalY /= collisionNormalLength;


				float dotProduct = relativeVelX * collisionNormalX + relativeVelY * collisionNormalY;
				float impulse = 2 * dotProduct / (2 + 1);


				balls[i].setxVel(balls[i].getxVel() + impulse * collisionNormalX);
				balls[i].setyVel(balls[i].getyVel() + impulse * collisionNormalY);
				balls[i].setX(balls[i].getX() - collisionNormalX);
				balls[i].setY(balls[i].getY() - collisionNormalY);
				cueBall.setxVel(cueBall.getxVel() - impulse * collisionNormalX);
				cueBall.setyVel(cueBall.getyVel() - impulse * collisionNormalY);
				cueBall.setX(cueBall.getX() + collisionNormalX);
				cueBall.setY(cueBall.getY() + collisionNormalY);
			}
		}

		cursor.setX(Gdx.input.getX());
		cursor.setY(Gdx.graphics.getHeight() - Gdx.input.getY());
	}


	//policy

	public float getDistance(Ball ball1, Ball ball2) {
		float dx = ball1.getX() - ball2.getX();
		float dy = ball1.getY() - ball2.getY();
		float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
		return distance;
	}


	public int evaluate(){
		int evaluation = 1;

		for(int i = 0; i< balls.length; i++){
			for(int j = i+1; j< balls.length; j++){

				evaluation += getDistance(balls[i], balls[j]) *AiBallDistanceBonus;
			}
		}

		return evaluation;
	}

	public void createSim(){

	}




	public void initializeBallsForTable(Table table, Ball[] balls) {
		int baseX = (int) (table.getX() + (table.getW() * 0.75)); // Base X coordinate for the triangle
		int baseX2 = (int) (table.getX() + (table.getW() * 0.25));
		int baseY = (int) (table.getY() + table.getH() / 2);      // Base Y coordinate for the first ball

		// Distance between balls (adjust as necessary)
		float ballRadius = 6.75f;
		float ballDiameter = ballRadius * 2;
		double rowHeight = ballDiameter * Math.sqrt(3) / 2;

		int ballIndex = 0;

		for (int row = 0; row < 5; row++) {
			for (int col = 0; col <= row; col++) {
				float x = baseX + row * ballDiameter;
				float y = baseY + col * ballDiameter - row * ballDiameter / 2;

				Color color = (ballIndex % 2 == 0) ? Color.YELLOW : Color.RED;
				balls[ballIndex] = new Ball(x, y, ballRadius, 0, 0, false, color);
				ballIndex++;
			}
		}

		// Assign specific colors to certain balls if needed
		balls[0].setCol(Color.RED);    // Example: set first ball color
		balls[7].setCol(Color.BLACK);  // Example: set 8-ball color

	}

	public void initialisePockets(Table table, Pocket[] pockets){
		float x = table.getX();
		float row1y = table.getY();

		float row2y = row1y + table.getH();

		for(int i = 0; i< 3; i++){
			pockets[i] = new Pocket(x, row1y, 9, Color.BLACK);
			x+= (table.getW()/2);
		}

		float x2 = table.getX();

		for(int i = 3; i < 6; i++){
			pockets[i] = new Pocket(x2, row2y, 9, Color.BLACK);
			x2+= (table.getW()/2);
		}
	}

	public void initialiseCueBalls(Table table, Ball CueBall){

		int baseX2 = (int) (table.getX() + (table.getW() * 0.25));
		int baseY = (int) (table.getY() + table.getH() / 2);
		float ballRadius = 6.75f;


		CueBall = new Ball(baseX2, baseY, ballRadius, 0, 0, false, Color.WHITE);
	}


}
