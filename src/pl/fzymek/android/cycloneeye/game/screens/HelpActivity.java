package pl.fzymek.android.cycloneeye.game.screens;

import static android.opengl.GLES10.GL_TEXTURE_2D;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.engine.Input.TouchEvent;
import pl.fzymek.android.cycloneeye.game.engine.Screen;
import pl.fzymek.android.cycloneeye.game.engine.gl.Camera2D;
import pl.fzymek.android.cycloneeye.game.engine.gl.SpriteBatcher;
import pl.fzymek.android.cycloneeye.game.engine.gl.Texture;
import pl.fzymek.android.cycloneeye.game.engine.gl.TextureRegion;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;
import pl.fzymek.android.cycloneeye.game.engine.impl.GLScreen;
import android.opengl.GLES10;
import android.util.Log;

public class HelpActivity extends CEGame {

	public class HelpScreen3 extends GLScreen {

		Camera2D camera;
		SpriteBatcher batcher;
		Texture texture;
		TextureRegion region;
		float w, h;

		public HelpScreen3(CEGame game) {
			super(game);
			w = game.getGlGraphics().getWidth();
			h = game.getGlGraphics().getHeight();
			camera = new Camera2D(glGraphics, w, h);
			batcher = new SpriteBatcher(glGraphics, 10);

		}

		@Override
		public void update(float deltaTime) {

			final List<TouchEvent> touchEvents = game.getInput()
					.getTouchEvents();
			final int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				final TouchEvent event = touchEvents.get(i);
				if (event.type != TouchEvent.TOUCH_UP) {
					continue;
				}
				((CEGame) game).finish();
				return;
			}

		}

		@Override
		public void present(float deltaTime) {

			final GL10 gl = glGraphics.getGl();
			gl.glClear(GLES10.GL_COLOR_BUFFER_BIT | GLES10.GL_DEPTH_BUFFER_BIT);

			camera.setViewportAndMatrices();

			gl.glEnable(GL_TEXTURE_2D);

			batcher.beginBatch(texture);
			batcher.drawSprite(w / 2, h / 2, w, h, region);
			batcher.endBatch();

		}

		@Override
		public void pause() {
			texture.dispose();
		}

		@Override
		public void resume() {

			texture = new Texture((CEGame) game, R.drawable.help_atlas);
			region = new TextureRegion(texture, 600, 0, 300, 450);
		}

		@Override
		public void dispose() {

		}

	}

	public class HelpScreen2 extends GLScreen {

		Camera2D camera;
		SpriteBatcher batcher;
		Texture texture;
		TextureRegion region;
		float w, h;

		public HelpScreen2(CEGame game) {
			super(game);
			w = game.getGlGraphics().getWidth();
			h = game.getGlGraphics().getHeight();
			camera = new Camera2D(glGraphics, w, h);
			batcher = new SpriteBatcher(glGraphics, 10);
		}

		@Override
		public void update(float deltaTime) {

			final List<TouchEvent> touchEvents = game.getInput()
					.getTouchEvents();
			final int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				final TouchEvent event = touchEvents.get(i);
				if (event.type != TouchEvent.TOUCH_UP) {
					continue;
				}
				game.setScreen(new HelpScreen3((CEGame) game));
				return;
			}

		}

		@Override
		public void present(float deltaTime) {

			final GL10 gl = glGraphics.getGl();
			gl.glClear(GLES10.GL_COLOR_BUFFER_BIT | GLES10.GL_DEPTH_BUFFER_BIT);

			camera.setViewportAndMatrices();

			gl.glEnable(GL_TEXTURE_2D);

			batcher.beginBatch(texture);
			batcher.drawSprite(w / 2, h / 2, w, h, region);
			batcher.endBatch();

		}

		@Override
		public void pause() {
			texture.dispose();
		}

		@Override
		public void resume() {

			texture = new Texture((CEGame) game, R.drawable.help_atlas);

			region = new TextureRegion(texture, 300, 0, 300, 450);
		}

		@Override
		public void dispose() {

		}

	}

	public class HelpScrren1 extends GLScreen {

		Camera2D camera;
		SpriteBatcher batcher;
		Texture texture;
		TextureRegion region;
		float w, h;

		public HelpScrren1(CEGame game) {
			super(game);
			Log.d("Help1", "getting graphics size");
			w = game.getGlGraphics().getWidth();
			h = game.getGlGraphics().getHeight();
			camera = new Camera2D(glGraphics, w, h);
			batcher = new SpriteBatcher(glGraphics, 10);

			Log.d("Help1", "w: " + w + ", h: " + h);

		}

		@Override
		public void update(float deltaTime) {
			Log.d("Help1", "update");
			final List<TouchEvent> touchEvents = game.getInput()
					.getTouchEvents();
			final int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				final TouchEvent event = touchEvents.get(i);
				if (event.type != TouchEvent.TOUCH_UP) {
					continue;
				}
				game.setScreen(new HelpScreen2((CEGame) game));
				return;
			}

		}

		@Override
		public void present(float deltaTime) {

			Log.d("Help1", "present");
			final GL10 gl = glGraphics.getGl();
			gl.glClear(GLES10.GL_COLOR_BUFFER_BIT | GLES10.GL_DEPTH_BUFFER_BIT);

			camera.setViewportAndMatrices();

			gl.glEnable(GL_TEXTURE_2D);
			
			batcher.beginBatch(texture);
			batcher.drawSprite(w / 2, h / 2, w, h, region);
			batcher.endBatch();

		}

		@Override
		public void pause() {
			Log.d("Help1", "pause");
			texture.dispose();
		}

		@Override
		public void resume() {

			Log.d("Help1", "resume");

			texture = new Texture((CEGame) game, R.drawable.help_atlas);

			region = new TextureRegion(texture, 0, 0, 300, 450);
		}

		@Override
		public void dispose() {

		}

	}

	@Override
	public Screen getStartScreen() {
		Log.d("HelpActivity", "Displaying help screen");
		return new HelpScrren1(this);
	}

}
