package pl.fzymek.android.cycloneeye.game.engine;

import android.util.Log;

public class FPSCounter {
	
	long startTime = System.nanoTime();
	int frames;
	
	public void LogFPS() {
		frames++;
		if (System.nanoTime() - startTime >= 1000000000) {
			Log.d("FPSCounter", "fps: "+ frames);
			frames = 0;
			startTime = System.nanoTime();
		}
		
	}

}
