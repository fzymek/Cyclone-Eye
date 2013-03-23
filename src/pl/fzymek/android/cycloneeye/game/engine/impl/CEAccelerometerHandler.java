package pl.fzymek.android.cycloneeye.game.engine.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class CEAccelerometerHandler implements SensorEventListener {

	private final static String TAG = CEAccelerometerHandler.class
			.getSimpleName();

	float accelX;
	float accelY;
	float accelZ;

	public CEAccelerometerHandler(Context context) {
		final SensorManager manager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
		if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			final Sensor accelerometer = manager.getSensorList(
                    Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_GAME);

			Log.d(TAG, "Accelerometer handler registered");
        }

    }

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// nothing to do here
	}

	public void onSensorChanged(SensorEvent event) {
		accelX = event.values[0];
		accelY = event.values[1];
		accelZ = event.values[2];
	}

	public float getAccelX() {
		return accelX;
	}

	public float getAccelY() {
		return accelY;
	}

	public float getAccelZ() {
		return accelZ;
	}
}