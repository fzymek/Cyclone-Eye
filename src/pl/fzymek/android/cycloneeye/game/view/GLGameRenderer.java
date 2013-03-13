package pl.fzymek.android.cycloneeye.game.view;

import static android.opengl.GLES10.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.shapes.IDrawable;
import pl.fzymek.android.cycloneeye.game.shapes.Triangle;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

public class GLGameRenderer implements Renderer {
	private static final int PROFILE_REPORT_DELAY = 3 * 1000;
	private Context context;
	private TextView score;
	private long mLastTime;
	private int mProfileFrames;
	private long mProfileWaitTime;
	private long mProfileFrameTime;
	private long mProfileSubmitTime;
	private int mProfileObjectCount;
	
	private IDrawable[] drawables;

	public GLGameRenderer(Context context, TextView score) {
		this.context = context;
		this.score = score;
	}

	@Override
	public void onDrawFrame(GL10 gl) {

		long time = SystemClock.uptimeMillis();
		long time_delta = (time - mLastTime);

		final long wait = SystemClock.uptimeMillis();
		// DRAWING GOES HERE

		for (int i = 0; i < drawables.length; i++) {
			drawables[i].draw(gl);
		}

		long time2 = SystemClock.uptimeMillis();
		mLastTime = time2;

		mProfileFrameTime += time_delta;
		mProfileSubmitTime += time2 - time;
		mProfileWaitTime += wait - time;

		mProfileFrames++;
		if (mProfileFrameTime > PROFILE_REPORT_DELAY) {
			final int validFrames = mProfileFrames;
			final long averageFrameTime = mProfileFrameTime / validFrames;
			final long averageSubmitTime = mProfileSubmitTime / validFrames;
			final float averageObjectsPerFrame = (float) mProfileObjectCount
					/ validFrames;
			final long averageWaitTime = mProfileWaitTime / validFrames;

			Log.d("Render Profile", "Average Submit: " + averageSubmitTime
					+ "  Average Draw: " + averageFrameTime
					+ " Objects/Frame: " + averageObjectsPerFrame
					+ " Wait Time: " + averageWaitTime);

			score.setText("FPS: " + averageWaitTime);
			mProfileFrameTime = 0;
			mProfileSubmitTime = 0;
			mProfileFrames = 0;
			mProfileObjectCount = 0;
		}

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

	}

}
