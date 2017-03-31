package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import ir.tahasystem.music.app.MyApplication;

public class TextViewAwsome extends TextView {
	
	private boolean hasMaxWidth;

	public TextViewAwsome(Context context) {
		super(context);
		init();
	}

	public TextViewAwsome(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TextViewAwsome(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}



	protected void init() {

		
		setTypeface(MyApplication.getFontAwsome(), Typeface.BOLD);

		
	}
}
