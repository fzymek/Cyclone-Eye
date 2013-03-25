package pl.fzymek.android.cycloneeye.game.screens;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.Game;
import pl.fzymek.android.cycloneeye.game.engine.Screen;
import pl.fzymek.android.cycloneeye.game.engine.gl.Texture;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;


public class TexturedActivity extends CEGame {

	@Override
	public Screen getStartScreen() {
		return new IndexedScreen(this);
	}

	static class IndexedScreen extends Screen {
		final int VERTEX_SIZE = (2 + 2) * 4;
		CEGLGraphics glGraphics;
		FloatBuffer vertices;
		ShortBuffer indices;
		Texture texture;

		public IndexedScreen(Game game) {
			super(game);
			glGraphics = ((CEGame) game).getGlGraphics();
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * VERTEX_SIZE);
			byteBuffer.order(ByteOrder.nativeOrder());
			vertices = byteBuffer.asFloatBuffer();
			vertices.put(new float[] { 
					-1.0f, -1.0f, 0.0f, 1.0f, 
					 1.0f, -1.0f, 1.0f, 1.0f, 
					 1.0f,  1.0f, 1.0f, 0.0f, 
					-1.0f,	1.0f, 0.0f, 0.0f });
			vertices.flip();
			byteBuffer = ByteBuffer.allocateDirect(6 * 2);
			byteBuffer.order(ByteOrder.nativeOrder());
			indices = byteBuffer.asShortBuffer();
			indices.put(new short[] { 
					0, 1, 2, 
					2, 3, 0 });
			indices.flip();
			texture = new Texture((CEGame) game, "gfx/android.png");
			// texture = new Texture((CEGame) game, R.drawable.tornado_sprites);
		}

		@Override
		public void present(float deltaTime) {
			GL10 gl = glGraphics.getGl();
			gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
//			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//			gl.glMatrixMode(GL10.GL_PROJECTION);
//			gl.glLoadIdentity();
//			gl.glOrthof(0, 320, 0, 480, 1, -1);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			texture.bind();
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			vertices.position(0);
			gl.glVertexPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
			vertices.position(2);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
			gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT,
					indices);
		}

		@Override
		public void update(float deltaTime) {
			// TODO Auto-generated method stub

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
