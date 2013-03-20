package pl.fzymek.android.cycloneeye.game.engine;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.ui.acitivites.GameActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class TextureLoader {
	
	private int[] textures = new int[3];
	
	public TextureLoader(GL10 gl) {
		gl.glGenTextures(textures.length, textures, 0);
	}
	
	
	public int[] loadTexture(GL10 gl, int texture, Context context,
			int textureNumber, int textureMode) {

		InputStream is = context.getResources().openRawResource(texture);
		Bitmap bitmap = null;

		try {
			bitmap = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// bind textures to array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[textureNumber]);

		// set texture mapping
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		// set looping texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				textureMode);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				textureMode);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();

		return textures;

	}


	public int[] loadContinuousTexture(GL10 gl, int grass,
			GameActivity context, int i, boolean b) {
		return null;
	}

}
