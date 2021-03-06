package pl.fzymek.android.cycloneeye.game.engine.impl;

import static android.opengl.GLES10.glClear;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.Audio;
import pl.fzymek.android.cycloneeye.game.engine.FileIO;
import pl.fzymek.android.cycloneeye.game.engine.GLGame;
import pl.fzymek.android.cycloneeye.game.engine.Input;
import pl.fzymek.android.cycloneeye.game.engine.Screen;
import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class CEGame extends Activity implements GLGame, Renderer {

	private final static String TAG = CEGame.class.getSimpleName();

	enum GLGameState {
		Initialized, Running, Paused, Finished, Idle
	}

	GLSurfaceView glView;
	CEGLGraphics glGraphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GLGameState state = GLGameState.Initialized;
	Object stateChanged = new Object();
	// long startTime = SystemClock.uptimeMillis();
	long startTime = System.nanoTime();
	WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Log.d(TAG, "Full screen requested");

		glView = new GLSurfaceView(this);
		glView.setRenderer(this);
		setContentView(glView);

		Log.d(TAG, "GL View created");

		glGraphics = new CEGLGraphics(glView);
		fileIO = new CEGameFileIO(this);
		audio = new CEGameAudio(this);
		input = new CEGameInput(this, glView, 1, 1);

		Log.d(TAG, "Graphics, Music, Audio created");

		final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				getClass().getSimpleName());

		Log.d(TAG, "Wake Lock created");

	}

	@Override
	protected void onResume() {
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
		Log.d(TAG, "onResume, Wake Lock acquired");
	}

	@Override
	protected void onPause() {
		synchronized (stateChanged) {
			if (isFinishing()) {
				state = GLGameState.Finished;
			} else {
				state = GLGameState.Paused;
			}

			Log.d(TAG, "onPause, state: " + state);

			while (true) {
				try {
					stateChanged.wait();
					break;
				} catch (InterruptedException e) {
				}
			}
		}
		super.onPause();
		glView.onPause();
		wakeLock.release();
		Log.d(TAG, "onPause, Wake Lock released");

	}

	@Override
	public void onDrawFrame(GL10 gl) {

		glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		GLGameState state = null;
		synchronized (stateChanged) {
			state = this.state;
		}
		if (state == GLGameState.Running) {
			// float deltaTime = SystemClock.uptimeMillis() - startTime;
			final float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			// startTime = SystemClock.uptimeMillis();
			startTime = System.nanoTime();
			screen.update(deltaTime);
			screen.present(deltaTime);
		}
		if (state == GLGameState.Paused) {
			screen.pause();
			synchronized (stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
			// Log.d(TAG, "onDrawFrame, state: " + state);
		}
		if (state == GLGameState.Finished) {
			screen.pause();
			screen.dispose();
			synchronized (stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
			// Log.d(TAG, "onDrawFrame, state: " + state);
		}

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-1f, 1f, -1f, 1f, -1f, 1f);

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		glGraphics.setGl(gl);
		synchronized (stateChanged) {
			if (state == GLGameState.Initialized) {
				screen = getStartScreen();
				Log.d(TAG, "onSurfaceCreated, showing screen: " + screen);
			}
			state = GLGameState.Running;
			screen.resume();
			// startTime = SystemClock.uptimeMillis();
			startTime = System.nanoTime();
		}

	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void setScreen(Screen newScreen) {
		if (screen == null) {
			throw new IllegalArgumentException("Screen must not be null");
		}
		this.screen.pause();
		this.screen.dispose();
		newScreen.resume();
		newScreen.update(0);
		Log.d(TAG, "setScreen: screen transition: from: " + screen + " to: "
				+ newScreen);
		this.screen = newScreen;
		

	}

	@Override
	public Screen getCurrentScreen() {
		return screen;
	}

	@Override
	public Screen getStartScreen() {
		return null;
	}

	@Override
	public CEGLGraphics getGlGraphics() {
		return glGraphics;
	}

}
