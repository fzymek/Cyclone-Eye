package pl.fzymek.android.cycloneeye.game.cyclone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.fzymek.android.cycloneeye.game.engine.GameObject;
import pl.fzymek.android.cycloneeye.game.engine.SpatialHashGrid;
import pl.fzymek.android.cycloneeye.game.engine.math.OverlapTester;
import pl.fzymek.android.cycloneeye.game.engine.math.Vector2;
import android.content.Context;
import android.util.Log;

public class World {

	public static int SCREEN_PER_LEVEL_MULTIPLIER = 5;
	public static int CURRENT_LEVEL = 1;

	// world states
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;

	// world properties
	public final static float WORLD_WIDTH = 48.0f;
	public float WORLD_HEIGHT;// = SCREEN_PER_LEVEL_MULTIPLIER * 85.4f;
	public final static long SECOND = 1000 * 1000 * 1000;

	public static final int NUMBER_OF_OBSTACLES = 15;
	public static final int NUMBER_OF_TARGETS = 8;
	public static final int NUMBER_OF_POWERUPS = 3;
	
	public static final float TARGET_PROBABILITY =   0.50f; //50%
	public static final float POWER_UP_PROBABILITY = 0.80f; //20%
	public static final float OBSTACLE_PROBABILITY = 0.30f; // 70%

	public final static float CAMERA_FRUSTUM_WIDTH = 48.0f;
	public final static float CAMERA_FRUSTUM_HEIGHT = 85.4f;

	public interface WorldEventsListener {
		void obstacle();

		void powerUp();

		void target();

		void gameOver();

		void levelEnd();
	}

	public final Cyclone cyclone;
	public final List<Obstacle> obstacles;
	public final List<Target> targets;
	public final List<PowerUp> powerUps;
	public final Random rnd;
	public final WorldEventsListener listener;
	public final static Vector2 gravity = new Vector2(0.0f, 2.0f);

	public float currentDistance;
	public int score;
	public int state;
	private SpatialHashGrid grid;

	public float bgScroll;

	private Target finalTarget;

	public World(final WorldEventsListener listener) {

		if (CURRENT_LEVEL % 3 == 0) {
			SCREEN_PER_LEVEL_MULTIPLIER++;
		}
		
		Log.d("WORLD", "LEVEL: " + CURRENT_LEVEL);
		Log.d("WORLD", "SCREENS PER LEVEL: " + SCREEN_PER_LEVEL_MULTIPLIER);

		WORLD_HEIGHT = SCREEN_PER_LEVEL_MULTIPLIER * 85.4f;

		this.listener = listener;
		this.cyclone = new Cyclone(WORLD_WIDTH / 2, 3.0f);
		this.obstacles = new ArrayList<Obstacle>();
		this.targets = new ArrayList<Target>();
		this.powerUps = new ArrayList<PowerUp>();
		this.rnd = new Random(System.currentTimeMillis());
		this.grid = new SpatialHashGrid(WORLD_WIDTH, WORLD_HEIGHT, 50.0f);

		generateLevel();

		currentDistance = 0.0f;
		Log.d("World", "World created");

	}

	private void generateLevel() {
		
		final float maxY = WORLD_HEIGHT
				- (Target.TARGET_HEIGHT * Target.TARGET_TYPE_LEVEL_END_SIZE_MULTIPLIER)
				/ 2 - 25.0f;
		final float maxX = WORLD_WIDTH - 3.0f;


		final int maxTargets = 12 + 3 * CURRENT_LEVEL;
		final int maxPowerUps = 3;
		final int maxObstaclesInRow = 2;
		final int maxOjectsInRow = 3;
		final float minPowerUpsDistance = 100.0f + 50 * CURRENT_LEVEL;
		final float minTargetsDistance = 20.0f;
		
		float startY = 20.0f;
		float startX = 1 + rnd.nextFloat() * 3.0f;

		int currentObjectsInRow = 0;
		int currentPowerUps = 0;
		int currentObstaclesInRow = 0;
		int currentTargets = 0;
		float lastPowerUpPlace = 0.0f;
		float lastTargetPlace = 0.0f;

		
		while (startY < maxY) {

			while (startX < maxX) {
				
				if (currentObstaclesInRow < maxObstaclesInRow
						&& currentObjectsInRow < maxOjectsInRow
						&& rnd.nextFloat() > OBSTACLE_PROBABILITY) {
					final Obstacle o = new Obstacle(startX, startY, 0.5f,
							3L * SECOND);

					obstacles.add(o);
					grid.insertStaticObject(o);

					currentObstaclesInRow++;
					currentObjectsInRow++;
					startX += 12 + rnd.nextFloat() * 9;
				}

				if (startY - lastTargetPlace > minTargetsDistance
						&& currentTargets < maxTargets
						&& currentObjectsInRow < maxOjectsInRow
						&& rnd.nextFloat() > TARGET_PROBABILITY) {
					final Target t = new Target(startX, startY, 200,
							Target.TYPE_NORMAL);

					targets.add(t);
					grid.insertStaticObject(t);

					lastTargetPlace = startY;
					currentTargets++;
					currentObjectsInRow++;
					startX += 12 + rnd.nextFloat() * 9;
				}

				if (startY - lastPowerUpPlace > minPowerUpsDistance
						&& currentObjectsInRow < maxOjectsInRow
						&& currentPowerUps < maxPowerUps
						&& rnd.nextFloat() > POWER_UP_PROBABILITY) {
					final PowerUp p = new PowerUp(startX, startY, 2.0f,
							5L * SECOND);

					powerUps.add(p);
					grid.insertStaticObject(p);

					lastPowerUpPlace = startY;
					currentObjectsInRow++;
					currentPowerUps++;
					startX += 12 + rnd.nextFloat() * 9;
				}

				startX += 12 + rnd.nextFloat() * 9;
			}
			currentObstaclesInRow = 0;
			currentObjectsInRow = 0;
			startX = 1 + rnd.nextFloat() * 3.0f;
			startY += 15 + rnd.nextFloat() * 7.5f;
		}
		
		finalTarget = new Target(WORLD_WIDTH / 2, WORLD_HEIGHT
				- Target.TARGET_HEIGHT / 2, 1000, Target.TYPE_LEVEL_END);

		targets.add(finalTarget);
		grid.insertStaticObject(finalTarget);
		
	}

