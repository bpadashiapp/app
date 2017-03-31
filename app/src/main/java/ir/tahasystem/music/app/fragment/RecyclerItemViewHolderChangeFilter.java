package ir.tahasystem.music.app.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import ir.onlinefood.app.factory.R;


public class RecyclerItemViewHolderChangeFilter extends RecyclerView.ViewHolder {

    public final TextView mItemTextViewName;
    public final TextView mItemTextViewCPrice;
    public final TextView mItemTextViewPrice;
    public final TextView mItemTextViewUintInStore;
    public final TextView mItemTextViewMinOrder;
    public final ImageView aImageView;
    public final RelativeLayout aButton;

    public final TextView mItemTextViewPriceDiscount;

    public final TextView mItemTextViewDiscount;

    ProgressBarCircularIndeterminate aProgressBarCircularIndeterminate;


    public RecyclerItemViewHolderChangeFilter(final View parent, final RecyclerAdapterChangeFilter aRecyclerAdapterKala, TextView itemTextViewName,
                                              TextView mItemTextViewPrice
            , ImageView aImageView, RelativeLayout aButton,
                                              TextView mItemTextViewCPrice, TextView mItemTextViewUintInStore, TextView mItemTextViewMinOrder
    , TextView mItemTextViewPriceDiscount,TextView mItemTextViewDiscount,ProgressBarCircularIndeterminate aProgressBarCircularIndeterminate) {
        super(parent);
        this.mItemTextViewName = itemTextViewName;
        this.mItemTextViewPrice = mItemTextViewPrice;
        this.aImageView = aImageView;
        this.aButton = aButton;

        this.mItemTextViewCPrice = mItemTextViewCPrice;
        this.mItemTextViewUintInStore = mItemTextViewUintInStore;
        this.mItemTextViewMinOrder = mItemTextViewMinOrder;

        this.mItemTextViewPriceDiscount=mItemTextViewPriceDiscount;

        this.mItemTextViewDiscount=mItemTextViewDiscount;

        this.aProgressBarCircularIndeterminate=aProgressBarCircularIndeterminate;


        aButton.setOnClickListener(new View.OnClickListener() {
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

    public static RecyclerItemViewHolderChangeFilter newInstance(RecyclerAdapterChangeFilter aRecyclerAdapterKala, View parent) {


        TextView itemTextViewName = (TextView) parent.findViewById(R.id.itemTextViewName);
        ImageView aImageView = (ImageView) parent.findViewById(R.id.itemImage);
        RelativeLayout aButton = (RelativeLayout) parent.findViewById(R.id.fab_add_basket);

        TextView mItemTextViewCPrice = (TextView) parent.findViewById(R.id.chg_cprice);
        TextView mItemTextViewUintInStore = (TextView) parent.findViewById(R.id.chg_unit_in_stock);
        TextView mItemTextViewMinOrder = (TextView) parent.findViewById(R.id.chg_min_order);
        TextView mItemTextViewPrice = (TextView) parent.findViewById(R.id.chg_price);
        TextView mItemTextViewDiscount = (TextView) parent.findViewById(R.id.itemTextViewDiscount);


        TextView mItemTextViewPriceDiscount = (TextView) parent.findViewById(R.id.chg_price_discount);

        ProgressBarCircularIndeterminate aProgressBarCircularIndeterminate=(ProgressBarCircularIndeterminate) parent.findViewById(R.id.list_load);

        return new RecyclerItemViewHolderChangeFilter(parent, aRecyclerAdapterKala, itemTextViewName, mItemTextViewPrice,
                aImageView, aButton, mItemTextViewCPrice, mItemTextViewUintInStore, mItemTextViewMinOrder,mItemTextViewPriceDiscount
        ,mItemTextViewDiscount, aProgressBarCircularIndeterminate);
    }

}
