package ir.tahasystem.music.app.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import ir.onlinefood.app.factory.R;


public class RecyclerItemViewHolderMsg2HOrder extends RecyclerView.ViewHolder {

    public final TextView mItemTextViewName;
    public final TextView mItemTextViewDes;

    public final ImageView mImageView;


    public RecyclerItemViewHolderMsg2HOrder(final View parent, final RecyclerAdapterMsg2HOrder aRecyclerAdapterKala, TextView itemTextViewName, TextView mItemTextViewDes
    , ImageView mImageView) {
        super(parent);

        this.mItemTextViewName = itemTextViewName;
        this.mItemTextViewDes = mItemTextViewDes;
        this.mImageView=mImageView;

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

    public static RecyclerItemViewHolderMsg2HOrder newInstance(RecyclerAdapterMsg2HOrder aRecyclerAdapterKala, View parent) {


        TextView itemTextViewName = (TextView) parent.findViewById(R.id.itemTextViewName);
        TextView mItemTextViewDes = (TextView) parent.findViewById(R.id.itemTextViewDes);
        ImageView mImageView= (ImageView) parent.findViewById(R.id.itemImage);

        return new RecyclerItemViewHolderMsg2HOrder(parent, aRecyclerAdapterKala, itemTextViewName,
                mItemTextViewDes,mImageView);
    }

}
