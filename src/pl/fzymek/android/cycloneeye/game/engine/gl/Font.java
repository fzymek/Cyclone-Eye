package pl.fzymek.android.cycloneeye.game.engine.gl;

import android.annotation.SuppressLint;

/**
 * Represents font from bitmap
 * 
 * @author Filip
 * 
 */
public class Font {

	public final Texture texture;
	public final int glyphWidth;
	public final int glyphHeight;
	public final TextureRegion[] glyphs = new TextureRegion[96];

	public Font(final Texture texture, final int offsetX, final int offsetY,
			final int glyphsPerRow, final int glyphWidth, final int glyphHeight) {
		this.texture = texture;
		this.glyphWidth = glyphWidth;
		this.glyphHeight = glyphHeight;
		int x = offsetX;
		int y = offsetY;
		for (int i = 0; i < 96; i++) {
			glyphs[i] = new TextureRegion(texture, x, y, glyphWidth,
					glyphHeight);
			x += glyphWidth;
			if (x == offsetX + glyphsPerRow * glyphWidth) {
				x = offsetX;
				y += glyphHeight;
			}
		}
	}

	@SuppressLint("DefaultLocale")
	public void drawText(final SpriteBatcher batcher, String text,
			float x, float y) {
		text = text.toUpperCase();
		final int len = text.length();
		for (int i = 0; i < len; i++) {
			int c = text.charAt(i) - ' ';
			if (c < 0 || c > glyphs.length - 1)
				continue;

			final TextureRegion glyph = glyphs[c];
			batcher.drawSprite(x, y, glyphWidth, glyphHeight, glyph);
			x += glyphWidth;
		}
	}
}