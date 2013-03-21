package pl.fzymek.android.cycloneeye.game.shapes;

import java.util.Random;

public class Cyclone {

	private final static Random rnd = new Random();
	public float x, y;
	float dirX, dirY;

	public Cyclone() {

		x = rnd.nextFloat();
		y = rnd.nextFloat();
		dirY = dirX = 0.005f;

	}

	public void update(float time) {

		x += dirX * time;
		y += dirY * time;

		if (x < -0.8f) {
			dirX = -dirX;
			x = -1;
		}

		if (x > 0.8f) {
			dirX = -dirX;
			x = 1;
		}

		if (y < -0.8f) {
			dirY = -dirY;
			y = -1;
		}

		if (y > 0.8f) {
			dirY = -dirY;
			y = 1;
		}

	}

}
