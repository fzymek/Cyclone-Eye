/**
 * 
 */
package pl.fzymek.android.cycloneeye.widgets;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.customfonts.CustomFont;
import pl.fzymek.android.cycloneeye.customfonts.CustomFontsManagerFactory;
import pl.fzymek.android.cycloneeye.customfonts.CustomFontsManagerFactory.CustomFontsManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author zymekfil
 * 
 */
public class TypefacedTextView extends TextView {

	public TypefacedTextView(Context context) {
		super(context);
	}

	public TypefacedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context, attrs);
	}

	public TypefacedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		parseAttributes(context, attrs);
	}

	public void parseAttributes(Context context, AttributeSet attrSet) {
		TypedArray typedArray = context.obtainStyledAttributes(attrSet,
				R.styleable.TypefacedTextView);
		int typeface = typedArray.getInt(R.styleable.TypefacedTextView_font, 0);

		if (!isInEditMode()) {
			// avoid exceptions in edit mode in eclipse
			final CustomFontsManagerFactory fontManagerFactory = CustomFontsManagerFactory
					.getInstance();
			final CustomFontsManager fontManager = fontManagerFactory
					.getFontManager(getContext());
			final Typeface font = fontManager.getFont(CustomFont
					.fromId(typeface));

			setTypeface(font);
		}
	}

}
