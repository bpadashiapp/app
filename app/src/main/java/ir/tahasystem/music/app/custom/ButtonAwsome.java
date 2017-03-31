package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import ir.tahasystem.music.app.MyApplication;

public class ButtonAwsome extends Button {
	

	public ButtonAwsome(Context context) {
		super(context);
		init();
	}

	public ButtonAwsome(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ButtonAwsome(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {

		
		setTypeface(MyApplication.getFontAwsome(), Typeface.NORMAL);

		
	}
}
