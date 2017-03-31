package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

public class CustomCardView extends CardView {

    public CustomCardView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void drawableStateChanged() {

        super.drawableStateChanged();

        //if (isPressed()) {
           // this.setCardBackgroundColor(Color.RED);
        //} else {
          //  this.setCardBackgroundColor(Color.RED);
        //}
    }
}