package pl.fzymek.android.cycloneeye.game.engine.gl;

import android.util.Log;

/**
 * Utility class for logging FPS
 * 
 * @author Filip
 * 
 */
public class FPSCounter {
	
	private final static String TAG = FPSCounter.class.getSimpleName();

	long startTime = System.nanoTime();
	int frames;
	
	public void LogFPS() {
		frames++;
		if (System.nanoTime() - startTime >= 1000000000) {
			Log.d(TAG, "fps: " + frames);
			frames = 0;
			startTime = System.nanoTime();
		}
		
	}

}
