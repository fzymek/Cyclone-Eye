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
	public static float CYCLONE_VELOCITY_X = 30.0f;
	public static float CYCLONE_VELOCITY_Y = 6.0f;

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
		velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * velocityMultiplier
				* deltaTime);
		bounds.lowerLeft.set(position).sub(CYCLONE_WIDTH / 2,
				CYCLONE_HEIGHT / 2);
		
		// if (numberOfObstaclesHit > 3) {
		// currentState = STATE_DEAD;
		// stateTime = 0.0f;
		// }

		if (position.x < 0) {
			position.x = World.WORLD_WIDTH;
		}

		if (position.x > World.WORLD_WIDTH) {
			position.x = 0;
		}

		final long nanoTime = System.nanoTime();
		if (lastMultiplierHitTime + multiplierDuration < nanoTime) {
			// powerup depleted
			Log.d(TAG, "Current time:" + nanoTime);
			Log.d(TAG, "Obstacle time depleted: " + lastMultiplierHitTime);
			velocityMultiplier = 1.0f;
		}

		stateTime += deltaTime;

	}
	
	public void hitTarget() {
		
	}
	
	public void hitObstacle(final Obstacle obstacle) {
		
		currentState = STATE_HIT;
		lastMultiplierHitTime = System.nanoTime();
		Log.d(TAG, "Obstacle hit time: " + lastMultiplierHitTime);
		Log.d(TAG, "Obstacle duration time: " + obstacle.duration);
		velocityMultiplier = obstacle.slowdown;
		multiplierDuration = obstacle.duration;

		if (lastObstacle != obstacle) {
			lastObstacle = obstacle;
			numberOfObstaclesHit++;
			Log.d(TAG, "Number of hits:" + numberOfObstaclesHit);
		}



	}
	
	public void hitPowerUp(final PowerUp pup) {

		lastMultiplierHitTime = System.nanoTime();
		velocityMultiplier = pup.bonus;
		multiplierDuration = pup.duration;

	}

}
