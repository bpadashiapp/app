package ir.tahasystem.music.app.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import ir.onlinefood.app.factory.R;

public class RecyclerItemViewHolderGallery extends RecyclerView.ViewHolder {

    public final TextView mItemTextViewName;
    public final ImageView aImageView;


    public RecyclerItemViewHolderGallery(final View parent, final RecyclerAdapterGallery aRecyclerAdapterKala, TextView itemTextViewName, ImageView aImageView) {
        super(parent);
        this.mItemTextViewName = itemTextViewName;
        this.aImageView = aImageView;

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

    public static RecyclerItemViewHolderGallery newInstance(RecyclerAdapterGallery aRecyclerAdapterKala, View parent) {


        TextView itemTextViewName = (TextView) parent.findViewById(R.id.itemTextViewName);
        ImageView aImageView = (ImageView) parent.findViewById(R.id.itemImage);


        return new RecyclerItemViewHolderGallery(parent, aRecyclerAdapterKala, itemTextViewName, aImageView);
    }

}
