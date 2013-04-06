package pl.fzymek.android.cycloneeye.game.engine.impl;

import pl.fzymek.android.cycloneeye.game.engine.GLGame;
import pl.fzymek.android.cycloneeye.game.engine.Screen;

public abstract class GLScreen extends Screen {
	protected final CEGLGraphics glGraphics;
	protected final GLGame game;

	public GLScreen(GLGame game) {
		super(game);
		this.game = game;
		this.glGraphics = this.game.getGlGraphics();
	}
}
