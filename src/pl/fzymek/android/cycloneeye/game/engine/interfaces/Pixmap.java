package pl.fzymek.android.cycloneeye.game.engine.interfaces;

import pl.fzymek.android.cycloneeye.game.engine.interfaces.Graphics.PixmapFormat;

public interface Pixmap {
	public int getWidth();

	public int getHeight();

	public PixmapFormat getFormat();

	public void dispose();
}