package pl.fzymek.android.cycloneeye.game.engine;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.services.MenuMusicService;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

/**
 * Cyclon Eye Game Engine
 * 
 * @author Filip
 * 
 */
@TargetApi(Build.VERSION_CODES.FROYO)
public class CEEngine {

	public static final class MenuBackgroundMusic {
		public static int VOLUME = 100;
		public static int MENU_BACKGROUND_MUSIC = R.raw.mellow_loop;
		public static boolean MENU_BACKGROUND_MUSIC_LOOP = true;

		private final static String TAG = MenuBackgroundMusic.class.getSimpleName();

		public static void start(final Context context) {
			Log.d(TAG, "Menu background music starting");
			final Intent music = new Intent(context, MenuMusicService.class);
			context.startService(music);
			Log.d(TAG, "Menu background music started");

		}

		public static void stop(final Context context) {
			final Intent music = new Intent(context, MenuMusicService.class);
			context.stopService(music);
			Log.d(TAG, "Menu background music paused");
		}

	}

	public static final class SoundEffects implements
			SoundPool.OnLoadCompleteListener {

		public static int MENU_BUTTON_CLICK = 0;
		public static int GAME_EXPLOSION = 1;
		public static int EFFECTS_COUNT = GAME_EXPLOSION + 1;

		private final static String TAG = SoundEffects.class.getSimpleName();
		private static SoundEffects instance = null;

		private final int[] soundEffects;
		private final SoundPool soundPool;
		private final Context context;

		private SoundEffects(final Context context) {
			Log.d(TAG, "Initializong sound effects");
			this.context = context;
			this.soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			soundPool.setOnLoadCompleteListener(this);
			this.soundEffects = new int[EFFECTS_COUNT];


			loadSounds();
		}

		public static SoundEffects getInstance(final Context context) {
			if (instance == null) {
				instance = new SoundEffects(context);
			}
			return instance;
		}

		private void loadSounds() {
			soundEffects[MENU_BUTTON_CLICK] = soundPool.load(context,
					R.raw.click, 1);
			soundEffects[GAME_EXPLOSION] = soundPool.load(context,
					R.raw.explosion, 1);

			Log.d(TAG, "Sounds loaded");
		}

		public void playSound(final int soundId) {
			Log.d(TAG, "Playing sound: " + soundId + " "
					+ soundEffects[soundId]);
			soundPool.play(soundEffects[soundId], 1.0f, 1.0f, 1, 0, 1.0f);
		}

		@Override
		public void onLoadComplete(final SoundPool soundPool,
				final int sampleId, final int status) {
			Log.d(TAG, "onLoadComplete: " + soundPool + " sample: " + sampleId
					+ " status: " + status);
		}

		public void release() {
			soundPool.release();
		}

	}

	
}
