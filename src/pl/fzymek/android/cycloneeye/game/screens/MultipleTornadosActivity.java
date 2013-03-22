package pl.fzymek.android.cycloneeye.game.screens;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.Screen;
import pl.fzymek.android.cycloneeye.game.engine.gl.FPSCounter;
import pl.fzymek.android.cycloneeye.game.engine.gl.Texture;
import pl.fzymek.android.cycloneeye.game.engine.gl.Vertices;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;
import pl.fzymek.android.cycloneeye.game.models.CycloneModel;

public class MultipleTornadosActivity extends CEGame {
	
	public class MovingTornadosScreen extends Screen {

		static final int NUM_BOBS = 15;
		CEGLGraphics glGraphics;
		Texture cycloneTex;
		Vertices cycloneModel;
		CycloneModel[] cyclones;
		FPSCounter fpcCounter;

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
			cyclones = new CycloneModel[NUM_BOBS];
			for (int i = 0; i < cyclones.length; i++) {
				cyclones[i] = new CycloneModel();
			}

			fpcCounter = new FPSCounter();

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

			fpcCounter.LogFPS();
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
