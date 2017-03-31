package ir.tahasystem.music.app.findMap;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import ir.onlinefood.app.factory.R;


public class RecyclerItemViewHolder2 extends RecyclerView.ViewHolder {

    public final TextView mItemTextViewName;
    public final TextView mItemTextViewDes;




    public RecyclerItemViewHolder2(final View parent, final RecyclerAdapter2 aRecyclerAdapterKala, TextView itemTextViewName, TextView mItemTextViewDes) {
        super(parent);
        this.mItemTextViewName = itemTextViewName;
        this.mItemTextViewDes = mItemTextViewDes;


        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aRecyclerAdapterKala.onTapz(getAdapterPosition());
                ani(v);
            }
        });
    }


    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }

    public static RecyclerItemViewHolder2 newInstance(RecyclerAdapter2 aRecyclerAdapterKala, View parent) {


        TextView itemTextViewName = (TextView) parent.findViewById(R.id.itemTextViewName);
        TextView mItemTextViewDes = (TextView) parent.findViewById(R.id.itemTextViewDes);


        return new RecyclerItemViewHolder2(parent, aRecyclerAdapterKala, itemTextViewName,
                mItemTextViewDes);
    }

}
