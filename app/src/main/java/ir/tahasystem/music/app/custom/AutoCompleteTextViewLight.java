package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class AutoCompleteTextViewLight extends AutoCompleteTextView {

	private boolean hasMaxWidth;

	public AutoCompleteTextViewLight(Context context) {
		super(context);
		init();
	}

	public AutoCompleteTextViewLight(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AutoCompleteTextViewLight(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {
		Typeface aFont = Typeface.createFromAsset(getContext().getAssets(),
				"irfontlight.ttf");
		
		setTypeface(aFont, Typeface.BOLD);

		
	}
}
