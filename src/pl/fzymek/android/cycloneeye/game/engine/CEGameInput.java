package pl.fzymek.android.cycloneeye.game.engine;

import java.util.List;

import pl.fzymek.android.cycloneeye.game.engine.impl.AccelerometerHandler;
import pl.fzymek.android.cycloneeye.game.engine.impl.KeyboardHandler;
import pl.fzymek.android.cycloneeye.game.engine.impl.MultiTouchHandler;
import pl.fzymek.android.cycloneeye.game.engine.impl.SingleTouchHandler;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.Input;
import pl.fzymek.android.cycloneeye.game.engine.interfaces.TouchHandler;
import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

public class CEGameInput implements Input {

	AccelerometerHandler accelHandler;
	KeyboardHandler keyHandler;
	TouchHandler touchHandler;

	public CEGameInput(Context context, View view, float scaleX, float scaleY) {
		accelHandler = new AccelerometerHandler(context);
		keyHandler = new KeyboardHandler(view);
		if (Integer.parseInt(VERSION.SDK) < 5)
			touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
		else
			touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
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