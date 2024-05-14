
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


		CueballMovement();
		BallMovement();
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
		float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
		return distance <= ball1.getR() + ball2.getR();
	}



	public void CueballMovement() {

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

		balls[1].setxVel(5);
		balls[1].setyVel(5);


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
		}

		for (int i = 0; i < 15; i++) {
			for (int j = i+1; j < 15; j++) { // Avoid checking collisions twice
				if (checkBallCollision(balls[i], balls[j])) {
					// Calculate relative velocity
					float relativeVelX = balls[j].getxVel() - balls[i].getxVel();
					float relativeVelY = balls[j].getyVel() - balls[i].getyVel();

					// Calculate collision normal
					float collisionNormalX = balls[j].getX() - balls[i].getX();
					float collisionNormalY = balls[j].getY() - balls[i].getY();
					float collisionNormalLength = (float) Math.sqrt(collisionNormalX * collisionNormalX + collisionNormalY * collisionNormalY);
					collisionNormalX /= collisionNormalLength;
					collisionNormalY /= collisionNormalLength;

					// Calculate impulse
					float dotProduct = relativeVelX * collisionNormalX + relativeVelY * collisionNormalY;
					float impulse = 2 * dotProduct / (1 + 1); // Assuming same mass for both balls

					// Apply impulse to update velocities
					balls[i].setxVel(balls[i].getxVel() + impulse * collisionNormalX);
					balls[i].setyVel(balls[i].getyVel() + impulse * collisionNormalY);
					balls[j].setxVel(balls[j].getxVel() - impulse * collisionNormalX);
					balls[j].setyVel(balls[j].getyVel() - impulse * collisionNormalY);
				}
			}
		}


		for (int i = 0; i < 15; i++) {
			for (int j = i + 1; j < 15; j++) { // Avoid checking collisions twice
				if (checkBallCollision(balls[i], cueBall)) {
					// Calculate relative velocity
					float relativeVelX = cueBall.getxVel() - balls[i].getxVel();
					float relativeVelY = cueBall.getyVel() - balls[i].getyVel();

					// Calculate collision normal
					float collisionNormalX = cueBall.getX() - balls[i].getX();
					float collisionNormalY = cueBall.getY() - balls[i].getY();
					float collisionNormalLength = (float) Math.sqrt(collisionNormalX * collisionNormalX + collisionNormalY * collisionNormalY);
					collisionNormalX /= collisionNormalLength;
					collisionNormalY /= collisionNormalLength;

					// Calculate impulse
					float dotProduct = relativeVelX * collisionNormalX + relativeVelY * collisionNormalY;
					float impulse = 2 * dotProduct / (1 + 1); // Assuming same mass for both balls

					// Apply impulse to update velocities
					balls[i].setxVel(balls[i].getxVel() + impulse * collisionNormalX);
					balls[i].setyVel(balls[i].getyVel() + impulse * collisionNormalY);
					cueBall.setxVel(cueBall.getxVel() - impulse * collisionNormalX);
					cueBall.setyVel(cueBall.getyVel() - impulse * collisionNormalY);
				}
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


	@Override
	public void dispose () {

	}
}
