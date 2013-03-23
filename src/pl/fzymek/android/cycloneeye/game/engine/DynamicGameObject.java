package pl.fzymek.android.cycloneeye.game.engine;

import pl.fzymek.android.cycloneeye.game.engine.math.Vector2;
import android.util.Log;

/**
 * Represents dynamic object which can move - has velocity and acceleration
 * 
 * @author Filip
 * 
 */
public class DynamicGameObject extends GameObject {
	public final Vector2 velocity;
	public final Vector2 accel;

	public DynamicGameObject(float x, float y, float width, float height) {
		super(x, y, width, height);
		velocity = new Vector2();
		accel = new Vector2();

		Log.d(TAG, String.format(
				"Initial Velocity: %s, initial Acceleration: %s", velocity,
				accel));

	}
}