	private void generateLevel(int level) {
		Log.d("World", "Generating level");

		for (int i = 0; i < NUMBER_OF_OBSTACLES; i++) {
			float slow = 0.5f;
			float posY = rnd.nextFloat() * WORLD_HEIGHT;
			posY = posY > WORLD_HEIGHT - 10 ? posY - 6 : posY < 5 ? posY + 5 : posY;
			Obstacle o = new Obstacle(rnd.nextFloat() * WORLD_WIDTH, posY,	slow, 3L * SECOND);

			obstacles.add(o);
			grid.insertStaticObject(o);

		}

		for (int i = 0; i < NUMBER_OF_TARGETS - 1; i++) {
			int score = rnd.nextInt(151) + 50;
			float posY = rnd.nextFloat() * WORLD_HEIGHT;
			posY = posY > WORLD_HEIGHT - 10 ? posY - 6 : posY < 5 ? posY + 5 : posY;
			Target t = new Target(rnd.nextFloat() * WORLD_WIDTH, posY, score, Target.TYPE_NORMAL);

			targets.add(t);
			grid.insertStaticObject(t);
		}

		finalTarget = new Target(WORLD_WIDTH / 2, WORLD_HEIGHT
				- Target.TARGET_HEIGHT / 2, 1000, Target.TYPE_LEVEL_END);

		targets.add(finalTarget);
		grid.insertStaticObject(finalTarget);

		for (int i = 0; i < NUMBER_OF_POWERUPS; i++) {
			float speedup = 2.0f;
			float posY = rnd.nextFloat() * WORLD_HEIGHT;
			posY = posY > WORLD_HEIGHT - 10 ? posY - 6 : posY < 5 ? posY + 5 : posY;
			PowerUp p = new PowerUp(rnd.nextFloat() * WORLD_WIDTH, posY, speedup, 3L * SECOND);

			powerUps.add(p);
			grid.insertStaticObject(p);
		}

	}

	public void update(float deltaTime, float accelX) {

		updateCyclone(deltaTime, accelX);
		updateTargets(deltaTime);
		updateObstacles(deltaTime);
		updatePowerUps(deltaTime);

		if (cyclone.currentState != Cyclone.STATE_DEAD) {
			checkCollisions();
		}

		updateScore();

		checkGameOver();

	}

	private void updateScore() {

		if (cyclone.currentState == Cyclone.STATE_SLOW) {
			if (score > 5) {
				score -= 5;
			} else if (score > 0) {
				score -= 1;
			} else {
				// nothing
			}
		} else {
			score += 1;
		}

	}

	private void checkGameOver() {
		
		if (cyclone.currentState == Cyclone.STATE_DEAD) {
			listener.gameOver();
			state = WORLD_STATE_GAME_OVER;
		}

		if (cyclone.position.y > finalTarget.position.y && finalTarget.state != Target.STATE_DESTROYED) {
			listener.gameOver();
			state = WORLD_STATE_GAME_OVER;
		}

	}

	private void checkCollisions() {

		final List<GameObject> potentialColliders = grid
				.getPotentialColliders(cyclone);

		final int size = potentialColliders.size();
		for (int i = 0; i < size; i++) {

			final GameObject obj = potentialColliders.get(i);

			if (OverlapTester.overlapRectangles(obj.bounds, cyclone.bounds)) {

				if (obj instanceof Obstacle) {
					final Obstacle o = (Obstacle) obj;

					cyclone.hitObstacle(o);
					Log.d("HIT", "Obstacle hit");
					listener.obstacle();
					grid.removeObject(o); // can only hit tree once

				}

				if (obj instanceof Target) {
					final Target t = (Target) obj;

					if (t.type == Target.TYPE_NORMAL) {
						cyclone.hitTarget(t);
						t.explode();
						Log.d("HIT", "Target hit");
						listener.target();
						// targets.remove(t);
						grid.removeObject(t);
					} else {
						Log.d("HIT", "BOSS HIT!");
						// t.explode();
						listener.levelEnd();
						state = WORLD_STATE_NEXT_LEVEL;
					}

					score += t.score;

				}

				if (obj instanceof PowerUp) {
					final PowerUp p = (PowerUp) obj;

					cyclone.hitPowerUp(p);
					Log.d("HIT", "PowerUp hit");
					listener.powerUp();
					powerUps.remove(p);
					grid.removeObject(p);

				}
			}

		}

	}

	private void updateObstacles(float deltaTime) {
		// for future use
	}

	private void updatePowerUps(float deltaTime) {
		final int size = powerUps.size();
		for (int i = 0; i < size; i++) {
			powerUps.get(i).update(deltaTime);
		}
	}

	private void updateTargets(float deltaTime) {
		final int size = targets.size();
		for (int i = 0; i < size; i++) {
			targets.get(i).update(deltaTime);
		}
	}

	private void updateCyclone(float deltaTime, float accelX) {

		if (cyclone.currentState != Cyclone.STATE_DEAD) {
			cyclone.velocity.x = -accelX / 10 * Cyclone.CYCLONE_VELOCITY_X;
		}
		cyclone.update(deltaTime);
		currentDistance = Math.max(cyclone.position.y, currentDistance);

	}

}
