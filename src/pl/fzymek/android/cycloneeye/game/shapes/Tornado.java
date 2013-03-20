package pl.fzymek.android.cycloneeye.game.shapes;

import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

import pl.fzymek.android.cycloneeye.utils.BufferUtils;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class Tornado implements IDrawable {

	private final int textures[] = new int[1];
	
	private final static float[] vertices = { 
			-1.0f, -1.0f, 0.0f, 
			 1.0f, -1.0f, 0.0f, 
			 1.0f,  1.0f, 0.0f,
			-1.0f,  1.0f, 0.0f };

//	private final static float[] texture = {
//			0.0f, 1.0f,	
//			1.0f, 1.0f,	
//			1.0f, 0.0f,	
//			0.0f, 0.0f	
//		};
		
	private final static float[] texture = {
				0.00f, 0.25f,	
				0.25f, 0.25f,	
				0.25f, 0.00f,	
				0.00f, 0.00f	
			};

	private final static byte[] indices = { 
				0, 1, 2, 
				0, 2, 3 };

	private final static FloatBuffer vertexBuffer;
	private final static FloatBuffer textureBuffer;
	private final static ByteBuffer indexBuffer;

	public static float[] position = { 0.0f, 0.0f, 0.0f };
	public float x;

	static {
		vertexBuffer = BufferUtils.makeFloatBuffer(vertices);
		textureBuffer = BufferUtils.makeFloatBuffer(texture);
		indexBuffer = BufferUtils.makeByteBuffer(indices);
	}

	public Tornado(int texture) {
		textures[0] = texture;
	}


	@Override
	public void draw(GL10 gl, long time) {
		
		// disable depth checking
		glDepthMask(false);
		glEnable(GL10.GL_TEXTURE_2D);
		glActiveTexture(GL10.GL_TEXTURE0);
		glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		glFrontFace(GL10.GL_CCW);
		glEnable(GL10.GL_CULL_FACE);
		glCullFace(GL10.GL_BACK);

		glEnableClientState(GL10.GL_VERTEX_ARRAY);
		glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		glPushMatrix();
		glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_BYTE, indexBuffer);

		glPopMatrix();
		glDisableClientState(GL10.GL_VERTEX_ARRAY);
		glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		glDisable(GL10.GL_CULL_FACE);
		glDepthMask(true);

	}

	public static class AccelerometerMoveListener implements
			SensorEventListener {

		private static final float SENSOR_NOISE = 0.18f;
		private final static float[] gravity = new float[3];
		private final static float[] linear_acceleration = new float[3];
		private final static float alpha = 0.8f;

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@Override
		public void onSensorChanged(SensorEvent event) {

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// ignore
			}

			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				// alpha is calculated as t / (t + dT)
				// with t, the low-pass filter's time-constant
				// and dT, the event delivery rate

				gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
				// gravity[1] = alpha * gravity[1] + (1 - alpha) *
				// event.values[1];
				// gravity[2] = alpha * gravity[2] + (1 - alpha) *
				// event.values[2];

				linear_acceleration[0] = event.values[0] - gravity[0];
				// linear_acceleration[1] = event.values[1] - gravity[1];
				// linear_acceleration[2] = event.values[2] - gravity[2];

				if (Math.abs(linear_acceleration[0]) < SENSOR_NOISE) {
					linear_acceleration[0] = 0;
				} else {
					linear_acceleration[0] = linear_acceleration[0] > 0 ? -1
							: 1;
				}
				Tornado.position = linear_acceleration;
				Log.d("ACCELEROMETER", "raw: " + Arrays.toString(event.values));
				Log.d("ACCELEROMETER", "gravity: " + Arrays.toString(gravity));
				Log.d("ACCELEROMETER",
						"acceleration: " + Arrays.toString(linear_acceleration));
			}

		}
	}
}
