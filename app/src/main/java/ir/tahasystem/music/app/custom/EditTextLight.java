package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import ir.tahasystem.music.app.MyApplication;
import ir.tahasystem.music.app.findMap.Fragment1;

public class EditTextLight extends EditText {
	
	private boolean hasMaxWidth;

	public EditTextLight(Context context) {
		super(context);
		init();
	}

	public EditTextLight(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EditTextLight(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {

		
		setTypeface(MyApplication.getFontLight(), Typeface.BOLD);

		
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			setFocusableInTouchMode(true);
			clearFocus();

			if (Fragment1.context != null && Fragment1.context.aButtonCall != null) {

				Fragment1.context.aButtonCall.setFocusableInTouchMode(true);
				Fragment1.context.aButtonCall.requestFocus();

			}

			return false;  // So it is not propagated.
		}
		return super.dispatchKeyEvent(event);
	}

	public void setHintz(String str){
		this.setHint(str);
	}
}


