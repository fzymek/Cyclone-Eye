package pl.fzymek.android.cycloneeye.game.screens;

import static android.opengl.GLES10.*;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.cyclone.Assets;
import pl.fzymek.android.cycloneeye.game.cyclone.Settings;
import pl.fzymek.android.cycloneeye.game.cyclone.World;
import pl.fzymek.android.cycloneeye.game.cyclone.WorldRenderer;
import pl.fzymek.android.cycloneeye.game.engine.Input.TouchEvent;
import pl.fzymek.android.cycloneeye.game.engine.Screen;
import pl.fzymek.android.cycloneeye.game.engine.gl.Camera2D;
import pl.fzymek.android.cycloneeye.game.engine.gl.FPSCounter;
import pl.fzymek.android.cycloneeye.game.engine.gl.SpriteBatcher;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEScreen;
import pl.fzymek.android.cycloneeye.game.engine.math.Rectangle;
import pl.fzymek.android.cycloneeye.game.engine.math.Vector2;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;

public class GameActivity extends CEGame {

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static class GameScreen extends CEScreen {

		static final int GAME_READY = 0;
		static final int GAME_RUNNING = 1;
		static final int GAME_PAUSED = 2;
		static final int GAME_LEVEL_END = 3;
		static final int GAME_OVER = 4;

		static final int GUI_WIDTH = 480;
		static final int GUI_HEIGHT = 854;
		final String TAG = GameScreen.class.getSimpleName();

		static int state;
		Camera2D guiCamera;
		Vector2 touchPoint;
		SpriteBatcher batcher;
		World world;
		World.WorldEventsListener listener;
		WorldRenderer renderer;
		Rectangle resumeBounds;
		Rectangle pauseBounds;
		Rectangle quitBounds;
		FPSCounter fpsCounter;

		public GameScreen(CEGame game) {
			super(game);
			state = GAME_READY;
			guiCamera = new Camera2D(glGraphics, GUI_WIDTH, GUI_HEIGHT);
			touchPoint = new Vector2();
			batcher = new SpriteBatcher(glGraphics, 500);
			fpsCounter = new FPSCounter();

			Log.d(TAG, "Creating GameScreen, camera, batcher state created");

			listener = new World.WorldEventsListener() {

				final Vibrator vibr = (Vibrator) GameScreen.this.game
						.getSystemService(Context.VIBRATOR_SERVICE);
				final String tag = "WorldEventsListener";

				@Override
				public void target() {
					Log.d(tag, "target hit");
					if (Settings.soundsEnabled) {
						Assets.explosionSound.play(1.0f);
					}
				}

				@Override
				public void powerUp() {
					Log.d(tag, "power up hit");
					if (Settings.soundsEnabled) {
						Assets.collectSound.play(1.0f);
					}
				}

				@Override
				public void obstacle() {
					Log.d(tag, "obstacle hit");
					if (Settings.vibrationsEnabled) {
						vibr.vibrate(50);
					}
				}

				@Override
				public void gameOver() {
					Log.d(tag, "game over");
				}
			};

			world = new World(listener);
			renderer = new WorldRenderer(glGraphics, batcher, world);

			Log.d(TAG, "World created");
		}

		@Override
		public void update(float deltaTime) {

			if (deltaTime > 0.1f) {
				deltaTime = 0.1f;
			}

			switch (state) {
				case GAME_READY:
					goToReady();
					break;
				case GAME_RUNNING:
					goToRunning(deltaTime);
					break;
				case GAME_PAUSED:
					goToPaused();
					break;
				case GAME_LEVEL_END:
					goToLevelEnd();
					break;
				case GAME_OVER:
					goToGameOver();
					break;

				default:
					break;
			}

		}

		private void goToGameOver() {

		}

		private void goToLevelEnd() {

			if (game.getInput().getTouchEvents().size() > 0) {
				int lastScore = world.score;
				state = GAME_READY;
				world = new World(listener);
				renderer = new WorldRenderer(glGraphics, batcher, world);
				world.score = lastScore;
			}

		}

		private void goToPaused() {

			if (game.getInput().getTouchEvents().size() > 0) {
				state = GAME_RUNNING;
			}

		}

		private void goToRunning(float deltaTime) {
			final List<TouchEvent> touchEvents = game.getInput()
					.getTouchEvents();
			final int size = touchEvents.size();
			for (int i = 0; i < size; i++) {
				final TouchEvent event = touchEvents.get(i);

				if (event.type != TouchEvent.TOUCH_UP) {
					// Log.d(TAG, "RUNNING, waiting for interaction");
					continue;
				}
				touchPoint.set(event.x, event.y);
				guiCamera.touchToWorld(touchPoint);

				// if (OverlapTester.pointInRectangle(pauseBounds, touchPoint))
				// {
				// state = GAME_PAUSED;
				// // Log.d(TAG, "RUNNING,  pausing");
				// return;
				// }
			}

			// Log.d(TAG, "RUNNING, updating world");
			world.update(deltaTime, game.getInput().getAccelX());

			// TODO: update scoring here

			if (world.state == World.WORLD_STATE_NEXT_LEVEL) {
				state = GAME_LEVEL_END;
			}

			if (world.state == World.WORLD_STATE_GAME_OVER) {
				state = GAME_OVER;
				// TODO: update scoring here
			}
		}

		private void goToReady() {
			if (game.getInput().getTouchEvents().size() > 0) {
				state = GAME_RUNNING;
			}
		}

		@Override
		public void present(float deltaTime) {

			final GL10 gl = glGraphics.getGl();
			gl.glClear(GL_COLOR_BUFFER_BIT);
			gl.glEnable(GL_TEXTURE_2D);

			// Log.d(TAG, "Rendering with renderer");
			renderer.render();
			//
			guiCamera.setViewportAndMatrices();
			// Log.d(TAG, "camera viewport set");

			gl.glEnable(GL_BLEND);
			gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			// batcher.beginBatch(Assets.text);

			switch (state) {

				case GAME_RUNNING:
					gl.glLoadIdentity();
					Assets.font.PrintAt(gl, "Score: " + world.score, 0, 0);
					break;
				case GAME_READY:
					gl.glLoadIdentity();
					final String msg = "Tap screen to start...";
					Assets.font.PrintAt(gl, msg, 220, 450);
					break;
				case GAME_PAUSED:
					gl.glLoadIdentity();
					final String msg1 = "PAUSED";
					final String msg2 = "Tap screen to resume...";
					Assets.font.PrintAt(gl, msg1, 220, 450);
					Assets.font.PrintAt(gl, msg2, 0, 0);
					break;
				case GAME_LEVEL_END:
					gl.glLoadIdentity();
					final String msg11 = "Level Finished";
					final String msg22 = "Tap screen to continue...";
					Assets.font.PrintAt(gl, msg11, 220, 450);
					Assets.font.PrintAt(gl, msg22, 0, 0);
					break;
				case GAME_OVER:
				default:
					break;
			}
			// batcher.endBatch();

			fpsCounter.LogFPS();

		}

		@Override
		public void pause() {
			if (state == GAME_READY) {
				state = GAME_PAUSED;
			}
		}

		@Override
		public void resume() {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

	}

	private boolean firstTimeLoading = true;

	@Override
	public Screen getStartScreen() {
		Log.d("GameActivity", "Fetching game screen");
		return new GameScreen(this);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);

		if (firstTimeLoading) {
			Log.d("GameActivity", "Loading assets");
			Assets.load(this);
			firstTimeLoading = false;
		} else {
			Log.d("GameActivity", "Reloading assets");
			Assets.reload(this);
		}

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		Log.d("CEGame", "Menu pressed");
		GameScreen.state = GameScreen.GAME_PAUSED;
		return super.onPrepareOptionsMenu(menu);
	}

}
