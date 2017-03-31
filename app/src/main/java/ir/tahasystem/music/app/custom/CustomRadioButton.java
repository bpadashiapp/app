package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.TextView;

import ir.tahasystem.music.app.MyApplication;

public class CustomRadioButton extends RadioButton {

	private boolean hasMaxWidth;

	public CustomRadioButton(Context context) {
		super(context);
		init();
	}

	public CustomRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {

		
		setTypeface(MyApplication.getFontBold(), Typeface.BOLD);

		
	}
}
