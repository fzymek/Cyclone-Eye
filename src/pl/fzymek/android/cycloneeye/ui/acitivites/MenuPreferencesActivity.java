package pl.fzymek.android.cycloneeye.ui.acitivites;

import pl.fzymek.android.cycloneeye.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
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
	}

	@Override
	public void onSharedPreferenceChanged(
			final SharedPreferences sharedPreferences, final String key) {

		// TODO: implement changes behaviour
	}

}
