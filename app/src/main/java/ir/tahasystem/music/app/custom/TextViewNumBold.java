package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import ir.tahasystem.music.app.MyApplication;

public class TextViewNumBold extends TextView {
	
	private boolean hasMaxWidth;

	public TextViewNumBold(Context context) {
		super(context);
		init();
	}

	public TextViewNumBold(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TextViewNumBold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {

		
		setTypeface(MyApplication.getFontBold(), Typeface.BOLD);

		
	}
}
