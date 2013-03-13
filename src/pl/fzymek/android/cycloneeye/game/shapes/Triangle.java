package pl.fzymek.android.cycloneeye.game.shapes;

import static java.lang.Math.sqrt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Triangle implements IDrawable {
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;
	private ShortBuffer indicesBuffer;
	
	static final int COORDS_PER_VERTEX = 3;
	private final static float a = 1.0f;
	private final static float h = (a*((float)sqrt(3)))/2.0f;
	
	//x, y, z
	static float triangleCoords[] = {
		 -a/2.0f,  -h/3.0f,     0.0f,
		  a/2.0f,  -h/3.0f,     0.0f,
		  0.0f,     h - h/3.0f, 0.0f
	};
		
	//rgba
	float color[] = { 
		0.367f, 0.896f, 0.464f, 1.0f ,
		0.832f, 0.421f, 0.452f, 1.0f ,
		0.000f, 1.000f, 0.000f, 1.0f
		};
	
	short indices[] = {0, 1, 2};
	
	public Triangle() {

		ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(triangleCoords);
		vertexBuffer.position(0);
		
		bb = ByteBuffer.allocateDirect(color.length * 4);
		bb.order(ByteOrder.nativeOrder());
		colorBuffer = bb.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);
		
		
		bb = ByteBuffer.allocateDirect(indices.length * 2);
		bb.order(ByteOrder.nativeOrder());
		indicesBuffer = bb.asShortBuffer();
		indicesBuffer.put(indices);
		indicesBuffer.position(0);
			
	}
	
	float angle = 0.0f;
	@Override
	public void draw(GL10 gl, long time) {
	
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPushMatrix();

		angle += 0.09f * time;
	
		gl.glScalef(0.5f,0.5f,1.0f);
		gl.glRotatef(angle, 0, 0, 1);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indicesBuffer);
		
		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
	}

}
