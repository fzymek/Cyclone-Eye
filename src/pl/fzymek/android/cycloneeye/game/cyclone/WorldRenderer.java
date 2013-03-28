package pl.fzymek.android.cycloneeye.game.cyclone;

import static android.opengl.GLES10.GL_BLEND;
import static android.opengl.GLES10.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES10.GL_SRC_ALPHA;
import static android.opengl.GLES10.GL_TEXTURE_2D;

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

	public WorldRenderer(final CEGLGraphics glGraphics,
			final SpriteBatcher batcher, final World world) {
		this.glGraphics = glGraphics;
		this.world = world;
		this.worldCamera = new Camera2D(glGraphics, World.CAMERA_FRUSTUM_WIDTH,
				World.CAMERA_FRUSTUM_HEIGHT);
		this.batcher = batcher;

	}

	public void render() {

		// move camera until it is below game world height
		if (worldCamera.position.y < World.WORLD_HEIGHT
				- World.CAMERA_FRUSTUM_HEIGHT / 2 + 10) {
			worldCamera.position.y = world.cyclone.position.y
					+ World.CAMERA_FRUSTUM_HEIGHT / 2 - Cyclone.CYCLONE_HEIGHT;
		}

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
				World.CAMERA_FRUSTUM_WIDTH, World.CAMERA_FRUSTUM_HEIGHT,
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
				Cyclone.CYCLONE_WIDTH + 5, Cyclone.CYCLONE_HEIGHT + 5, frame);
		batcher.endBatch();

	}

	private void drawTargets() {

		final int size = world.targets.size();
		for (int i = 0; i < size; i++) {
			final Target t = world.targets.get(i);
			// improve batching!!
			TextureRegion frame;
			if (t.state != Target.STATE_DESTROYED) {
				batcher.beginBatch(Assets.targetsTexture);
				frame = Assets.targets[t.id];
			} else {
				batcher.beginBatch(Assets.explosionTexture);
				frame = Assets.explosionAnim.getKeyFrame(t.stateTime,
						Animation.ANIMATION_NONLOOPING);
			}

			batcher.drawSprite(t.position.x, t.position.y,
					Target.TARGET_WIDTH + 3, Target.TARGET_HEIGHT + 3, frame);
			batcher.endBatch();
		}

	}

	private void drawObstacles() {

		batcher.beginBatch(Assets.treeTexture);
		final int size = world.obstacles.size();
		for (int i = 0; i < size; i++) {
			final Obstacle o = world.obstacles.get(i);
			batcher.drawSprite(o.position.x, o.position.y,
					Obstacle.OBSTACLE_WIDTH + 3, Obstacle.OBSTACLE_HEIGHT + 3,
					Assets.trees[o.id]);
		}
		batcher.endBatch();

	}

	private void drawPowerUps() {

		batcher.beginBatch(Assets.jewelsTexture);
		final int size = world.powerUps.size();
		for (int i = 0; i < size; i++) {
			final PowerUp p = world.powerUps.get(i);
			final TextureRegion frame = Assets.jewelsAnim[p.id].getKeyFrame(
					p.stateTime, Animation.ANIMATION_LOOPING);
			batcher.drawSprite(p.position.x, p.position.y,
					Obstacle.OBSTACLE_WIDTH, Obstacle.OBSTACLE_HEIGHT, frame);
		}
		batcher.endBatch();

	}

}
