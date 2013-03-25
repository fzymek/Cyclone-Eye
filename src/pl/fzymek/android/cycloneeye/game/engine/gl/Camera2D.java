package pl.fzymek.android.cycloneeye.game.engine.gl;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;
import pl.fzymek.android.cycloneeye.game.engine.math.Vector2;
import android.util.Log;

/**
 * Represents 2d world camera
 * 
 * @author Filip
 * 
 */
public class Camera2D {

	private final static String TAG = Camera2D.class.getSimpleName();

	public final Vector2 position;
	public float zoom;
	public final float frustumWidth;
	public final float frustumHeight;
	final CEGLGraphics glGraphics;

	public Camera2D(CEGLGraphics glGraphics, float frustumWidth,
			float frustumHeight) {
		this.glGraphics = glGraphics;
		this.frustumWidth = frustumWidth;
		this.frustumHeight = frustumHeight;
		this.position = new Vector2(frustumWidth / 2, frustumHeight / 2);
		this.zoom = 1.0f;

		Log.d(TAG, "Created with parameters: " + "Frustum width: "
				+ frustumWidth + " Frustum height: " + frustumHeight
				+ " Position: " + position + " Zoom: " + zoom);

	}

	public void setViewportAndMatrices() {
		GL10 gl = glGraphics.getGl();
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(position.x - frustumWidth * zoom / 2, position.x
				+ frustumWidth * zoom / 2, position.y - frustumHeight * zoom
				/ 2, position.y + frustumHeight * zoom / 2, 1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void touchToWorld(Vector2 touch) {
		touch.x = (touch.x / (float) glGraphics.getWidth()) * frustumWidth
				* zoom;
		touch.y = (1 - touch.y / (float) glGraphics.getHeight())
				* frustumHeight * zoom;
		touch.add(position).sub(frustumWidth * zoom / 2,
				frustumHeight * zoom / 2);
	}

}
