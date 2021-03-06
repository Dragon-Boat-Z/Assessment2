package com.hardgforgif.dragonboatracing;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.hardgforgif.dragonboatracing.UI.*;
import com.hardgforgif.dragonboatracing.core.*;



import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Game extends ApplicationAdapter implements InputProcessor {
    private static Player player;
	private static AI[] opponents;
	private static Map[] map;
	private Batch batch;
	private Batch UIbatch;
	private static OrthographicCamera camera;
	private static World[] world;


	private Vector2 mousePosition = new Vector2();
	private Vector2 clickPosition = new Vector2();
	private boolean[] pressedKeys = new boolean[4]; // W, A, S, D buttons status

	private static ArrayList<Body> toBeRemovedBodies = new ArrayList<>();
	private static ArrayList<Body> toUpdateHealth = new ArrayList<>();
	private static ArrayList<Body[]> toApplyPowerUps = new ArrayList<>();


	@Override
	public void create() {
		// Initialize the sprite batches
		batch = new SpriteBatch();
		UIbatch = new SpriteBatch();

		// Get the values of the screen dimensions
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// Initialise the world and the map arrays
		world = new World[GameData.numberOfLegs];
		map = new Map[GameData.numberOfLegs];
		for (int i = 0; i < GameData.numberOfLegs; i++){
			// Initialize the physics game World
			world[i] = new World(new Vector2(0f, 0f), true);

			// Initialize the map
			map[i] = new Map("Map1/Map2.tmx", w);

			// Initialise opponents.
			opponents = new AI[GameData.numberOfBoats - 1];

			// Calculate the ratio between pixels, meters and tiles
			GameData.TILES_TO_METERS = map[i].getTilesToMetersRatio();
			GameData.PIXELS_TO_TILES = 1/(GameData.METERS_TO_PIXELS * GameData.TILES_TO_METERS);

			// Create the collision with the land
			map[i].createMapCollisions("CollisionLayerLeft", world[i]);
			map[i].createMapCollisions("CollisionLayerRight", world[i]);

			// Create the lanes, and the obstacles in the physics game world
			map[i].createLanes(world[i], 30 + (i * 5) + (GameData.difficultySelected * 5));

			// Create the start line
			map[i].createStartLine("finishLine.png");

			// Create the finish line
			map[i].createFinishLine("finishLine.png");

			// Create a new collision handler for the world
			createContactListener(world[i]);
		}
		// Initialize the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);

		// Set the app's input processor
		Gdx.input.setInputProcessor(this);
	}

	/**
	 * This method creates new ContactListener who's methods are executed when objects collide
	 * @param world This is the physics world in which the collisions happen
	 */
	private static void createContactListener(World world){

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {

				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();

				//if (fixtureA.getBody().getUserData() instanceof Boat) {
					//toUpdateHealth.add(fixtureA.getBody());
				//} else if (fixtureB.getBody().getUserData() instanceof Boat) {
					//toUpdateHealth.add(fixtureB.getBody());
				//}
				if(fixtureA.getBody().getUserData() instanceof Boat) {
					if(fixtureB.getBody().getUserData() instanceof PowerUp) {
						//Use the power up.
						//Add [Boat, PowerUp] to a list of boats that need PowerUps applying to them.
						toApplyPowerUps.add(new Body[]{fixtureA.getBody(), fixtureB.getBody()});
						//toBeRemovedBodies.add(fixtureB.getBody());
					}
					else {
						//Fixture B is not a PowerUp, so hitting it will hurt.
						toUpdateHealth.add(fixtureA.getBody());
					}
				}
				else if(fixtureB.getBody().getUserData() instanceof Boat) {
					if(fixtureA.getBody().getUserData() instanceof PowerUp) {
						//Use the power up.
						//Add [Boat, PowerUp] to a list of boats that need PowerUps applying to them.
						toApplyPowerUps.add(new Body[]{fixtureB.getBody(), fixtureA.getBody()});
						//toBeRemovedBodies.add(fixtureA.getBody());
					}
					else {
						//Fixture A is not a PowerUp, so hitting it will hurt.
						toUpdateHealth.add(fixtureB.getBody());
					}
				}
				if (fixtureA.getBody().getUserData() instanceof Obstacle) {
					toBeRemovedBodies.add(fixtureA.getBody());
				} else if (fixtureB.getBody().getUserData() instanceof Obstacle) {
					toBeRemovedBodies.add(fixtureB.getBody());
				}
			}

			@Override
			public void endContact(Contact contact) {

			}

			@Override
			public void preSolve(Contact contact, Manifold manifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse contactImpulse) {

			}
		});
	}

	/**
	 * Sets the camera y position at the y position of a player's sprite
	 * @param player The target player
	 */
	private void updateCamera(Player player) {
		camera.position.set(camera.position.x, player.getBoatSprite().getY() + 400, 0);
		camera.update();
	}

	/**
	 * Updates the GameData.standings array by comparing boats positions
	 */
	private void updateStandings(){
		// If the player hasn't finished the race...
		if(!player.hasFinished()){
			// Reset his position
			GameData.standings[0] = 1;

			// For every AI that is ahead, increment by 1
			for (Boat boat: opponents)
				if (boat.getBoatSprite().getY() + boat.getBoatSprite().getHeight() / 2 > player.getBoatSprite().getY() + player.getBoatSprite().getHeight() / 2) {
					GameData.standings[0]++;
				}

		}

		// Iterate through all the AIs to update their standings too
		for (int i = 0; i < opponents.length; i++)
			// If the AI hasn't finished the race...
			if(!opponents[i].hasFinished()){
				// Reset his position
				GameData.standings[i + 1] = 1;

				// If the player is ahead, increment the standing by 1
				if (player.getBoatSprite().getY() > opponents[i].getBoatSprite().getY())
					GameData.standings[i + 1]++;

				// For every other AI that is ahead, increment by 1
				for (int j = 0; j < opponents.length; j++)
					if(opponents[j].getBoatSprite().getY() > opponents[i].getBoatSprite().getY())
						GameData.standings[i + 1]++;
			}
	}

	/**
	 * Updates the GameData.results list by adding a new result every time a boat finishes the game
	 */
	private void checkForResults(){
		// If the player has finished and we haven't added his result already...
		if(player.hasFinished() && player.getAcceleration() > 0 && GameData.results.size() < GameData.numberOfBoats){
			// Add the result to the list with key 0, the player's lane
			GameData.results.add(new Float[]{0f, GameData.currentTimer, Float.valueOf(player.getBoatType())});

			// Transition to the results UI
			GameData.showResultsState = true;
			GameData.currentUI = new ResultsUI();

			// Change the player's acceleration so the boat stops moving
			player.setAcceleration(-200f);
		}

		// Iterate through the AI to see if any of them finished the race
		for (int i = 0; i < opponents.length; i++){
			// If the AI has finished and we haven't added his result already...
			if(opponents[i].hasFinished() && opponents[i].getAcceleration() > 0 && GameData.results.size() < GameData.numberOfBoats){
				// Add the result to the list with the his lane numer as key
				GameData.results.add(new Float[]{Float.valueOf(i + 1), GameData.currentTimer, Float.valueOf(opponents[i].getBoatType())});

				// Change the AI's acceleration so the boat stops moving
				opponents[i].setAcceleration(-200f);
			}
		}
	}

	/**
	 * This method checks the position of all the boats to add penalties if necessary
	 */
	private void updatePenalties() {
		// Update the penalties for the player, if he is outside his lane
		float boatCenter = player.getBoatSprite().getX() + player.getBoatSprite().getWidth() / 2;
		if (!player.hasFinished() && player.getRobustness() > 0 && (boatCenter < player.getLeftLimit() || boatCenter > player.getRightLimit())){
			GameData.penalties[0] += Gdx.graphics.getDeltaTime();
			GameData.playerWarning = true;
		} else {
			GameData.playerWarning = false;
		}

		// Update the penalties for the opponents, if they are outside the lane
		for (int i = 0; i < opponents.length; i++){
			boatCenter = opponents[i].getBoatSprite().getX() + opponents[i].getBoatSprite().getWidth() / 2;
			if (!opponents[i].hasFinished() && opponents[i].getRobustness() > 0 &&(boatCenter < opponents[i].getLeftLimit() || boatCenter > opponents[i].getRightLimit())){
				GameData.penalties[i + 1] += Gdx.graphics.getDeltaTime();
			}
		}
	}


	/**
	 * This method marks all the boats that haven't finished the race as dnfs
	 */
	private void dnfRemainingBoats() {
		// If the player hasn't finished
		if (!player.hasFinished() && player.getRobustness() > 0 && GameData.results.size() < (opponents.length + 1)){
			// Add a dnf result
			GameData.results.add(new Float[]{0f, Float.MAX_VALUE});

			// Transition to the showResult screen
			GameData.showResultsState = true;
			GameData.currentUI = new ResultsUI();
		}

		// Iterate through the AI and add a dnf result for any who haven't finished
		for (int i = 0; i < opponents.length; i++){
		  if (!opponents[i].hasFinished() && opponents[i].getRobustness() > 0 && GameData.results.size() < (opponents.length + 1))
				GameData.results.add(new Float[]{Float.valueOf(i + 1), Float.MAX_VALUE, Float.valueOf(opponents[i].getBoatType())});
		}
	}

	@Override
	public void render() {
		// Reset the screen
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// If the game is in one of the static state
		if (GameData.mainMenuState || GameData.choosingBoatState || GameData.GameOverState || GameData.pauseState || GameData.saveState){
			// Draw the UI and wait for the input
			GameData.currentUI.drawUI(UIbatch, mousePosition, Gdx.graphics.getWidth(), Gdx.graphics.getDeltaTime());
			GameData.currentUI.getInput(Gdx.graphics.getWidth(), clickPosition);

		}

		// Otherwise, if we are in the game play state
		else if(GameData.gamePlayState){
			// If it's the first iteration in this state, the boats need to be created at their starting positions
			if (player == null){
			    if(GameData.loadState) {
                    setBoats(GameData.boats);
                    GameData.loadState = false;
                }

			    else {
                    // Create the player boat
                    int playerBoatType = GameData.boatTypes[0];
                    player = new Player(GameData.boatsStats[playerBoatType][0], GameData.boatsStats[playerBoatType][1],
                            GameData.boatsStats[playerBoatType][2], GameData.boatsStats[playerBoatType][3],
                            playerBoatType, map[GameData.currentLeg].getLanes()[0]);
                    player.createBoatBody(world[GameData.currentLeg], GameData.startingPoints[0][0], GameData.startingPoints[0][1], "Boat1.json");
                    GameData.boats[0] = player;
                    player.setLimits(player.getLane().getLeftBoundary()[0][1], player.getLane().getRightBoundary()[0][1]);

					// Create the AI boats
					// If last leg, have less boats. Otherwise, have normal number of boats.
					if(GameData.currentLeg == GameData.numberOfLegs - 1) {
						//CHECKING FOR GAME OVER BY NOT BEING FAST ENOUGH AS OPPOSED TO REGULAR DNF METHOD:
						boolean shouldBeInFinal = false;
						for(int i = 0; i < GameData.numberOfFinalists; i++) {
							if(GameData.results.get(i)[0] == 0) {
								shouldBeInFinal = true;
								break;
							}
						}
						if(shouldBeInFinal) {
							opponents = new AI[GameData.numberOfFinalists - 1];
							int incrementer = 0;
							ArrayList<Integer> finalistTypes = new ArrayList<>();
							while (finalistTypes.size() < opponents.length) {
								//Add the first numberOfFinalists-1 boats from the latest results to the final.
								//Goes through results looking for AI, has to skip a Player as it could be in any position.
								if (GameData.results.get(incrementer)[0] != 0) {
									finalistTypes.add(Math.round(GameData.results.get(incrementer)[2]));
								}
								incrementer += 1;
							}
							for (int i = 0; i < opponents.length; i++) {
								int AIBoatType = finalistTypes.get(i);
								opponents[i] = new AI(GameData.boatsStats[AIBoatType][0], GameData.boatsStats[AIBoatType][1],
										GameData.boatsStats[AIBoatType][2], GameData.boatsStats[AIBoatType][3],
										AIBoatType, map[GameData.currentLeg].getLanes()[i + 1]);
								opponents[i].createBoatBody(world[GameData.currentLeg], GameData.startingPoints[i + 1][0], GameData.startingPoints[i + 1][1], "Boat1.json");
								GameData.boats[i + 1] = opponents[i];
								opponents[i].setLimits(opponents[i].getLane().getLeftBoundary()[0][1], opponents[i].getLane().getRightBoundary()[0][1]);
							}
						} else {
							GameData.currentUI = new GameOverUI();
							GameData.GameOverState = true;
						}
					}

					else {
						opponents = new AI[GameData.numberOfBoats - 1];
						for (int i = 0; i < opponents.length; i++) {
							int AIBoatType = GameData.boatTypes[i + 1];
							opponents[i] = new AI(GameData.boatsStats[AIBoatType][0], GameData.boatsStats[AIBoatType][1],
									GameData.boatsStats[AIBoatType][2], GameData.boatsStats[AIBoatType][3],
									AIBoatType, map[GameData.currentLeg].getLanes()[i + 1]);
							opponents[i].createBoatBody(world[GameData.currentLeg], GameData.startingPoints[i + 1][0], GameData.startingPoints[i + 1][1], "Boat1.json");
							GameData.boats[i + 1] = opponents[i];
							opponents[i].setLimits(opponents[i].getLane().getLeftBoundary()[0][1], opponents[i].getLane().getRightBoundary()[0][1]);
						}
					}
					GameData.results.clear();


                }

			}

			if(GameData.GameOverState) {

			} else {

				//Iterate through the PowerUps that need applying.
				for (Body[] bodyPair : toApplyPowerUps) {
					if(player.getBoatBody() == bodyPair[0] && !player.hasFinished()) {
						//The boat in question is the Player.
						for (Lane lane : map[GameData.currentLeg].getLanes()) {
							for (Obstacle obstacle : lane.getObstacles()) {
								if (obstacle.getObstacleBody() == bodyPair[1]) {
									obstacle.setObstacleBody(null);
									((PowerUp) obstacle).applyPowerUp(player);
								}
							}
						}
					}
					else {
						//The boat is one of the AI.
						for(int i = 0; i < opponents.length; i++) {
							if (opponents[i].getBoatBody() == bodyPair[0] && !opponents[i].hasFinished()) {
								for (Lane lane : map[GameData.currentLeg].getLanes()) {
									for (Obstacle obstacle : lane.getObstacles()) {
										if (obstacle.getObstacleBody() == bodyPair[1]) {
											obstacle.setObstacleBody(null);
											((PowerUp) obstacle).applyPowerUp(opponents[i]);
										}
									}
								}
							}
						}
					}
				}

				// Iterate through the bodies that need to be removed from the world after a collision
				for (Body body : toBeRemovedBodies){
					// Find the obstacle that has this body and mark it as null
					// so it's sprite doesn't get rendered in future frames
					for (Lane lane : map[GameData.currentLeg].getLanes())
						for (Obstacle obstacle : lane.getObstacles())
							if (obstacle.getObstacleBody() == body) {
								obstacle.setObstacleBody(null);
							}

					// Remove the body from the world to avoid other collisions with it
					if(body != null) {
						world[GameData.currentLeg].destroyBody(body);
					}
				}

				// Iterate through the bodies marked to be damaged after a collision
				for (Body body : toUpdateHealth){
					// if it's the player body
					if (player.getBoatBody() == body && !player.hasFinished() && !player.isInvulnerable()){
						// Reduce the health and the speed
						player.setRobustness(player.getRobustness()-10f);
						player.setCurrentSpeed(player.getCurrentSpeed()-10f);

						// If all the health is lost
						if(player.getRobustness() <= 0 && GameData.results.size() < GameData.numberOfBoats){
							// Remove the body from the world, but keep it's sprite in place
							world[GameData.currentLeg].destroyBody(player.getBoatBody());

							// Add a DNF result
							GameData.results.add(new Float[]{0f, Float.MAX_VALUE, Float.valueOf(player.getBoatType())});

							// Transition to the show result screen
							GameData.showResultsState = true;
							GameData.currentUI = new ResultsUI();
						}
					}

					// Otherwise, one of the AI has to be updated similarly
					else {
						for (int i = 0; i < opponents.length; i++){
							if (opponents[i].getBoatBody() == body && !opponents[i].hasFinished() && !opponents[i].isInvulnerable()) {

								opponents[i].setRobustness(opponents[i].getRobustness()-10f);
								opponents[i].setCurrentSpeed(opponents[i].getCurrentSpeed()-10f);

								if(opponents[i].getRobustness() < 0 && GameData.results.size() < GameData.numberOfBoats){
									world[GameData.currentLeg].destroyBody(opponents[i].getBoatBody());
									GameData.results.add(new Float[]{Float.valueOf(i + 1), Float.MAX_VALUE, Float.valueOf(opponents[i].getBoatType())});
								}
							}
						}
					}
				}

				toBeRemovedBodies.clear();
				toUpdateHealth.clear();
				toApplyPowerUps.clear();

				// Advance the game world physics
				world[GameData.currentLeg].step(1f/60f, 6, 2);

				// Update the timers
				GameData.currentTimer += Gdx.graphics.getDeltaTime();
				if(player.getPowerUpTimer() > 0) {
					player.setPowerUpTimer(player.getPowerUpTimer() - Gdx.graphics.getDeltaTime());
					if(player.getPowerUpTimer() < 0) {
						player.setPowerUpTimer(0f);
						player.setSpeed(GameData.boatsStats[player.getBoatType()][1]);
						player.setInvulnerability(false);
					}
				}
				for(int i = 0; i < opponents.length; i++) {
					if(opponents[i].getPowerUpTimer() > 0) {
						opponents[i].setPowerUpTimer(player.getPowerUpTimer() - Gdx.graphics.getDeltaTime());
						if(opponents[i].getPowerUpTimer() < 0) {
							opponents[i].setPowerUpTimer(0f);
							opponents[i].setSpeed(GameData.boatsStats[opponents[i].getBoatType()][1]);
							opponents[i].setInvulnerability(false);
						}
					}
				}

				// Update the player's and the AI's movement
				player.updatePlayer(pressedKeys, Gdx.graphics.getDeltaTime());
				for (AI opponent : opponents)
					opponent.updateAI(Gdx.graphics.getDeltaTime());

				// Set the camera as the batches projection matrix
				batch.setProjectionMatrix(camera.combined);

				// Render the map
				map[GameData.currentLeg].renderMap(camera, batch);

				// Render the player and the AIs
				player.drawBoat(batch);
				for (AI opponent : opponents)
					opponent.drawBoat(batch);

				// Render the objects that weren't destroyed yet
				for (Lane lane : map[GameData.currentLeg].getLanes())
					for (Obstacle obstacle : lane.getObstacles()){
						if (obstacle.getObstacleBody() != null)
							obstacle.drawObstacle(batch);
					}

				// Update the camera at the player's position
				updateCamera(player);

				updatePenalties();

				// Update the standings of each boat
				updateStandings();

				// If it's been 15 seconds since the winner completed the race, dnf all boats who haven't finished yet
				// Then transition to the result state
				if(GameData.results.size() > 0 && GameData.results.size() < (opponents.length + 1) &&
						GameData.currentTimer > GameData.results.get(0)[1] + 15f){
					dnfRemainingBoats();
					GameData.showResultsState = true;
					GameData.currentUI = new ResultsUI();
				}
				// Otherwise keep checking for new results
				else {
					checkForResults();
				}


				// Choose which UI to display based on the current state
				if(!GameData.showResultsState)
					GameData.currentUI.drawPlayerUI(UIbatch, player);
				else {
					GameData.currentUI.drawUI(UIbatch, mousePosition, Gdx.graphics.getWidth(), Gdx.graphics.getDeltaTime());
					GameData.currentUI.getInput(Gdx.graphics.getWidth(), clickPosition);
				}

			}
		}

		// Otherwise we need need to reset elements of the game to prepare for the next race
		else if(GameData.resetGameState){
			player = null;
			for (int i = 0; i < opponents.length; i++)
				opponents[i] = null;
			//GameData.results.clear();
			GameData.currentTimer = 0f;
			//player.setPowerUpTimer(0f);
			//player.setSpeed(GameData.boatsStats[player.getBoatType()][1]);
			//player.setInvulnerability(false);
			//for(int i = 0; i < opponents.length; i++) {
				//opponents[i].setPowerUpTimer(0f);
				//opponents[i].setSpeed(GameData.boatsStats[opponents[i].getBoatType()][1]);
				//opponents[i].setInvulnerability(false);
			//}
			GameData.penalties = new float[GameData.numberOfBoats];

			// If we're coming from the result screen, then we need to advance to the next leg
			if (GameData.showResultsState){
				GameData.currentLeg += 1;
				GameData.showResultsState = false;
				GameData.gamePlayState = true;
				GameData.currentUI = new GamePlayUI();

			}
			// Otherwise we're coming from the endgame screen so we need to return to the main menu
			else{
				resetGame();
				GameData.mainMenuState = true;
				GameData.currentUI = new MenuUI();
			}
			GameData.resetGameState = false;

		}

		// If we haven't clicked anywhere in the last frame, reset the click position
		if(clickPosition.x != 0f && clickPosition.y != 0f)
			clickPosition.set(0f,0f);
	}

    /**
     * Reset game state and all variables. Create new map and nullify player and opponents.
     */
	public static void resetGame() {
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.update();
        // Reset everything for the next game
        world = new World[GameData.numberOfLegs];
        map = new Map[GameData.numberOfLegs];
        for (int i = 0; i < GameData.numberOfLegs; i++){
            // Initialize the physics game World
            world[i] = new World(new Vector2(0f, 0f), true);

            // Initialize the map
            map[i] = new Map("Map1/Map2.tmx", Gdx.graphics.getWidth());

            // Calculate the ratio between pixels, meters and tiles
            GameData.TILES_TO_METERS = map[i].getTilesToMetersRatio();
            GameData.PIXELS_TO_TILES = 1/(GameData.METERS_TO_PIXELS * GameData.TILES_TO_METERS);

            // Create the collision with the land
            map[i].createMapCollisions("CollisionLayerLeft", world[i]);
            map[i].createMapCollisions("CollisionLayerRight", world[i]);

            // Create the lanes, and the obstacles in the physics game world
            map[i].createLanes(world[i], 30 + (i * 5) + (GameData.difficultySelected * 5));

            // Create the finish line
            map[i].createFinishLine("finishLine.png");

            // Create the start line
            map[i].createStartLine("finishLine.png");

            // Create a new collision handler for the world
            createContactListener(world[i]);
        }
        GameData.boats = new Boat[GameData.numberOfBoats];
        GameData.boatTypes = new int[GameData.numberOfBoats];
        GameData.currentTimer = 0f;
        GameData.difficultySelected = 0;
        GameData.currentLeg = 0;
        player = null;
        for(AI a : opponents) a = null;
    }
	public void dispose() {
		world[GameData.currentLeg].dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.W)
			pressedKeys[0] = true;
		if (keycode == Input.Keys.A)
			pressedKeys[1] = true;
		if (keycode == Input.Keys.S)
			pressedKeys[2] = true;
		if (keycode == Input.Keys.D)
			pressedKeys[3] = true;
        if (keycode == Input.Keys.ESCAPE) {
            // Logic for which screen to pause to and from.
            // If currently in pause screen.
            if (GameData.pauseState) {
                GameData.pauseState = false;
                // Look at previous state and go back to that state.
                switch (GameData.previousState) {
                    case "GamePlayUI":
                        GameData.currentUI = new GamePlayUI();
                        GameData.gamePlayState = true;
                        break;
                    case "ChoosingUI":
                        GameData.currentUI = new ChoosingUI();
                        GameData.choosingBoatState = true;
                        break;
                    case "ResultsUI":
                        GameData.currentUI = new ResultsUI();
                        GameData.showResultsState = true;
                        break;
                    case "GameOverUI":
                        GameData.currentUI = new GameOverUI();
                        GameData.GameOverState = true;
                        break;
                }
            }
            else
            {
                // Check if the state we are in is pause-able from.
                if(GameData.gamePlayState || GameData.choosingBoatState || GameData.saveState) {
                    GameData.pauseState = true;
                    // If pausing from the save state, we want to preserve the current previous state.
                    GameData.previousState = GameData.saveState ? GameData.previousState : GameData.currentUI.getClass().getSimpleName();
                    // If currently in game screen.
                    if (GameData.gamePlayState) {
                        GameData.gamePlayState = false;
                    }
                    // If currently choosing a boat.
                    if (GameData.choosingBoatState) {
                        GameData.choosingBoatState = false;
                    }
                    // If currently in results screen.
                    if (GameData.showResultsState) {
                        GameData.showResultsState = false;
                    }
                    // If currently in save screen.
                    if (GameData.saveState) {
                        GameData.saveState = false;
                    }
                    GameData.currentUI = new PauseUI();
                }
            }
        }
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.W)
			pressedKeys[0] = false;
		if (keycode == Input.Keys.A)
			pressedKeys[1] = false;
		if (keycode == Input.Keys.S)
			pressedKeys[2] = false;
		if (keycode == Input.Keys.D)
			pressedKeys[3] = false;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(GameData.gamePlayState) {
            Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
            clickPosition.set(position.x, position.y);
        }
        else {
            clickPosition.set(screenX, Gdx.graphics.getHeight()-screenY);
        }
        return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(GameData.gamePlayState) {
            Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
            mousePosition.set(position.x, position.y);
        }
		else {
            mousePosition.set(screenX, Gdx.graphics.getHeight()-screenY);
        }
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}


	public static void setBoats(Boat[] boats) {
        // Create the player boat
        player = (Player)GameData.boats[0];
        player.setLimits(player.getLane().getLeftBoundary()[0][1],player.getLane().getRightBoundary()[0][1]);

        // Create the AI boats
        for(int i = 1; i < GameData.numberOfBoats; i++) {
            opponents[i - 1] = (AI)GameData.boats[i];
            opponents[i-1].setLimits(opponents[i-1].getLane().getLeftBoundary()[0][1],opponents[i-1].getLane().getRightBoundary()[0][1]);
        }
    }

    public static void setObstacles(Obstacle[][] obsts) {

    }

	//getters
	public Player getPlayer(){
		return this.player;
	}

	public AI[] getOpponents(){
		return this.opponents;
	}

	public static Map[] getMap(){
		return map;
	}

	public Batch getBatch(){
		return this.batch;
	}

	public Batch getUIBatch(){
		return this.UIbatch;
	}

	public OrthographicCamera getCamera(){
		return this.camera;
	}

	public static World[] getWorld(){
		return world;
	}

	public Vector2 getMousePosition(){
		return this.mousePosition;
	}

	public Vector2 getClickPosition(){
		return this.clickPosition;
	}

	public boolean[] getPressedKeys(){
		return this.pressedKeys;
	}

	public static ArrayList<Body> getToBeRemovedBodies(){
		return toBeRemovedBodies;
	}

	public static void setToBeRemovedBodies(ArrayList<Body> tbRB) {
		toBeRemovedBodies = tbRB;
	}

	public ArrayList<Body> getToUpdateHealth(){
		return this.toUpdateHealth;
	}
}
