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

	public static final int NUMBER_OF_OBSTACLES = 16;
	public static final int NUMBER_OF_TARGETS = 5;
	public static final int NUMBER_OF_POWERUPS = 5;
	public static final int TARGET_CASTLE_ID = NUMBER_OF_TARGETS - 1;

	// textures
	public static Texture objectsTexture;
	public static Texture targetsTexture;

	// texture regions
	public static TextureRegion trees[];
	public static TextureRegion targets[];
	public static TextureRegion backgroundRegion;

	// animations
	public static Animation explosionAnim;
	public static Animation powerUpAnim;
	public static Animation cycloneAnim;
	public static Animation jewelsAnim[];

	// music
	public static Music music;

	// sounds

	public static Sound collectSound;
	public static Sound powerUpSound;
	public static Sound explosionSound;
	public static Sound levelUpSound;
	public static Sound gameOverSound;

	// fonts
	public static TexFont font;
	public static String fontVersion;


	public static void load(final CEGame game) {

		objectsTexture = new Texture(game, R.drawable.objects_atlas);
		backgroundRegion = new TextureRegion(objectsTexture, 512, 0, 512, 512);
		fontVersion = game.getResources().getString(R.string.font);

		float w, h;
		w = h = 80;
		cycloneAnim = new Animation(0.1f, 
				new TextureRegion(objectsTexture, 0,   512, w, h),
				new TextureRegion(objectsTexture, 80,  512, w, h),
				new TextureRegion(objectsTexture, 160, 512, w, h),
				new TextureRegion(objectsTexture, 80,  512, w, h),
				new TextureRegion(objectsTexture, 240, 512, w, h),
				new TextureRegion(objectsTexture, 400, 512, w, h),
				new TextureRegion(objectsTexture, 320, 512, w, h),
				new TextureRegion(objectsTexture, 400, 512, w, h),
				new TextureRegion(objectsTexture, 240, 512, w, h)
		);
		
		int cntr = 0;
		w = h = 128;
		trees = new TextureRegion[NUMBER_OF_OBSTACLES];
		trees[cntr++] = new TextureRegion(objectsTexture, 0.0f,  0.0f,  w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, w,     0.0f,  w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 2 * w, 0.0f,  w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 3 * w, 0.0f,  w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 0.0f,  h,     w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, w,     h,     w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 2 * w, h,     w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 3 * w, h,     w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 0.0f,  2 * h, w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, w,     2 * h, w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 2 * w, 2 * h, w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 3 * w, 2 * h, w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 0.0f,  3 * h, w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, w,     3 * h, w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 2 * w, 3 * h, w, h);
		trees[cntr++] = new TextureRegion(objectsTexture, 3 * w, 3 * h, w, h);

		// for (int i = 0; i < trees.length; i++) {
		// Log.d("Assets - trees", trees[i].toString());
		// }

		targetsTexture = new Texture(game, R.drawable.targets_atlas);
		targets = new TextureRegion[NUMBER_OF_TARGETS];

		cntr = 0;
		w = h = 200;
		targets[cntr++] = new TextureRegion(targetsTexture, 0.0f,   512.0f,     w, 		h);
		targets[cntr++] = new TextureRegion(targetsTexture, w,      512.0f,     w, 		h);
		targets[cntr++] = new TextureRegion(targetsTexture, 0.0f,   512.0f + h, w, 		h);
		targets[cntr++] = new TextureRegion(targetsTexture, w,      512.0f + h, w, 		h);
		targets[cntr++] = new TextureRegion(targetsTexture, 512.0f, 512.0f,     512.0f, 512.0f); 
		// for (int i = 0; i < targets.length; i++) {
		// Log.d("Assets - targets", targets[i].toString());
		// }


		w = h = 256;
		explosionAnim = new Animation(0.1f,
				new TextureRegion(targetsTexture, 0,     0, w, h),
				new TextureRegion(targetsTexture, w,     0, w, h),
				new TextureRegion(targetsTexture, 2 * w, 0, w, h),
				new TextureRegion(targetsTexture, 3 * w, 0, w, h),
				new TextureRegion(targetsTexture, 0,     h, w, h),
				new TextureRegion(targetsTexture, w,     h, w, h),
				new TextureRegion(targetsTexture, 2 * w, h, w, h),
				new TextureRegion(targetsTexture, 3 * w, h, w, h)
				);

		jewelsAnim = new Animation[NUMBER_OF_POWERUPS];
		w = 64.0f;
		h = 256.0f / 5.0f;
		jewelsAnim[0] = new Animation(0.1f, 
				new TextureRegion(objectsTexture, 0,   594.0f, w, h), 
				new TextureRegion(objectsTexture, w,   594.0f, w, h),
				new TextureRegion(objectsTexture, 2*w, 594.0f, w, h),
				new TextureRegion(objectsTexture, 3*w, 594.0f, w, h));
		
		jewelsAnim[1] = new Animation(0.1f, 
				new TextureRegion(objectsTexture, 0,   594.0f + h, w, h), 
				new TextureRegion(objectsTexture, w,   594.0f + h, w, h),
				new TextureRegion(objectsTexture, 2*w, 594.0f + h, w, h),
				new TextureRegion(objectsTexture, 3*w, 594.0f + h, w, h));
		
		jewelsAnim[2] = new Animation(0.1f, 
				new TextureRegion(objectsTexture, 0,   594.0f + 2*h, w, h), 
				new TextureRegion(objectsTexture, w,   594.0f + 2*h, w, h),
				new TextureRegion(objectsTexture, 2*w, 594.0f + 2*h, w, h),
				new TextureRegion(objectsTexture, 3*w, 594.0f + 2*h, w, h));
		
		jewelsAnim[3] = new Animation(0.1f, 
				new TextureRegion(objectsTexture, 0,   594.0f + 3*h, w, h), 
				new TextureRegion(objectsTexture, w,   594.0f + 3*h, w, h),
				new TextureRegion(objectsTexture, 2*w, 594.0f + 3*h, w, h),
				new TextureRegion(objectsTexture, 3*w, 594.0f + 3*h, w, h));

		jewelsAnim[4] = new Animation(0.1f, 
				new TextureRegion(objectsTexture, 0,     594.0f + 4 * h, w, h), 
				new TextureRegion(objectsTexture, w,     594.0f + 4 * h, w, h),
				new TextureRegion(objectsTexture, 2 * w, 594.0f + 4 * h, w, h),
				new TextureRegion(objectsTexture, 3 * w, 594.0f + 4 * h, w, h));


		font = new TexFont(game, game.getGlGraphics().getGl());
		try {

			Log.d("ASSETS", "Loading font from: " + fontVersion);
			font.LoadFont(fontVersion, game.getGlGraphics().getGl());
		} catch (IOException e) {
			Log.e("Assets", "Cannot load font! " + e.getMessage());
		}

		// init sound
		loadSounds(game);

		Settings.initialize(game);

	}

	private static void loadSounds(final CEGame game) {
		final Audio audio = game.getAudio();
		collectSound = audio.newSound(R.raw.coin_collect);
		explosionSound = audio.newSound(R.raw.explosion);
		levelUpSound = audio.newSound(R.raw.level_up);
		gameOverSound = audio.newSound(R.raw.game_over);
	}
	
	public static void reload(final CEGame game) {

		targetsTexture.reload();
		objectsTexture.reload();

		try {
			font.LoadFont(fontVersion, game.getGlGraphics().getGl());
		} catch (IOException e) {
			Log.e("Assets", "Cannot load font!");
		}

		// init sound
		loadSounds(game);

		Settings.initialize(game);
	}

}
