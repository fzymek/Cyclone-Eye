package pl.fzymek.android.cycloneeye.game.engine.gl;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.game.engine.impl.CEGLGraphics;
import pl.fzymek.android.cycloneeye.game.engine.math.Vector2;
import android.util.FloatMath;
import android.util.Log;

/**
 * Class responsible for drawing sprites
 * 
 * @author Filip
 * 
 */
public class SpriteBatcher {

	private final static String TAG = SpriteBatcher.class.getSimpleName();

	final float[] verticesBuffer;
	int bufferIndex;
	final Vertices vertices;
	int numSprites;

	public SpriteBatcher(final CEGLGraphics glGraphics, final int maxSprites) {
		this.verticesBuffer = new float[maxSprites * 4 * 4];
		this.vertices = new Vertices(glGraphics, maxSprites * 4,
				maxSprites * 6, false, true);
		this.bufferIndex = 0;
		this.numSprites = 0;
		short[] indices = new short[maxSprites * 6];
		int len = indices.length;
		short j = 0;
		for (int i = 0; i < len; i += 6, j += 4) {
			indices[i + 0] = (short) (j + 0);
			indices[i + 1] = (short) (j + 1);
			indices[i + 2] = (short) (j + 2);
			indices[i + 3] = (short) (j + 2);
			indices[i + 4] = (short) (j + 3);
			indices[i + 5] = (short) (j + 0);
		}
		vertices.setIndices(indices, 0, indices.length);

		Log.d(TAG, "Created with parameters: " + "Vertices: " + vertices
				+ " Sprites: " + numSprites);

	}

	public void beginBatch(final Texture texture) {
		texture.bind();
		numSprites = 0;
		bufferIndex = 0;
	}

	public void endBatch() {
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
		vertices.unbind();
	}

	public void drawSprite(final float x, final float y, final float width,
			final float height, final TextureRegion region) {
		final float halfWidth = width / 2;
		final float halfHeight = height / 2;
		final float x1 = x - halfWidth;
		final float y1 = y - halfHeight;
		final float x2 = x + halfWidth;
		final float y2 = y + halfHeight;
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		numSprites++;
	}

	public void drawSprite(final float x, final float y, final float width,
			final float height, final float angle, final TextureRegion region) {
		final float halfWidth = width / 2;
		final float halfHeight = height / 2;
		final float rad = angle * Vector2.TO_RADIANS;
		final float cos = FloatMath.cos(rad);
		final float sin = FloatMath.sin(rad);
		float x1 = -halfWidth * cos - (-halfHeight) * sin;
		float y1 = -halfWidth * sin + (-halfHeight) * cos;
		float x2 = halfWidth * cos - (-halfHeight) * sin;
		float y2 = halfWidth * sin + (-halfHeight) * cos;
		float x3 = halfWidth * cos - halfHeight * sin;
		float y3 = halfWidth * sin + halfHeight * cos;
		float x4 = -halfWidth * cos - halfHeight * sin;
		float y4 = -halfWidth * sin + halfHeight * cos;
		x1 += x;
		y1 += y;
		x2 += x;
		y2 += y;
		x3 += x;
		y3 += y;
		x4 += x;
		y4 += y;
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;
		verticesBuffer[bufferIndex++] = x3;
		verticesBuffer[bufferIndex++] = y3;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;
		verticesBuffer[bufferIndex++] = x4;
		verticesBuffer[bufferIndex++] = y4;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		numSprites++;
	}
}
