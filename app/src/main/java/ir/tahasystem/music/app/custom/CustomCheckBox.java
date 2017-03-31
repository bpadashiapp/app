package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import ir.tahasystem.music.app.MyApplication;

public class CustomCheckBox extends android.support.v7.widget.AppCompatCheckBox  {


	private boolean hasMaxWidth;

	public CustomCheckBox(Context context) {
		super(context);
		init();
	}

	public CustomCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {



		setTypeface(MyApplication.getFontLight(), Typeface.BOLD);



	}


}
