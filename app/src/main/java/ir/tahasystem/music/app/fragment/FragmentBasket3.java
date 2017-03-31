package ir.tahasystem.music.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.gson.Gson;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.regex.Pattern;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.BasketActivity;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.MapTocuhActivity;
import ir.tahasystem.music.app.Model.Company;
import ir.tahasystem.music.app.Model.ConsumerInfo;
import ir.tahasystem.music.app.Model.ConsumerInfoHolder;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.LoginModel;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.SaveOrder;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.WebViewsActivityBuy;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.OnCompleteListener;
import ir.tahasystem.music.app.utils.FontUtils;
import ir.tahasystem.music.app.utils.NetworkUtil;


public class FragmentBasket3 extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";

    public static FragmentBasket3 context;

    LinearLayout aLayout;
    Button aRip;

    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;
    public EditText edtName, edtFamily, edtTel, edtMobile, edtAddress1, edtAddress2;



    private LoginModel aLoginModel;
    public ConsumerInfo acons;
    private OnCompleteListener mListener;

     CheckBox aCheckBox3;

    public static FragmentBasket3 createInstance(int itemsCount) {
        FragmentBasket3 partThreeFragment = new FragmentBasket3();
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

        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        View aView = (View) inflater.inflate(R.layout.fragment_basket3, container, false);
        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);
        aLayout = (LinearLayout) aView.findViewById(R.id.layout);

        edtName = (EditText) aView.findViewById(R.id.reg_name);
        edtFamily = (EditText) aView.findViewById(R.id.reg_family);
        edtTel = (EditText) aView.findViewById(R.id.reg_tel);
        edtMobile = (EditText) aView.findViewById(R.id.reg_mobile);
        edtAddress1 = (EditText) aView.findViewById(R.id.reg_address1);
        edtAddress2 = (EditText) aView.findViewById(R.id.reg_address2);



        final CheckBox aCheckBox1 = (CheckBox) aView.findViewById(R.id.reg_address1_chk);
        final CheckBox aCheckBox2 = (CheckBox) aView.findViewById(R.id.reg_address2_chk);
         aCheckBox3 = (CheckBox) aView.findViewById(R.id.reg_address3_chk);
        final CheckBox aCheckBox4 = (CheckBox) aView.findViewById(R.id.reg_address4_chk);

        acons = new ConsumerInfo();

        acons.activeAddress = 1;
        acons.username = LoginHolder.getInstance().getLogin().uid;

        aCheckBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {

                    ModelHolder.getInstance().delivery = 0;

                    aCheckBox2.setChecked(false);
                    aCheckBox3.setChecked(false);
                    aCheckBox4.setChecked(false);

                    edtAddress2.setError(null);

                    acons.activeAddress = 1;

                    ModelHolder.getInstance().ShippingCost = Integer.parseInt(distanceCost[1]);


                    if (edtAddress1.getText().toString().trim().length() != 0) {
                        edtAddress1.setError(null);
                    } else {
                        edtAddress1.setError(getString(R.string.fill));
                    }

                }

            }
        });


        aCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {

                    ModelHolder.getInstance().delivery = 1;

                    aCheckBox1.setChecked(false);
                    aCheckBox3.setChecked(false);
                    aCheckBox4.setChecked(false);

                    edtAddress1.setError(null);

                    acons.activeAddress = 2;
                    ModelHolder.getInstance().ShippingCost = Integer.parseInt(distanceCost[1]);

                    if (edtAddress2.getText().toString().trim().length() != 0) {
                        edtAddress2.setError(null);
                    } else {
                        edtAddress2.setError(getString(R.string.fill));
                    }

                }

            }
        });


        aCheckBox3.setText(getString(R.string.get_in_place_app_name) + " "
                + getString(R.string.app_name) + " (" + getString(R.string.get_in_place_by_customer) + ")");

        aCheckBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {

                    ModelHolder.getInstance().delivery = 2;

                    edtAddress1.setError(null);
                    edtAddress2.setError(null);

                    acons.activeAddress = 0;

                    aCheckBox1.setChecked(false);
                    aCheckBox2.setChecked(false);
                    aCheckBox4.setChecked(false);

                    ModelHolder.getInstance().ShippingCost = -2;

                }


            }
        });

        if (Values.appId == 0) {
            aCheckBox4.setVisibility(View.GONE);
        }

        aCheckBox4.setText(getString(R.string.server_in_place) + " ("
                + getString(R.string.resturan_special) + ")");

        aCheckBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {

                    ModelHolder.getInstance().delivery = 3;

                    edtAddress1.setError(null);
                    edtAddress2.setError(null);

                    acons.activeAddress = 0;

                    aCheckBox1.setChecked(false);
                    aCheckBox2.setChecked(false);
                    aCheckBox3.setChecked(false);

                    ModelHolder.getInstance().ShippingCost = -3;

                }

            }
        });

        ModelHolder.getInstance().delivery = 0;
        aCheckBox1.setChecked(true);


        aRip = (Button) aView.findViewById(R.id.reg_btn);
        aRip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean check = true;

                if (edtName.getText().toString().trim().length() != 0) {
                    edtName.setError(null);
                    acons.name = edtName.getText().toString();
                } else {
                    edtName.setError(v.getContext().getString(R.string.fill));
                    check = false;
                }

                if (edtFamily.getText().toString().trim().length() != 0) {
                    edtFamily.setError(null);
                    acons.family = edtFamily.getText().toString();
                } else {
                    edtFamily.setError(v.getContext().getString(R.string.fill));
                    check = false;
                }

                if (edtTel.getText().toString().trim().length() != 0) {
                    edtTel.setError(null);
                    acons.phone = edtTel.getText().toString();
                } else {
                    edtTel.setError(v.getContext().getString(R.string.fill));
                    check = false;
                }


                if (edtAddress1.getText().toString().trim().length() != 0) {
                    edtAddress1.setError(null);
                    acons.address = edtAddress1.getText().toString();
                } else if (aCheckBox1.isChecked()) {
                    edtAddress1.setError(v.getContext().getString(R.string.fill));
                    check = false;
                }

                if (edtAddress2.getText().toString().trim().length() != 0) {
                    edtAddress2.setError(null);
                    acons.address2 = edtAddress2.getText().toString();
                } else if (aCheckBox2.isChecked()) {
                    edtAddress2.setError(v.getContext().getString(R.string.fill));
                    check = false;
                }
                synchronized (this) {

                    acons.latPos = String.valueOf(Values.lat);
                    acons.longPos = String.valueOf(Values.lng);

                }

                if (aCheckBox1.isChecked() == false && aCheckBox2.isChecked() == false &&
                        aCheckBox3.isChecked() == false && aCheckBox4.isChecked() == false)
                    check = false;

                if (check)
                    getDataGoNext();
                else
                    msg(v.getContext().getString(R.string.plz_fill));

            }
        });

        aView.findViewById(R.id.find_route_map0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FragmentBasket3.this.getActivity(), MapTocuhActivity.class));
            }
        });

        aView.findViewById(R.id.find_route_map1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FragmentBasket3.this.getActivity(), MapTocuhActivity.class));
            }
        });


        if (Values.appId == 2) {
            aCheckBox4.setVisibility(View.GONE);
        } else if (Values.appId == 3) {
            aCheckBox4.setVisibility(View.GONE);

            aCheckBox1.setText(getString(R.string.address_1_service));


            aCheckBox2.setText(getString(R.string.address_2_service));


            aCheckBox3.setText(getString(R.string.get_in_place_service));

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

                getData(false);

            }
        });


        return aView;
    }

    public boolean isInit = false;

    public void init() {

        if (isInit || context == null)
            return;

        isInit = true;

        synchronized (this) {


            getData(false);

        }

    }


    public void getDataGoNext() {

        synchronized (this) {

            getData(true);

        }

    }


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onStart() {
        super.onStart();

    }

    protected void getData(final boolean next) {


        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {


                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapObject aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) == null) {
                        noServerResponse();
                        return;
                    }

                    aSoapObject = aConnectionPool.ConnectGetInfo(LoginHolder.getInstance().getLogin().uid);
                    if (aSoapObject == null) {
                        // noServerResponse();
                        return;
                    }

                    LoginModel aLoginModel = new LoginModel();

                    System.out.println(aSoapObject.getPropertyCount());

                    aLoginModel.name = (String) aSoapObject.getProperty("name").toString().trim().replace(".", "");
                    aLoginModel.family = (String) aSoapObject.getProperty("family").toString().trim().replace(".", "");
                    aLoginModel.phone = (String) aSoapObject.getProperty("phone").toString().trim().replace(".", "");
                    aLoginModel.mobile = (String) aSoapObject.getProperty("username").toString().trim().replace(".", "");
                    aLoginModel.address = (String) aSoapObject.getProperty("address").toString().trim().replace(".", "");
                    aLoginModel.address2 = (String) aSoapObject.getProperty("address2").toString().trim().replace(".", "");
                    SetupUiUser(aLoginModel);

                    System.out.println(aLoginModel.phone + "-->aLoginModel.phone");
                    SoapPrimitive aSoapPrimitive;
                    synchronized (this) {
                        aSoapPrimitive = aConnectionPool.ConnectGetShippingCostOfDistance(Values.companyId,
                                Values.lat,
                                Values.lng);
                    }
                    if (aSoapPrimitive == null) {
                        noServerResponse();
                        return;
                    }
                    System.out.println("distanceCost-->" + aSoapPrimitive.toString());
                    distanceCost = aSoapPrimitive.toString().split(Pattern.quote("_"));
                    SetupUiDistance(distanceCost);

                    aSoapPrimitive = aConnectionPool.ConnectGetVat(Values.companyId);
                    if (aSoapPrimitive == null) {
                        noServerResponse();
                        return;
                    }
                    System.out.println("vat-->" + aSoapPrimitive);


                    SoapPrimitive bSoapPrimitive = aConnectionPool.ConnectGetServiceCharge(Values.companyId);
                    if (bSoapPrimitive == null) {
                        noServerResponse();
                        return;
                    }
                    System.out.println("serviceCharge-->" + bSoapPrimitive);

                    aSoapPrimitive = aConnectionPool.ConnectGetCompanyInfo(Values.companyId);
                    if (aSoapPrimitive == null) {
                        noServerResponse();
                        return;
                    }
                    Company aCompany = new Gson().fromJson(aSoapPrimitive.toString(), Company.class);
                    if (aCompany != null)
                        ModelHolder.getInstance().saleType = aCompany.saleType;

                    SetupUiSaleType();

                    aSoapPrimitive = aConnectionPool.ConnectGetSharjAmount(Values.companyId, LoginHolder.getInstance().getLogin().uid);
                    if (aSoapPrimitive == null) {
                        noServerResponse();
                        return;
                    }
                    System.out.println("sharjAmount-->" + aSoapPrimitive);
                    ModelHolder.getInstance().systemSharjAmount = aSoapPrimitive.toString();
                    SetupUiVatService(aSoapPrimitive.toString(), bSoapPrimitive.toString(), next);


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }

    public void SetupUiUser(final LoginModel aLoginModel) {


        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                edtName.setText(aLoginModel.name);
                edtFamily.setText(aLoginModel.family);
                edtTel.setText(aLoginModel.phone);
                edtMobile.setText(aLoginModel.mobile);
                edtAddress1.setText(aLoginModel.address);
                edtAddress2.setText(aLoginModel.address2);


                //HideShow(View.GONE, View.VISIBLE);


            }
        });
    }

    public void SetupUiDistance(final String[] distanceCost) {


        if (FragmentBasket4.context == null || FragmentBasket4.context.getActivity() == null || FragmentBasket4.context.getView() == null)
            return;

        ((Activity) FragmentBasket4.context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (distanceCost[1] != null && Integer.parseInt(distanceCost[1]) < 0)
                    ModelHolder.getInstance().dontPay = true;
                else
                    ModelHolder.getInstance().dontPay = false;


                ModelHolder.getInstance().ShippingCostDes = distanceCost[0];

                ModelHolder.getInstance().ShippingCost = Integer.parseInt(distanceCost[1]);
                //HideShow(View.GONE, View.VISIBLE);


            }
        });
    }

    public void SetupUiSaleType() {



        ((Activity) FragmentBasket4.context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if(ModelHolder.getInstance().saleType==1)
                    aCheckBox3.setVisibility(View.GONE);




            }
        });
    }

    public void SetupUiVatService(final String vat, final String service, final boolean next) {

        ModelHolder.getInstance().vat = vat;
        ModelHolder.getInstance().service = service;

        ((Activity) FragmentBasket4.context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                if (next && BasketActivity.context != null)
                        BasketActivity.context.viewPager.setCurrentItem(3);

                HideShow(View.GONE, View.VISIBLE);


            }
        });
    }


    protected void pay(final ConsumerInfo acons) {


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
                aSaveOrder.description = "";
                aSaveOrder.paymentTypeId = ModelHolder.getInstance().paymentTypeId;
                aSaveOrder.shippingTimeId = ModelHolder.getInstance().ShippingTimeId;
                aSaveOrder.shippingDate = ModelHolder.getInstance().ShippingDate;
                aSaveOrder.ShippingCost = ModelHolder.getInstance().ShippingCost;
                aSaveOrder.usesharjAmount = false;
                aSaveOrder.lst = ModelHolder.getInstance().getOrderParams();


                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapPrimitive = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) == null) {
                        noServerResponse();
                        return;
                    }

                    aSoapPrimitive = aConnectionPool.ConnectSaveOrder(aSaveOrder);
                    if (aSoapPrimitive == null || Integer.parseInt(aSoapPrimitive.toString()) <= 0) {
                        noServerResponse();
                        return;
                    }

                    System.out.println(aSoapPrimitive.toString());
                    Intent intent = new Intent(context.getActivity(), WebViewsActivityBuy.class);
                    intent.putExtra("url", "http://onlinepakhsh.com/wservices/onlinePayByMob.aspx?OrderID=" + aSoapPrimitive.toString());
                    context.getActivity().startActivity(intent);


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


    String[] distanceCost;

    protected void CheckpayInPlace() {

        HideShow(View.VISIBLE, View.GONE);
        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                String vat;
                String serviceCharge;
                String sharjAmount;

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapPrimitive = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) == null) {
                        noServerResponse();
                        return;
                    }
                    synchronized (this) {
                        if (false) {
                            //MainActivity.context.getMyLocation();
                            HideShow(View.GONE, View.VISIBLE);
                            return;
                        }


                        aSoapPrimitive = aConnectionPool.ConnectGetShippingCostOfDistance(Values.companyId,
                                Values.lat,
                                Values.lng);
                        if (aSoapPrimitive == null) {
                            noServerResponse();
                            return;
                        }

                    }

                    System.out.println("distanceCost-->" + aSoapPrimitive.toString().split(Pattern.quote("_"))[0] + "===" + aSoapPrimitive.toString().split(Pattern.quote("_"))[1]);
                    distanceCost = aSoapPrimitive.toString().split(Pattern.quote("_"));

                    aSoapPrimitive = aConnectionPool.ConnectGetServiceCharge(Values.companyId);
                    if (aSoapPrimitive == null) {
                        noServerResponse();
                        return;
                    }
                    System.out.println("serviceCharge-->" + aSoapPrimitive);
                    serviceCharge = aSoapPrimitive.toString();


                    aSoapPrimitive = aConnectionPool.ConnectGetVat(Values.companyId);
                    if (aSoapPrimitive == null) {
                        noServerResponse();
                        return;
                    }
                    System.out.println("vat-->" + aSoapPrimitive);
                    vat = aSoapPrimitive.toString();


                    aSoapPrimitive = aConnectionPool.ConnectGetSharjAmount(Values.companyId, LoginHolder.getInstance().getLogin().uid);
                    if (aSoapPrimitive == null) {
                        noServerResponse();
                        return;
                    }
                    System.out.println("sharjAmount-->" + aSoapPrimitive);
                    sharjAmount = aSoapPrimitive.toString();


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


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

                        if (true)
                            getData(false);

                    }
                });

            }
        });

    }

    private boolean isGPSEnabled, isNetworkEnabled;
    private boolean canGetLocation;
    LocationManager locationManager;

    private static final long MIN_TIME_BW_UPDATES = 10000;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    AlertDialog alertDialog;

    public void GpsBox() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(), R.style.MyAlertDialogStyle);

        Typeface typeface = Typeface.createFromAsset(this.getActivity().getAssets(), "irfontnumbold.ttf");

        builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.gps)));
        builder.setMessage(FontUtils.typeface(typeface, context.getString(R.string.gps_not_enabled)));
        builder.setCancelable(false);

        builder.setPositiveButton(context.getString(R.string.enable), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.dismiss();

                BasketActivity.context.finish();

                //MainActivity.context.bottomNavigation.setCurrentItem(3);


                Intent gpsOptionsIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(gpsOptionsIntent, 1000);


            }
        });


        alertDialog = builder.create();
        alertDialog.show();


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
                pay(acons);

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

}
