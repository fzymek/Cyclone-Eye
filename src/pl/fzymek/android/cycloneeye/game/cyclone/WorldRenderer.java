package pl.fzymek.android.cycloneeye.game.cyclone;

import static android.opengl.GLES10.*;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.gl.Animation;
import pl.fzymek.android.cycloneeye.game.engine.gl.Camera2D;
import pl.fzymek.android.cycloneeye.game.engine.gl.SpriteBatcher;
import pl.fzymek.android.cycloneeye.game.engine.gl.TextureRegion;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;

public class WorldRenderer {
	final CEGLGraphics glGraphics;
	final World world;
	final Camera2D worldCamera;
	final SpriteBatcher batcher;
	final int[] obstacles;

	public WorldRenderer(final CEGLGraphics glGraphics,
			final SpriteBatcher batcher, final World world) {
		this.glGraphics = glGraphics;
		this.world = world;
		this.worldCamera = new Camera2D(glGraphics, World.CAMERA_FRUSTUM_WIDTH,
				World.CAMERA_FRUSTUM_HEIGHT);
		this.batcher = batcher;
		final Random r = new Random();
		obstacles = new int[world.obstacles.size()];
		for (int i = 0; i < obstacles.length; i++) {
			obstacles[i] = r.nextInt(16);
		}

	}

	public void render() {

		worldCamera.position.y = world.cyclone.position.y
				+ World.CAMERA_FRUSTUM_HEIGHT / 2 - Cyclone.CYCLONE_HEIGHT;

		worldCamera.setViewportAndMatrices();
		drawBackground();
		drawGameObjects();

	}

	float pos = 0.0f;
	private void drawBackground() {

		// gl.glMatrixMode(GL_TEXTURE);
		// gl.glLoadIdentity();
		batcher.beginBatch(Assets.background);
		// gl.glTranslatef(0.0f, -world.bgScroll, 0.0f);
		batcher.drawSprite(worldCamera.position.x, worldCamera.position.y,
				World.CAMERA_FRUSTUM_WIDTH,
				World.CAMERA_FRUSTUM_HEIGHT,
				Assets.backgroundRegion);
		batcher.endBatch();
		// gl.glDisable(GL_TEXTURE_2D);
		// gl.glLoadIdentity();
		// gl.glMatrixMode(GL_MODELVIEW);
		// gl.glLoadIdentity();


	}

	private void drawGameObjects() {
		
		final GL10 gl = glGraphics.getGl();

		gl.glEnable(GL_TEXTURE_2D);
		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		drawCyclone();
		drawTargets();
		drawObstacles();
		drawPowerUps();

		gl.glDisable(GL_BLEND);
		gl.glDisable(GL_TEXTURE_2D);
	}


	private void drawCyclone() {

		final TextureRegion frame = Assets.cycloneAnim.getKeyFrame(
				world.cyclone.stateTime, Animation.ANIMATION_LOOPING);

		batcher.beginBatch(Assets.cyclone);
		batcher.drawSprite(world.cyclone.position.x, world.cyclone.position.y,
				Cyclone.CYCLONE_WIDTH + 4, Cyclone.CYCLONE_HEIGHT + 4, frame);
		batcher.endBatch();

	}

	private void drawTargets() {



	}

	private void drawObstacles() {

		batcher.beginBatch(Assets.treeTexture);
		final int size = world.obstacles.size();
		for (int i = 0; i < size; i++) {
			final Obstacle o = world.obstacles.get(i);
			batcher.drawSprite(o.position.x, o.position.y,
					Obstacle.OBSTACLE_WIDTH, Obstacle.OBSTACLE_HEIGHT,
					Assets.tree[obstacles[i]]);
		}
		batcher.endBatch();


	}

	private void drawPowerUps() {

	}

}
