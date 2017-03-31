package ir.tahasystem.music.app.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import java.text.DecimalFormat;
import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.OrderDetails;


/*
* RecyclerView Adapter that allows to add a header view.
* */
public class RecyclerAdapterOrder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private List<OrderDetails> mItemList;

    private Context context;
    private FragmentOrder fragmentActivity;

    public RecyclerAdapterOrder(final FragmentOrder fragmentActivity, List<OrderDetails> itemList) {
        mItemList = itemList;
        this.fragmentActivity = fragmentActivity;


    }

    public void scaleView(View v) {

        Animation logoMoveAnimation = AnimationUtils.loadAnimation(context, R.anim.scalez_half_down);
        v.startAnimation(logoMoveAnimation);

    }


    public void addItem(List<OrderDetails> aList, int position) {
        mItemList.addAll(aList);
        notifyItemInserted(position);
    }

    public void clearItem() {
        mItemList.clear();
        this.notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();


        final View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_order_details, parent, false);
        return RecyclerItemViewHolderOrder.newInstance(this, view);

    }

    DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        RecyclerItemViewHolderOrder holder = (RecyclerItemViewHolderOrder) viewHolder;

        OrderDetails aOrderDetails = mItemList.get(position); // header

        holder.mItemTextViewName.setText(aOrderDetails.description);


        holder.mItemTextViewPriceByDiscount.setText(df.format((int) aOrderDetails.price) + " " + context.getString(R.string.toman));
        holder.mItemTextViewPrice.setText(df.format((int) aOrderDetails.discountPrice) + " " + context.getString(R.string.toman));
        holder.mItemTextViewPriceByDiscount
                .setPaintFlags(holder.mItemTextViewPriceByDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        if (aOrderDetails.price == aOrderDetails.discountPrice|| aOrderDetails.price == 0) {
            holder.mItemTextViewPriceByDiscount.setVisibility(View.GONE);
            holder.mItemTextViewDes.setText(fragmentActivity.getString(R.string.count)
                    + " " + (int) aOrderDetails.quantity+ " " +
                    aOrderDetails.saleType);
        } else {
            holder.mItemTextViewPriceByDiscount.setVisibility(View.VISIBLE);
            holder.mItemTextViewDes.setText(fragmentActivity.getString(R.string.count)
                    + " " + (int) aOrderDetails.quantity+ " " +
                    aOrderDetails.saleType+" "+context.getString(R.string.discount)+" "+aOrderDetails.discount+"%");
        }




    }

    public int getBasicItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {


        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return getBasicItemCount(); // header
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void onTapz(final int pos) {


    }


    public void onTapzBasket(int adapterPosition) {


    }

    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }
}
