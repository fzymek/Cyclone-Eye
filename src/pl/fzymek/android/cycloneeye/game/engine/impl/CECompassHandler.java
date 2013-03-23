package pl.fzymek.android.cycloneeye.game.engine.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class CECompassHandler implements SensorEventListener {

	private final static String TAG = CECompassHandler.class.getSimpleName();

	float yaw;
	float pitch;
	float roll;

	public CECompassHandler(Context context) {
		final SensorManager manager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
		if (manager.getSensorList(Sensor.TYPE_ORIENTATION).size() != 0) {
			final Sensor compass = manager
					.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            manager.registerListener(this, compass,
                    SensorManager.SENSOR_DELAY_GAME);
			Log.d(TAG, "Compass handler registered");
        }
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// nothing to do here
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		yaw = event.values[0];
		pitch = event.values[1];
		roll = event.values[2];
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}
}