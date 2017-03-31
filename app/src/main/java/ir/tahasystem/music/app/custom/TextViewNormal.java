package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import ir.tahasystem.music.app.MyApplication;

public class TextViewNormal extends TextView {
	
	private boolean hasMaxWidth;

	public TextViewNormal(Context context) {
		super(context);
		init();
	}

	public TextViewNormal(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TextViewNormal(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {

		
		setTypeface(MyApplication.getFontLight(), Typeface.BOLD);

		
	}
}
