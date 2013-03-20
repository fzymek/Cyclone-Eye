package pl.fzymek.android.cycloneeye.game.shapes;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.utils.BufferUtils;

public class Grass implements IDrawable {

	private final int textures[] = new int[1];

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

	public Grass(int texture) {
		// TODO Auto-generated constructor stub
		textures[0] = texture;
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
	
}
