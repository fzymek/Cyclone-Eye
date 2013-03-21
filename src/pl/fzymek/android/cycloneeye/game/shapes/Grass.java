package pl.fzymek.android.cycloneeye.game.shapes;

import static android.opengl.GLES10.*;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.CEEngine;


public class Grass extends TexturedObject {

	public float y;

	public Grass(int texture, float[] texCoords) {
		super(texture, texCoords);
	}

	@Override
	public void update(long time) {

	}
	
	/*
	 * private void scrollGrass(GL10 gl, long time) {

		glMatrixMode(GL10.GL_MODELVIEW);
		glLoadIdentity();
		glPushMatrix();
		glScalef(1.0f, 1.0f, 1.0f);
		glTranslatef(0.0f, 0.0f, 0.0f);

		glMatrixMode(GL_TEXTURE);
		glLoadIdentity();
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glTranslatef(0.0f, grass.y, 0.0f);
		grass.draw(gl, time);
		glPopMatrix();

		grass.y += CEEngine.BACKGROUND_SCROLL;

		glLoadIdentity();
		glBlendFunc(GL_ONE, GL_ONE);
	}
	 */

}
