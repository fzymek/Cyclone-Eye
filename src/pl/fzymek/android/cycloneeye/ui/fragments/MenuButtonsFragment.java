package pl.fzymek.android.cycloneeye.ui.fragments;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.engine.CEEngine;
import pl.fzymek.android.cycloneeye.ui.acitivites.ChooserActivity;
import pl.fzymek.android.cycloneeye.ui.acitivites.MenuPreferencesActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MenuButtonsFragment extends Fragment {

	public final static class FragmentIds {

		public final static int MENU_BUTTONS_FRAGMENT = 0;
		public final static int MENU_OPTIONS_FRAGMENT = 1;
		public final static int MENU_HIGHSCORES_FRAGMENT = 1;
		public final static int FRAGMENT_COUNT = MENU_HIGHSCORES_FRAGMENT + 1;

		private FragmentIds() {
		}
	}

	private final static String TAG = MenuButtonsFragment.class.getSimpleName();
	private static FragmentNavigationListener navigationListener;
	private final View.OnClickListener noActionHandler = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			displayAlertDialog();
		}

	};

	private final View.OnClickListener startGameHandler = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d("StartGameHandler", "Starting game activity");
//			final Intent game = new Intent(getActivity(), TriangleActivity.class);
//			final Intent game = new Intent(getActivity(), TexturedActivity.class);
			// final Intent game = new Intent(getActivity(),
			// MultipleTornadosActivity.class);
			// final Intent game = new Intent(getActivity(),
			// BasicPhysicsActivity.class);
			final Intent game = new Intent(getActivity(), ChooserActivity.class);
			getActivity().startActivity(game);

		}
	};

	private TextView play, options, highscores, exit;
	
	public interface FragmentNavigationListener {

		void navigateTo(int currentFragment, int nextFragment);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		final View view = inflater.inflate(R.layout.menu_buttons_fragment,
				container, false);

		play = (TextView) view.findViewById(R.id.play);
		options = (TextView) view.findViewById(R.id.options);
		highscores = (TextView) view.findViewById(R.id.highscores);
		exit = (TextView) view.findViewById(R.id.exit);

		fadeInButtons(new TextView[] { play, options, highscores, exit },
				R.anim.fade_in, 300);

		// play.setOnClickListener(noActionHandler);
		play.setOnClickListener(startGameHandler);
		options.setOnClickListener(new OpenPreferencesListener(getActivity()));
		highscores.setOnClickListener(new NavigationListener(
				FragmentIds.MENU_BUTTONS_FRAGMENT,
				FragmentIds.MENU_HIGHSCORES_FRAGMENT));

		exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: clean up resources/music/etc
				Log.d(TAG, "Stopping music thread with context from activity: "
						+ getActivity());
				CEEngine.MenuBackgroundMusic.stop(getActivity().getApplicationContext());
				
				final int myPid = Process.myPid();
				Log.d(TAG, "Quitting application by killing process with pid: "
						+ myPid);
				Process.killProcess(myPid);
			}
		});

		return view;
	};


	public static FragmentNavigationListener getNavigationListener() {
		return navigationListener;
	}

	public static void setNavigationListener(
			FragmentNavigationListener navigationListener) {
		MenuButtonsFragment.navigationListener = navigationListener;
	}

	private void displayAlertDialog() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder
				.setTitle("Info")
				.setMessage(
						"This functionality is not yet implemented. Please be patient, author is surely working hard to deliver this feature.")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d(TAG, "Hiding alert dialog");
						dialog.dismiss();
					}
				});
		final AlertDialog dialog = alertDialogBuilder.create();
		Log.d(TAG, "Displaying alert dialog");
		dialog.show();
	}

	private void fadeInButtons(final TextView[] buttons, int animation,
			int delay) {
		Log.d(TAG, "Fading in buttons");
		for (int i = 0; i < buttons.length; i++) {
			final Animation anim = AnimationUtils.loadAnimation(getActivity()
					.getApplicationContext(), R.anim.fade_in);
			anim.setStartOffset(delay);
			buttons[i].startAnimation(anim);
			delay += 300;
		}

	}

	private static class NavigationListener implements View.OnClickListener {

		private final static String TAG = OpenPreferencesListener.class
				.getSimpleName();
		private final int currentFragment;
		private final int nextFragment;

		public NavigationListener(final int currentFragment,
				final int nextFragment) {
			this.currentFragment = currentFragment;
			this.nextFragment = nextFragment;
		}

		@Override
		public void onClick(View v) {
			Log.d(TAG, "onClick -> navigating from " + currentFragment
					+ " to: " + nextFragment);
			navigationListener.navigateTo(currentFragment, nextFragment);
		}

	}
	
	private static class OpenPreferencesListener implements View.OnClickListener {

		private final static String TAG = OpenPreferencesListener.class
				.getSimpleName();
		private final Context context;

		public OpenPreferencesListener(final Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			Log.d(TAG, "Opening preferences");
			final Intent preferences = new Intent(context,
					MenuPreferencesActivity.class);
			context.startActivity(preferences);
		}

	}

	

}
