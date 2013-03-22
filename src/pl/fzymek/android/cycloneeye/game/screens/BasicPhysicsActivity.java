package pl.fzymek.android.cycloneeye.game.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.Game;
import pl.fzymek.android.cycloneeye.game.engine.Screen;
import pl.fzymek.android.cycloneeye.game.engine.Input.TouchEvent;
import pl.fzymek.android.cycloneeye.game.engine.gl.Texture;
import pl.fzymek.android.cycloneeye.game.engine.gl.Vertices;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;
import pl.fzymek.android.cycloneeye.game.engine.math.Vector2;
import android.util.FloatMath;

public class BasicPhysicsActivity extends CEGame {

	public class PhysicsScreen extends Screen {

		float FRUSTUM_WIDTH = 9.6f;
		float FRUSTUM_HEIGHT = 6.4f;
		CEGLGraphics glGraphics;
		Vertices cannonVertices;
		Vertices ballVertices;
		Vector2 cannonPos = new Vector2();
		float cannonAngle = 0;
		Vector2 touchPos = new Vector2();
		Vector2 ballPos = new Vector2(0, 0);
		Vector2 ballVelocity = new Vector2(0, 0);
		Vector2 gravity = new Vector2(0, -10);
		Texture ballTexture;

		public PhysicsScreen(Game game) {
			super(game);
			glGraphics = ((CEGame) game).getGlGraphics();
			cannonVertices = new Vertices(glGraphics, 3, 0, false, false);
			cannonVertices.setVertices(new float[] { -0.5f, -0.5f, 0.5f, 0.0f,
					-0.5f, 0.5f }, 0, 6);
			
			ballTexture = new Texture((CEGame) game, "gfx/android.png");
			ballVertices = new Vertices(glGraphics, 4, 12, false, true);
			ballVertices.setVertices(new float[] { 
					-0.2f, -0.2f, 0.0f, 1.0f,
					 0.2f, -0.2f, 1.0f, 1.0f,
					 0.2f,  0.2f, 1.0f, 0.0f,
					-0.2f,  0.2f, 0.0f, 0.0f}, 0, 16);
			ballVertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0 }, 0, 6);
			
		}

		@Override
		public void update(float deltaTime) {
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			game.getInput().getKeyEvents();
			int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				touchPos.x = (event.x / (float) glGraphics.getWidth())
						* FRUSTUM_WIDTH;
				touchPos.y = (1 - event.y / (float) glGraphics.getHeight())
						* FRUSTUM_HEIGHT;
				cannonAngle = touchPos.sub(cannonPos).angle();
				if (event.type == TouchEvent.TOUCH_UP) {
					float radians = cannonAngle * Vector2.TO_RADIANS;
					float ballSpeed = touchPos.len();
					ballPos.set(cannonPos);
					ballVelocity.x = FloatMath.cos(radians) * ballSpeed;
					ballVelocity.y = FloatMath.sin(radians) * ballSpeed;
				}
			}
			ballVelocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
			ballPos.add(ballVelocity.x * deltaTime, ballVelocity.y * deltaTime);
		}

		@Override
		public void present(float deltaTime) {
			GL10 gl = glGraphics.getGl();

			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrthof(0, FRUSTUM_WIDTH, 0, FRUSTUM_HEIGHT, 1, -1);

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glTranslatef(cannonPos.x, cannonPos.y, 0);
			gl.glRotatef(cannonAngle, 0, 0, 1);
			gl.glColor4f(1, 1, 1, 1);
			cannonVertices.bind();
			cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
			cannonVertices.unbind();

			gl.glEnable(GL10.GL_TEXTURE_2D);
			ballTexture.bind();

			gl.glLoadIdentity();
			gl.glTranslatef(ballPos.x, ballPos.y, 0);
			ballVertices.bind();
			ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
			ballVertices.unbind();
			gl.glDisable(GL10.GL_TEXTURE_2D);
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
		return new PhysicsScreen(this);
	}

}
