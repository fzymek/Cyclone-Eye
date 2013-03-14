package pl.fzymek.android.cycloneeye.game.view;

import static android.opengl.GLES10.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.shapes.IDrawable;
import pl.fzymek.android.cycloneeye.game.shapes.Triangle;
import pl.fzymek.android.cycloneeye.ui.acitivites.GameActivity;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;

public class GLGameRenderer implements Renderer {

	private static final String TAG = GLGameRenderer.class.getSimpleName();
	private GameActivity context;
	private long mLastTime;

	private IDrawable[] drawables;

	public GLGameRenderer(GameActivity context) {
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 gl) {

		glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		long time = SystemClock.uptimeMillis();
		long time_delta = (time - mLastTime);
		final long fps = (long) (1000.0f / time_delta);
		Log.d(TAG, "FPS: " + fps);
		
		// DRAWING GOES HERE
		for (int i = 0; i < drawables.length; i++) {
			drawables[i].draw(gl, time_delta);
		}

		mLastTime = SystemClock.uptimeMillis();

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		glViewport(0, 0, width, height);
		glMatrixMode(GL10.GL_PROJECTION);
		glLoadIdentity();
		glOrthof(-1f, 1f, -1f, 1f, -1f, 1f);

	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

		glShadeModel(GL10.GL_SMOOTH);
		glClearDepthf(1.0f);
		glEnable(GL10.GL_DEPTH_TEST);
		glDepthFunc(GL10.GL_LEQUAL);

		glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		glEnable(GL10.GL_BLEND);
		glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);

		glClearColor(0.45f, 0.56f, 0.67f, 1.0f);
		
		drawables = new IDrawable[] { new Triangle() };
		Log.d(TAG, "onSurfaceCreated");

	}

}
