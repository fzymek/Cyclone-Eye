package pl.fzymek.android.cycloneeye.ui.acitivites;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.view.GLGameRenderer;
import pl.fzymek.android.cycloneeye.game.view.GLGameSurfaceView;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GameActivity extends Activity {

	private final static String TAG = GameActivity.class.getSimpleName();
	private GLSurfaceView glSurface;
	public static TextView score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.game);
		score = (TextView) findViewById(R.id.score_text);

		glSurface = (GLSurfaceView) findViewById(R.id.gl_game_view);
		GLGameSurfaceView.context = this;
		glSurface.setEGLConfigChooser(false);
		glSurface.setRenderer(new GLGameRenderer(this, score));
		glSurface.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		glSurface.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		glSurface.onPause();
	}

}
