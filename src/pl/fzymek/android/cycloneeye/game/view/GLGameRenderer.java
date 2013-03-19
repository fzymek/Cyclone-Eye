package pl.fzymek.android.cycloneeye.game.view;

import static android.opengl.GLES10.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.shapes.IDrawable;
import pl.fzymek.android.cycloneeye.game.shapes.Tornado;
import pl.fzymek.android.cycloneeye.game.shapes.Triangle;
import pl.fzymek.android.cycloneeye.ui.acitivites.GameActivity;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;

public class GLGameRenderer implements Renderer {

	private static final String TAG = GLGameRenderer.class.getSimpleName();
	private static final int NUMBER_OF_SPRITES = 5;
	private static final float NUMBER_OF_ROWS = 1;
	private static final float NUMBER_OF_COLUMNS = 8;
	private GameActivity context;
	private long mLastTime;

	private IDrawable[] drawables;
	private Tornado tornado;

	public GLGameRenderer(GameActivity context) {
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 gl) {

		glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		long time = SystemClock.uptimeMillis();
		long time_delta = (time - mLastTime);
		// final long fps = (long) (1000.0f / time_delta);
		// Log.d(TAG, "FPS: " + fps);

		// DRAWING GOES HERE

		draw_tornado(gl, time_delta);

		for (int i = 0; i < drawables.length; i++) {
			// glMatrixMode(GL10.GL_MODELVIEW);
			// glLoadIdentity();
			// glPushMatrix();
			// glScalef(1.0f, 1.0f, 1.0f);
			// glTranslatef(0.0f, 0.0f, 0.0f);
			drawables[i].draw(gl, time_delta);
//			glPopMatrix();
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
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		glShadeModel(GL10.GL_SMOOTH);
		glClearDepthf(1.0f);
		glEnable(GL10.GL_DEPTH_TEST);
		glDepthFunc(GL10.GL_LEQUAL);

		glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		glEnable(GL10.GL_BLEND);
		glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);

		glClearColor(0.45f, 0.56f, 0.67f, 1.0f);
		tornado = new Tornado();
		tornado.loadTexture(gl, R.drawable.tornado_sprites_mine, context);

		drawables = new IDrawable[] { new Triangle() };
		Log.d(TAG, "onSurfaceCreated");

	}

	static int frames = 0;
	private void draw_tornado(GL10 gl, long time) {

		glMatrixMode(GL10.GL_MODELVIEW);
		glLoadIdentity();
		glPushMatrix();
		glScalef(0.25f, 0.25f, 1.0f);
		glTranslatef(0.0f, 0.0f, 0.0f);

		glMatrixMode(GL_TEXTURE);
		glLoadIdentity();

		int id = frames++ % NUMBER_OF_SPRITES;

		float u = id * (1 / NUMBER_OF_COLUMNS);
		float v = 0.0f;// id * (1 / NUMBER_OF_ROWS);

		Log.d("DrawTornado", "(" + u + ", " + v + ")");

		glTranslatef(u, v, 0.0f);

		tornado.draw(gl, time);
		glPopMatrix();
		glLoadIdentity();
	}
}
