package pl.fzymek.android.cycloneeye.game.cyclone;

import pl.fzymek.android.cycloneeye.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

	public static String name;
	public static boolean soundsEnabled;
	public static boolean musicEnabled;
	public static boolean vibrationsEnabled;

	public static void initialize(final Context context) {

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		name = prefs.getString(
				context.getResources().getString(
						R.string.preference_player_name_key),
				context.getResources().getString(
						R.string.preference_default_player_name));

		musicEnabled = prefs.getBoolean(
				context.getResources().getString(
						R.string.preference_background_music_key),
				Boolean.valueOf(context.getResources().getString(
						R.string.preference_default_background_music_enabled)));

		soundsEnabled = prefs.getBoolean(
				context.getResources().getString(
						R.string.preference_sound_effects_key),
				Boolean.valueOf(context.getResources().getString(
						R.string.preference_default_sound_effects_enabled)));

		vibrationsEnabled = prefs.getBoolean(
				context.getResources().getString(
						R.string.preference_vibrations_key),
				Boolean.valueOf(context.getResources().getString(
						R.string.preference_default_vibrations_enabled)));
	}

}
