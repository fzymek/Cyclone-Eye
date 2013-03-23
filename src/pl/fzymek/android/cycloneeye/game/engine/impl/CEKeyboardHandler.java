package pl.fzymek.android.cycloneeye.game.engine.impl;

import java.util.ArrayList;
import java.util.List;

import pl.fzymek.android.cycloneeye.game.engine.Input.KeyEvent;
import pl.fzymek.android.cycloneeye.game.engine.Pool;
import pl.fzymek.android.cycloneeye.game.engine.Pool.PoolObjectFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnKeyListener;

public class CEKeyboardHandler implements OnKeyListener {

	private static final String TAG = CEKeyboardHandler.class.getSimpleName();

	final boolean[] pressedKeys = new boolean[128];
	final Pool<KeyEvent> keyEventPool;
	final List<KeyEvent> keyEventsBuffer = new ArrayList<KeyEvent>();
	final List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();

	public CEKeyboardHandler(View view) {
		final PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
			public KeyEvent createObject() {
				return new KeyEvent();
			}
		};
		keyEventPool = new Pool<KeyEvent>(factory, 100);
		view.setOnKeyListener(this);
		view.setFocusableInTouchMode(true);
		view.requestFocus();

		Log.d(TAG, "Key handler created");
	}

	public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
		if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
			return false;
		synchronized (this) {
			final KeyEvent keyEvent = keyEventPool.newObject();
			keyEvent.keyCode = keyCode;
			keyEvent.keyChar = (char) event.getUnicodeChar();
			if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
				keyEvent.type = KeyEvent.KEY_DOWN;
				if (keyCode > 0 && keyCode < 127) {
					pressedKeys[keyCode] = true;
				}
			}
			if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
				keyEvent.type = KeyEvent.KEY_UP;
				if (keyCode > 0 && keyCode < 127) {
					pressedKeys[keyCode] = false;
				}
			}
			keyEventsBuffer.add(keyEvent);
		}
		return false;
	}

	public boolean isKeyPressed(int keyCode) {
		if (keyCode < 0 || keyCode > 127)
			return false;
		return pressedKeys[keyCode];
	}

	public List<KeyEvent> getKeyEvents() {
		synchronized (this) {
			final int len = keyEvents.size();
			for (int i = 0; i < len; i++) {
				keyEventPool.free(keyEvents.get(i));
			}
			keyEvents.clear();
			keyEvents.addAll(keyEventsBuffer);
			keyEventsBuffer.clear();
			return keyEvents;
		}
	}
}