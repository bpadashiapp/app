package ir.tahasystem.music.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.gson.Gson;

import org.ksoap2.serialization.SoapPrimitive;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.BasketActivity;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.ConsumerInfo;
import ir.tahasystem.music.app.Model.ConsumerInfoHolder;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.LoginModel;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.SaveOrder;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.OnCompleteListener;
import ir.tahasystem.music.app.utils.FontUtils;
import ir.tahasystem.music.app.utils.NetworkUtil;


public class FragmentBasket4 extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";

    public static FragmentBasket4 context;

    LinearLayout aLayout;
    Button aRip;

    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    private LoginModel aLoginModel;
    private OnCompleteListener mListener;

    CheckBox checkBoxUseSharjAmount;

    private TextView vatTxt, serviceTxt, distanceTxt;
    private CheckBox serviceTitle;
    TextInputLayout serviceTitleLayout;

    private EditText desEdt;



     RadioButton radioGroupInPlace;
     RadioButton radioGroupOnline;


    public static FragmentBasket4 createInstance(int itemsCount) {
        FragmentBasket4 partThreeFragment = new FragmentBasket4();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        aLoginModel = new LoginModel();

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.fragment_basket4, container, false);
        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);
        aLayout = (LinearLayout) aView.findViewById(R.id.layout);

        aRip = (Button) aView.findViewById(R.id.reg_btn);

        final RadioGroup radioGroup = (RadioGroup) aView.findViewById(R.id.reg_rg_pay);

        radioGroupInPlace = (RadioButton) aView.findViewById(R.id.reg_rb_in_place);
         radioGroupOnline = (RadioButton) aView.findViewById(R.id.reg_rb_pay_online);

        radioGroup.clearCheck();




        final RadioButton aRadioButtonInPlace = (RadioButton) aView.findViewById(R.id.reg_rb_in_place);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                System.out.println(" notInPlace000000000 " + ModelHolder.getInstance().ShippingCost);


                if (checkedId == R.id.reg_rb_in_place &&
                        (ModelHolder.getInstance().delivery == 2
                                || ModelHolder.getInstance().delivery == 3
                                || ModelHolder.getInstance().saleType == 5)
                        ) {

                    notInPlaceAvalible();

                    System.out.println(" notInPlaceAvalible()");

                    radioGroupOnline.setChecked(true);

                    ModelHolder.getInstance().paymentTypeId = 5;

                    return;

                } else if (checkedId == R.id.reg_rb_pay_online &&
                        ModelHolder.getInstance().saleType == 1) {

                    notInOnlineAvalible();

                    System.out.println(" notInOnlineAvalible()");

                    radioGroupInPlace.setChecked(true);

                    ModelHolder.getInstance().paymentTypeId = 1;

                    return;

                }


                if (checkedId == R.id.reg_rb_pay_online) {
                    ModelHolder.getInstance().paymentTypeId = 5;
                } else if (checkedId == R.id.reg_rb_in_place) {
                    ModelHolder.getInstance().paymentTypeId = 1;
                }


            }
        });


        aRip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean check = true;


                FragmentBasket3.context.acons.latPos = String.valueOf(Values.lat);
                FragmentBasket3.context.acons.longPos = String.valueOf(Values.lng);


                if (ModelHolder.getInstance().dontPay) {
                    msg(v.getContext().getString(R.string.not_done) + " - " + ModelHolder.getInstance().ShippingCostDes);
                    return;
                }

                if (check)
                    if (ModelHolder.getInstance().paymentTypeId == 5)
                        payBox(FragmentBasket3.context.acons);
                    else
                        payBoxInPlace(FragmentBasket3.context.acons);
                else
                    msg(v.getContext().getString(R.string.plz_fill));

            }
        });

        checkBoxUseSharjAmount = (CheckBox) aView.findViewById(R.id.reg_use_boon);
        checkBoxUseSharjAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ModelHolder.getInstance().systemSharjAmount.trim().equals("0")) {
                    boon();
                    checkBoxUseSharjAmount.setChecked(false);
                }

            }
        });

        vatTxt = (TextView) aView.findViewById(R.id.reg_s_maliyat_fee);
        serviceTxt = (TextView) aView.findViewById(R.id.reg_s_service_fee);
        serviceTitle = (CheckBox) aView.findViewById(R.id.reg_chk7);
        serviceTitleLayout = (TextInputLayout) aView.findViewById(R.id.reg_s_service_fee_layout);
        distanceTxt = (TextView) aView.findViewById(R.id.reg_s_peyak_price);

        desEdt = (EditText) aView.findViewById(R.id.reg_des);


        if (Values.appId == 0) {
            serviceTitle.setVisibility(View.GONE);
            serviceTitleLayout.setVisibility(View.GONE);
            serviceTxt.setVisibility(View.GONE);
            //checkBoxUseSharjAmount.setVisibility(View.GONE);
        }else if (Values.appId == 3) {

            aView.findViewById(R.id.reg_s_peyak_price_layout).setVisibility(View.GONE);
            aView.findViewById(R.id.reg_s_maliyat_fee_layout).setVisibility(View.GONE);
            aView.findViewById(R.id.reg_s_service_fee_layout).setVisibility(View.GONE);
            aView.findViewById(R.id.reg_use_boon_layout).setVisibility(View.GONE);


        }

        aLayout.setVisibility(View.VISIBLE);
        aProgress.setVisibility(View.GONE);
        aRip.setVisibility(View.VISIBLE);

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) aView.findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(Color.BLUE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkUtil.getConnectivityStatusString(MainActivity.context) == null) {
                    refreshLayout.setRefreshing(false);
                    return;
                }
                refreshLayout.setRefreshing(false);


            }
        });

        return aView;
    }

    public boolean isInit = false;


    public void onReCheck() {

        System.out.println(ModelHolder.getInstance().saleType);

        if(ModelHolder.getInstance().saleType==5) {
            radioGroupOnline.setSelected(true);
            radioGroupOnline.setChecked(true);
        }else if(ModelHolder.getInstance().saleType==1) {
            radioGroupInPlace.setSelected(true);
            radioGroupInPlace.setChecked(true);
        }else{
            radioGroupOnline.setSelected(true);
            radioGroupOnline.setChecked(true);
        }

        vatTxt.setText(ModelHolder.getInstance().vat + " " + context.getString(R.string.toman));
        serviceTxt.setText(ModelHolder.getInstance().service + " " + context.getString(R.string.toman));

        if (ModelHolder.getInstance().delivery != 3)
            serviceTxt.setText(0 + " " + getString(R.string.toman));

        if (ModelHolder.getInstance().delivery == 2 || ModelHolder.getInstance().delivery == 3)
            distanceTxt.setText(0 + " " + getString(R.string.toman));
        else
            distanceTxt.setText(ModelHolder.getInstance().ShippingCostDes);

    }


    public void onResume() {
        super.onResume();

        ((TextView) FragmentBasket4.context.getView().findViewById(R.id.reg_s_maliyat_fee)).setText(ModelHolder.getInstance().vat + " " + context.getString(R.string.toman));
        ((TextView) FragmentBasket4.context.getView().findViewById(R.id.reg_s_service_fee)).setText(ModelHolder.getInstance().service + " " + context.getString(R.string.toman));

        if (ModelHolder.getInstance().delivery != 3)
            ((TextView) FragmentBasket4.context.getView().findViewById(R.id.reg_s_service_fee)).setText(0 + " " + getString(R.string.toman));

        if (ModelHolder.getInstance().ShippingCost == -2 || ModelHolder.getInstance().ShippingCost == -3)
            ((TextView) FragmentBasket4.context.getView().findViewById(R.id.reg_s_peyak_price)).setText(0 + " " + getString(R.string.toman));
        else
            ((TextView) FragmentBasket4.context.getView().findViewById(R.id.reg_s_peyak_price)).setText(ModelHolder.getInstance().ShippingCostDes);






    }


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onStart() {
        super.onStart();

    }

    protected void pay(final ConsumerInfo acons, final boolean isInPlace) {


        HideShow(View.VISIBLE, View.GONE);
        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                Gson gson = new Gson();

                ConsumerInfoHolder aConsumerInfoHolder = new ConsumerInfoHolder(acons);

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapPrimitive = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) == null) {
                        noServerResponse();
                        return;
                    }

                    aSoapPrimitive = aConnectionPool.ConnectUpdateInfo(gson.toJson(acons));
                    if (aSoapPrimitive == null || aSoapPrimitive.equals("false")) {
                        noServerResponse();
                        return;
                    }


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


                SaveOrder aSaveOrder = new SaveOrder();
                aSaveOrder.companyId = Values.companyId;
                aSaveOrder.username = LoginHolder.getInstance().getLogin().uid;
                aSaveOrder.description = desEdt.getText().toString();
                aSaveOrder.paymentTypeId = ModelHolder.getInstance().paymentTypeId;
                aSaveOrder.shippingTimeId = ModelHolder.getInstance().ShippingTimeId;
                aSaveOrder.shippingDate = ModelHolder.getInstance().ShippingDate;
                aSaveOrder.ShippingCost = ModelHolder.getInstance().ShippingCost;
                aSaveOrder.usesharjAmount = checkBoxUseSharjAmount.isChecked();
                aSaveOrder.lst = ModelHolder.getInstance().getOrderParams();


                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapPrimitive = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) == null) {
                        noServerResponse();
                        return;
                    }

                    aSoapPrimitive = aConnectionPool.ConnectSaveOrder(aSaveOrder);
                    if (aSoapPrimitive == null || Integer.parseInt(aSoapPrimitive.toString()) == 0) {
                        noServerResponse();
                        return;
                    }

                    if (Integer.parseInt(aSoapPrimitive.toString()) == -2) {

                        payBoxInPlaceEmpty();
                        HideShow(View.GONE, View.VISIBLE);
                        return;

                    }

                    if (isInPlace) {
                        payBoxInPlaceDone();
                        return;
                    }


                    String url = "http://onlinepakhsh.com/wservices/onlinePayByMob.aspx?OrderID=" + aSoapPrimitive.toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                    System.out.println(aSoapPrimitive.toString());
                    // Intent intent = new Intent(context.getActivity(), WebViewsActivityBuy.class);
                    //intent.putExtra("url", );
                    // context.getActivity().startActivity(intent);

                    ModelHolder.getInstance().removeBasketAll();

                    if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
                        return;

                    ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {


                            if (BasketActivity.context != null) {
                                // MainActivity.context.bottomNavigation.setNotification(String.valueOf(0), 2);
                                //MainActivity.context.bottomNavigation.setCurrentItem(3);
                                BasketActivity.context.finish();
                            }


                        }
                    });


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }

    public void SetupWebView(final String str) {


        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                ((TextView) context.getView().findViewById(R.id.reg_s_maliyat_fee)).setText(str + " " + context.getString(R.string.toman));
                HideShow(View.GONE, View.VISIBLE);


            }
        });
    }


    public void msg(final String msg) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                final Style customStyle = new Style();
                customStyle.animations = SuperToast.Animations.SCALE;
                customStyle.background = SuperToast.Background.RED;
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


    public void HideShow(final int pro, final int layout) {
        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (aProgress != null) aProgress.setVisibility(pro);
                aLayout.setVisibility(layout);
                aRip.setVisibility(layout);
            }
        });
    }


    public void noServerResponse() {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                context.getView().findViewById(R.id.no_server_response).setVisibility(View.VISIBLE);
                context.getView().findViewById(R.id.no_server_response_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.getView().findViewById(R.id.no_server_response).setVisibility(View.GONE);
                        //HideShow(View.VISIBLE, View.GONE);
                        //pay();

                    }
                });

            }
        });

    }


    public void payBox(final ConsumerInfo acons) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

        Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

        builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.pay)));
        builder.setMessage(FontUtils.typeface(typeface, context.getString(R.string.do_pay)));
        builder.setCancelable(true);

        builder.setPositiveButton(context.getString(R.string.pay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                pay(acons, false);

            }
        });

        builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    public void payBoxInPlace(final ConsumerInfo acons) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

        Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

        builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.save_order)));
        builder.setMessage(FontUtils.typeface(typeface, context.getString(R.string.save_order_des)));
        builder.setCancelable(true);

        builder.setPositiveButton(context.getString(R.string.saved), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                pay(acons, true);

            }
        });

        builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    public void payBoxInPlaceDone() {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

                Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

                builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.request_send)));
                builder.setMessage(FontUtils.typeface(typeface, context.getString(R.string.request_send_des)));
                builder.setCancelable(true);

                builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        BasketActivity.context.finish();

                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ModelHolder.getInstance().removeBasketAll();

               /* if (MainActivity.context != null)
                    MainActivity.context.bottomNavigation.setCurrentItem(3);

                if (MainActivity.context != null)
                    MainActivity.context.bottomNavigation.setNotification(String.valueOf(0), 2);*/

                if (BasketActivity.context != null)
                    BasketActivity.context.finish();

            }
        });


    }

    public void payBoxInPlaceEmpty() {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

                Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

                builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.not_done)));
                builder.setMessage(FontUtils.typeface(typeface, context.getString(R.string.empty_order)));
                builder.setCancelable(true);

                builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
    }

    public void notInPlaceAvalible() {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

                Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

                builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.in_place)));
                builder.setMessage(FontUtils.typeface(typeface, context.getString(R.string.in_place_des)));
                builder.setCancelable(true);

                builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


    }

    public void notInOnlineAvalible() {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

                Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

                builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.pay_online)));
                builder.setMessage(FontUtils.typeface(typeface, context.getString(R.string.online_des)));
                builder.setCancelable(true);

                builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


    }

    public void boon() {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

                Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

                builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.use_boon)));
                builder.setMessage(FontUtils.typeface(typeface, context.getString(R.string.no_boon_des)));
                builder.setCancelable(true);

                builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


    }


}
