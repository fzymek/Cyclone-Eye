package pl.fzymek.android.cycloneeye.game.shapes;

import static java.lang.Math.sqrt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class Triangle implements IDrawable {

	public static float position[] = { 0.0f, 0.0f, 0.0f }; // x, y, z

	private final static int COORDS_PER_VERTEX = 3;
	private final static float a = 1.0f;
	private final static float h = (a * ((float) sqrt(3))) / 2.0f;

	// x, y, z
	private static float triangleCoords[] = { 
		-a / 2.0f, 	-h / 3.0f, 		0.0f,
		a / 2.0f, 	-h / 3.0f,  	0.0f,
		0.0f, 		h - h / 3.0f,	0.0f };

	// rgba
	private float color[] = { 
		0.367f, 0.896f, 0.464f, 
		1.000f, 0.832f, 0.421f,
		0.452f, 1.000f, 0.000f, 
		1.000f, 0.000f, 1.000f };

	private short indices[] = { 0, 1, 2 };

	private final FloatBuffer vertexBuffer;
	private final FloatBuffer colorBuffer;
	private final ShortBuffer indicesBuffer;
	private float angle = 0.0f;
	private float posX = 0.0f;

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

	@Override
	public void draw(GL10 gl, long time) {

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();

		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPushMatrix();



		angle += 0.09f * time;
		posX += 0.003f * time * position[0];

		if (posX > 1.0f) {
			posX = 1.0f;
		}
		if (posX < -1.0f) {
			posX = -1.0f;
		}

		gl.glTranslatef(posX, -0.5f, 0.0f);
		gl.glScalef(0.5f, 0.5f, 1.0f);
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
		gl.glPopMatrix();
	}

	public static class TriangleAccelerometerMoveListener implements
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

				Triangle.position = linear_acceleration;
				Log.d("ACCELEROMETER", "raw: "+ Arrays.toString(event.values));
				Log.d("ACCELEROMETER", "gravity: "+ Arrays.toString(gravity));				
				Log.d("ACCELEROMETER", "acceleration: "+ Arrays.toString(linear_acceleration));
			}

		}

	}

}
