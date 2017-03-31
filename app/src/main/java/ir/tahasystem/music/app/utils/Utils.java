package ir.tahasystem.music.app.utils;

import android.content.Context;
import android.content.res.TypedArray;

import ir.onlinefood.app.factory.R;


public class Utils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }
    
    public static int getSliderHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.slider_h);
    }
}
