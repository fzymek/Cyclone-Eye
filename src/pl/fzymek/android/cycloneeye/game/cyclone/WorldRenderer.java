package pl.fzymek.android.cycloneeye.game.cyclone;

import static android.opengl.GLES10.*;

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
		// batcher.beginBatch(Assets.objectsTexture);
		// gl.glTranslatef(0.0f, -world.bgScroll, 0.0f);
		batcher.drawSprite(worldCamera.position.x, worldCamera.position.y,
				World.CAMERA_FRUSTUM_WIDTH, World.CAMERA_FRUSTUM_HEIGHT,
				Assets.backgroundRegion);
		// batcher.endBatch();
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

		// draw all non target object in single texture batch
		batcher.beginBatch(Assets.objectsTexture);
		drawBackground();
		drawCyclone();
		drawObstacles();
		drawPowerUps();
		batcher.endBatch();

		drawTargets();

		gl.glDisable(GL_BLEND);
		gl.glDisable(GL_TEXTURE_2D);
	}

	private void drawCyclone() {

		final TextureRegion frame = Assets.cycloneAnim.getKeyFrame(
				world.cyclone.stateTime, Animation.ANIMATION_LOOPING);

		// batcher.beginBatch(Assets.objectsTexture);
		batcher.drawSprite(world.cyclone.position.x, world.cyclone.position.y,
				Cyclone.DRAW_WIDTH, Cyclone.DRAW_HEIGHT, frame);
		// batcher.endBatch();

	}

	private void drawTargets() {

		final int size = world.targets.size();
		batcher.beginBatch(Assets.targetsTexture);
		for (int i = 0; i < size; i++) {
			final Target t = world.targets.get(i);
			TextureRegion frame;
			if (t.state != Target.STATE_DESTROYED) {
				frame = t.type == Target.TYPE_NORMAL ? Assets.targets[t.id]
						: Assets.targets[Assets.TARGET_CASTLE_ID];
			} else {
				frame = Assets.explosionAnim.getKeyFrame(t.stateTime,
						Animation.ANIMATION_NONLOOPING);
			}

			int mul = t.type == Target.TYPE_LEVEL_END ? Target.TARGET_TYPE_LEVEL_END_SIZE_MULTIPLIER
					: 1;

			batcher.drawSprite(t.position.x, t.position.y, Target.DRAW_WIDTH
					* mul, Target.DRAW_HEIGHT * mul, frame);

		}

		batcher.endBatch();
	}

	private void drawObstacles() {

		// batcher.beginBatch(Assets.objectsTexture);
		final int size = world.obstacles.size();
		for (int i = 0; i < size; i++) {
			final Obstacle o = world.obstacles.get(i);
			batcher.drawSprite(o.position.x, o.position.y, Obstacle.DRAW_WIDTH,
					Obstacle.DRAW_HEIGHT, Assets.trees[o.id]);
		}
		// batcher.endBatch();

	}

	private void drawPowerUps() {

		// batcher.beginBatch(Assets.objectsTexture);
		final int size = world.powerUps.size();
		for (int i = 0; i < size; i++) {
			final PowerUp p = world.powerUps.get(i);
			final TextureRegion frame = Assets.jewelsAnim[p.id].getKeyFrame(
					p.stateTime, Animation.ANIMATION_LOOPING);
			batcher.drawSprite(p.position.x, p.position.y, PowerUp.DRAW_WIDTH,
					PowerUp.DRAW_HEIGHT, frame);
		}
		// batcher.endBatch();

	}

}
