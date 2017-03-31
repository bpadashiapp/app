package ir.tahasystem.music.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.DetailsActivity;
import ir.tahasystem.music.app.DetialsOrdersActivity;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.Order;
import ir.tahasystem.music.app.Model.ModelHolder;


/*
* RecyclerView Adapter that allows to add a header view.
* */
public class RecyclerAdapterOrderNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private List<Order> mItemList;

    private Context context;
    private FragmentOrderNew fragmentActivity;

    public RecyclerAdapterOrderNew(final FragmentOrderNew fragmentActivity, List<Order> itemList) {
        mItemList = itemList;
        this.fragmentActivity = fragmentActivity;


    }

    public void scaleView(View v) {

        Animation logoMoveAnimation = AnimationUtils.loadAnimation(context, R.anim.scalez_half_down);
        v.startAnimation(logoMoveAnimation);

    }


    public void addItem(List<Order> aList, int position) {
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


        final View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_order, parent, false);
        return RecyclerItemViewHolderOrderNew.newInstance(this, view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        RecyclerItemViewHolderOrderNew holder = (RecyclerItemViewHolderOrderNew) viewHolder;

        Order aOrder = mItemList.get(position); // header

        holder.mItemTextViewName.setText(fragmentActivity.getString(R.string.order_no)+" "+aOrder.orderId);
        holder.mItemTextViewDes.setText(fragmentActivity.getString(R.string.buyer)+" "+aOrder.fullname);

        System.out.println("--->" + aOrder.description);

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


        //MainActivity.context.viewPager.setCurrentItem(2);
        //MainActivity.context.fragmentSubCates.init();
        //MainActivity.context.fragmentSubCates.cateId=mItemList.get(pos).id;

        //LoadBox.ShowLoad(context, context.getString(R.string.plz_wait));
        DetialsOrdersActivity.isNew=true;
        DetialsOrdersActivity.aOrder=mItemList.get(pos);
        fragmentActivity.startActivity(new Intent(fragmentActivity.getActivity(), DetialsOrdersActivity.class));


    }

    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }
}
