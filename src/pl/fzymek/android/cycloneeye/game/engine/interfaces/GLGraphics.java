package pl.fzymek.android.cycloneeye.game.engine.interfaces;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class GLGraphics {

	GLSurfaceView glView;
	GL10 gl;

	public GLGraphics(GLSurfaceView glView) {
		this.glView = glView;
	}

	public GL10 getGl() {
		return gl;
	}

	public void setGl(GL10 gl) {
		this.gl = gl;
	}

	public int getWidth() {
		return glView.getWidth();
	}

	public int getHeight() {
		return glView.getHeight();
	}

}
