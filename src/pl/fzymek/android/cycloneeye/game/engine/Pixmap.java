package pl.fzymek.android.cycloneeye.game.engine;

import pl.fzymek.android.cycloneeye.game.engine.Graphics.PixmapFormat;

public interface Pixmap {
	public int getWidth();

	public int getHeight();

	public PixmapFormat getFormat();

	public void dispose();
}