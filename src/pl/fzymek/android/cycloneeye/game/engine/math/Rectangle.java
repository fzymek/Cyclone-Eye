package pl.fzymek.android.cycloneeye.game.engine.math;

import android.annotation.SuppressLint;


public class Rectangle {
	public final Vector2 lowerLeft;
	public float width, height;

	public Rectangle(float x, float y, float width, float height) {
		this.lowerLeft = new Vector2(x, y);
		this.width = width;
		this.height = height;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public String toString() {
		return String.format(
				"Rectangle: (x: %f, y: %f, width: %f, height: %d)",
				lowerLeft.x, lowerLeft.y, width, height);
	}
}
