package pl.fzymek.android.cycloneeye.game.screens;

import static java.lang.Math.sqrt;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.Game;
import pl.fzymek.android.cycloneeye.game.engine.Screen;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;
import pl.fzymek.android.cycloneeye.utils.BufferUtils;

public class TriangleActivity extends CEGame {

	@Override
	public Screen getStartScreen() {
		return new TraingleScreen(this);
	}

	static class TraingleScreen extends Screen {


		public static float position[] = { 0.0f, 0.0f, 0.0f }; // x, y, z

		private final static float a = 1.0f;
		private final static float h = (a * ((float) sqrt(3))) / 2.0f;

		// x, y, z
		private static float triangleCoords[] = { 
			-a / 2.0f, -h / 3.0f, 	 0.0f, 
			 a / 2.0f, -h / 3.0f,  	 0.0f, 
			     0.0f, h - h / 3.0f, 0.0f };

		// rgba
		private float color[] = { 
				0.367f, 0.896f, 0.464f, 1.000f, 
				0.832f,	0.421f, 0.452f, 1.000f, 
				0.000f, 1.000f, 0.000f, 1.000f };

		private short indices[] = { 0, 1, 2 };

		private final FloatBuffer vertexBuffer;
		private final FloatBuffer colorBuffer;
		private final ShortBuffer indicesBuffer;
		private float angle = 0.0f;
		private float posX = 0.0f;
		private CEGLGraphics glGraphics;

		public TraingleScreen(Game game) {
			super(game);
			glGraphics = ((CEGame) game).getGlGraphics();
			vertexBuffer = BufferUtils.makeFloatBuffer(triangleCoords);
			colorBuffer = BufferUtils.makeFloatBuffer(color);
			indicesBuffer = BufferUtils.makeShortBuffer(indices);
		}

		@Override
		public void update(float time) {
			game.getInput().getKeyEvents();
			game.getInput().getTouchEvents();
			angle += 0.09f * time;
			posX += 0.003f * time * position[0];

			if (posX > 1.0f) {
				posX = 1.0f;
			}
			if (posX < -1.0f) {
				posX = -1.0f;
			}

		}

		@Override
		public void present(float deltaTime) {
			GL10 gl = glGraphics.getGl();
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();

			gl.glDisable(GL10.GL_BLEND);
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glPushMatrix();

			gl.glTranslatef(posX, -0.5f, 0.0f);
			gl.glScalef(0.5f, 0.5f, 1.0f);
			gl.glRotatef(angle, 0, 0, 1);

			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indicesBuffer);

			gl.glPopMatrix();
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			gl.glEnable(GL10.GL_BLEND);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glPopMatrix();

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
}
