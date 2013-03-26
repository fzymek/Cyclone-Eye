package pl.fzymek.android.cycloneeye.game.engine.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;
import android.util.Log;

public class Vertices {

	private final static String TAG = Vertices.class.getSimpleName();

	final CEGLGraphics glGraphics;
	final boolean hasColor;
	final boolean hasTexCoords;
	final int vertexSize;
	final IntBuffer vertices; //not FloatBuffer -> include fix for JNI bug in Android 1.5
	final ShortBuffer indices;
	final int[] tmpBuffer;

	public Vertices(CEGLGraphics glGraphics, int maxVertices, int maxIndices,
			boolean hasColor, boolean hasTexCoords) {
		this.glGraphics = glGraphics;
		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.vertexSize = (2 + (hasColor ? 4 : 0) + (hasTexCoords ? 2 : 0)) * 4;
		this.tmpBuffer = new int[maxVertices * vertexSize / 4];
		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asIntBuffer();
		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();
		} else {
			indices = null;
		}

		Log.d(TAG, "Created with " + 
				"color: " + hasColor + 
				" texture: " + hasTexCoords + 
				" ver. size: " + vertexSize + 
				" vetices: " + vertices + 
				" indices: " + indices);
				

	}

	public void setVertices(float[] vertices, int offset, int length) {
		this.vertices.clear();
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			// store vertices in temp buffer
			tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
		}
		// copy content to buffer
		this.vertices.put(tmpBuffer, 0, length);
		this.vertices.flip();
	}

	public void setIndices(short[] indices, int offset, int length) {
		this.indices.clear();
		this.indices.put(indices, offset, length);
		this.indices.flip();
	}

	public void bind() {
		final GL10 gl = glGraphics.getGl();
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		vertices.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		if (hasColor) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			vertices.position(2);
			gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
		}
		if (hasTexCoords) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			final int pos = hasColor ? 6 : 2;
			Log.d(TAG, "Pos bind: " + pos);
			Log.d(TAG, "Verices: " + vertices);
			vertices.position(pos);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		}
	}
	public void draw(int primitiveType, int offset, int numVertices) {
		final GL10 gl = glGraphics.getGl();
		if (indices != null) {
			indices.position(offset);
			gl.glDrawElements(primitiveType, numVertices,
					GL10.GL_UNSIGNED_SHORT, indices);
		} else {
			gl.glDrawArrays(primitiveType, offset, numVertices);
		}
	}

	public void unbind() {
		final GL10 gl = glGraphics.getGl();
		if (hasTexCoords)
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		if (hasColor)
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}
