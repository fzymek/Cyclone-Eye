package pl.fzymek.android.cycloneeye.game.engine.impl;

import pl.fzymek.android.cycloneeye.game.engine.Sound;
import android.media.SoundPool;
import android.util.Log;


public class CEGameSound implements Sound {
	private static final String TAG = CEGameSound.class.getSimpleName();
	final int soundId;
	final SoundPool soundPool;

	public CEGameSound(SoundPool soundPool, int soundId) {
		this.soundId = soundId;
		this.soundPool = soundPool;
		Log.d(TAG, "Sound created");
	}

	public void play(float volume) {
		Log.d(TAG, "Playing sound: " + soundId + " with volume" + volume);
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}

	public void dispose() {
		soundPool.unload(soundId);
		Log.d(TAG, "Playing sound: " + soundId + " disposed");
	}
}