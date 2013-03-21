package pl.fzymek.android.cycloneeye.game.engine.screens;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.CEGame;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.GLGraphics;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.Screen;
import pl.fzymek.android.cycloneeye.game.shapes.Cyclone;
import pl.fzymek.android.cycloneeye.game.shapes.Texture;
import pl.fzymek.android.cycloneeye.game.shapes.Vertices;

public class MultipleTornadosActivity extends CEGame {
	
	public class MovingTornadosScreen extends Screen {

		static final int NUM_BOBS = 10;
		GLGraphics glGraphics;
		Texture cycloneTex;
		Vertices cycloneModel;
		Cyclone[] cyclones;

		public MovingTornadosScreen(CEGame game) {
			super(game);
			glGraphics = game.getGlGraphics();

			cycloneTex = new Texture(game, "gfx/cyclone.png");
			cycloneModel = new Vertices(glGraphics, 4, 12, false, true);
			cycloneModel.setVertices(new float[] { 
					-0.5f, -0.5f, 0.00f, 0.25f,	
					 0.5f, -0.5f, 0.25f, 0.25f,
					 0.5f,  0.5f, 0.25f, 0.00f,	
					-0.5f,  0.5f, 0.00f, 0.00f}, 0, 16);
			
			cycloneModel.setIndices(new short[] {0,1,2, 2,3,0}, 0, 6);
			cyclones = new Cyclone[NUM_BOBS];
			for (int i = 0; i < cyclones.length; i++) {
				cyclones[i] = new Cyclone();
			}

		}

		@Override
		public void update(float deltaTime) {
			game.getInput().getTouchEvents();
			game.getInput().getKeyEvents();

			for (int i = 0; i < cyclones.length; i++) {
				cyclones[i].update(deltaTime);
			}

		}

		@Override
		public void present(float deltaTime) {

			final GL10 gl = glGraphics.getGl();
			gl.glEnable(GL10.GL_TEXTURE_2D);
			cycloneTex.bind();

			cycloneModel.bind();
			for (int i = 0; i < cyclones.length; i++) {
				gl.glLoadIdentity();
				gl.glTranslatef(cyclones[i].x, cyclones[i].y, 0);
				gl.glScalef(0.2f, 0.2f, 1.0f);
				cycloneModel.draw(GL10.GL_TRIANGLES, 0, 6);
			}
			cycloneModel.unbind();
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub

		}

		@Override
		public void resume() {
			GL10 gl = glGraphics.getGl();
			cycloneTex.reload();
			gl.glEnable(GL10.GL_TEXTURE_2D);
			cycloneTex.bind();

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public Screen getStartScreen() {
		return new MovingTornadosScreen(this);
	}

}
