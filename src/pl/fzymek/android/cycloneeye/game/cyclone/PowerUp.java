package pl.fzymek.android.cycloneeye.game.cyclone;

import pl.fzymek.android.cycloneeye.game.engine.GameObject;

/**
 * Represents in-game powerup
 * 
 * Multiplies current velocity by 1.5
 * 
 * @author Filip
 * 
 */
public class PowerUp extends GameObject {

	public static float POWERUP_WIDTH = 6.0f;
	public static float POWERUP_HEIGHT = 4.0f;

	public static final int STATE_OK = 0;
	public static final int STATE_COLLECTED = 1;

	public int state;
	public float bonus;
	public long duration;


	public PowerUp(float x, float y, float bonus, long duration) {
		super(x, y, POWERUP_WIDTH, POWERUP_HEIGHT);
		this.bonus = bonus;
		this.duration = duration;
		state = STATE_OK;
	}

	public void update(float deltaTime) {

	}

}
