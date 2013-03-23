package pl.fzymek.android.cycloneeye.game.engine.gl;

import android.util.Log;

/**
 * Animation class
 * 
 * @author Filip
 * 
 */
public class Animation {

	private final static String TAG = Animation.class.getSimpleName();

	public static final int ANIMATION_LOOPING = 0;
	public static final int ANIMATION_NONLOOPING = 1;
	final TextureRegion[] keyFrames;
	final float frameDuration;

	public Animation(float frameDuration, TextureRegion... keyFrames) {
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;

		Log.d(TAG, "Animation frame length: " + frameDuration + ", with: "
				+ keyFrames.length + " frames");

	}

	public TextureRegion getKeyFrame(float stateTime, int mode) {
		int frameNumber = (int) (stateTime / frameDuration);
		if (mode == ANIMATION_NONLOOPING) {
			frameNumber = Math.min(keyFrames.length - 1, frameNumber);
		} else {
			frameNumber = frameNumber % keyFrames.length;
		}
		return keyFrames[frameNumber];
	}
}