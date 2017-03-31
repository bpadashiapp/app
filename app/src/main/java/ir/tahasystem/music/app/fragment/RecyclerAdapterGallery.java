package ir.tahasystem.music.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.tahasystem.music.app.ImageActivity;
import ir.tahasystem.music.app.Model.Company;
import ir.tahasystem.music.app.Model.Kala;
import ir.onlinefood.app.factory.R;


/*
* RecyclerView Adapter that allows to add a header view.
* */
public class RecyclerAdapterGallery extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private List<Company> mItemList;

    private Context context;
    private FragmentGallery fragmentActivity;

    public RecyclerAdapterGallery(final FragmentGallery fragmentActivity, List<Company> itemList) {
        mItemList = itemList;
        this.fragmentActivity = fragmentActivity;


    }

    public void scaleView(View v) {

        Animation logoMoveAnimation = AnimationUtils.loadAnimation(context, R.anim.scalez_half_down);
        v.startAnimation(logoMoveAnimation);

    }


    public void addItem(List<Company> aList, int position) {
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


        final View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_photo, parent, false);
        return RecyclerItemViewHolderGallery.newInstance(this, view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        RecyclerItemViewHolderGallery holder = (RecyclerItemViewHolderGallery) viewHolder;

        Company aCompany = mItemList.get(position); // header

        //holder.mItemTextViewName.setText(aCompany.shortDescription);

        if (aCompany.shortDescription != null) {

            Picasso.with(context)
                    .load(aCompany.shortDescription).skipMemoryCache()
                    .into(holder.aImageView);

            //ImageLoader.getInstance().displayImage(url, holder.aImageView, options);
        } else {
            holder.aImageView.setImageResource(R.drawable.ic_launcher);
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

        ImageActivity.img = mItemList.get(pos).img;
        fragmentActivity.startActivity(new Intent(fragmentActivity.getActivity(), ImageActivity.class));


    }


}
