package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MyApplication;

public class NumberPicker extends android.widget.NumberPicker {
    private Context context;
    private Typeface aFont;

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        aFont = Typeface.createFromAsset(context.getAssets(),
                "irfontnumlight.ttf");
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTypeface(MyApplication.getFontBold(), Typeface.BOLD);
            ((EditText) view).setTextColor(getResources().getColor(R.color.color_primary));

        }

       // overrideFonts(context,view);
    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(aFont, Typeface.BOLD);
            }
        } catch (Exception e) {
        }
    }

}