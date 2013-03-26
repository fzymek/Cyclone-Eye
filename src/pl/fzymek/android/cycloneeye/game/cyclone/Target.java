package pl.fzymek.android.cycloneeye.game.cyclone;

import java.util.Random;

import pl.fzymek.android.cycloneeye.game.engine.GameObject;

public class Target extends GameObject {

	public static float TARGET_WIDTH = 8.0f;
	public static float TARGET_HEIGHT = 8.0f;

	public static final int STATE_OK = 0;
	public static final int STATE_EXPLODING = 1;
	public static final int STATE_DESTROYED = 2;

	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_LEVEL_END = 0;

	public int score;
	public int state;
	public int type;
	public float stateTime;
	public final int id;

	public Target(float x, float y, int score, int type) {
		super(x, y, TARGET_WIDTH, TARGET_HEIGHT);
		this.score = score;
		this.state = STATE_OK;
		this.type = type;
		stateTime = 0;
		id = new Random().nextInt(Assets.NUMBER_OF_TARGETS);
	}

	public void update(float deltaTime) {
		stateTime += deltaTime;
	}

	public void explode() {
		stateTime = 0.0f;
		state = STATE_EXPLODING;
	}

}
