package pl.fzymek.android.cycloneeye.game.engine.impl;

import java.util.ArrayList;
import java.util.List;

import pl.fzymek.android.cycloneeye.game.engine.Input.TouchEvent;
import pl.fzymek.android.cycloneeye.game.engine.Pool;
import pl.fzymek.android.cycloneeye.game.engine.Pool.PoolObjectFactory;
import pl.fzymek.android.cycloneeye.game.engine.TouchHandler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CESingleTouchHandler implements TouchHandler {

	private static final String TAG = CESingleTouchHandler.class
			.getSimpleName();

	boolean isTouched;
	int touchX;
	int touchY;
	final Pool<TouchEvent> touchEventPool;
	final List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	final List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	final float scaleX;
	final float scaleY;

	public CESingleTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		this.scaleX = scaleX;
		this.scaleY = scaleY;

		Log.d(TAG, "SingleTouch created with scales: (" + scaleX + ", "
				+ scaleY + ")");
	}

	public boolean onTouch(View v, MotionEvent event) {
		synchronized (this) {
			TouchEvent touchEvent = touchEventPool.newObject();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				isTouched = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouched = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouched = false;
				break;
			}
			touchEvent.x = touchX = (int) (event.getX() * scaleX);
			touchEvent.y = touchY = (int) (event.getY() * scaleY);
			touchEventsBuffer.add(touchEvent);

			Log.d(TAG, String.format("Touch type: %d, X: %d, Y: %d",
					touchEvent.type, touchEvent.x, touchEvent.y));

			return true;
		}
	}

	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			if (pointer == 0)
				return isTouched;
			else
				return false;
		}
	}

	public int getTouchX(int pointer) {
		synchronized (this) {
			return touchX;
		}
	}

	public int getTouchY(int pointer) {
		synchronized (this) {
			return touchY;
		}
	}

	public List<TouchEvent> getTouchEvents() {
		synchronized (this) {
			final int len = touchEvents.size();
			for (int i = 0; i < len; i++)
				touchEventPool.free(touchEvents.get(i));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}
}
