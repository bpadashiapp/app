package ir.tahasystem.music.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarIndeterminate;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.gson.Gson;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapPrimitive;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.BasketActivity;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.KalaPrice;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.LoginModel;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPoolSingle;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.WebViewsActivityBuy;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.utils.FontUtils;
import ir.tahasystem.music.app.utils.NetworkUtil;

public class FragmentBasket extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    public static FragmentBasket context;

    public LinearLayout itemHolder;

    public CheckBox aCheckBoxAll;
    TextView aTextViewFactor, aTextViewTotalSubtotal, aTextViewTotalShipping, aTextViewTotalTolal,
            aTextViewTotalTolalPay;

    Button aButtonTotoalPay;

    RelativeLayout aLayout1;
    LinearLayout aLayout0;

    private ProgressBarIndeterminate aProgress;

    private int uniq;
    private int bag;

    private BasketModel savedBasket;


    public static FragmentBasket createInstance(int itemsCount) {
        FragmentBasket partThreeFragment = new FragmentBasket();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = (View) inflater.inflate(R.layout.fragment_basket, container, false);

        context = this;


        aProgress = (ProgressBarIndeterminate) view.findViewById(R.id.list_load);
        aLayout0 = (LinearLayout) view.findViewById(R.id.layout0);
        aLayout1 = (RelativeLayout) view.findViewById(R.id.layout1);

        return view;

    }


    public boolean isInit = false;

    public void init() {

        if (context == null || context.getActivity() == null || context.getView() == null)
            return;

        if (isInit || context == null)
            return;

        isInit = true;

        HideShow(View.VISIBLE, View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                context.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        aTextViewTotalSubtotal = (TextView) context.getView().findViewById(R.id.total_subtotal);
                        aTextViewTotalShipping = (TextView) context.getView().findViewById(R.id.total_shipping);
                        aTextViewTotalTolal = (TextView) context.getView().findViewById(R.id.total_total);
                        aTextViewFactor = (TextView) context.getView().findViewById(R.id.factor);

                        aTextViewTotalTolalPay = (TextView) context.getView().findViewById(R.id.total_total_pay);
                        aButtonTotoalPay = (Button) context.getView().findViewById(R.id.total_total_pay_btn);

                        aButtonTotoalPay.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {

                                int checkCountz = 0;
                                for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
                                    checkCountz++;

                                if (checkCountz > 0)
                                    showDialog(null);
                            }

                        });

                        aCheckBoxAll = (CheckBox) context.getView().findViewById(R.id.chk_select_all);
                        aCheckBoxAll.setText(getString(R.string.select_all) + " (" + ModelHolder.getInstance().getBasketCount() + ") ");
                        aCheckBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if (ModelHolder.getInstance().getBasketCount() == 0)
                                    aCheckBoxAll.setChecked(true);

                                if (isChecked) {
                                    for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
                                        aBasketModel.aCheckBoxName.setChecked(true);

                                }
                            }

                        });

                        itemHolder = (LinearLayout) context.getView().findViewById(R.id.item_holder);


                        initBasketItem();

                        HideShow(View.GONE, View.VISIBLE);

                    }
                });


            }
        }).start();

    }

    boolean isPeyakParam = false;

    public void onResume() {
        super.onResume();

        init();

    }


    public void initBasketItem() {

        List<BasketModel> aList = ModelHolder.getInstance().getBasket();

        for (final BasketModel aBasketModel : aList) {

            CardView view = (CardView) getView(R.layout.item_basket);

            //
            final CheckBox aCheckBoxName = (CheckBox) view.findViewById(R.id.chk_name);
            final CheckBox aCheckBoxSelectItem = (CheckBox) view.findViewById(R.id.chk_select_item);
            ImageView aImageView = (ImageView) view.findViewById(R.id.image);
            TextView aTextViewName = (TextView) view.findViewById(R.id.name);
            TextView aTextViewPrice = (TextView) view.findViewById(R.id.price);
            TextView aTextViewdPrice = (TextView) view.findViewById(R.id.dprice);

            final TextView aTextViewCounter = (TextView) view.findViewById(R.id.counter);
            Button aButtonUp = (Button) view.findViewById(R.id.up);
            Button aButtonDown = (Button) view.findViewById(R.id.down);

            final Button aButtonBuy = (Button) view.findViewById(R.id.buy_item);

            if (Values.appId==3) {

                aTextViewCounter.setVisibility(View.GONE);
                aButtonUp.setVisibility(View.GONE);
                aButtonDown.setVisibility(View.GONE);
            }

            aButtonBuy.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (aBasketModel.aCheckBoxName.isChecked())
                        showDialog(aBasketModel);

                }
            });

            final TextView aTextViewSubtotal = (TextView) view.findViewById(R.id.subtotal);
            final TextView aTextViewItemShipping = (TextView) view.findViewById(R.id.shipping);
            final TextView aTextViewTotal = (TextView) view.findViewById(R.id.total);

            //
            if (aBasketModel.aKala.store_name != null && !aBasketModel.aKala.offer_type.equals("1"))
                aCheckBoxName.setText(getString(R.string.seller) + " : " + aBasketModel.aKala.store_name);
            else
                aCheckBoxName.setText(getString(R.string.seller) + " : " + getString(R.string.app_name));

            if (aBasketModel.aKala.image != null) {

                Picasso.with(context.getActivity())
                        .load(aBasketModel.aKala.image)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(aImageView);

            } else {
                aImageView.setImageResource(R.drawable.ic_launcher);
            }

            aTextViewName.setText(aBasketModel.aKala.name);
            aTextViewPrice.setText(aBasketModel.aKala.initgetPrice() + " " + context.getString(R.string.toman));
            aTextViewPrice.setPaintFlags(aTextViewPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (aBasketModel.aKala.initprice == 0 || aBasketModel.aKala.initprice == aBasketModel.aKala.initpriceByDiscount) {
                aTextViewPrice.setVisibility(View.GONE);
                view.findViewById(R.id.shipping_f).setVisibility(View.GONE);
                view.findViewById(R.id.subtotal_f).setVisibility(View.GONE);
            }

            aTextViewdPrice.setText(aBasketModel.aKala.initgetPriceByDiscount() + " " + context.getString(R.string.toman));

            aBasketModel.count = aBasketModel.aKala.count;

            if (aBasketModel.count < aBasketModel.aKala.minOrder)
                aBasketModel.count = aBasketModel.aKala.minOrder;

            aTextViewSubtotal.setText(formatM(aBasketModel.count * aBasketModel.aKala.price)
                    + " " + context.getString(R.string.toman));
            aTextViewItemShipping.setText(formatM(aBasketModel.count * aBasketModel.aKala.priceByDiscount) + " "
                    + context.getString(R.string.toman));
            aTextViewTotal.setText(formatM(aBasketModel.count * aBasketModel.aKala.priceByDiscount) + " "
                    + context.getString(R.string.toman));


            System.out.println(aBasketModel.count + "->");

            aButtonBuy.setText(context.getString(R.string.buy) + " (" + aBasketModel.count + ") ");

            aImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    // Intent intent = new Intent(context.getActivity(), DetailsActivity.class);
                    //intent.putExtra("kala", aBasketModel.aKala);
                    // startActivity(intent);

                }
            });

            aTextViewCounter.setText(aBasketModel.count + " " + aBasketModel.aKala.saleType);

            aTextViewCounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DFragment alertdFragment = new DFragment(aTextViewCounter, aBasketModel.aKala.id, aBasketModel.aKala.minOrder, aBasketModel, aTextViewSubtotal, aTextViewItemShipping, aTextViewTotal);
                    alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    alertdFragment.show(getChildFragmentManager(), "");
                }
            });

            calPrice(aBasketModel, aTextViewSubtotal, aTextViewItemShipping, aTextViewTotal);

            aTextViewSubtotal.setText(getString(R.string.calculate));
            aTextViewItemShipping.setText(getString(R.string.calculate));
            aTextViewTotal.setText(getString(R.string.calculate));


            aButtonUp.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (aBasketModel.aKala.count > aBasketModel.aKala.unitsInStock) {
                        msg(getString(R.string.not_allowed));
                        return;
                    }

                    aBasketModel.count = aBasketModel.count + 1;

                    aTextViewCounter.setText(String.valueOf(aBasketModel.count) + " " + aBasketModel.aKala.saleType);

                    aBasketModel.aKala.count = aBasketModel.count;

                    calPrice(aBasketModel, aTextViewSubtotal, aTextViewItemShipping, aTextViewTotal);

                    aTextViewSubtotal.setText(getString(R.string.calculate));
                    aTextViewItemShipping.setText(getString(R.string.calculate));
                    aTextViewTotal.setText(getString(R.string.calculate));

                    aButtonBuy.setText(context.getString(R.string.buy) + " (" + aBasketModel.count + ") ");

                    sumFinalCal();
                }
            });

            aButtonDown.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (aBasketModel.count <= 1 || aBasketModel.count <= aBasketModel.aKala.minOrder) {
                        msg(getString(R.string.not_allowed));
                        return;
                    }


                    aBasketModel.count = aBasketModel.count - 1;

                    aTextViewCounter.setText(String.valueOf(aBasketModel.count) + " " + aBasketModel.aKala.saleType);

                    aBasketModel.aKala.count = aBasketModel.count;

                    calPrice(aBasketModel, aTextViewSubtotal, aTextViewItemShipping, aTextViewTotal);

                    aTextViewSubtotal.setText(getString(R.string.calculate));
                    aTextViewItemShipping.setText(getString(R.string.calculate));
                    aTextViewTotal.setText(getString(R.string.calculate));
                    aButtonBuy.setText(context.getString(R.string.buy) + " (" + aBasketModel.count + ") ");

                    sumFinalCal();
                }
            });

            Button aButtonDel = (Button) view.findViewById(R.id.delete_item);

            aButtonDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    ModelHolder.getInstance().removeBasket(aBasketModel);

                    FragmentBasket.context.itemHolder.removeAllViews();
                    FragmentBasket.context.initBasketItem();
                    FragmentBasket.context.aCheckBoxAll.setChecked(true);
                    FragmentBasket.context.aCheckBoxAll
                            .setText(getString(R.string.select_all) + " (" + ModelHolder.getInstance().getBasketCount() + ") ");
                    FragmentBasket.context.sumFinal();

                }
            });

            aBasketModel.aCheckBoxName = aCheckBoxName;
            aBasketModel.aCheckBoxSelectItem = aCheckBoxSelectItem;

            itemHolder.addView(view);

            sumFinal();
        }

        aCheckBoxAll.setChecked(true);

    }

    private void calPriceThreadLess(final BasketModel aBasketModel, final TextView aTextViewSubtotal,
                                    final TextView aTextViewItemShipping, final TextView aTextViewTotal) {


        try {

            ConnectionPool aConnectionPool = new ConnectionPool();

            SoapPrimitive aSoapObject = null;
            if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                aSoapObject = aConnectionPool.CalculatePrice(aBasketModel);


            if (aSoapObject == null) {
                noServerResponse();
                return;
            }

            if (aSoapObject.toString().toLowerCase().equals("false")) {
                noServerResponse();
                return;
            }

            System.out.println(aSoapObject);

            final KalaPrice aKalaPrice = new Gson().fromJson(aSoapObject.toString(), KalaPrice.class);

            if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
                return;

            context.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    aTextViewSubtotal.setText(formatM(aKalaPrice.price)
                            + " " + context.getString(R.string.toman));
                    aTextViewItemShipping.setText(formatM(aKalaPrice.discount_price) + " "
                            + context.getString(R.string.toman));
                    aTextViewTotal.setText(formatM(aKalaPrice.discount_price) + " "
                            + context.getString(R.string.toman));

                    aBasketModel.aKala.price = aKalaPrice.price;
                    aBasketModel.aKala.priceByDiscount = aKalaPrice.discount_price;

                    sumFinal();


                }
            });


        } catch (Exception e) {
            noServerResponse();
            e.printStackTrace();
        }


    }

    private void calPrice(final BasketModel aBasketModel, final TextView aTextViewSubtotal,
                          final TextView aTextViewItemShipping, final TextView aTextViewTotal) {

        HideShow(View.VISIBLE, View.VISIBLE);


        ConnectionThreadPoolSingle.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.CalculatePrice(aBasketModel);
                    else {

                        context.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                aTextViewSubtotal.setText(context.getString(R.string.no_internet));
                                aTextViewItemShipping.setText(context.getString(R.string.no_internet));
                                aTextViewTotal.setText(context.getString(R.string.no_internet));

                                aTextViewTotalSubtotal.setText(context.getString(R.string.no_internet));
                                aTextViewTotalShipping.setText(context.getString(R.string.no_internet));
                                aTextViewTotalTolal.setText(context.getString(R.string.no_internet));


                            }
                        });

                        return;

                    }


                    if (aSoapObject == null) {
                        noServerResponse();
                        return;
                    }

                    if (aSoapObject.toString().toLowerCase().equals("false")) {
                        noServerResponse();
                        return;
                    }

                    System.out.println(aSoapObject);

                    final KalaPrice aKalaPrice = new Gson().fromJson(aSoapObject.toString(), KalaPrice.class);


                    if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
                        return;

                    context.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            aTextViewSubtotal.setText(formatM(aKalaPrice.price)
                                    + " " + context.getString(R.string.toman));
                            aTextViewItemShipping.setText(formatM(aKalaPrice.discount_price) + " "
                                    + context.getString(R.string.toman));
                            aTextViewTotal.setText(formatM(aKalaPrice.discount_price) + " "
                                    + context.getString(R.string.toman));

                            aBasketModel.aKala.price = aKalaPrice.price;
                            aBasketModel.aKala.priceByDiscount = aKalaPrice.discount_price;

                            sumFinal();

                            HideShow(View.GONE, View.VISIBLE);


                        }
                    });


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });


    }


    public void sumFinalCal() {

        long subtotal = 0;
        long shipping = 0;
        long total = 0;
        long checkCount = 0;

        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
            if (aBasketModel.aCheckBoxName.isChecked()) {

                checkCount++;
                subtotal = subtotal + (aBasketModel.aKala.price);
                shipping = shipping + (aBasketModel.aKala.priceByDiscount);
                total = total + (aBasketModel.aKala.priceByDiscount);

            }

        if (shipping == subtotal) {
            context.getView().findViewById(R.id.total_shipping_f).setVisibility(View.GONE);
            context.getView().findViewById(R.id.total_subtotal_f).setVisibility(View.GONE);
        }

        aTextViewFactor.setText(getString(R.string.factor) + " (" + checkCount + ") ");

        aTextViewTotalSubtotal.setText(context.getString(R.string.calculate));
        aTextViewTotalShipping.setText(context.getString(R.string.calculate));
        aTextViewTotalTolal.setText(context.getString(R.string.calculate));

        aTextViewTotalTolalPay.setText(context.getString(R.string.calculate));


    }

    public void sumFinal() {

        long subtotal = 0;
        long shipping = 0;
        long total = 0;
        long checkCount = 0;

        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket()) {

            checkCount++;
            subtotal = subtotal + (aBasketModel.aKala.price);
            shipping = shipping + (aBasketModel.aKala.priceByDiscount);
            total = total + (aBasketModel.aKala.priceByDiscount);

        }

        if (shipping == subtotal) {
            context.getView().findViewById(R.id.total_shipping_f).setVisibility(View.GONE);
            context.getView().findViewById(R.id.total_subtotal_f).setVisibility(View.GONE);
        }

        aTextViewFactor.setText(getString(R.string.factor) + " (" + checkCount + ") ");

        aTextViewTotalSubtotal.setText(formatM(subtotal) + " " + context.getString(R.string.toman));
        aTextViewTotalShipping.setText(formatM(shipping) + " " + context.getString(R.string.toman));
        aTextViewTotalTolal.setText(formatM(total) + " " + context.getString(R.string.toman));

        aTextViewTotalTolalPay.setText(formatM(total) + " " + context.getString(R.string.toman));
        aButtonTotoalPay.setText(getString(R.string.next));

    }

    public View getView(int layout) {
        LayoutInflater inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(layout, null);
    }


    public int PxToDpi(float dipValue) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
        return px;

    }

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

    }

    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }

    public String formatM(long price) {

        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");
        return df.format(price);

    }


    public void onDestroy() {
        super.onDestroy();
        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
            aBasketModel.count = 0;
    }


    public void getData() {

        if (LoginHolder.getInstance().getLogin() == null) {
            MainActivity.context.Login();
            BasketActivity.context.finish();
            return;
        }

        //aLayout1.setVisibility(View.GONE);
        //aLayout0.setVisibility(View.GONE);
        //aProgress.setVisibility(View.VISIBLE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                ModelHolder.getInstance().addBasketBuyingAll();

                ////// setupWebView(URLConnectionz.ApiCheckBasket + getParam());


            }
        });

    }

    public void getData(final BasketModel aBasketModel) {

        if (LoginHolder.getInstance().getLogin() == null) {
            MainActivity.context.Login();
            BasketActivity.context.finish();
            return;
        }


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                if (aBasketModel != null)
                    ModelHolder.getInstance().addBasketBuying(aBasketModel);

                //// setupWebView(URLConnectionz.ApiCheckBasket + getParam(aBasketModel));


            }
        });

    }

    private StringBuilder getParam(BasketModel aBasketModel) {

        StringBuilder aStringBuilder = new StringBuilder();

        if (LoginHolder.getInstance().getLogin() != null) {
            aStringBuilder.append("?uid=" + LoginHolder.getInstance().getLoginId());
            aStringBuilder.append("&bag=" + bag);
            aStringBuilder.append("&uniq=" + uniq);
        }

        aStringBuilder.append(("&basket[" + aBasketModel.aKala.id + "]=" + aBasketModel.count));

        return aStringBuilder;
    }

    protected StringBuilder getParam() {

        StringBuilder aStringBuilder = new StringBuilder();

        if (LoginHolder.getInstance().getLogin() != null) {
            aStringBuilder.append("?uid=" + LoginHolder.getInstance().getLoginId());
            aStringBuilder.append("&bag=" + bag);
            aStringBuilder.append("&uniq=" + uniq);
        }

        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
            if (aBasketModel.aCheckBoxName.isChecked()) {
                aStringBuilder.append(("&basket[" + aBasketModel.aKala.id + "]=" + aBasketModel.count));
            }

        return aStringBuilder;
    }


    private StringBuilder getParamBasket(BasketModel aBasketModel) {

        StringBuilder aStringBuilder = new StringBuilder();


        aStringBuilder.append(("?basket[" + aBasketModel.aKala.id + "]=" + aBasketModel.count));

        return aStringBuilder;
    }

    protected StringBuilder getParamBasket() {

        StringBuilder aStringBuilder = new StringBuilder();

        int count = 0;
        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
            if (aBasketModel.aCheckBoxName.isChecked()) {
                if (count == 0)
                    aStringBuilder.append(("?basket[" + aBasketModel.aKala.id + "]=" + aBasketModel.count));
                else
                    aStringBuilder.append(("&basket[" + aBasketModel.aKala.id + "]=" + aBasketModel.count));
                count++;
            }

        return aStringBuilder;
    }

    public String encode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    protected void setupWebView(String url) {

        Intent intent = new Intent(BasketActivity.context, WebViewsActivityBuy.class);
        intent.putExtra("url", url);
        startActivityForResult(intent, 21);

        //finish();

    }

    public void showDialog(final BasketModel aBasketModel) {

        if (LoginHolder.getInstance().getLogin() == null) {

            this.savedBasket = aBasketModel;

            MainActivity.context.Login();
            BasketActivity.context.finish();

            //  Intent intent = new Intent(this, LoginActivity.class);
            //  intent.putExtra("id", "basket");
            //  startActivity(intent);

            return;
        }

        // prepareToPay(aBasketModel);

        BasketActivity.context.viewPager.setCurrentItem(1);


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       /* client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Basket Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ir.shiraztakhfif.shop/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       /* Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Basket Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ir.shiraztakhfif.shop/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
    }


    // #check and send

    public synchronized void prepareToPay(final BasketModel aBasketModel) {


        HideShow(View.VISIBLE, View.GONE);

        new Thread(new Runnable() {

            @Override
            public void run() {

                System.out.println("BASKET CHECK");

                /*try {

                    List<BasketModel> oldBasketModelList = ModelHolder.getInstance().getBasket();
                    if (oldBasketModelList == null) {
                        HideShow(View.GONE, View.VISIBLE);
                        return;
                    }

                    ConnectionPool aConnectionPool = new ConnectionPool();
                    StringBuilder aStringBuilder = null;

                    /if (NetworkUtil.getConnectivityStatusString(context) != null) {
                        aStringBuilder = aConnectionPool.ConnectAndGet(
                                URLConnectionz.ApiBasketOffer + (aBasketModel == null ? getParamBasket() : getParamBasket(aBasketModel)));
                    } else {
                        noServerResponse();
                        return;
                    }

                } catch (IOException e) {
                    noServerResponse();
                    e.printStackTrace();
                }*/


            }
        }).start();

    }

    public List<Kala> getBasketKalaListToRemove(List<BasketModel> finalBasketModelList) {

        List<Kala> aList = new ArrayList<Kala>();
        for (BasketModel aBasketModel : finalBasketModelList) {
            Kala aKala = aBasketModel.aKala;
            aKala.basketStatus = "remove";
            aList.add(aKala);
        }

        return aList;

    }


    public void refresh() {

        if (context == null || context.getActivity() == null)
            return;

        context.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                itemHolder.removeAllViews();
                initBasketItem();
                aCheckBoxAll.setChecked(true);
                aCheckBoxAll
                        .setText(getString(R.string.select_all) + " (" + ModelHolder.getInstance().getBasketCount() + ") ");
                sumFinal();

            }
        });


    }

    public void HideShow(final int pro, final int layout) {
        if (context != null && context.getActivity() != null)
            ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (aProgress != null) aProgress.setVisibility(pro);
                    aLayout0.setVisibility(layout);
                    aLayout1.setVisibility(layout);

                }
            });
    }


    public void noServerResponse() {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null || context.getActivity() == null)
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                context.getView().findViewById(R.id.no_server_response).setVisibility(View.VISIBLE);
                context.getView().findViewById(R.id.no_server_response_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.getView().findViewById(R.id.no_server_response).setVisibility(View.GONE);
                    }
                });

            }
        });

    }

    public void showCopon(String url) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

        Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

        builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.buy_done)));
        builder.setMessage(FontUtils.typeface(typeface, context.getString(R.string.buy_done_des) + "\n\n" + Html.fromHtml("<b>" + context.getString(R.string.transaction_code) + " " +
                url.replace("http://shiraztakhfif.ir/api/endBuy/", "") + "</b>")));
        builder.setCancelable(false);

        builder.setPositiveButton(context.getString(R.string.my_copon), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                BasketActivity.context.finish();

                Intent intent = new Intent(context.getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MainActivity.context.isShowCopon = true;


            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    class DFragment extends DialogFragment {

        TextView aTextViewCounter;
        int id;
        int count;

        TextView aTextViewSubtotal, aTextViewItemShipping, aTextViewTotal;
        BasketModel aBasketModel;

        public DFragment(final TextView aTextViewCounter, int id, int count, BasketModel aBasketModel, TextView aTextViewSubtotal, TextView aTextViewItemShipping, TextView aTextViewTotal) {

            this.aTextViewCounter = aTextViewCounter;
            this.id = id;
            this.count = count;

            this.aTextViewSubtotal = aTextViewSubtotal;
            this.aTextViewItemShipping = aTextViewItemShipping;
            this.aTextViewTotal = aTextViewTotal;
            this.aBasketModel = aBasketModel;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            View dialog = inflater.inflate(R.layout.dialogz_count, container, false);

            final EditText aEdt = (EditText) dialog.findViewById(R.id.value);

            dialog.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (aEdt.getText().toString().trim() != null && aEdt.getText().toString().trim().length() > 0 && Long.parseLong(aEdt.getText().toString().trim()) >= aBasketModel.aKala.minOrder && aEdt.getText().toString().trim().length() > 0 && Long.parseLong(aEdt.getText().toString().trim()) < aBasketModel.aKala.unitsInStock) {

                        long value = Long.parseLong(aEdt.getText().toString());
                        float frac = (float) value / (float) aBasketModel.aKala.minOrder;

                        System.out.println(frac + "==" + Math.round(frac));

                        // if(frac!=Math.round(frac)){
                        //  aEdt.setError(getString(R.string.not_accpet_by_this_value));
                        //  return;
                        //  }


                        aTextViewCounter.setText(aEdt.getText().toString() + " " + aBasketModel.aKala.saleType);
                        aTextViewSubtotal.setText(getString(R.string.calculate));
                        aTextViewItemShipping.setText(getString(R.string.calculate));
                        aTextViewTotal.setText(getString(R.string.calculate));

                        sumFinalCal();

                        aBasketModel.count = Integer.parseInt(aEdt.getText().toString());

                        calPrice(aBasketModel, aTextViewSubtotal, aTextViewItemShipping, aTextViewTotal);

                        dismiss();

                        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
                            if (aBasketModel.aKala.id == id) {
                                aBasketModel.count = Long.parseLong(aEdt.getText().toString());
                            }
                        aEdt.setError(null);

                    } else {
                        aEdt.setError(getString(R.string.not_accpet));
                    }


                }
            });


            return dialog;
        }
    }

    public void msg(final String msg) {

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                final Style customStyle = new Style();
                customStyle.animations = SuperToast.Animations.SCALE;
                customStyle.background = SuperToast.Background.BLUE;
                customStyle.textColor = Color.parseColor("#ffffff");
                customStyle.buttonTextColor = Color.WHITE;
                customStyle.dividerColor = Color.WHITE;

                SuperActivityToast superActivityToast = new SuperActivityToast(context.getActivity(), customStyle);
                superActivityToast.setDuration(SuperToast.Duration.MEDIUM);
                superActivityToast.setText(msg);
                superActivityToast.show();

            }
        });

    }


}
