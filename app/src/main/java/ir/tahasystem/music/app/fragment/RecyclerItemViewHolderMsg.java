package ir.tahasystem.music.app.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.onlinefood.app.factory.R;


public class RecyclerItemViewHolderMsg extends RecyclerView.ViewHolder {

    public final TextView mItemTextViewName;
    public final TextView mItemTextViewDes;
    public final TextView mItemTextViewPrice;
    public final TextView mItemTextViewPriceByDiscount;
    public final ImageView aImageView;
    public final RelativeLayout aButton;

    public final LinearLayout mItemLi;




    public RecyclerItemViewHolderMsg(final View parent, final RecyclerAdapterMsg aRecyclerAdapterKala, TextView itemTextViewName, TextView mItemTextViewPrice,
                                     TextView mItemTextViewPriceByDiscount, ImageView aImageView, TextView mItemTextViewDes, RelativeLayout aButton
    , LinearLayout mItemLi) {
        super(parent);
        this.mItemTextViewName = itemTextViewName;
        this.mItemTextViewPrice = mItemTextViewPrice;
        this.mItemTextViewPriceByDiscount = mItemTextViewPriceByDiscount;
        this.aImageView = aImageView;
        this.mItemTextViewDes = mItemTextViewDes;
        this.aButton=aButton;
        this.mItemLi=mItemLi;



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

    public static RecyclerItemViewHolderMsg newInstance(RecyclerAdapterMsg aRecyclerAdapterKala, View parent) {


        TextView itemTextViewName = (TextView) parent.findViewById(R.id.itemTextViewName);
        TextView mItemTextViewPrice = (TextView) parent.findViewById(R.id.itemTextViewPrice);
        TextView mItemTextViewPriceByDiscount = (TextView) parent.findViewById(R.id.itemTextViewPriceByDiscount);
        ImageView aImageView = (ImageView) parent.findViewById(R.id.itemImage);
        TextView mItemTextViewDes = (TextView) parent.findViewById(R.id.itemTextViewDes);
        RelativeLayout aButton = (RelativeLayout) parent.findViewById(R.id.fab_add_basket);

        LinearLayout mItemLi=(LinearLayout)parent.findViewById(R.id.itemLi);


        return new RecyclerItemViewHolderMsg(parent, aRecyclerAdapterKala, itemTextViewName, mItemTextViewPrice,
                mItemTextViewPriceByDiscount, aImageView, mItemTextViewDes,aButton,mItemLi);
    }

}
