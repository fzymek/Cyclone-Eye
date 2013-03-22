package pl.fzymek.android.cycloneeye.game.models;

import java.util.Random;

public class CycloneModel {

	private final static String TAG = CycloneModel.class.getSimpleName();

	private final static Random rnd = new Random();
	public float x, y;
	float dirX, dirY;

	public CycloneModel() {

		x = rnd.nextFloat();
		y = rnd.nextFloat();
		dirY = rnd.nextFloat();
		dirX = rnd.nextFloat();

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
