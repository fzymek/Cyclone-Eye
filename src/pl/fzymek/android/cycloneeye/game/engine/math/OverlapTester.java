package pl.fzymek.android.cycloneeye.game.engine.math;

import android.util.Log;

public class OverlapTester {

	private final static String TAG = OverlapTester.class.getSimpleName();

	public static boolean overlapCircles(final Circle c1, final Circle c2) {
		final float distance = c1.center.distSquared(c2.center);
		final float radiusSum = c1.radius + c2.radius;

		//Log.d(TAG, "Test Overlap: " + c1 + " " + c2);

		return distance <= radiusSum * radiusSum;
	}

	public static boolean overlapRectangles(final Rectangle r1,
			final Rectangle r2) {
		//Log.d(TAG, "Test Overlap: " + r1 + " " + r2);

		if (r1.lowerLeft.x < r2.lowerLeft.x + r2.width
				&& r1.lowerLeft.x + r1.width > r2.lowerLeft.x
				&& r1.lowerLeft.y < r2.lowerLeft.y + r2.height
				&& r1.lowerLeft.y + r1.height > r2.lowerLeft.y)
			return true;
		else
			return false;
	}

	public static boolean overlapCircleRectangle(final Circle c,
			final Rectangle r) {

		//Log.d(TAG, "Test Overlap: " + c + " " + r);

		float closestX = c.center.x;
		float closestY = c.center.y;
		if (c.center.x < r.lowerLeft.x) {
			closestX = r.lowerLeft.x;
		} else if (c.center.x > r.lowerLeft.x + r.width) {
			closestX = r.lowerLeft.x + r.width;
		}
		if (c.center.y < r.lowerLeft.y) {
			closestY = r.lowerLeft.y;
		} else if (c.center.y > r.lowerLeft.y + r.height) {
			closestY = r.lowerLeft.y + r.height;
		}
		return c.center.distSquared(closestX, closestY) < c.radius * c.radius;
	}

	public static boolean pointInCircle(final Circle c, final Vector2 p) {

		//Log.d(TAG, "Test Overlap: " + c + " " + p);

		return c.center.distSquared(p) < c.radius * c.radius;
	}

	public static boolean pointInCircle(final Circle c, final float x,
			final float y) {
		
		//Log.d(TAG, "Test Overlap: " + c + " " + "(" + x + ", " + y + ")");
		
		return c.center.distSquared(x, y) < c.radius * c.radius;
	}

	public static boolean pointInRectangle(final Rectangle r, final Vector2 p) {

		//Log.d(TAG, "Test Overlap: " + r + " " + p);

		return r.lowerLeft.x <= p.x && r.lowerLeft.x + r.width >= p.x
				&& r.lowerLeft.y <= p.y && r.lowerLeft.y + r.height >= p.y;
	}

	public static boolean pointInRectangle(final Rectangle r, final float x,
			final float y) {

		//Log.d(TAG, "Test Overlap: " + r + " " + "(" + x + ", " + y + ")");

		return r.lowerLeft.x <= x && r.lowerLeft.x + r.width >= x
				&& r.lowerLeft.y <= y && r.lowerLeft.y + r.height >= y;
	}
}
