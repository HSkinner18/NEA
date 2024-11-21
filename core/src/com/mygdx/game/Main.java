
package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.bullet.collision._btMprSimplex_t;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashMap;

import java.util.*;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Main extends ApplicationAdapter {

	private final float UPDATE_TIME = 1/120f;
	float timer;

	Scanner s = new Scanner(System.in);

	private String roomCode;

	Ball[] balls;
	private Socket socket;
	String playerId;
	String cueballId;

	Player player;
	HashMap<String, Player> players;
	HashMap<String, Ball> cueballs;
	HashMap<String, Ball[]> ballSets;
	ShapeRenderer sr;
	ShapeRenderer sr2;
	PoolTable table;

	Sound sound;
	Sound music;
	Sound ambiance;


	Ball cueBall;
	Cursor cursor = new Cursor(10, 10, 10, Color.BROWN);

	ArrayList<Ball> pocketedBalls = new ArrayList<>();
	Pocket[] pockets = new Pocket[6];


	ShotLine reboundLine;
	Player player1;

	Player player2;

	Stage stage;


	Skin skin;

	Boolean clicked = false;



	@Override
	public void create() {

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);


		sr= new ShapeRenderer();
		connectSocket();
		configSocketEvents();
		players = new HashMap<String, Player>();
		cueballs = new HashMap<String, Ball>();
		ballSets = new HashMap<String, Ball[]>();


		sound = Gdx.audio.newSound((Gdx.files.internal("BallNoise.mp3")));
		music = Gdx.audio.newSound((Gdx.files.internal("BackgroundMusic.mp3")));
		ambiance = Gdx.audio.newSound((Gdx.files.internal("AmbientPub.mp3")));

		music.loop();


		ambiance.loop(1f);


		// start of buttons

		skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		Table root = new Table();
		root.setFillParent(true);
		stage.addActor(root);


		final TextButton create = new TextButton("Create", skin);
		root.add(create).spaceBottom(50).width(750).height(100);

		root.row();
		root.defaults().reset();
		TextButton join = new TextButton("Join", skin);
		root.add(join).spaceBottom(50).width(750).height(100);

		root.row();
		root.defaults().reset();
		TextField codeInput = new TextField("", skin);
		root.add(codeInput).spaceBottom(50).width(200).height(25);


		root.row();
		root.defaults().reset();
		TextButton rules = new TextButton("Rules", skin);
		root.add(rules).spaceBottom(50).width(750).height(100);




		create.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				clicked = true;
				createGame();

			}
		});

		join.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				clicked = true;
				joinGame(codeInput.getText());
			}
		});




		//end of buttons

		int row1x = 45;
		int row1Y = 45;
		int row2Y = 270;
		int row3Y = row2Y + (row2Y - row1Y);
		int row4Y = row3Y + (row2Y - row1Y);


		sr = new ShapeRenderer();
		sr2 = new ShapeRenderer();
		table = new PoolTable(200, 175, 1100, 550, Color.GREEN);




		reboundLine = new ShotLine(0, 0, 0, 0, 3, Color.BLUE);



		initialisePockets(table, pockets);
		initialiseBalls();


		/*int Help_Guides = 12;
		int row_height = Gdx.graphics.getWidth() / 12;
		int col_width = Gdx.graphics.getWidth() / 12;

		Skin mySkin = new Skin(Gdx.files.internal(""));

		TextButton button2 = new TextButton("Text Button", mySkin, "small");
		button2.setSize(col_width*4, row_height);
		button2.setPosition(col_width*7,Gdx.graphics.getHeight()-row_height*3);
		button2.addListener(new InputListener(){
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				outputLabel.setText("Press a Button");
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				outputLabel.setText("Pressed Text Button");
				return true;
			}
		});
		stage.addActor(button2);

		 */




	}

	@Override
	public void render() {

		ScreenUtils.clear(0, 0, 0, 1);
		handleInput(Gdx.graphics.getDeltaTime());
		updateServer(Gdx.graphics.getDeltaTime());
		stage.act();
		stage.draw();


		sr.begin(ShapeRenderer.ShapeType.Filled);



		if(clicked) {
			table.draw(sr);

			if (player != null) {
				player.draw(sr);
			}

			if (cueBall != null) {
				cueBall.draw(sr);
			}


			for (HashMap.Entry<String, Player> entry : players.entrySet()) {
				entry.getValue().draw(sr);
			}

			for (HashMap.Entry<String, Ball> entry : cueballs.entrySet()) {
				entry.getValue().draw(sr);
			}


			for (int i = 0; i < 15; i++) {
				balls[i].draw(sr);
			}

			for (int i = 0; i < pockets.length; i++) {
				pockets[i].draw(sr);
			}


			BallMovement();
			tableEdgeCollision();
			CueballMovement();
			Shot();

			//potBall();


			for (Ball pocketedBall : pocketedBalls) {
				pocketedBall.setxVel(0);
				pocketedBall.setyVel(0);
				pocketedBall.setX(4000);
				pocketedBall.setY(4000);
			}

			cursor.draw(sr);
			reboundLine.draw(sr);

		}
		sr.end();




	}




	public boolean checkBallCollision(Ball ball1, Ball ball2) {
		float dx = ball1.getX() - ball2.getX();
		float dy = ball1.getY() - ball2.getY();
		float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
		return distance <= ball1.getR() + ball2.getR();
	}


	public void CueballMovement() {

		if (cueBall != null) {
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

	}

	public void BallMovement() {

		for (int i = 0; i < 15; i++) {

			if(balls[i] != null) {

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

	}


	public void Shot() {

		if (cueBall != null) {
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
				for (int j = i + 1; j < 15; j++) {
					if (checkBallCollision(balls[i], balls[j])) {
						sound.play(2f);

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
					sound.play(2f);

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
			ScreenUtils.clear(0, 0, 0, 1);


		}
	}


	public void tableEdgeCollision() {

		if (cueBall != null) {


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
				if (balls[i] != null) {
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

					if (balls[i].getX() + balls[i].getR() >= table.getTableEdge1X()) {
						balls[i].setxVel(-(balls[i].getxVel()));
						balls[i].setX(table.getTableEdge1X() - balls[i].getR());
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
			if(balls[i] != null) {
				if (balls[i].getxVel() > 0 || balls[i].getyVel() > 0) {
					return false;
				}
			}
		}
		if(cueBall.getyVel() > 0 || cueBall.getxVel() > 0){
			return false;
		}

		else {

			return true;
		}

	}

	/*public void shotPredictor() {
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


		/*if (cueBallY > cursorY) {
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




	//policy

	public float getDistance(Ball ball1, Ball ball2) {
		float dx = ball1.getX() - ball2.getX();
		float dy = ball1.getY() - ball2.getY();
		float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
		return distance;
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


	 */


	public void initialisePockets(PoolTable table, Pocket[] pockets){
		float x = table.getX();
		float row1y = table.getY();

		float row2y = row1y + table.getH();

		for(int i = 0; i< 3; i++){
			pockets[i] = new Pocket(x, row1y, 17, Color.BLACK);
			x+= (table.getW()/2);
		}

		float x2 = table.getX();

		for(int i = 3; i < 6; i++){
			pockets[i] = new Pocket(x2, row2y, 17, Color.BLACK);
			x2+= (table.getW()/2);
		}
	}







	public void handleInput(float dt) {
		if (player != null) {
			float playerWidth = player.getR()*2;
			float playerHeight = player.getR()*2;

			float rectX = 225;
			float rectY = 200;
			float rectWidth = 1150;
			float rectHeight = 600;

			float newX = player.getX();
			float newY = player.getY();

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				newX += (-400 * dt);
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				newX += (400 * dt);
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				newY += (-400 * dt);
			} else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				newY += (400 * dt);
			}

			if (!isColliding(newX, newY, playerWidth, playerHeight, rectX, rectY, rectWidth, rectHeight)) {
				player.setX(newX);
				player.setY(newY);
			}
		}
	}

	private boolean isColliding(float x, float y, float width, float height, float rectX, float rectY, float rectWidth, float rectHeight) {
		return x < rectX + rectWidth && x + width > rectX && y < rectY + rectHeight && y + height > rectY;
	}

	public void initialiseBalls() {

		balls = new Ball[15];  // Assuming standard 15-ball pool

		// Define the starting position for the triangle (for the apex ball)
		float startX = table.getX() + table.getW() * 0.75f;  // 3/4th of the table width
		float startY = table.getY() + (float) table.getH() / 2;     // Middle of the table height

		float ballRadius = 15f;  // Assuming the radius of each ball is 10
		float rowSpacing = (float) (ballRadius * Math.sqrt(3));  // Vertical distance between rows

		int ballIndex = 0;

		// Place balls row by row in a triangle shape
		for (int row = 0; row < 5; row++) {
			// Calculate X position for each row, with balls shifting left by half a ball each row
			float rowX = startX - row * ballRadius;

			// Calculate Y position for the first ball in the row
			float rowY = startY - (row * rowSpacing / 2);

			// Place balls in the current row
			for (int i = 0; i <= row; i++) {
				if(ballIndex % 2 == 0) {
					balls[ballIndex] = new Ball(rowX, rowY + i * rowSpacing, ballRadius, 0, 0, false, Color.YELLOW);
					ballIndex++;
				}
				else{
					balls[ballIndex] = new Ball(rowX, rowY + i * rowSpacing, ballRadius, 0, 0, false, Color.RED);
					ballIndex++;
				}
			}
		}
	}


	public void connectSocket() {
		try {
			socket = IO.socket("http://localhost:8080");
			socket.connect();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void configSocketEvents() {
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
				player = new Player(0, 0, 50, 0, 0, Color.BLUE);
				cueBall = new Ball(300, 500, 15f, 0, 0, false, Color.WHITE);

			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					playerId = data.getString("id");
					Gdx.app.log("SocketIO", "My ID: " + playerId);
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting ID");
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String playerId = data.getString("id");
					Gdx.app.log("SocketIO", "New Player Connect: " + playerId);
					players.put(playerId, new Player(0, 0, 50, 0, 0, Color.CORAL));
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting New PlayerID");
				}
			}
		}).on("newCueBall", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					String cueballId = data.getString("id");
					cueballs.put(cueballId, new Ball(300, 500, 50, 0, 0, false ,Color.WHITE));
				}catch(Exception e){
					Gdx.app.log("SocketIO", "Error getting New cueballId");
				}
				
			}
		}).on("playerDisconnected", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String playerId = data.getString("id");
					players.remove(playerId);
					Gdx.app.log("SocketIO", "Player Disconnected: " + playerId);
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting New PlayerID");
				}
			}
		}).on("playerMoved", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String playerId = data.getString("id");
					Double x = data.getDouble("x");
					Double y = data.getDouble("y");
					if (players.get(playerId) != null) {
						players.get(playerId).setX(x.floatValue());
						players.get(playerId).setY(y.floatValue());
					}
				} catch (JSONException e) {
				}
			}
		}).on("cueballMoved", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					String cueballId = data.getString("id");
					Double x = data.getDouble("x");
					Double y = data.getDouble("y");
					if (cueballs.get(cueballId) != null) {
						cueballs.get(cueballId).setX(x.floatValue());
						cueballs.get(cueballId).setY(y.floatValue());
					}
				}catch (JSONException e) {
				}
						
			}
		}).on("getPlayers", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray objects = (JSONArray) args[0];
				try {
					for (int i = 0; i < objects.length(); i++) {
						Player coopPlayer = new Player(0, 0, 50, 0, 0, Color.CORAL);
						Vector2 position = new Vector2();
						position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
						position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
						coopPlayer.setX(position.x);
						coopPlayer.setY(position.y);

						players.put(objects.getJSONObject(i).getString("id"), coopPlayer);
					}
				} catch (JSONException e) {
				}
			}
		}).on("getCueballs", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray objects = (JSONArray) args[0];
				try {
					for (int i = 0; i < objects.length(); i++) {
						Ball coopCueball = new Ball(0, 0, 50, 0, 0, false ,Color.CORAL);
						Vector2 position = new Vector2();
						position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
						position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
						coopCueball.setX(position.x);
						coopCueball.setY(position.y);

						cueballs.put(objects.getJSONObject(i).getString("id"), coopCueball);
					}
				} catch (JSONException e) {
				}
			}
		});
	}

	public void createGame() {
		socket.emit("createGame", new Ack() {
			@Override
			public void call(Object... args) {
				String newRoomCode = (String) args[0]; // Server returns the room code
				roomCode = newRoomCode;
				Gdx.app.log("SocketIO", "Room created with code: " + roomCode);
			}
		});
	}

	public void joinGame(String code) {
		socket.emit("joinGame", code, new Ack() {
			@Override
			public void call(Object... args) {
				JSONObject response = (JSONObject) args[0];
				try {
					if (response.getBoolean("success")) {
						roomCode = code;
						Gdx.app.log("SocketIO", "Successfully joined room: " + roomCode);
						socket.emit("getPlayers", roomCode);
						socket.emit("getCueballs", roomCode); // Request cue balls as well
					} else {
						Gdx.app.log("SocketIO", "Failed to join room: " + response.getString("message"));
					}
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error processing join response");
				}
			}
		});
	}

	public void updateServer(float dt) {
		timer += dt;
		if (timer > UPDATE_TIME && player != null && player.hasMoved()) {
			JSONObject playerData = new JSONObject();
			JSONObject cueBallData = new JSONObject();
			try {
				// Send player movement data
				playerData.put("x", player.getX());
				playerData.put("y", player.getY());
				playerData.put("roomCode", roomCode); // Include room code for scoping the data
				socket.emit("playerMoved", playerData);

				cueBallData.put("x", cueBall.getX());
				cueBallData.put("y", cueBall.getY());
				cueBallData.put("roomCode", roomCode); // Include room code for scoping the data
				socket.emit("cueballMoved", cueBallData); // Emit cueballMoved for cue ball data

			} catch (JSONException e) {
				Gdx.app.log("SocketIO", "Error sending update data");
			}
			timer = 0; // Reset timer after sending the update
		}
	}
}

