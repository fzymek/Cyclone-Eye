package pl.fzymek.android.cycloneeye.game.cyclone;

import java.util.Random;

import pl.fzymek.android.cycloneeye.game.engine.GameObject;
import android.util.Log;

public class Obstacle extends GameObject {

	public static float OBSTACLE_WIDTH = 10f;
	public static float OBSTACLE_HEIGHT = 10f;

	public static float DRAW_WIDTH = 13.0f;
	public static float DRAW_HEIGHT = 13.0f;

	public static final int STATE_OK = 0;
	public static final int STATE_HIT = 1;

	public int state;
	public float slowdown;
	public long duration;
	public final int id;

	public Obstacle(float x, float y, float bonus, long duration) {
		super(x, y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
		this.slowdown = bonus;
		this.duration = duration;
		Log.d("Obstacle", "Dur: " + duration);

		id = new Random().nextInt(Assets.NUMBER_OF_OBSTACLES);

	}

	public void update(float deltaTime) {

	}
}
