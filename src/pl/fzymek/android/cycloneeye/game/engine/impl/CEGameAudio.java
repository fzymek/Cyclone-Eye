package pl.fzymek.android.cycloneeye.game.engine.impl;

import java.io.IOException;

import pl.fzymek.android.cycloneeye.game.engine.Audio;
import pl.fzymek.android.cycloneeye.game.engine.Music;
import pl.fzymek.android.cycloneeye.game.engine.Sound;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class CEGameAudio implements Audio {

	private static final String TAG = CEGameAudio.class.getSimpleName();

	final AssetManager assets;
	final SoundPool soundPool;
	final Context context;

	public CEGameAudio(Activity activity) {
		this.context = activity;
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		Log.d(TAG, "Game audio created");
	}

	public Music newMusic(String filename) {
		try {
			final AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			Log.d(TAG, "Creating music from asset: " + filename);
			return new CEGameMusic(assetDescriptor);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load music '" + filename + "'");
		}
	}

	public Music newMusic(Context context, int music) {
		Log.d(TAG, "Creating music from resource: " + music);
		return new CEGameMusic(context, music);
	}

	public Sound newSound(String filename) {
		try {
			final AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			Log.d(TAG, "Creating sound from resource: " + filename);
			final int soundId = soundPool.load(assetDescriptor, 0);
			return new CEGameSound(soundPool, soundId);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load sound '" + filename + "'");
		}
	}

	public Sound newSound(int sound) {
		final int soundId = soundPool.load(context, sound, 0);
		Log.d(TAG, "Creating sound from resource: " + sound);
		return new CEGameSound(soundPool, soundId);
	}

}
