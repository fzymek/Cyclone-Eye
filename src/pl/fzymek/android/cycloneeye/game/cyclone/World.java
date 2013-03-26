package pl.fzymek.android.cycloneeye.game.cyclone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.fzymek.android.cycloneeye.game.engine.GameObject;
import pl.fzymek.android.cycloneeye.game.engine.SpatialHashGrid;
import pl.fzymek.android.cycloneeye.game.engine.math.OverlapTester;
import pl.fzymek.android.cycloneeye.game.engine.math.Vector2;
import android.util.Log;

public class World {

	public static final float SCREEN_PER_LEVEL_MULTIPLIER = 1.5f;

	// world states
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;

	// world properties
	public final static float WORLD_WIDTH = 48.0f;
	public final static float WORLD_HEIGHT = 5 * 85.4f;
	public final static long SECOND = 1000 * 1000 * 1000;

	public static final int NUMBER_OF_OBSTACLES = 15;
	public static final int NUMBER_OF_TARGETS = 8;
	public static final int NUMBER_OF_POWERUPS = 5;

	public final static float CAMERA_FRUSTUM_WIDTH = 48.0f;
	public final static float CAMERA_FRUSTUM_HEIGHT = 85.4f;

	public interface WorldEventsListener {
		void obstacle();

		void powerUp();

		void target();

		void gameOver();
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

	public World(final WorldEventsListener listener) {
		this.listener = listener;

		cyclone = new Cyclone(WORLD_WIDTH / 2, 3.0f);
		obstacles = new ArrayList<Obstacle>();
		targets = new ArrayList<Target>();
		powerUps = new ArrayList<PowerUp>();
		rnd = new Random(System.currentTimeMillis());

		grid = new SpatialHashGrid(WORLD_WIDTH, WORLD_HEIGHT, 50.0f);

		generateLevel(1);

		currentDistance = 0.0f;
		Log.d("World", "World created");

	}

	private void generateLevel(int level) {
		Log.d("World", "Generating level");

		for (int i = 0; i < NUMBER_OF_OBSTACLES; i++) {
			float slow = (rnd.nextInt(30) + 60) / 100.0f;
			Obstacle o = new Obstacle(rnd.nextFloat() * WORLD_WIDTH,
					rnd.nextFloat() * WORLD_HEIGHT + 10, slow, 3L * SECOND);

			obstacles.add(o);
			grid.insertStaticObject(o);

		}

		for (int i = 0; i < NUMBER_OF_TARGETS; i++) {
			int score = rnd.nextInt(151) + 50;
			Target t = new Target(rnd.nextFloat() * WORLD_WIDTH,
					rnd.nextFloat() * WORLD_HEIGHT + 15, score,
					Target.TYPE_NORMAL);

			targets.add(t);
			grid.insertStaticObject(t);
		}

		for (int i = 0; i < NUMBER_OF_POWERUPS; i++) {
			float speedup = 2.0f;
			PowerUp p = new PowerUp(rnd.nextFloat() * WORLD_WIDTH,
					rnd.nextFloat() * WORLD_HEIGHT + 10, speedup, 2L * SECOND);

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

		if (cyclone.currentState == Cyclone.STATE_HIT) {
			score -= score > 5 ? 5 : 0;
		} else {
			score += 1;
		}

	}

	private void checkGameOver() {

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

				}

				if (obj instanceof Target) {
					final Target t = (Target) obj;

					cyclone.hitTarget(t);
					t.explode();
					Log.d("HIT", "Target hit");
					listener.target();
					// targets.remove(t);
					grid.removeObject(t);

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

		if (cyclone.position.y >= WORLD_HEIGHT - 5.0f) {
			Log.d("World", "Level End!");
			state = WORLD_STATE_NEXT_LEVEL;
		}

	}

}
