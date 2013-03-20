package pl.fzymek.android.cycloneeye.game.view;

import static android.opengl.GLES10.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.engine.CEEngine;
import pl.fzymek.android.cycloneeye.game.engine.TextureLoader;
import pl.fzymek.android.cycloneeye.game.shapes.Grass;
import pl.fzymek.android.cycloneeye.game.shapes.IDrawable;
import pl.fzymek.android.cycloneeye.game.shapes.Tornado;
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
	private Tornado tornado;
	private Grass grass;

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
		scrollGrass(gl, time_delta);
		draw_tornado(gl, time_delta);


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
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		glShadeModel(GL10.GL_SMOOTH);
		glClearDepthf(1.0f);
		glEnable(GL10.GL_DEPTH_TEST);
		glDepthFunc(GL10.GL_LEQUAL);

		glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		glEnable(GL10.GL_BLEND);
		glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);

		glClearColor(0.45f, 0.56f, 0.67f, 1.0f);

		TextureLoader txLoader = new TextureLoader(gl);

		int[] sprites = txLoader.loadTexture(gl, R.drawable.tornado_sprites,
				context, 0, GL_CLAMP_TO_EDGE);
		sprites = txLoader.loadTexture(gl, R.drawable.grass, context, 1,
				GL_REPEAT);
		grass = new Grass(sprites[1]);
		tornado = new Tornado(sprites[0]);

		drawables = new IDrawable[] { new Triangle() };
		Log.d(TAG, "onSurfaceCreated");

	}

	private void draw_tornado(GL10 gl, long time) {

		tornado.x += 0.005f * time * Tornado.position[0];

		if (tornado.x > 3.5f) {
			tornado.x = 3.5f;
		}
		if (tornado.x < -3.5f) {
			tornado.x = -3.5f;
		}

		glMatrixMode(GL10.GL_MODELVIEW);
		glLoadIdentity();
		glScalef(0.25f, 0.25f, 1.0f);
		glTranslatef(tornado.x, 0.0f, 0.0f);
		glPushMatrix();

		glMatrixMode(GL_TEXTURE);
		glLoadIdentity();
		glPushMatrix();
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		float u = 0.0f;

		if (Tornado.position[0] > 0.0f) {
			u = 0.5f;
		} else if (Tornado.position[0] < 0.0f) {
			u = 0.25f;
		}
		//
		Log.d("DrawTornado", "(" + u + ", " + 0.0f + ")");
		//

		glTranslatef(u, 0.0f, 0.0f);

		tornado.draw(gl, time);
		glPopMatrix();
		glLoadIdentity();
		glBlendFunc(GL_ONE, GL_ONE);
	}

	private void scrollGrass(GL10 gl, long time) {

		glMatrixMode(GL10.GL_MODELVIEW);
		glLoadIdentity();
		glPushMatrix();
		glScalef(1.0f, 1.0f, 1.0f);
		glTranslatef(0.0f, 0.0f, 0.0f);

		glMatrixMode(GL_TEXTURE);
		glLoadIdentity();
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glTranslatef(0.0f, grass.y, 0.0f);
		grass.draw(gl, time);
		glPopMatrix();

		grass.y += CEEngine.BACKGROUND_SCROLL;

		glLoadIdentity();
		glBlendFunc(GL_ONE, GL_ONE);
	}
}
