package pl.fzymek.android.cycloneeye;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends Activity
{
	
	private TextView play, exit;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		enableFullScreen();

		setContentView(R.layout.main);

		play = (TextView) findViewById(R.id.play);
		exit = (TextView) findViewById(R.id.exit);

		final Animation playAnim = AnimationUtils.loadAnimation(this,
				R.anim.fade_in);
		final Animation exitAnim = AnimationUtils.loadAnimation(this,
				R.anim.fade_in);
		play.startAnimation(playAnim);
		exitAnim.setStartOffset(300);
		exit.startAnimation(exitAnim);

    }

	private void enableFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}
}
