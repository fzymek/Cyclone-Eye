package pl.fzymek.android.cycloneeye.game.shapes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.utils.BufferUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Grass implements IDrawable {

	private final static int textures[] = new int[1];

	private final static float[] vertices = { 
		-1.0f, -1.0f, 0.0f,
		 1.0f, -1.0f, 0.0f, 
		 1.0f, 1.0f, 0.0f, 
		-1.0f, 1.0f, 0.0f };

	private final static float[] texture = { 
		0.0f, 1.0f, 
		1.0f, 1.0f, 
		1.0f, 0.0f, 
		0.0f, 0.0f };

	private final static byte[] indices = {
		0, 1, 2,
		0, 2, 3 };

	private final static FloatBuffer vertexBuffer;
	private final static FloatBuffer textureBuffer;
	private final static ByteBuffer indexBuffer;

	public static float[] position = { 0.0f, 0.0f, 0.0f };

	static {
		vertexBuffer = BufferUtils.makeFloatBuffer(vertices);
		textureBuffer = BufferUtils.makeFloatBuffer(texture);
		indexBuffer = BufferUtils.makeByteBuffer(indices);
	}

	public float y;

	public Grass() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(GL10 gl, long time) {

		// disable depth checking
		gl.glDepthMask(false);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glActiveTexture(GL10.GL_TEXTURE0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glPushMatrix();
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_BYTE, indexBuffer);

		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDepthMask(true);

	}
	
	public void loadTexture(GL10 gl, int texture, Context context) {

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

		// generate textures
		gl.glGenTextures(1, textures, 0);

		// bind textures to array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		// set texture mapping
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		// set looping texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();

	}

}
