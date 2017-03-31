package ir.tahasystem.music.app.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;



import java.util.List;
import java.util.regex.Pattern;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.BasketActivity;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Values;


/*
* RecyclerView Adapter that allows to add a header view.
* */
public class RecyclerAdapterBasket2V extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private List<Kala> mItemList;

    private Context context;
    private FragmentBasket2 fragmentActivity;

    public RecyclerAdapterBasket2V(final FragmentBasket2 fragmentActivity, List<Kala> itemList) {
        mItemList = itemList;
        this.fragmentActivity = fragmentActivity;


    }

    public void scaleView(View v) {

        Animation logoMoveAnimation = AnimationUtils.loadAnimation(context, R.anim.scalez_half_down);
        v.startAnimation(logoMoveAnimation);

    }


    public void addItem(List<Kala> aList, int position) {
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


        final View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_basket2v, parent, false);
        return RecyclerItemViewHolderBasket2V.newInstance(this, view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        RecyclerItemViewHolderBasket2V holder = (RecyclerItemViewHolderBasket2V) viewHolder;

        Kala aKala = mItemList.get(position); // header

        holder.mItemTextViewName.setText(aKala.getName());
        //String des = aKala.getDescriptionKala().split(Pattern.quote("*"))[1];
        holder.mItemTextViewDes.setText(aKala.tag);


        if (aKala.getDescriptionKala().split(Pattern.quote("*"))[0].equals("T")) {
            holder.mItemTextViewDes2.setText(fragmentActivity.getString(R.string.is_possible));
            holder.mItemTextViewDes2.setTextColor(Color.parseColor("#009900"));
        } else {
            holder.mItemTextViewDes2.setText(aKala.getDescriptionKala().split(Pattern.quote("*"))[1]);
            holder.mItemTextViewDes2.setTextColor(Color.parseColor("#990000"));
        }

        if (fragmentActivity.vSelect == position ) {
            holder.mItemCardView.setCardBackgroundColor(fragmentActivity.getResources().getColor(R.color.color_primary));
            holder.mItemTextViewName.setTextColor(Color.WHITE);
            holder.mItemTextViewDes.setTextColor(Color.WHITE);
        }else{

            holder.mItemCardView.setCardBackgroundColor(Color.WHITE);
            holder.mItemTextViewName.setTextColor(Color.BLACK);
            holder.mItemTextViewDes.setTextColor(Color.BLACK);
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

        fragmentActivity.vSelect=pos;
        this.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentActivity.mLayoutManagerV.scrollToPositionWithOffset(pos, 0);
            }
        }, 200);





        //MainActivity.context.viewPager.setCurrentItem(2);
        //MainActivity.context.fragmentSubCates.init();
        //MainActivity.context.fragmentSubCates.cateId=mItemList.get(pos).id;

        //LoadBox.ShowLoad(context, context.getString(R.string.plz_wait));



        if (mItemList.get(pos).getDescriptionKala().split(Pattern.quote("*"))[0].equals("T")) {
                BasketActivity.context.viewPager.setCurrentItem(2);
            ModelHolder.getInstance().ShippingTimeId = mItemList.get(pos).id;
            System.out.println(mItemList.get(pos).getDescriptionKala());
            String str0=mItemList.get(pos).getDescriptionKala().split(Pattern.quote("*"))[1];
            ModelHolder.getInstance().ShippingDate=str0;
        }

    }


}
