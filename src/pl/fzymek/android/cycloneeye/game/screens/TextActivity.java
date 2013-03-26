package pl.fzymek.android.cycloneeye.game.screens;

import static android.opengl.GLES10.*;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.engine.DynamicGameObject;
import pl.fzymek.android.cycloneeye.game.engine.Game;
import pl.fzymek.android.cycloneeye.game.engine.Screen;
import pl.fzymek.android.cycloneeye.game.engine.gl.Animation;
import pl.fzymek.android.cycloneeye.game.engine.gl.Camera2D;
import pl.fzymek.android.cycloneeye.game.engine.gl.Font;
import pl.fzymek.android.cycloneeye.game.engine.gl.SpriteBatcher;
import pl.fzymek.android.cycloneeye.game.engine.gl.Texture;
import pl.fzymek.android.cycloneeye.game.engine.gl.TextureRegion;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEScreen;

public class TextActivity extends CEGame {

	public class TextScreen extends CEScreen {

		class Droid extends DynamicGameObject {
			
			public float stateTime = 0.0f;

			public Droid(float x, float y) {
				super(x, y, 6, 6);
				velocity.set(2.0f, 2.0f);
			}

			public void update(float deltaTime) {
				position.add(velocity.x * deltaTime, velocity.y * deltaTime);
				stateTime += deltaTime;
			}

		}

		final float GUI_WIDTH = 480;
		final float GUI_HEIGHT = 854;

		final float WORLD_WIDTH = 48.0f;
		final float WORLD_HEIGHT = 85.4f;

		Texture textTexture;
		Texture backgroundTexture;
		Texture droidTexture;
		TextureRegion backgroundRegion;
		TextureRegion droidRegion;
		Animation droidAnim;
		Font font;
		SpriteBatcher batcher;
		Camera2D gameWorldCamera;
		Camera2D guiCamera;
		Droid droid;
		Random rnd = new Random();

		public TextScreen(Game game) {
			super(game);

//			droidTexture = new Texture(this.game, "gfx/android.png");
			droidTexture = new Texture(this.game,
					R.drawable.tornado_sprites_full);
			backgroundTexture = new Texture(this.game, R.drawable.grass);
			textTexture = new Texture(this.game, "fonts/font.png");

			guiCamera = new Camera2D(glGraphics, GUI_WIDTH, GUI_HEIGHT);

			gameWorldCamera = new Camera2D(glGraphics, WORLD_WIDTH,
					WORLD_HEIGHT);

			batcher = new SpriteBatcher(glGraphics, 500);

			backgroundRegion = new TextureRegion(backgroundTexture, 0, 0, 512, 512);
//			droidRegion = new TextureRegion(droidTexture, 0, 0, 256, 256);
			droidAnim = new Animation(0.1f, 
							new TextureRegion(droidTexture, 0, 0, 64, 64),
							new TextureRegion(droidTexture, 64, 0, 64, 64),
							new TextureRegion(droidTexture, 128, 0, 64, 64)
					);
			
			font = new Font(textTexture, 0, 0, 16, 32, 32);
			droid = new Droid(rnd.nextFloat() * WORLD_WIDTH, rnd.nextFloat()
					* WORLD_HEIGHT);

		}

		@Override
		public void update(float deltaTime) {

			if (droid.position.x > WORLD_WIDTH) {
				droid.position.x = 0;
			}

			if (droid.position.x < 0) {
				droid.position.x = WORLD_WIDTH;
			}

			if (droid.position.y > WORLD_HEIGHT) {
				droid.position.y = 0;
			}

			if (droid.position.y < 0) {
				droid.position.y = WORLD_HEIGHT;
			}

			droid.update(deltaTime);

		}

		@Override
		public void present(float deltaTime) {

			final GL10 gl = glGraphics.getGl();
			gl.glClear(GL_COLOR_BUFFER_BIT);

			gameWorldCamera.setViewportAndMatrices();
			gl.glEnable(GL_TEXTURE_2D);
			batcher.beginBatch(backgroundTexture);
			batcher.drawSprite(gameWorldCamera.position.x,
					gameWorldCamera.position.y, WORLD_WIDTH, WORLD_HEIGHT,
					backgroundRegion);
			batcher.endBatch();

			gl.glEnable(GL_BLEND);
			gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			batcher.beginBatch(droidTexture);
			// batcher.drawSprite(droid.position.x, droid.position.y, 6, 6,
			// droidRegion);
			final TextureRegion frame = droidAnim.getKeyFrame(droid.stateTime,
					Animation.ANIMATION_LOOPING);
			batcher.drawSprite(droid.position.x, droid.position.y, 6, 6,
					frame);
			batcher.endBatch();
			gl.glDisable(GL_BLEND);

			guiCamera.setViewportAndMatrices();
			gl.glEnable(GL_BLEND);
			gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			final String msg = "Score: 123";
			batcher.beginBatch(textTexture);
			font.drawText(batcher, msg, 0, GUI_HEIGHT - 32);
			batcher.endBatch();
			gl.glDisable(GL_BLEND);

		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub

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

	@Override
	public Screen getStartScreen() {
		return new TextScreen(this);
	}

}
