package pl.fzymek.android.cycloneeye.game.cyclone;

import pl.fzymek.android.cycloneeye.game.engine.GameObject;
import android.util.Log;

public class Obstacle extends GameObject {

	public static float OBSTACLE_WIDTH = 8f;
	public static float OBSTACLE_HEIGHT = 8f;

	public static final int STATE_OK = 0;
	public static final int STATE_HIT = 1;

	public int state;
	public float slowdown;
	public long duration;

	public Obstacle(float x, float y, float bonus, long duration) {
		super(x, y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
		this.slowdown = bonus;
		this.duration = duration;
		Log.d("Obstacle", "Dur: " + duration);
	}

	public void update(float deltaTime) {

	}
}
