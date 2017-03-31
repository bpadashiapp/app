package ir.tahasystem.music.app.fragment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import ir.onlinefood.app.factory.R;


public class RecyclerItemViewHolderBasket2V extends RecyclerView.ViewHolder {

    public final TextView mItemTextViewName;
    public final TextView mItemTextViewDes;
    public final TextView mItemTextViewDes2;
    public final TextView mItemTextViewPrice;
    public final TextView mItemTextViewPriceByDiscount;
    public final CardView mItemCardView;






    public RecyclerItemViewHolderBasket2V(final View parent, final RecyclerAdapterBasket2V aRecyclerAdapterKala, TextView itemTextViewName, TextView mItemTextViewPrice,
                                          TextView mItemTextViewPriceByDiscount, TextView mItemTextViewDes, TextView mItemTextViewDes2, CardView mItemCardView) {
        super(parent);
        this.mItemTextViewName = itemTextViewName;
        this.mItemTextViewPrice = mItemTextViewPrice;
        this.mItemTextViewPriceByDiscount = mItemTextViewPriceByDiscount;
        this.mItemTextViewDes = mItemTextViewDes;
        this.mItemTextViewDes2 = mItemTextViewDes2;
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

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }

    public static RecyclerItemViewHolderBasket2V newInstance(RecyclerAdapterBasket2V aRecyclerAdapterKala, View parent) {


        TextView itemTextViewName = (TextView) parent.findViewById(R.id.itemTextViewName);
        TextView mItemTextViewPrice = (TextView) parent.findViewById(R.id.itemTextViewPrice);
        TextView mItemTextViewPriceByDiscount = (TextView) parent.findViewById(R.id.itemTextViewPriceByDiscount);
        //ImageView aImageView = (ImageView) parent.findViewById(R.id.itemImage);
        TextView mItemTextViewDes = (TextView) parent.findViewById(R.id.itemTextViewDes);
        TextView mItemTextViewDes2 = (TextView) parent.findViewById(R.id.itemTextViewDes2);
        CardView mItemCardView= (CardView) parent.findViewById(R.id.itemCardView);




        return new RecyclerItemViewHolderBasket2V(parent, aRecyclerAdapterKala, itemTextViewName, mItemTextViewPrice,
                mItemTextViewPriceByDiscount,mItemTextViewDes, mItemTextViewDes2,mItemCardView);
    }

}
