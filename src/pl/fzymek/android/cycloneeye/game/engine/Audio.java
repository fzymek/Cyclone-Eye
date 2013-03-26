package pl.fzymek.android.cycloneeye.game.engine;

import android.content.Context;

public interface Audio {
	public Music newMusic(String filename);

	public Music newMusic(Context context, int music);

	public Sound newSound(String filename);

	public Sound newSound(int sound);
}