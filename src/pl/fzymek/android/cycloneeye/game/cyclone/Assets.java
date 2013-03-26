package pl.fzymek.android.cycloneeye.game.cyclone;

import java.io.IOException;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.game.engine.Audio;
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
	public static Texture targetsTexture;
	public static Texture treeTexture;
	public static Texture explosionTexture;

	// texture regions
	public static TextureRegion trees[];
	public static TextureRegion targets[];
	public static TextureRegion backgroundRegion;

	// animations
	public static Animation explosionAnim;
	public static Animation powerUpAnim;
	public static Animation cycloneAnim;

	// music
	public static Music music;

	// sounds

	public static Sound collectSound;
	public static Sound powerUpSound;
	public static Sound explosionSound;

	// fonts
	public static TexFont font;





	public static void load(final CEGame game) {

		background = new Texture(game, R.drawable.grass);
		backgroundRegion = new TextureRegion(background, 0, 0, 512, 512);

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
		trees = new TextureRegion[16];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				trees[cntr++] = new TextureRegion(treeTexture, i * 0.25f,
						j * 0.25f, 128, 128);

			}
		}

		targetsTexture = new Texture(game, R.drawable.targets);
		targets = new TextureRegion[4];
		w = h = 200;
		cntr = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				targets[cntr++] = new TextureRegion(targetsTexture, i * cntr, j * cntr, w, h);
			}
		}

		explosionTexture = new Texture(game, R.drawable.explosion);
		w = h = 256;
		explosionAnim = new Animation(0.1f,
				new TextureRegion(explosionTexture, 0, 	 0,   w, h),
				new TextureRegion(explosionTexture, 256, 0,   w, h),
				new TextureRegion(explosionTexture, 512, 0,   w, h),
				new TextureRegion(explosionTexture, 768, 0,   w, h),
				new TextureRegion(explosionTexture, 0,	 256, w, h),
				new TextureRegion(explosionTexture, 256, 256, w, h),
				new TextureRegion(explosionTexture, 512, 256, w, h),
				new TextureRegion(explosionTexture, 768, 256, w, h)
				);


		font = new TexFont(game, game.getGlGraphics().getGl());
		try {
			font.LoadFont("fonts/Verdana.bff", game
					.getGlGraphics().getGl());
		} catch (IOException e) {
			Log.e("Assets", "Cannot load font!");
		}

		// init sound
		final Audio audio = game.getAudio();
		collectSound = audio.newSound(R.raw.coin_collect);
		explosionSound = audio.newSound(R.raw.explosion);

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

		final Audio audio = game.getAudio();
		collectSound = audio.newSound(R.raw.coin_collect);
		explosionSound = audio.newSound(R.raw.explosion);
	}

}
