package pl.fzymek.android.cycloneeye.game.engine.gl;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.FileIO;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class Texture {

	private final static String TAG = Texture.class.getSimpleName();

	final CEGLGraphics glGraphics;
	final FileIO fileIO;
	final String fileName;
	int textureId;
	int minFilter;
	int magFilter;
	int width;
	int height;
	final int resource;

	private int wrapS;

	private int wrapT;

	public Texture(final CEGame glGame, final String fileName) {
		this.glGraphics = glGame.getGlGraphics();
		this.fileIO = glGame.getFileIO();
		this.fileName = fileName;
		this.resource = -1;
		load();

		Log.d(TAG, "Created and loaded from assets:" + fileName);

	}

	public Texture(final CEGame glGame, final int resource) {
		this.glGraphics = glGame.getGlGraphics();
		this.fileIO = glGame.getFileIO();
		this.resource = resource;
		this.fileName = null;
		load();
		Log.d(TAG, "Created and loaded from resources:" + resource);
	}

	private void load() {
		final GL10 gl = glGraphics.getGl();
		final int[] textureIds = new int[1];
		gl.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];
		InputStream in = null;
		try {
			if (fileName != null) {
				in = fileIO.readAsset(fileName);
			} else {
				in = fileIO.readResource(resource);
			}
			final Bitmap bitmap = BitmapFactory.decodeStream(in);
			width = bitmap.getWidth();
			height = bitmap.getHeight();

			Log.d(TAG, "Bitmap -> w: " + width + "h:" + height);

			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST, GL10.GL_CLAMP_TO_EDGE,
					GL10.GL_CLAMP_TO_EDGE);
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
		setFilters(minFilter, magFilter, wrapS, wrapT);
		glGraphics.getGl().glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}

	public void setFilters(final int minFilter, final int magFilter, int wrapS,
			int wrapT) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		this.wrapS = wrapS;
		this.wrapT = wrapT;
		final GL10 gl = glGraphics.getGl();

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, wrapS);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, wrapT);
	}

	public void bind() {
		final GL10 gl = glGraphics.getGl();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	}

	public void dispose() {
		final GL10 gl = glGraphics.getGl();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		final int[] textureIds = { textureId };
		gl.glDeleteTextures(1, textureIds, 0);
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Texture from ");
		if (resource == -1) {
			sb.append("asset: ").append(fileName);
		} else {
			sb.append("resource: ").append(resource);
		}

		return sb.append(" width: ").append(width).append(" height: ")
				.append(height).append(" minFiler: ").append(minFilter)
				.append(" magFilter: ").append(magFilter).toString();

	}
}