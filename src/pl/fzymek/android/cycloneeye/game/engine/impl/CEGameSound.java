package pl.fzymek.android.cycloneeye.game.engine.impl;

import pl.fzymek.android.cycloneeye.game.engine.interfaces.Sound;
import android.media.SoundPool;


public class CEGameSound implements Sound {
	int soundId;
	SoundPool soundPool;

	public CEGameSound(SoundPool soundPool, int soundId) {
		this.soundId = soundId;
		this.soundPool = soundPool;
	}

	public void play(float volume) {
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}

	public void dispose() {
		soundPool.unload(soundId);
	}
}