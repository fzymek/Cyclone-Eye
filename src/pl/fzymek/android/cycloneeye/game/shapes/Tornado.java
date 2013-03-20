package pl.fzymek.android.cycloneeye.game.shapes;

import java.util.Arrays;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class Tornado extends TexturedObject {

	public float x;

	public Tornado(int texture, float[] texCoords) {
		super(texture, texCoords);
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
