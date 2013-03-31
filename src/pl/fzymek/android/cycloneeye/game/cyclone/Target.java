package pl.fzymek.android.cycloneeye.game.cyclone;

import java.util.Random;

import pl.fzymek.android.cycloneeye.game.engine.GameObject;

public class Target extends GameObject {

	public static float TARGET_WIDTH = 10.0f;
	public static float TARGET_HEIGHT = 10.0f;

	public static float DRAW_WIDTH = 13.0f;
	public static float DRAW_HEIGHT = 13.0f;

	public static final int STATE_OK = 0;
	public static final int STATE_DESTROYED = 1;

	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_LEVEL_END = 1;

	public static final int TARGET_TYPE_LEVEL_END_SIZE_MULTIPLIER = 3;

	public int score;
	public int state;
	public int type;
	public float stateTime;
	public final int id;

	public Target(float x, float y, int score, int type) {
		super(x, y, type == TYPE_LEVEL_END ? TARGET_HEIGHT
				* TARGET_TYPE_LEVEL_END_SIZE_MULTIPLIER : TARGET_HEIGHT,
				type == TYPE_LEVEL_END ? TARGET_WIDTH
						* TARGET_TYPE_LEVEL_END_SIZE_MULTIPLIER : TARGET_WIDTH);

		this.score = score;
		this.state = STATE_OK;
		this.type = type;
		stateTime = 0;
		id = new Random().nextInt(Assets.NUMBER_OF_TARGETS - 1);
	}

	public void update(float deltaTime) {
		stateTime += deltaTime;
	}

	public void explode() {
		stateTime = 0.0f;
		state = STATE_DESTROYED;
	}

}
