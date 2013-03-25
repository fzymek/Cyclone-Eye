package pl.fzymek.android.cycloneeye.game.cyclone;

import static android.opengl.GLES10.*;

import java.io.IOException;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.engine.Music;
import pl.fzymek.android.cycloneeye.game.engine.Sound;
import pl.fzymek.android.cycloneeye.game.engine.gl.Animation;
import pl.fzymek.android.cycloneeye.game.engine.gl.Texture;
import pl.fzymek.android.cycloneeye.game.engine.gl.TextureRegion;
import pl.fzymek.android.cycloneeye.game.engine.impl.CEGame;
import android.util.Log;
import codehead.cbfg.TexFont;

public class Assets {

	// textures
	public static Texture background;
	public static Texture cyclone;
	public static Texture text;
	public static Texture items;
	public static Texture treeTexture;

	// texture regions
	public static TextureRegion tree[];
	public static TextureRegion house;
	public static TextureRegion ready;
	public static TextureRegion pause;
	public static TextureRegion resume;
	public static TextureRegion gameOver;
	public static TextureRegion backgroundRegion;
	public static TextureRegion endLevel;
	public static TextureRegion powerUp;

	// animations
	public static Animation explosionAnim;
	public static Animation powerUpAnim;
	public static Animation cycloneAnim;

	// music
	public static Music music;

	// sounds
	public static Sound powerUpSound;
	public static Sound explosionSound;
	// public static Font font;
	public static TexFont font;
	private static Boolean hasTextLoaded;





	public static void load(final CEGame game) {

		background = new Texture(game, R.drawable.grass);
		background.bind();
		background.setFilters(GL_NEAREST, GL_LINEAR, GL_REPEAT, GL_REPEAT);
		
		backgroundRegion = new TextureRegion(background, 0, 0, 512, 512);

		// cyclone = new Texture(game, R.drawable.tornado_sprites);
		cyclone = new Texture(game, R.drawable.tornado_sprites_full);
		int w, h;
		w = h = 80;
		cycloneAnim = new Animation(0.1f, 
				new TextureRegion(cyclone, 0, 0, w, h),
				new TextureRegion(cyclone, 80, 0, w, h),
				new TextureRegion(cyclone, 160, 0, w, h),
				new TextureRegion(cyclone, 80, 0, w, h),
				new TextureRegion(cyclone, 240, 0, w, h),
				new TextureRegion(cyclone, 400, 0, w, h),
				new TextureRegion(cyclone, 320, 0, w, h),
				new TextureRegion(cyclone, 400, 0, w, h),
				new TextureRegion(cyclone, 240, 0, w, h)
		);
		
		treeTexture = new Texture(game, R.drawable.tree_sprites);
		int cntr = 0;
		tree = new TextureRegion[16];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				tree[cntr++] = new TextureRegion(treeTexture, i * 0.25f,
						j * 0.25f, 128, 128);

			}
		}
		// text = new Texture(game, "fonts/font.png");

		font = new TexFont(game, game.getGlGraphics().getGl());
		try {
			font.LoadFont("fonts/Verdana.bff", game
					.getGlGraphics().getGl());
		} catch (IOException e) {
			Log.e("Assets", "Cannot load font!");
		}

		// init sound

	}
	
	public static void reload(final CEGame game) {

		background.reload();
		cyclone.reload();
		treeTexture.reload();
		try {
			font.LoadFont("fonts/Verdana.bff", game.getGlGraphics().getGl());
		} catch (IOException e) {
			Log.e("Assets", "Cannot load font!");
		}
		// init soud

	}

}
