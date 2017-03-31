package ir.tahasystem.music.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.BasketActivity;
import ir.tahasystem.music.app.DetailsActivity;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.Company;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.utils.NetworkUtil;


/*
* RecyclerView Adapter that allows to add a header view.
* */
public class RecyclerAdapterHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private List<Kala> mItemList;

    private Context context;
    private FragmentHome fragmentActivity;

    public RecyclerAdapterHome(final FragmentHome fragmentActivity, List<Kala> itemList) {
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


        if (fragmentActivity.isList) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_kala_list, parent, false);
            return RecyclerItemViewHolderHome.newInstance(this, view);
        } else {
            View view= LayoutInflater.from(context).inflate(R.layout.recycler_item_kala, parent, false);
            return RecyclerItemViewHolderHome.newInstance(this, view);
        }


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        final RecyclerItemViewHolderHome holder = (RecyclerItemViewHolderHome) viewHolder;

        Kala aKala = mItemList.get(position); // header

        aKala.initprice = aKala.price;
        aKala.initpriceByDiscount = aKala.priceByDiscount;

        System.out.println(LoginHolder.getInstance().getLogin().showPrice);


        if (LoginHolder.getInstance().getLogin() == null ||
                !LoginHolder.getInstance().getLogin().showPrice) {

            holder.mItemTextViewPrice.setVisibility(View.GONE);
            holder.mItemTextViewPriceByDiscount.setVisibility(View.GONE);
            holder.aButton.setBackgroundResource(R.drawable.bg_btn_red);
            ((TextView) holder.aButton.findViewById(R.id.fab_add_basket_text)).setText(R.string.view_price);
            holder.itemTextViewDiscount.setVisibility(View.GONE);
        } else {
            holder.mItemTextViewPrice.setVisibility(View.VISIBLE);
            holder.mItemTextViewPriceByDiscount.setVisibility(View.VISIBLE);
            holder.aButton.setVisibility(View.VISIBLE);
            holder.aButton.setBackgroundResource(R.drawable.bg_btn_selector);
            ((TextView) holder.aButton.findViewById(R.id.fab_add_basket_text)).setText(R.string.add_basket);
            holder.itemTextViewDiscount.setVisibility(View.VISIBLE);

            if (aKala.price == aKala.priceByDiscount || aKala.price == 0) {
                holder.mItemTextViewPriceByDiscount.setVisibility(View.GONE);
                holder.itemTextViewDiscount.setVisibility(View.GONE);
            } else {
                holder.mItemTextViewPriceByDiscount.setVisibility(View.VISIBLE);
                holder.itemTextViewDiscount.setVisibility(View.VISIBLE);
            }

        }
        if (FragmentSubCates.cateId == Values.takCatId) {
            holder.mItemTextViewPrice.setVisibility(View.VISIBLE);
            holder.mItemTextViewPriceByDiscount.setVisibility(View.VISIBLE);
            holder.aButton.setVisibility(View.VISIBLE);
            holder.aButton.setBackgroundResource(R.drawable.bg_btn_selector);
            ((TextView) holder.aButton.findViewById(R.id.fab_add_basket_text)).setText(R.string.add_basket);

            if (aKala.price == aKala.priceByDiscount || aKala.price == 0) {
                holder.mItemTextViewPriceByDiscount.setVisibility(View.GONE);
                holder.itemTextViewDiscount.setVisibility(View.GONE);
            } else {
                holder.mItemTextViewPriceByDiscount.setVisibility(View.VISIBLE);
                holder.itemTextViewDiscount.setVisibility(View.VISIBLE);
            }
        }


        holder.mItemTextViewName.setText(aKala.getName());
        holder.mItemTextViewDes.setText(aKala.getDescriptionKala());
        holder.mItemTextViewPriceByDiscount.setText(aKala.getPrice() + " " + context.getString(R.string.toman));
        holder.mItemTextViewPrice.setText(aKala.getPriceByDiscount() + " " + context.getString(R.string.toman));
        holder.mItemTextViewPriceByDiscount
                .setPaintFlags(holder.mItemTextViewPriceByDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        holder.itemTextViewDiscount.setText(String.valueOf(aKala.discount) + "%");


        if (aKala.image != null) {


            if(aKala.image.contains("zzznoimage.png") && fragmentActivity.isList){
                holder.aProgressBarCircularIndeterminate.setVisibility(View.VISIBLE);
                holder.aImageView.setVisibility(View.VISIBLE);
            }else if(aKala.image.contains("zzznoimage.png")){
                holder.aProgressBarCircularIndeterminate.setVisibility(View.GONE);
                holder.aImageView.setVisibility(View.GONE);
            }else{
                holder.aProgressBarCircularIndeterminate.setVisibility(View.VISIBLE);
                holder.aImageView.setVisibility(View.VISIBLE);

            }

            Picasso.with(context)
                    .load(aKala.image).skipMemoryCache()

                    .into(holder.aImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.aProgressBarCircularIndeterminate.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                            holder.aProgressBarCircularIndeterminate.setVisibility(View.GONE);

                        }
                    });

            //ImageLoader.getInstance().displayImage(url, holder.aImageView, options);
        }

        if (aKala.unitsInStock == 0) {

            holder.aButton.setVisibility(View.INVISIBLE);
            holder.itemTextViewDiscount.setVisibility(View.VISIBLE);
            holder.itemTextViewDiscount.setText(fragmentActivity.getString(R.string.kala_end));

        } else {

            holder.aButton.setVisibility(View.VISIBLE);
            holder.itemTextViewDiscount.setVisibility(View.GONE);
            holder.itemTextViewDiscount.setText("");

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


        //MainActivity.context.viewPager.setCurrentItem(2);
        //MainActivity.context.fragmentSubCates.init();
        //MainActivity.context.fragmentSubCates.cateId=mItemList.get(pos).id;

        //LoadBox.ShowLoad(context, context.getString(R.string.plz_wait));
        DetailsActivity.aKala = mItemList.get(pos);
        fragmentActivity.startActivity(new Intent(fragmentActivity.getActivity(), DetailsActivity.class));


    }


    public void onTapzBasket(int adapterPosition) {

        if (LoginHolder.getInstance().getLogin() == null ||
                !LoginHolder.getInstance().getLogin().showPrice) {
            ManagerCall();
            return;
        }

        Kala aKala = mItemList.get(adapterPosition);
        aKala.limt = aKala.count;

        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
            if (aBasketModel.aKala.id == aKala.id) {
                MainActivity.context.msg(MainActivity.context.getString(R.string.added_before));
                return;
            }

        if (aKala.unitsInStock < aKala.minOrder) {
            MainActivity.context.msg(MainActivity.context.getString(R.string.no_store));
            return;
        }

        ModelHolder.getInstance().addBasket(aKala);
        if (MainActivity.context.aTextViewBasket != null)
            if (ModelHolder.getInstance().getBasketCount() > 0) {
                MainActivity.context.aTextViewBasket.setVisibility(View.VISIBLE);
                MainActivity.context.aTextViewBasket.setText(String.valueOf(ModelHolder.getInstance().getBasketCount()));
                ani(MainActivity.context.aTextViewBasket);

            } else {
                MainActivity.context.aTextViewBasket.setVisibility(View.INVISIBLE);
            }


    }

    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }

    public void ManagerCall() {

        DFragmentCall alertdFragment = new DFragmentCall();
        alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        alertdFragment.show(fragmentActivity.getChildFragmentManager(), "");

    }


    class DFragmentCall extends DialogFragment {


        public DFragmentCall() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View dialog = inflater.inflate(R.layout.dialogz_call, container, false);

            dialog.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DFragmentCall.this.dismiss();

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Values.phone));
                    startActivity(intent);

                }
            });


            return dialog;
        }
    }

    private void getDataCompanyInfo() {


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectGetCompanyInfo(Values.companyId);


                    if (aSoapObject == null || aSoapObject.toString() == null) {

                        return;
                    }

                    Company aCompany = new Gson().fromJson(aSoapObject.toString(), Company.class);
                    Values.phone = aCompany.phone;


                } catch (Exception e) {

                    e.printStackTrace();
                }


            }
        });

    }
}
