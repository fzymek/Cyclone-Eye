package pl.fzymek.android.cycloneeye.game.shapes;

import java.util.Arrays;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class Tornado {// extends TexturedObject {

	public float x;

	public Tornado(int texture, float[] texCoords) {
		// super(texture, texCoords);
	}

	// @Override
	// public void update(long time) {
	// // TODO Auto-generated method stub
	//
	// }
	
	/*
	 * private void draw_tornado(GL10 gl, long time) {

		tornado.x += 0.005f * time * Tornado.position[0];

		if (tornado.x > 3.5f) {
			tornado.x = 3.5f;
		}
		if (tornado.x < -3.5f) {
			tornado.x = -3.5f;
		}

		glMatrixMode(GL10.GL_MODELVIEW);
		glLoadIdentity();
		glScalef(0.25f, 0.25f, 1.0f);
		glTranslatef(tornado.x, 0.0f, 0.0f);
		glPushMatrix();

		glMatrixMode(GL_TEXTURE);
		glLoadIdentity();
		glPushMatrix();
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		float u = 0.0f;

		if (Tornado.position[0] > 0.0f) {
			u = 0.5f;
		} else if (Tornado.position[0] < 0.0f) {
			u = 0.25f;
		}
		//
		Log.d("DrawTornado", "(" + u + ", " + 0.0f + ")");
		//

		glTranslatef(u, 0.0f, 0.0f);

		tornado.draw(gl, time);
		glPopMatrix();
		glLoadIdentity();
		glBlendFunc(GL_ONE, GL_ONE);
	}
	 * 
	 */

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
				// Tornado.position = linear_acceleration;
				Log.d("ACCELEROMETER", "raw: " + Arrays.toString(event.values));
				Log.d("ACCELEROMETER", "gravity: " + Arrays.toString(gravity));
				Log.d("ACCELEROMETER",
						"acceleration: " + Arrays.toString(linear_acceleration));
			}

		}
	}

}
