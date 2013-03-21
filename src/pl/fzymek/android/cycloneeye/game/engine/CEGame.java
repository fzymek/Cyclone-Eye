package pl.fzymek.android.cycloneeye.game.engine;

import static android.opengl.GLES10.glClear;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.impl.CEGameAudio;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGameFileIO;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.Audio;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.FileIO;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.GLGraphics;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.Game;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.Graphics;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.Input;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.Screen;
import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class CEGame extends Activity implements Game, Renderer {

	private final static String TAG = CEGame.class.getSimpleName();

	enum GLGameState {
		Initialized, Running, Paused, Finished, Idle
	}

	GLSurfaceView glView;
	GLGraphics glGraphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GLGameState state = GLGameState.Initialized;
	Object stateChanged = new Object();
	long startTime = System.currentTimeMillis();
	WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		glView = new GLSurfaceView(this);
		glView.setRenderer(this);
		setContentView(glView);
		glGraphics = new GLGraphics(glView);
		fileIO = new CEGameFileIO(this);
		audio = new CEGameAudio(this);
		input = new CEGameInput(this, glView, 1, 1);
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				getClass().getSimpleName());

	}

	@Override
	protected void onResume() {
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
	}

	@Override
	protected void onPause() {
		synchronized (stateChanged) {
			if (isFinishing())
				state = GLGameState.Finished;
			else
				state = GLGameState.Paused;
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

	}

	@Override
	public void onDrawFrame(GL10 gl) {

		glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		GLGameState state = null;
		synchronized (stateChanged) {
			state = this.state;
		}
		if (state == GLGameState.Running) {
			float deltaTime = (SystemClock.uptimeMillis() - startTime) / 1000.0f;
			Log.d(TAG, "Delta Time: " + deltaTime);
			Log.d(TAG, "FPS: " + (long) (1000.0f / deltaTime));
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
		}
		if (state == GLGameState.Finished) {
			screen.pause();
			screen.dispose();
			synchronized (stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
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
			}
			state = GLGameState.Running;
			screen.resume();
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
	public Graphics getGraphics() {
		throw new IllegalStateException("OpenGL is in use!");
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

	public GLGraphics getGlGraphics() {
		return glGraphics;
	}

}
