package ir.tahasystem.music.app.fragment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import ir.onlinefood.app.factory.R;


public class RecyclerItemViewHolderBasket2H extends RecyclerView.ViewHolder {

    public final TextView mItemTextViewName;
    public final TextView mItemTextViewDes;
    public final TextView mItemTextImage;
    public final CardView mItemCardView;



    public RecyclerItemViewHolderBasket2H(final View parent, final RecyclerAdapterBasket2H aRecyclerAdapterKala, TextView itemTextViewName, TextView mItemTextViewDes
    , TextView mItemTextImage, CardView mItemCardView) {
        super(parent);

        this.mItemTextViewName = itemTextViewName;
        this.mItemTextViewDes = mItemTextViewDes;
        this.mItemTextImage=mItemTextImage;
        this.mItemCardView=mItemCardView;


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

        fadeIn.setDuration(200);

        v.startAnimation(fadeIn);

    }

    public static RecyclerItemViewHolderBasket2H newInstance(RecyclerAdapterBasket2H aRecyclerAdapterKala, View parent) {


        TextView itemTextViewName = (TextView) parent.findViewById(R.id.itemTextViewName);

        TextView mItemTextViewDes = (TextView) parent.findViewById(R.id.itemTextViewDes);

        TextView mItemTextImage = (TextView) parent.findViewById(R.id.itemImage);

        CardView mItemCardView= (CardView) parent.findViewById(R.id.itemCardView);



        return new RecyclerItemViewHolderBasket2H(parent, aRecyclerAdapterKala, itemTextViewName, mItemTextViewDes,mItemTextImage,mItemCardView);
    }

}
