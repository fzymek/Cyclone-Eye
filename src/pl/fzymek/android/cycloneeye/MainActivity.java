package pl.fzymek.android.cycloneeye;

import pl.fzymek.android.cycloneeye.game.engine.CEEngine;
import pl.fzymek.android.cycloneeye.ui.fragments.MenuButtonsFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends FragmentActivity implements
		MenuButtonsFragment.FragmentNavigationListener {

	private final static String TAG = MainActivity.class.getSimpleName();
	private Fragment[] fragments = new Fragment[MenuButtonsFragment.FragmentIds.FRAGMENT_COUNT];

	// private SoundEffects sfx;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		enableFullScreen();

		setContentView(R.layout.main);
		final FragmentManager fm = getSupportFragmentManager();

		initializeFragments(fm);
		// sfx = SoundEffects.getInstance(this);
		Log.d(TAG, "Starting music service");
		CEEngine.MenuBackgroundMusic.start(MainActivity.this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		CEEngine.MenuBackgroundMusic.start(MainActivity.this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		CEEngine.MenuBackgroundMusic.stop(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CEEngine.MenuBackgroundMusic.stop(MainActivity.this);
		// sfx.release();
	}

	@Override
	public void navigateTo(int currentFragment, int nextFragment) {
		Log.d(TAG, "navigate: " + currentFragment + " -> " + nextFragment);

		final FragmentManager manager = getSupportFragmentManager();
		final FragmentTransaction transaction = manager.beginTransaction();

		// replace hide current fragment and show selected fragment
		transaction.hide(fragments[currentFragment]);
		transaction.show(fragments[nextFragment]);
		transaction.addToBackStack(null);
		transaction.commit();
		// sfx.playSound(SoundEffects.MENU_BUTTON_CLICK);

	}

	private void initializeFragments(final FragmentManager fm) {

		fragments[MenuButtonsFragment.FragmentIds.MENU_BUTTONS_FRAGMENT] = fm
				.findFragmentById(R.id.menu_buttons_fragment);
		fragments[MenuButtonsFragment.FragmentIds.MENU_OPTIONS_FRAGMENT] = fm
				.findFragmentById(R.id.menu_options_fragment);
		fragments[MenuButtonsFragment.FragmentIds.MENU_HIGHSCORES_FRAGMENT] = fm
				.findFragmentById(R.id.menu_highscores_fragment);

		MenuButtonsFragment.setNavigationListener(this);

		final FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < MenuButtonsFragment.FragmentIds.FRAGMENT_COUNT; i++) {
			transaction.hide(fragments[i]);
		}
		transaction
				.show(fragments[MenuButtonsFragment.FragmentIds.MENU_BUTTONS_FRAGMENT]);
		transaction.commit();
	}

	private void enableFullScreen() {
		Log.d(TAG, "Enabling full screen mode");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}

}
