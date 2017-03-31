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
import ir.tahasystem.music.app.Values;


public class RecyclerItemViewHolderHomeSmall extends RecyclerView.ViewHolder {

    public final TextView mItemTextViewName;
    public final TextView mItemTextViewDes;
    public final TextView mItemTextViewPrice;
    public final TextView mItemTextViewPriceByDiscount;
    public final ImageView aImageView;
    public final RelativeLayout aButton;

    public final TextView itemTextViewDiscount;
    public final RelativeLayout aButtonD;

    ProgressBarCircularIndeterminate aProgressBarCircularIndeterminate;



    public RecyclerItemViewHolderHomeSmall(final View parent, final RecyclerAdapterHomeSmall aRecyclerAdapterKala, TextView itemTextViewName, TextView mItemTextViewPrice,
                                           TextView mItemTextViewPriceByDiscount, ImageView aImageView, TextView mItemTextViewDes, RelativeLayout aButton,TextView itemTextViewDiscount
            ,RelativeLayout aButtonD,ProgressBarCircularIndeterminate aProgressBarCircularIndeterminate) {
        super(parent);
        this.mItemTextViewName = itemTextViewName;
        this.mItemTextViewPrice = mItemTextViewPrice;
        this.mItemTextViewPriceByDiscount = mItemTextViewPriceByDiscount;
        this.aImageView = aImageView;
        this.mItemTextViewDes = mItemTextViewDes;
        this.aButton=aButton;
        this.itemTextViewDiscount=itemTextViewDiscount;

        this.aButtonD=aButtonD;

        this.aProgressBarCircularIndeterminate=aProgressBarCircularIndeterminate;


        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aRecyclerAdapterKala.onTapzBasket(getAdapterPosition());

                ani(v);

            }
        });

        if(Values.hasDescription){
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    aRecyclerAdapterKala.onTapz(getAdapterPosition());
                    ani(v);
                }
            });
        }else{
            this.aButtonD.setVisibility(View.GONE);
        }

    }

    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }

    public static RecyclerItemViewHolderHomeSmall newInstance(RecyclerAdapterHomeSmall aRecyclerAdapterKala, View parent) {


        TextView itemTextViewName = (TextView) parent.findViewById(R.id.itemTextViewName);
        TextView mItemTextViewPrice = (TextView) parent.findViewById(R.id.itemTextViewPrice);
        TextView mItemTextViewPriceByDiscount = (TextView) parent.findViewById(R.id.itemTextViewPriceByDiscount);
        ImageView aImageView = (ImageView) parent.findViewById(R.id.itemImage);
        TextView mItemTextViewDes = (TextView) parent.findViewById(R.id.itemTextViewDes);
        RelativeLayout aButton = (RelativeLayout) parent.findViewById(R.id.fab_add_basket);

        TextView itemTextViewDiscount=(TextView) parent.findViewById(R.id.itemTextViewDiscount);

        RelativeLayout aButtonD = (RelativeLayout) parent.findViewById(R.id.fab_details);

        ProgressBarCircularIndeterminate aProgressBarCircularIndeterminate=(ProgressBarCircularIndeterminate) parent.findViewById(R.id.list_load);

        return new RecyclerItemViewHolderHomeSmall(parent, aRecyclerAdapterKala, itemTextViewName, mItemTextViewPrice,
                mItemTextViewPriceByDiscount, aImageView, mItemTextViewDes,aButton,itemTextViewDiscount,aButtonD, aProgressBarCircularIndeterminate);
    }

}
