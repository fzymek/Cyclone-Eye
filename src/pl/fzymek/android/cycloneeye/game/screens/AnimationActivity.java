package pl.fzymek.android.cycloneeye.game.screens;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.engine.Game;
import pl.fzymek.android.cycloneeye.game.engine.GameObject;
import pl.fzymek.android.cycloneeye.game.engine.Screen;
import pl.fzymek.android.cycloneeye.game.engine.gl.Animation;
import pl.fzymek.android.cycloneeye.game.engine.gl.Camera2D;
import pl.fzymek.android.cycloneeye.game.engine.gl.SpriteBatcher;
import pl.fzymek.android.cycloneeye.game.engine.gl.Texture;
import pl.fzymek.android.cycloneeye.game.engine.gl.TextureRegion;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;

public class AnimationActivity extends CEGame {

	private static final float WORLD_WIDTH = 4.8f;
	private static final float WORLD_HEIGHT = 3.2f;

	public static class Tree extends GameObject {

		public float animTime = 0.0f;

		public Tree(float x, float y, float width, float height) {
			super(x, y, width, height);
			this.position.set((float) Math.random() * WORLD_WIDTH,
					(float) Math.random() * WORLD_HEIGHT);
		}

		public void update(float deltaTime) {
			animTime += deltaTime;
		}
	}

	public class AnimationScreen extends Screen {

		static final int NUM_CAVEMEN = 10;
		CEGLGraphics glGraphics;
		Tree[] tree;
		SpriteBatcher batcher;
		Camera2D camera;
		Texture texture;
		Animation walkAnim;

		public AnimationScreen(Game game) {
			super(game);
			glGraphics = ((CEGame) game).getGlGraphics();
			tree = new Tree[NUM_CAVEMEN];
			for (int i = 0; i < NUM_CAVEMEN; i++) {
				tree[i] = new Tree((float) Math.random(),
						(float) Math.random(), 1, 1);
			}
			batcher = new SpriteBatcher(glGraphics, NUM_CAVEMEN);
			camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		}

		@Override
		public void update(float deltaTime) {
			int len = tree.length;
			for (int i = 0; i < len; i++) {
				tree[i].update(deltaTime);
			}
		}

		@Override
		public void present(float deltaTime) {

			GL10 gl = glGraphics.getGl();
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			camera.setViewportAndMatrices();
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			batcher.beginBatch(texture);
			int len = tree.length;
			for (int i = 0; i < len; i++) {
				Tree tr = tree[i];
				TextureRegion keyFrame = walkAnim.getKeyFrame(tr.animTime,
						Animation.ANIMATION_LOOPING);
				batcher.drawSprite(tr.position.x, tr.position.y, 1, 1, keyFrame);
			}
			batcher.endBatch();

		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub

		}

		@Override
		public void resume() {
			texture = new Texture((CEGame)game, R.drawable.tree_sprites);
	        walkAnim = new Animation( 0.2f,
	                                  new TextureRegion(texture, 0, 0, 128, 128),
	                                  new TextureRegion(texture, 128, 0, 128, 128),
	                                  new TextureRegion(texture, 256, 0, 128, 128),
	                                  new TextureRegion(texture, 384, 0, 128, 128));

		}

		@Override
		public void dispose() {

		}

	}

	@Override
	public Screen getStartScreen() {
		return new AnimationScreen(this);
	}

}
