package pl.fzymek.android.cycloneeye.ui.acitivites;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.view.GLGameRenderer;
import pl.fzymek.android.cycloneeye.game.view.GLGameSurfaceView;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {

	private final static String TAG = GameActivity.class.getSimpleName();
	private GLSurfaceView glSurface;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Log.d(TAG, "onCreate");
		setContentView(R.layout.game);

		glSurface = (GLSurfaceView) findViewById(R.id.gl_game_view);
		Log.d(TAG, "GlSurface view found:" + glSurface);
		GLGameSurfaceView.context = this;
		glSurface.setEGLConfigChooser(false);
		glSurface.setRenderer(new GLGameRenderer(this));
		glSurface.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		Log.d(TAG, "Renderer created and started");

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
