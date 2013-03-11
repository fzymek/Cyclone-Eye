package pl.fzymek.android.cycloneeye.ui.acitivites;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.engine.CEEngine;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class MenuPreferencesActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	private final static String TAG = MenuPreferencesActivity.class
			.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "OnCreate");

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		initializePreferencesSummary();
	}

	private void initializePreferencesSummary() {
		Log.d(TAG, "Initializing preferences summary");
		final SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		handlePreferenceSummary(R.string.preference_background_music_key,
				R.string.preference_background_music_summary,
				R.array.preference_background_music_summary_suffixes,
				preferences);

		handlePreferenceSummary(R.string.preference_sound_effects_key,
				R.string.preference_sound_effects_summary,
				R.array.preference_sound_effects_summary_suffixes, preferences);
	}

	private void handlePreferenceSummary(final int preference,
			final int preferenceSummary, final int summarySuffixesArrayId,
			final SharedPreferences preferences) {

		final String prefKey = getResources().getString(preference);
		final Preference pref = findPreference(prefKey);
		final boolean prefValue = preferences.getBoolean(prefKey, true);
		final String[] suffixes = getResources().getStringArray(
				summarySuffixesArrayId);

		Log.d(TAG, "Handle preferences summary: " + prefKey);
		pref.setSummary(getResources().getString(preferenceSummary) + " "
				+ (prefValue ? suffixes[0] : suffixes[1]));
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "Registering preference listener");
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		CEEngine.MenuBackgroundMusic.start(getApplicationContext());

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "Unregistering preference listener");
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		// CEEngine.MenuBackgroundMusic.stop(getApplicationContext());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Unregistering preference listener");
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		// CEEngine.MenuBackgroundMusic.stop(getApplicationContext());
	}

	@Override
	public void onSharedPreferenceChanged(
			final SharedPreferences sharedPreferences, final String key) {

		Log.d(TAG, "Preference changed: " + key);
		final String BG_MUSIC_KEY = getResources().getString(
				R.string.preference_background_music_key);

		final String SOUND_EFFECTS_KEY = getResources().getString(
				R.string.preference_sound_effects_key);

		if (key.equals(BG_MUSIC_KEY)) {
			handlePreferenceSummary(R.string.preference_background_music_key,
					R.string.preference_background_music_summary,
					R.array.preference_background_music_summary_suffixes,
					sharedPreferences);

			if (sharedPreferences.getBoolean(BG_MUSIC_KEY, true)) {
				CEEngine.MenuBackgroundMusic.start(getApplicationContext());
			} else {
				CEEngine.MenuBackgroundMusic.stop(getApplicationContext());
			}

		} else if (key.equals(SOUND_EFFECTS_KEY)) {
			handlePreferenceSummary(R.string.preference_sound_effects_key,
					R.string.preference_sound_effects_summary,
					R.array.preference_sound_effects_summary_suffixes,
					sharedPreferences);
		}
	}

}
