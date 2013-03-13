package pl.fzymek.android.cycloneeye.game.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GLGameSurfaceView extends GLSurfaceView {

	public static Context context = null;

	public GLGameSurfaceView(Context context) {
		super(context);
	}

	public GLGameSurfaceView(Context context, AttributeSet attr) {
		super(context, attr);
	}

}
