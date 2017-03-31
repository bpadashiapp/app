package ir.tahasystem.music.app.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.Kala;


/*
* RecyclerView Adapter that allows to add a header view.
* */
public class RecyclerAdapterMsg2H extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    public static int selected;
    public List<Kala> mItemList;

    public static String reciever;

    private Context context;
    private FragmentMsg fragmentActivity;

    public RecyclerAdapterMsg2H(final FragmentMsg fragmentActivity, List<Kala> itemList) {
        mItemList = itemList;
        this.fragmentActivity = fragmentActivity;


    }

    public void scaleView(View v) {

        Animation logoMoveAnimation = AnimationUtils.loadAnimation(context, R.anim.scalez_half_down);
        v.startAnimation(logoMoveAnimation);

    }

    public void addItem(Kala aKala, int position) {

        if (mItemList.size() == 0)
            aKala.isSelected = true;

        mItemList.add(aKala);
        this.notifyDataSetChanged();

    }


    public void addItem(List<Kala> aList, int position) {
        synchronized (this) {
            for (Kala aKala : aList)
                if (aKala.isServer == 2)
                    mItemList.add(aKala);
            this.notifyDataSetChanged();
        }
    }

    public void clearItem() {
        synchronized (this) {
            mItemList.clear();
            this.notifyDataSetChanged();
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();


        final View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_msg2h, parent, false);
        return RecyclerItemViewHolderMsg2H.newInstance(this, view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        RecyclerItemViewHolderMsg2H holder = (RecyclerItemViewHolderMsg2H) viewHolder;

        Kala aKala = mItemList.get(position); // header

        holder.mItemTextViewName.setText(aKala.name);

        if (aKala.isSelected) {
            reciever=mItemList.get(position).sender;
            holder.mItemTextViewName.setTextColor(fragmentActivity.getResources().getColor(R.color.color_primary));
            holder.mItemTextViewName.setBackgroundColor(Color.WHITE);
        } else {
            holder.mItemTextViewName.setTextColor(Color.WHITE);
            holder.mItemTextViewName.setBackgroundColor(fragmentActivity.getResources().getColor(R.color.color_primary));
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

        selected = mItemList.get(pos).numServer;

        reciever=mItemList.get(pos).sender;

        for (Kala dKala : mItemList)
            dKala.isSelected = false;

        mItemList.get(pos).isSelected = true;

        this.notifyDataSetChanged();


        //MainActivity.context.viewPager.setCurrentItem(2);
        //MainActivity.context.fragmentSubCates.init();
        //MainActivity.context.fragmentSubCates.cateId=mItemList.get(pos).id;

        //LoadBox.ShowLoad(context, context.getString(R.string.plz_wait));

        //fragmentActivity.getDataV(mItemList.get(pos).id);
        //fragmentActivity.selected = mItemList.get(pos).id;

        //this.notifyDataSetChanged();
        //fragmentActivity.mLayoutManagerH.scrollToPositionWithOffset(fragmentActivity.selected, 0);

    }


}
