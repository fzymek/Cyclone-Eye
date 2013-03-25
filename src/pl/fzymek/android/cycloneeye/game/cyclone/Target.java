package pl.fzymek.android.cycloneeye.game.cyclone;

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

	public Target(float x, float y, int score, int type) {
		super(x, y, TARGET_WIDTH, TARGET_HEIGHT);
		this.score = score;
		this.state = STATE_OK;
		this.type = type;
	}

	public void update(float deltaTime) {

	}

}
