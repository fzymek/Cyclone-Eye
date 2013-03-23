package pl.fzymek.android.cycloneeye.game.engine.impl;

import java.io.IOException;

import pl.fzymek.android.cycloneeye.game.engine.Music;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

/**
 * Represents game music
 * 
 * @author Filip
 * 
 */
public class CEGameMusic implements Music, OnCompletionListener {

	private final static String TAG = CEGameMusic.class.getSimpleName();
	final MediaPlayer mediaPlayer;
	boolean isPrepared = false;

	/**
	 * Initializes music from assets
	 * 
	 * @param assetDescriptor
	 *            - music asset
	 */
	public CEGameMusic(AssetFileDescriptor assetDescriptor) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
					assetDescriptor.getStartOffset(),
					assetDescriptor.getLength());
			mediaPlayer.prepare();
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);

		} catch (Exception e) {
			throw new RuntimeException("Couldn't load music");
		}

		Log.d(TAG, "Music initialized from asset: " + assetDescriptor);
	}

	/**
	 * Initializes music from resource
	 * 
	 * @param context
	 * @param music
	 *            - music resource
	 */
	public CEGameMusic(Context context, int music) {
		mediaPlayer = MediaPlayer.create(context, music);
		isPrepared = true;
		mediaPlayer.setOnCompletionListener(this);
		Log.d(TAG, "Music initialized from resource: " + music);
	}

	public void dispose() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		mediaPlayer.release();

		Log.d(TAG, "Media player disposed");
	}

	public boolean isLooping() {
		return mediaPlayer.isLooping();
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public boolean isStopped() {
		return !isPrepared;
	}

	public void pause() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
		Log.v(TAG, "Music paused");
	}

	public void play() {
		if (mediaPlayer.isPlaying()) {
			return;
		}
		try {
			synchronized (this) {
				if (!isPrepared) {
					mediaPlayer.prepare();
				}
				mediaPlayer.start();
				Log.d(TAG, "Music started");
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setLooping(boolean isLooping) {
		mediaPlayer.setLooping(isLooping);
	}

	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);
	}

	public void stop() {
		mediaPlayer.stop();
		synchronized (this) {
			isPrepared = false;
		}
		Log.v(TAG, "Music topped");
	}

	@Override
	public void onCompletion(MediaPlayer player) {
		synchronized (this) {
			isPrepared = false;
			Log.v(TAG, "Music media player is prepared now");
		}
	}
}