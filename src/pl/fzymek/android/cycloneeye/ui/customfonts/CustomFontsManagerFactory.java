package pl.fzymek.android.cycloneeye.ui.customfonts;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;


public final class CustomFontsManagerFactory {
	
	private final static Map<Context, CustomFontsManager> fontManagerFactoryMap = new HashMap<Context, CustomFontsManager>();

	private static CustomFontsManagerFactory instance;

	private CustomFontsManagerFactory() {
	}
	
	public static CustomFontsManagerFactory getInstance() {
		if (instance == null) {
			instance = new CustomFontsManagerFactory();
		}
		return instance;
	}
	
	public CustomFontsManager getFontManager(Context context) {
		if (fontManagerFactoryMap.containsKey(context)) {
			return fontManagerFactoryMap.get(context);
		} else {
			final CustomFontsManager fontManager = new CustomFontsManager(context);
			fontManagerFactoryMap.put(context, fontManager);
			return fontManager;
		}
	}
	
	public static class CustomFontsManager {

		private Context context;

		private final SparseArray<Typeface> fontsArray;

		private CustomFontsManager(Context context) {
			this.context = context;
			fontsArray = new SparseArray<Typeface>();

			for (CustomFont customFont : CustomFont.values()) {
				final Typeface font = Typeface.createFromAsset(
						this.context.getAssets(), customFont
						.getResourcePath());
				fontsArray.put(customFont.getId(), font);
			}

		}

		public Typeface getFont(CustomFont font) {
			return fontsArray.get(font.getId());
		}

	}

}
