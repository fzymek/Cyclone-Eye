package pl.fzymek.android.cycloneeye.game.engine;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.services.MenuMusicService;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Cyclon Eye Game Engine
 * 
 * @author Filip
 * 
 */
public class CEEngine {

	public static final class MenuBackgroundMusic {
		public static int VOLUME = 100;
		public static int MENU_BACKGROUND_MUSIC = R.raw.mellow_loop;
		public static boolean MENU_BACKGROUND_MUSIC_LOOP = true;

		private final static String TAG = MenuBackgroundMusic.class
				.getSimpleName();

		public static void start(final Context context) {

			if (isBackgroundMusicEnabled(context)) {
				Log.d(TAG, "Menu background music enabled, starting");
				final Intent music = new Intent(context, MenuMusicService.class);
				context.startService(music);
			} else {
				Log.d(TAG, "Menu background music disabled");
			}

		}

		public static void stop(final Context context) {
			final Intent music = new Intent(context, MenuMusicService.class);
			context.stopService(music);
			Log.d(TAG, "Menu background music paused");
		}

		private static boolean isBackgroundMusicEnabled(final Context context) {

			final SharedPreferences defaultSharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			final boolean isBGMusicEnabled = defaultSharedPreferences
					.getBoolean(
							context.getResources().getString(
									R.string.preference_background_music_key),
							true);
			return isBGMusicEnabled;
		}

	}

	public static class SoundEffects implements
			SoundPool.OnLoadCompleteListener {

		public static int MENU_BUTTON_CLICK = 0;
		public static int GAME_EXPLOSION = 1;
		public static int EFFECTS_COUNT = GAME_EXPLOSION + 1;

		protected static SoundEffects instance = null;

		private static final String TAG = SoundEffects.class.getSimpleName();
		private final int[] soundEffects;
		private final SoundPool soundPool;
		private final Context context;

		protected SoundEffects(final Context context) {
			Log.d(TAG, "Initializong sound effects");
			this.context = context;
			this.soundPool = createSoundPool();

			registerOnLoadCompleteListener();
			this.soundEffects = new int[EFFECTS_COUNT];

			loadSounds();
		}

		public static SoundEffects getInstance(final Context context) {

			if (soundEffectsEnabled(context)) {
				Log.d(TAG, "Sound effects enabled");
				if (instance == null || instance instanceof EmptySoundEffects) {
					Log.d(TAG,
							"instance == null or instance is EmptySoundEffects");
					instance = new SoundEffects(context);
				}

			} else {
				Log.d(TAG, "Sound effects disabled");
				if (instance == null || instance instanceof SoundEffects) {
					Log.d(TAG, "instance == null or instance is SoundEffects");
					instance = new EmptySoundEffects(context);
				}
			}
			Log.d(TAG, "returning soundeffects class: "
					+ instance.getClass().getSimpleName());
			return instance;
		}

		public static boolean soundEffectsEnabled(final Context context) {
			final SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);

			return prefs.getBoolean(
					context.getResources().getString(
							R.string.preference_sound_effects_key), true);
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

		protected void loadSounds() {
			soundEffects[MENU_BUTTON_CLICK] = soundPool.load(context,
					R.raw.click, 1);
			soundEffects[GAME_EXPLOSION] = soundPool.load(context,
					R.raw.explosion, 1);

			Log.d(TAG, "Sounds loaded");
		}

		@TargetApi(Build.VERSION_CODES.FROYO)
		protected void registerOnLoadCompleteListener() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				soundPool.setOnLoadCompleteListener(this);
			}
		}

		protected SoundPool createSoundPool() {
			return new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		}

	}

	private static final class EmptySoundEffects extends SoundEffects {
		
		private static final String TAG = EmptySoundEffects.class.getSimpleName();

		public EmptySoundEffects(Context context) {
			super(context);
			Log.d(TAG, "Creating empty sound effects");
		}

		@Override
		public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
			Log.d(TAG, "onLoadComplete");
		}

		@Override
		@TargetApi(8)
		protected void registerOnLoadCompleteListener() {
			Log.d(TAG, "registerOnLoadCompleteListener");
		}

		@Override
		public void playSound(int soundId) {
			Log.d(TAG, "playSound");
		}

		@Override
		public void release() {
			Log.d(TAG, "release");
		}

		@Override
		protected void loadSounds() {
			Log.d(TAG, "loadSounds");
		}

		@Override
		protected SoundPool createSoundPool() {
			Log.d(TAG, "createSoundPool");
			return null;
		}
	}

}
