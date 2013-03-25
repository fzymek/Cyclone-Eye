/**
 * 
 */
package pl.fzymek.android.cycloneeye.game.cyclone;

import pl.fzymek.android.cycloneeye.game.engine.DynamicGameObject;
import android.util.Log;

/**
 * @author Filip
 *
 */
public class Cyclone extends DynamicGameObject {

	public static float CYCLONE_WIDTH = 6.0f;
	public static float CYCLONE_HEIGHT = 6.0f;
	public static float CYCLONE_VELOCITY_X = 40.0f;
	public static float CYCLONE_VELOCITY_Y = 10.0f;

	public static final int STATE_RUNNING = 0;
	public static final int STATE_DEAD = 1;
	public static final int STATE_HIT = 2;
	public static final int MAX_OBSTACLES_HIT = 3;

	public int currentState = STATE_RUNNING;
	float stateTime = 0.0f;
	
	long lastMultiplierHitTime = 0L;
	long multiplierDuration = 0L;
	float velocityMultiplier = 1.0f;
	int numberOfObstaclesHit = 0;
	Obstacle lastObstacle;

	public Cyclone(float x, float y) {
		super(x, y, CYCLONE_WIDTH, CYCLONE_HEIGHT);
		velocity.set(0, CYCLONE_VELOCITY_Y);
	}

	public void update(final float deltaTime) {
		velocity.add(World.gravity.x * deltaTime, 0);
		position.add(velocity.x * deltaTime, velocity.y * velocityMultiplier
				* deltaTime);
		bounds.lowerLeft.set(position).sub(CYCLONE_WIDTH / 2,
				CYCLONE_HEIGHT / 2);
		
		// if (numberOfObstaclesHit > 3) {
		// currentState = STATE_DEAD;
		// stateTime = 0.0f;
		// }

		// Log.d(TAG, "Multiplier: " + velocityMultiplier);
		// Log.d(TAG, "Velocity: " + velocity.y);

		if (position.x < 0) {
			position.x = World.WORLD_WIDTH;
		}

		if (position.x > World.WORLD_WIDTH) {
			position.x = 0;
		}

		final long currentTime = System.nanoTime();
		if (currentTime < lastMultiplierHitTime + multiplierDuration) {

			velocityMultiplier = 1.0f;
			currentState = STATE_RUNNING;

		}

		stateTime += deltaTime;

	}
	
	public void hitTarget() {
		
	}
	
	public void hitObstacle(final Obstacle obstacle) {
		
		currentState = STATE_HIT;
		lastMultiplierHitTime = System.nanoTime();
		// Log.d(TAG, "Obstacle hit time: " + lastMultiplierHitTime);
		// Log.d(TAG, "Obstacle duration time: " + obstacle.duration);
		// Log.d(TAG, "Obstacle multiplier: " + obstacle.slowdown);
		velocityMultiplier = obstacle.slowdown;
		multiplierDuration = obstacle.duration;

		if (lastObstacle != obstacle) {
			lastObstacle = obstacle;
			numberOfObstaclesHit++;
			Log.d(TAG, "Number of hits:" + numberOfObstaclesHit);
		}

	}
	
	public void hitPowerUp(final PowerUp pup) {

		velocityMultiplier = pup.bonus;

	}

}