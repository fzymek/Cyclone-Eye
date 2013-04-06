package pl.fzymek.android.cycloneeye.game.engine;


public interface Game {

	Input getInput();

	FileIO getFileIO();

	Audio getAudio();

	void setScreen(Screen screen);

	Screen getCurrentScreen();

	Screen getStartScreen();

}
