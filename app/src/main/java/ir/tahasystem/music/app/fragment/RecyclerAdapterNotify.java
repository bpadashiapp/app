package ir.tahasystem.music.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.OrderActivity;


/*
* RecyclerView Adapter that allows to add a header view.
* */
public class RecyclerAdapterNotify extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private List<Kala> mItemList;

    private Context context;
    private FragmentNotify fragmentActivity;

    public RecyclerAdapterNotify(final FragmentNotify fragmentActivity, List<Kala> itemList) {
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


        final View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_notify, parent, false);
        return RecyclerItemViewHolderNotify.newInstance(this, view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        RecyclerItemViewHolderNotify holder = (RecyclerItemViewHolderNotify) viewHolder;


        Kala aKala = mItemList.get(position); // header

        holder.mItemTextViewName.setText(Html.fromHtml(aKala.name), TextView.BufferType.SPANNABLE);
        holder.mItemTextViewDes.getSettings().setAppCacheEnabled(true);
        WebSettings settings = holder.mItemTextViewDes.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        holder.mItemTextViewDes.loadData(aKala.description, "text/html; charset=utf-8", "utf-8");

        if (aKala.image != null) {

            Picasso.with(context)
                    .load(aKala.image).skipMemoryCache()

                    .into(holder.aImageView);

            //ImageLoader.getInstance().displayImage(url, holder.aImageView, options);
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


      Kala kala=  mItemList.get(pos);

        if (kala.isOrder) {


            if(LoginHolder.getInstance().getLogin().user!=null) {
                Intent intent = new Intent(fragmentActivity.getActivity(), OrderActivity.class);
                intent.putExtra("manager", true);
                fragmentActivity.startActivity(intent);
            }else if(MainActivity.context!=null){
                //MainActivity.context.Login();
            }


        }
    }



    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }
}
