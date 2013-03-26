package pl.fzymek.android.cycloneeye.game.cyclone;

import java.util.Random;

import pl.fzymek.android.cycloneeye.game.engine.GameObject;

/**
 * Represents in-game powerup
 * 
 * 
 * @author Filip
 * 
 */
public class PowerUp extends GameObject {

	public static float POWERUP_WIDTH = 8.0f;
	public static float POWERUP_HEIGHT = 8.0f;

	public static final int STATE_OK = 0;
	public static final int STATE_COLLECTED = 1;

	public int state;
	public float bonus;
	public long duration;
	public float stateTime;
	public final int id;

	public PowerUp(float x, float y, float bonus, long duration) {
		super(x, y, POWERUP_WIDTH, POWERUP_HEIGHT);
		this.bonus = bonus;
		this.duration = duration;
		state = STATE_OK;
		id = new Random().nextInt(Assets.NUMBER_OF_POWERUPS);
	}

	public void update(float deltaTime) {
		stateTime += deltaTime;
	}

}
