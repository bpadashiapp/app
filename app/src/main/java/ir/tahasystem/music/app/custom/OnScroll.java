package ir.tahasystem.music.app.custom;

import android.widget.AbsListView;

public abstract class OnScroll implements AbsListView.OnScrollListener {

    public abstract void onScrollUp();

    public abstract void onScrollDown();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    int last = 0;
    boolean control = true;

    @Override
    public void onScroll(AbsListView view, int current, int visibles, int total) {
        if (current < last && !control) {
            onScrollUp();
            control = true;
        } else if (current > last && control) {
            onScrollDown();
            control = false;
        }

        last = current;
    }
}