package pl.fzymek.android.cycloneeye.game.models;

import pl.fzymek.android.cycloneeye.game.engine.GameObject;

public class Cannon extends GameObject {
	public float angle;

	public Cannon(float x, float y, float width, float height) {
		super(x, y, width, height);
		angle = 0;
	}
}
