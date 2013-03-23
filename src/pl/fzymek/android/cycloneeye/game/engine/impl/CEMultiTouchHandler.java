package pl.fzymek.android.cycloneeye.game.engine.impl;

import java.util.ArrayList;
import java.util.List;

import pl.fzymek.android.cycloneeye.game.engine.Input.TouchEvent;
import pl.fzymek.android.cycloneeye.game.engine.Pool;
import pl.fzymek.android.cycloneeye.game.engine.Pool.PoolObjectFactory;
import pl.fzymek.android.cycloneeye.game.engine.TouchHandler;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

@TargetApi(Build.VERSION_CODES.ECLAIR)
public class CEMultiTouchHandler implements TouchHandler {

	private static final String TAG = CEMultiTouchHandler.class.getSimpleName();

	private static final int MAX_TOUCHPOINTS = 10;
	final boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];
	final int[] touchX = new int[MAX_TOUCHPOINTS];
	final int[] touchY = new int[MAX_TOUCHPOINTS];
	final int[] id = new int[MAX_TOUCHPOINTS];
	final Pool<TouchEvent> touchEventPool;
	final List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	final List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	final float scaleX;
	final float scaleY;

	public CEMultiTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		this.scaleX = scaleX;
		this.scaleY = scaleY;

		Log.d(TAG, "MultiTouch created with scales: (" + scaleX + ", " + scaleY
				+ ") and max touch points:" + MAX_TOUCHPOINTS);
	}

	public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
			final int action = event.getAction() & MotionEvent.ACTION_MASK;
			final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			final int pointerCount = event.getPointerCount();
			TouchEvent touchEvent = null;
            for (int i  =  0; i  <  MAX_TOUCHPOINTS; i++) {
                if (i >= pointerCount) {
                    isTouched[i]  =  false;
                    id[i]  =  -1;
                    continue;
                }
                int pointerId  =  event.getPointerId(i);
                if (event.getAction() != MotionEvent.ACTION_MOVE && i != pointerIndex) {
                    continue;
                }
                switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchEvent  =  touchEventPool.newObject();
                    touchEvent.type  =  TouchEvent.TOUCH_DOWN;
                    touchEvent.pointer  =  pointerId;
                    touchEvent.x  =  touchX[i]  =  (int) (event.getX(i) * scaleX);
                    touchEvent.y  =  touchY[i]  =  (int) (event.getY(i) * scaleY);
                    isTouched[i]  =  true;
                    id[i]  =  pointerId;
                    touchEventsBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent  =  touchEventPool.newObject();
                    touchEvent.type  =  TouchEvent.TOUCH_UP;
                    touchEvent.pointer  =  pointerId;
                    touchEvent.x  =  touchX[i]  =  (int) (event.getX(i) * scaleX);
                    touchEvent.y  =  touchY[i]  =  (int) (event.getY(i) * scaleY);
                    isTouched[i]  =  false;
                    id[i]  =  -1;
                    touchEventsBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchEvent  =  touchEventPool.newObject();
                    touchEvent.type  =  TouchEvent.TOUCH_DRAGGED;
                    touchEvent.pointer  =  pointerId;
                    touchEvent.x  =  touchX[i]  =  (int) (event.getX(i) * scaleX);
                    touchEvent.y  =  touchY[i]  =  (int) (event.getY(i) * scaleY);
                    isTouched[i]  =  true;
                    id[i]  =  pointerId;
                    touchEventsBuffer.add(touchEvent);
                    break;
                }
				Log.d(TAG, String.format("Pointer: %d, Type: %d, X: %d, Y: %d",
						i, touchEvent.type, touchEvent.x, touchEvent.y));
            }
            return true;
        }
	}

	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			final int index = getIndex(pointer);
			if (index < 0 || index >= MAX_TOUCHPOINTS)
				return false;
			else
				return isTouched[index];
		}
	}

	public int getTouchX(int pointer) {
		synchronized (this) {
			final int index = getIndex(pointer);
			if (index < 0 || index >= MAX_TOUCHPOINTS)
				return 0;
			else
				return touchX[index];
		}
	}

	public int getTouchY(int pointer) {
		synchronized (this) {
			final int index = getIndex(pointer);
			if (index < 0 || index >= MAX_TOUCHPOINTS)
				return 0;
			else
				return touchY[index];
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

	// returns the index for a given pointerId or -1 if no index.
	private int getIndex(int pointerId) {
		for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
			if (id[i] == pointerId) {
				return i;
			}
		}
		return -1;
	}
}