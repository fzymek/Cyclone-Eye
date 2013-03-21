package pl.fzymek.android.cycloneeye.game.shapes;

import java.util.Random;

public class Cyclone {

	private final static String TAG = Cyclone.class.getSimpleName();

	private final static Random rnd = new Random();
	public float x, y;
	float dirX, dirY;

	public Cyclone() {

		x = rnd.nextFloat();
		y = rnd.nextFloat();
		dirY = rnd.nextFloat() / 1000;
		dirX = rnd.nextFloat() / 1000;

		// Log.d(TAG, String.format(
		// "x: %1.5f, y: %1.5f, dirX: %1.5f, dirY: %1.5f", x, y, dirX,
		// dirY));

	}

	public void update(float time) {

		x += dirX * time;
		y += dirY * time;

		// Log.d(TAG, "" + x + " " + y);

		if (x < -1f) {
			dirX = -dirX;
			x = -1f;
		}

		if (x > 1f) {
			dirX = -dirX;
			x = 1f;
		}

		if (y < -1f) {
			dirY = -dirY;
			y = -1f;
		}

		if (y > 1f) {
			dirY = -dirY;
			y = 1f;
		}

	}

}
