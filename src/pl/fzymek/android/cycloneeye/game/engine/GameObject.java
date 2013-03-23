package pl.fzymek.android.cycloneeye.game.engine;

import pl.fzymek.android.cycloneeye.game.engine.math.Rectangle;
import pl.fzymek.android.cycloneeye.game.engine.math.Vector2;
import android.util.Log;

/**
 * Represents static object with position only and bounding box
 * 
 * @author Filip
 * 
 */
public class GameObject {

	protected final String TAG;

	public final Vector2 position;
	public final Rectangle bounds;

	public GameObject(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x - width / 2, y - height / 2, width,
				height);

		TAG = getClass().getSimpleName();

		Log.d(TAG, String.format("Initial Position: %s, Initial Bounds: %s",
				position, bounds));
	}
}
