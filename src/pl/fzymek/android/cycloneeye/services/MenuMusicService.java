/**
 * 
 */
package pl.fzymek.android.cycloneeye.services;

import pl.fzymek.android.cycloneeye.game.engine.CEEngine;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Filip
 * 
 */
@TargetApi(Build.VERSION_CODES.ECLAIR)
public class MenuMusicService extends Service {

	private final static String TAG = MenuMusicService.class.getSimpleName();
	private static boolean isRunning = false;
	private MediaPlayer player = null;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		initMenuBackgroundMusic(this,
				CEEngine.MenuBackgroundMusic.MENU_BACKGROUND_MUSIC,
				CEEngine.MenuBackgroundMusic.VOLUME,
				CEEngine.MenuBackgroundMusic.MENU_BACKGROUND_MUSIC_LOOP);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	public void onStop() {
		isRunning = false;
	}

	public void onPause() {
		isRunning = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		try {
			player.start();
			isRunning = true;
		} catch (Exception e) {
			Log.d(TAG, "Exception happened: " + e.getMessage());
			isRunning = false;
			player.stop();
		}

		return 1;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		player.stop();
		player.release();
	}

	@Override
	public void onLowMemory() {
		Log.d(TAG, "onLowMemory");
		super.onLowMemory();
		player.stop();
	}

	private void initMenuBackgroundMusic(final Context context,
			final int music, final int volume, final boolean isLooped) {

		player = MediaPlayer.create(context, music);
		player.setLooping(isLooped);
		player.setVolume(volume, volume);

	}

	public static boolean isRunning() {
		return isRunning;
	}

	public static void setRunning(boolean isRunning) {
		MenuMusicService.isRunning = isRunning;
	}

}
