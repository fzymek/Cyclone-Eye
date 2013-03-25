package pl.fzymek.android.cycloneeye.game.engine.impl;

import pl.fzymek.android.cycloneeye.game.engine.Game;
import pl.fzymek.android.cycloneeye.game.engine.Screen;

public abstract class CEScreen extends Screen {


	protected final CEGLGraphics glGraphics;
	protected final CEGame game;

	public CEScreen(Game game) {
		super(game);
		this.game = (CEGame) game;
		this.glGraphics = this.game.getGlGraphics();
	}



}
