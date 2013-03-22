package pl.fzymek.android.cycloneeye.game.engine;

public interface Audio {
	public Music newMusic(String filename);

	public Sound newSound(String filename);
}