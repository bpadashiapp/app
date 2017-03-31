package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewMenu extends TextView {

	private boolean hasMaxWidth;

	public TextViewMenu(Context context) {
		super(context);
		init();
	}

	public TextViewMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TextViewMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {
		Typeface aFont = Typeface.createFromAsset(getContext().getAssets(),
				"menufont.ttf");
		
		setTypeface(aFont, Typeface.BOLD);

		
	}
}
