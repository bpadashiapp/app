package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FooRecyclerView extends RecyclerView {

private boolean verticleScrollingEnabled = true;

public void enableVersticleScroll (boolean enabled) {
    verticleScrollingEnabled = enabled;
}

public boolean isVerticleScrollingEnabled() {
    return verticleScrollingEnabled;
}

@Override
public int computeVerticalScrollRange() {

    if (isVerticleScrollingEnabled())
        return super.computeVerticalScrollRange();
    return 0;
}


@Override
public boolean onInterceptTouchEvent(MotionEvent e) {

    if(isVerticleScrollingEnabled())
        return super.onInterceptTouchEvent(e);
    return false;

}



public FooRecyclerView(Context context) {
    super(context);
}
public FooRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
}
public FooRecyclerView(Context context, @Nullable AttributeSet attrs,        int defStyle) {
        super(context, attrs, defStyle);
    }
}