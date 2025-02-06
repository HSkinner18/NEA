
package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main extends ApplicationAdapter {

	private final float UPDATE_TIME = 1 / 120f;
	private boolean cueballUpdatedByServer = false;
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


	Stage stage;


	Skin skin;

	Boolean clicked = false;

	ShotLine shotLine = new ShotLine(0, 0, 0, 0, 3, Color.BLUE);


	@Override
	public void create() {

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);


		sr = new ShapeRenderer();
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


		create.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				clicked = true;
				createGame();
				create.remove();

			}
		});

		join.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				joinGame(codeInput.getText());
					clicked = true;
					join.remove();
			}
		});


		//end of buttons




		sr = new ShapeRenderer();
		sr2 = new ShapeRenderer();
		table = new PoolTable(200, 175, 1100, 550, Color.GREEN);


		reboundLine = new ShotLine(0, 0, 0, 0, 3, Color.BLUE);


		initialisePockets(table, pockets);
		initialiseBalls();


	}

	@Override
	public void render() {

		ScreenUtils.clear(0, 0, 0, 1);
		handleInput(Gdx.graphics.getDeltaTime());
		updateServer(Gdx.graphics.getDeltaTime());

		stage.draw();


		sr.begin(ShapeRenderer.ShapeType.Filled);


		if (clicked) {
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
			shotPredictor();

			if (checkBallsStationary()) {
				shotLine.draw(sr);
				reboundLine.draw(sr);
			}

			potBall();


			for (Ball pocketedBall : pocketedBalls) {
				pocketedBall.setxVel(0);
				pocketedBall.setyVel(0);
				pocketedBall.setX(4000);
				pocketedBall.setY(4000);
			}

			cursor.draw(sr);

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

	}

	public void BallMovement() {

		for (int i = 0; i < 15; i++) {

			if (balls[i] != null) {

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
						float impulse = dotProduct;

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





	public void potBall() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 15; j++) {
				if (checkPocketCollision(pockets[i], balls[j])) {
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





	public boolean checkBallsStationary() {
		for (int i = 0; i < balls.length; i++) {
			if (balls[i] != null) {
				if (balls[i].getxVel() > 0 || balls[i].getyVel() > 0) {
					return false;
				}
			}
		}
		if (cueBall.getyVel() > 0 || cueBall.getxVel() > 0) {
			return false;
		} else {

			return true;
		}

	}

	public void shotPredictor() {

		if (checkBallsStationary()) {
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
				shotLine.setX2(topX + cueBall.getR());
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
	}


	public void initialisePockets(PoolTable table, Pocket[] pockets) {
		float x = table.getX();
		float row1y = table.getY();

		float row2y = row1y + table.getH();

		for (int i = 0; i < 3; i++) {
			pockets[i] = new Pocket(x, row1y, 20, Color.BLACK);
			x += (table.getW() / 2);
		}

		float x2 = table.getX();

		for (int i = 3; i < 6; i++) {
			pockets[i] = new Pocket(x2, row2y, 17, Color.BLACK);
			x2 += (table.getW() / 2);
		}
	}


	public void handleInput(float dt) {
		if (player != null) {
			float playerWidth = player.getR() * 2;
			float playerHeight = player.getR() * 2;

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

		balls = new Ball[15];

		float startX = table.getX() + table.getW() * 0.75f;
		float startY = table.getY() + (float) table.getH() / 2;

		float ballRadius = 15f;
		float rowSpacing = (float) (ballRadius * Math.sqrt(3));

		int ballIndex = 0;

		for (int row = 0; row < 5; row++) {
			float rowX = startX + row * ballRadius;

			float rowY = startY - (row * rowSpacing / 2);


			for (int i = 0; i <= row; i++) {
				if (ballIndex % 2 == 0) {
					balls[ballIndex] = new Ball(rowX, rowY + i * rowSpacing, ballRadius, 0, 0, false, Color.YELLOW);
					ballIndex++;
				} else {
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
				cueballs.put("cueball", cueBall); // Add cueBall to the map
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					playerId = data.getString("id");
					Gdx.app.log("SocketIO", "My ID: " + playerId);
					players.put(playerId, player);
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

					if (!players.containsKey(playerId)) {
						players.put(playerId, new Player(0, 0, 50, 0, 0, Color.CORAL));
					}
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting New PlayerID");
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
					Gdx.app.log("SocketIO", "Error parsing playerMoved data");
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

						if (!players.containsKey(objects.getJSONObject(i).getString("id"))) {
							players.put(objects.getJSONObject(i).getString("id"), coopPlayer);
						}
					}
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error fetching players");
				}
			}
		}).on("cueballMoved", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject cueballData = (JSONObject) args[0];
				try {
					String cueballId = cueballData.getString("id");
					Double x = cueballData.getDouble("x");
					Double y = cueballData.getDouble("y");

					if (cueballs.containsKey(cueballId)) {
						cueballUpdatedByServer = true;
						cueBall.setX(x.floatValue());
						cueBall.setY(y.floatValue());
					} else {
						System.out.println("Cueball with ID " + cueballId + " not found!");
					}

					System.out.println("Received cueballMoved: " + cueballData.toString());
				} catch (JSONException e) {
					System.err.println("Error parsing cueballMoved data: " + e.getMessage());
				}
			}

			}).on("getCueballs", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					double x = data.getDouble("x");
					double y = data.getDouble("y");

					cueBall.setX((float) x);
					cueBall.setY((float) y);
					cueballs.put("cueball", cueBall); // Add cueBall to the map here as well
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error fetching cueballs");
				}
			}
		});
	}

	public void createGame() {
		socket.emit("createGame", new Ack() {
			@Override
			public void call(Object... args) {
				String newRoomCode = (String) args[0];
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

						socket.emit("getCueballs", roomCode);

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

		if (timer > UPDATE_TIME) {
			JSONObject playerData = new JSONObject();
			JSONObject cueballData = new JSONObject();

			try {
				if (player != null && player.hasMoved()) {
					playerData.put("x", player.getX());
					playerData.put("y", player.getY());
					playerData.put("id", playerId);
					playerData.put("roomCode", roomCode);
					socket.emit("playerMoved", playerData);
				}

				if (cueBall != null && cueBall.hasMoved()) {
					if (!cueballUpdatedByServer) {
						cueballData.put("x", cueBall.getX());
						cueballData.put("y", cueBall.getY());
						cueballData.put("id", cueballId);
						cueballData.put("roomCode", roomCode);
						System.out.println("Sending cueballMoved: " + cueballData.toString());
						socket.emit("cueballMoved", cueballData);
					} else {
						cueballUpdatedByServer = false; // Reset the flag
					}
				}

			} catch (JSONException e) {
				Gdx.app.log("SocketIO", "Error sending update data");
			}

			timer = 0;
		}
	}
}





