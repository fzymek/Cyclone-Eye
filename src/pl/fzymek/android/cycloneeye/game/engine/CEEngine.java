package pl.fzymek.android.cycloneeye.game.engine;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.services.MenuMusicService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Cyclon Eye Game Engine
 * 
 * @author Filip
 * 
 */
public class CEEngine {

	public static final class Music {
		public static int VOLUME = 100;
		public static int MENU_BACKGROUND_MUSIC = R.raw.mellow_loop;
		public static boolean MENU_BACKGROUND_MUSIC_LOOP = true;

		private final static String TAG = Music.class.getSimpleName();

		public static void startMenuBackgroundMusic(final Context context) {
			Log.d(TAG, "Menu background music starting");
			final Intent music = new Intent(context, MenuMusicService.class);
			context.startService(music);
			Log.d(TAG, "Menu background music started");

		}

		public static void stopMenuBackgroundMusic(final Context context) {
			pauseMenuBackgrounMusic(context);
		}

		public static void pauseMenuBackgrounMusic(final Context context) {
			final Intent music = new Intent(context, MenuMusicService.class);
			context.stopService(music);
			Log.d(TAG, "Menu background music paused");
		}
	}

}
