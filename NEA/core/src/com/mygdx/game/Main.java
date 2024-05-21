


package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	ShapeRenderer sr;
	ShapeRenderer sr2;

	int numberPotted = 0;
	Table table;
	TableEdge[] tableEdges = new TableEdge[4];

	Ball[] balls = new Ball[15];
	Ball cueBall;
	Cursor cursor = new Cursor(10, 10, 10, Color.BROWN);

	Pocket[] pockets = new Pocket[6];

	ShotLine shotLine;
	ShotLine reboundLine;
	int turn = 1;
	Player player;
	Player ai;


	@Override
	public void create() {
		sr = new ShapeRenderer();
		sr2 = new ShapeRenderer();
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
				if(i % 2 == 0){
					balls[i] = new Ball(x, y, 12, 0,0, false, Color.YELLOW);
				}
				else {
					balls[i] = new Ball(x, y, 12, 0,0, false,  Color.RED);
				}
			}
			else {
				y = y + 25;
				if(i % 2 == 0){
					balls[i] = new Ball(x, y, 12, 0,0, false, Color.YELLOW);
				}
				else {
					balls[i] = new Ball(x, y, 12, 0,0,false, Color.RED);
				}
			}
		}

		balls[0].setCol(Color.RED);
		balls[7].setCol(Color.BLACK);




		cueBall = new Ball(300, 500, 12,0,0, false, Color.WHITE);

		pockets[0] = new Pocket(table.getX() + table.getW(), table.getY() + table.getH(), 15, Color.BLACK);  //top right
		pockets[1] = new Pocket(table.getX() + (table.getW() / 2), table.getY() + table.getH(), 15, Color.BLACK); // top middle
		pockets[2] = new Pocket(table.getX(), table.getY() + table.getH(), 15, Color.BLACK); // top left
		pockets[3] = new Pocket(table.getX(), table.getY(), 15, Color.BLACK);  // bottom right
		pockets[4] = new Pocket(table.getX() + (table.getW() / 2), table.getY(), 15, Color.BLACK); //bottom middle
		pockets[5] = new Pocket(table.getX() + table.getW(), table.getY(), 15, Color.BLACK);  //bottom left

		shotLine = new ShotLine(0, 0, 0, 0, 3, Color.BLUE);
		reboundLine = new ShotLine(0, 0, 0, 0, 3, Color.BLUE);

		player = new Player(Color.BLACK);
		ai = new Player(Color.BLACK);
	}

	@Override
	public void render() {
		sr.begin(ShapeRenderer.ShapeType.Filled);

		for(int i = 0; i < 4; i++){
			tableEdges[i].draw(sr);
		}


		table.draw(sr);


		CueballMovement();
		BallMovement();
		Shot();
		tableEdgeCollision();
		shotPredictor();


		for(int i = 0; i<15; i++){
			potBall(balls[i]);

			if(balls[i].getPotted()){
				balls[i].setX(10000);
			}

			if(balls[7].getPotted()){
				ScreenUtils.clear(0, 0, 0, 1);
				balls[i].setY(-1000);
			}


		}

		potBall(cueBall);

		if(cueBall.getPotted()){
			cueBall.setX(300);
			cueBall.setY(500);
			cueBall.setxVel(0);
			cueBall.setyVel(0);
			cueBall.setPotted(false);
		}

		chooseColour();



		for (int i = 0; i < 15; i++) {
			balls[i].draw(sr);
		}
		cueBall.draw(sr);

		for (int i = 0; i < 6; i++) {
			pockets[i].draw(sr);
		}

		cursor.draw(sr);

		shotLine.draw(sr);
		reboundLine.draw(sr);

		sr.end();
	}



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


	public void Shot() {

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


		for(int i=0; i<15; i++){
			if(balls[i].getY() + balls[i].getR() >= tableEdges[3].getY()){
				balls[i].setyVel(-(balls[i].getyVel()));
				balls[i].setY(tableEdges[3].getY() - balls[i].getR());
				balls[i].setxVel((float) (0.8 * balls[i].getxVel()));
				balls[i].setyVel((float) (0.8 * balls[i].getyVel()));
			}

			if(balls[i].getY() - balls[i].getR()<= tableEdges[2].getY() + tableEdges[2].getH()){
				balls[i].setyVel(-(balls[i].getyVel()));
				balls[i].setY(tableEdges[2].getY()+ tableEdges[2].getH() + balls[i].getR());
				balls[i].setxVel((float) (0.8 * balls[i].getxVel()));
				balls[i].setyVel((float) (0.8 * balls[i].getyVel()));
			}

			if(balls[i].getX() + balls[i].getR() >= tableEdges[1].getX()){
				balls[i].setxVel(-(balls[i].getxVel()));
				balls[i].setX(tableEdges[1].getX() - balls[i].getR());
				balls[i].setxVel((float) (0.8 * balls[i].getxVel()));
				balls[i].setyVel((float) (0.8 * balls[i].getyVel()));
			}

			if(balls[i].getX() - balls[i].getR() <= tableEdges[0].getX() + tableEdges[0].getW()){
				balls[i].setxVel(-(balls[i].getxVel()));
				balls[i].setX(tableEdges[0].getX() + tableEdges[0].getW() + balls[i].getR());
				balls[i].setxVel((float) (0.8 * balls[i].getxVel()));
				balls[i].setyVel((float) (0.8 * balls[i].getyVel()));
			}
		}

	}


	public boolean checkPocketCollision(Pocket pocket, Ball ball) {
		float dx = pocket.getX() - ball.getX();
		float dy = pocket.getY() - ball.getY();
		float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
		return distance <= pocket.getR() + ball.getR();
	}

	public void potBall(Ball ball){
		for(int i = 0; i<6; i++){
			if(checkPocketCollision(pockets[i], ball)){
				ball.setPotted(true);
				numberPotted +=1;
			}
		}
	}

	public boolean checkBallsStationary(){
		for(int i = 0; i<15; i++){
			if(balls[i].getxVel() != 0 && balls[i].getyVel() != 0){
				return false;
			}
		}
		return true;
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


		if (cueBallY > cursorY) {
			shotLine.setX2(topX);
			shotLine.setY2(topY);
		}
		else {
			shotLine.setX2(bottomX);
			shotLine.setY2(bottomY);
		}

		shotLine.setX1(cueBallX);
		shotLine.setY1(cueBallY);

		if (cueBallY > cursorY) {
			reboundLine.setX2(topX + (topX - cueBallX));
			reboundLine.setY2(cueBallY);
			reboundLine.setX1(topX);
			reboundLine.setY1(topY);

		}
		else {
			reboundLine.setX2(bottomX + (bottomX - cueBallX));
			reboundLine.setY2(cueBallY);
			reboundLine.setX1(bottomX);
			reboundLine.setY1(bottomY);
		}
	}

	public void chooseColour(){
		for(int i = 0; i < 15; i++){
			if(balls[i].getPotted() && numberPotted < 1){
					player.setCol(balls[i].getCol());
					break;
			}
		}
		if(player.getCol() == Color.BLACK){
			System.out.println("black");
		}
		if(player.getCol() == Color.RED){
			System.out.println("red");
		}
		if(player.getCol() == Color.YELLOW){
			System.out.println("yellow");
		}
	}
}
