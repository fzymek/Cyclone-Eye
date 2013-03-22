package pl.fzymek.android.cycloneeye.game.engine.gl;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.FileIO;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Texture {
	CEGLGraphics glGraphics;
	FileIO fileIO;
	String fileName;
	int textureId;
	int minFilter;
	int magFilter;
	int width;
	int height;
	int resource;
	Context context;

	public Texture(CEGame glGame, String fileName) {
		this.glGraphics = glGame.getGlGraphics();
		this.fileIO = glGame.getFileIO();
		this.fileName = fileName;
		load();
	}

	public Texture(CEGame glGame, int resource) {
		this.context = glGame;
		this.glGraphics = glGame.getGlGraphics();
		this.fileIO = glGame.getFileIO();
		this.resource = resource;
		load();
	}

	private void load() {
		GL10 gl = glGraphics.getGl();
		int[] textureIds = new int[1];
		gl.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];
		InputStream in = null;
		try {
			if (fileName != null) {
				in = fileIO.readAsset(fileName);
			} else {
				in = context.getResources().openRawResource(resource);
			}
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load texture '" + fileName
					+ "'", e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
	}

	public void reload() {
		load();
		bind();
		setFilters(minFilter, magFilter);
		glGraphics.getGl().glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}

	public void setFilters(int minFilter, int magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		GL10 gl = glGraphics.getGl();
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				minFilter);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				magFilter);
	}

	public void bind() {
		GL10 gl = glGraphics.getGl();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	}

	public void dispose() {
		GL10 gl = glGraphics.getGl();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		int[] textureIds = { textureId };
		gl.glDeleteTextures(1, textureIds, 0);
	}
}