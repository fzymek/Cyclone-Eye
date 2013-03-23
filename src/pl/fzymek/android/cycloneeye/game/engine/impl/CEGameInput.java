package pl.fzymek.android.cycloneeye.game.engine.impl;

import java.util.List;

import pl.fzymek.android.cycloneeye.game.engine.Input;
import pl.fzymek.android.cycloneeye.game.engine.TouchHandler;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

public class CEGameInput implements Input {

	private final static String TAG = CEGameInput.class.getSimpleName();

	final CEAccelerometerHandler accelHandler;
	final CEKeyboardHandler keyHandler;
	final TouchHandler touchHandler;

	public CEGameInput(Context context, View view, float scaleX, float scaleY) {
		accelHandler = new CEAccelerometerHandler(context);
		keyHandler = new CEKeyboardHandler(view);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ECLAIR) {
			touchHandler = new CESingleTouchHandler(view, scaleX, scaleY);
		} else {
			touchHandler = new CEMultiTouchHandler(view, scaleX, scaleY);
		}

		Log.d(TAG, "Touch handler: " + touchHandler);
		Log.d(TAG, "Accelerometer handler: " + accelHandler);
		Log.d(TAG, "Key handler: " + keyHandler);
		Log.d(TAG, "Input created");
	}

	public boolean isKeyPressed(int keyCode) {
		return keyHandler.isKeyPressed(keyCode);
	}

	public boolean isTouchDown(int pointer) {
		return touchHandler.isTouchDown(pointer);
	}

	public int getTouchX(int pointer) {
		return touchHandler.getTouchX(pointer);
	}

	public int getTouchY(int pointer) {
		return touchHandler.getTouchY(pointer);
	}

	public float getAccelX() {
		return accelHandler.getAccelX();
	}

	public float getAccelY() {
		return accelHandler.getAccelY();
	}

	public float getAccelZ() {
		return accelHandler.getAccelZ();
	}

	public List<TouchEvent> getTouchEvents() {
		return touchHandler.getTouchEvents();
	}

	public List<KeyEvent> getKeyEvents() {
		return keyHandler.getKeyEvents();
	}
}