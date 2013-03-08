package pl.fzymek.android.cycloneeye;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends Activity
{
	
	private final static String TAG = MainActivity.class.getSimpleName();
	private TextView play, options, highscores, exit;
	private final View.OnClickListener clickHandler = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			displayAlertDialog();
		}

	};

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		enableFullScreen();

		setContentView(R.layout.main);

		play = (TextView) findViewById(R.id.play);
		options = (TextView) findViewById(R.id.options);
		highscores = (TextView) findViewById(R.id.highscores);
		exit = (TextView) findViewById(R.id.exit);

		
		fadeInButtons(new TextView[] { play, options, highscores, exit },
				R.anim.fade_in, 300);

		exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: clean up resources/music/etc
				final int myPid = Process.myPid();
				Log.d(TAG, "Quitting application by killing process with pid: "
						+ myPid);
				Process.killProcess(myPid);
			}
		});
		
		play.setOnClickListener(clickHandler);
		options.setOnClickListener(clickHandler);
		highscores.setOnClickListener(clickHandler);


    }

	private void enableFullScreen() {
		Log.d(TAG, "Enabling full screen mode");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}
	
	private void displayAlertDialog() {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder
				.setTitle("Info")
				.setMessage(
						"This functionality is not yet implemented. Please be patient, author is surely working hard to deliver this feature.")
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
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
			final Animation anim = AnimationUtils.loadAnimation(this,
					R.anim.fade_in);
			anim.setStartOffset(delay);
			buttons[i].startAnimation(anim);
			delay += 300;
		}

	}
}
