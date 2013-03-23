package pl.fzymek.android.cycloneeye.game.engine.math;

import android.annotation.SuppressLint;


public class Circle {

	public final Vector2 center = new Vector2();
	public float radius;

	public Circle(float x, float y, float radius) {
		center.set(x, y);
		this.radius = radius;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public String toString() {
		return String.format("Circle (x: %f, y: %d, R: %f)", center.x,
				center.y, radius);
	}

}
