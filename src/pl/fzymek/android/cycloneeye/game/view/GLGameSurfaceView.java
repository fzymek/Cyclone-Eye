package pl.fzymek.android.cycloneeye.game.view;

import pl.fzymek.android.cycloneeye.game.shapes.Tornado;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

public class GLGameSurfaceView extends GLSurfaceView {

	private static final SensorEventListener listener = new Tornado.AccelerometerMoveListener();
	private static final String TAG = GLGameSurfaceView.class.getSimpleName();

	public static Context context = null;
	private SensorManager sensorManager = null;
	private Sensor accelerometer = null;


	public GLGameSurfaceView(Context context) {
		super(context);
		initilizeSensors(context);
	}

	public GLGameSurfaceView(Context context, AttributeSet attr) {
		super(context, attr);

		initilizeSensors(context);
	}

	private void initilizeSensors(Context context) {
		Log.d(TAG, "Initializing sensors");
		sensorManager = (SensorManager) context
				.getSystemService(Service.SENSOR_SERVICE);
		accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}


	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(listener, accelerometer,
				SensorManager.SENSOR_DELAY_GAME);
		Log.d(TAG, "onResume");

	}

	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(listener);
		Log.d(TAG, "onPause");
	}

}
