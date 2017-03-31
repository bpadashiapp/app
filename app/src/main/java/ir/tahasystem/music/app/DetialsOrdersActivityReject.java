package ir.tahasystem.music.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import org.ksoap2.serialization.SoapPrimitive;

import java.text.DecimalFormat;
import java.util.Locale;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.Order;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.fragment.FragmentOrder;
import ir.tahasystem.music.app.utils.NetworkUtil;


public class DetialsOrdersActivityReject extends AppCompatActivity {

    Toolbar mToolbar;

    public static DetialsOrdersActivityReject context;
    public static Order aOrder;

    LinearLayout aLayout;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    public static boolean isNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_order_detials_reject);
        initToolbar();

        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) findViewById(R.id.list_load);
        aLayout = (LinearLayout) findViewById(R.id.layout);

        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");

       ((TextView) this.findViewById(R.id.ordersCode)).setText(String.valueOf(aOrder.orderId));

        ((TextView) this.findViewById(R.id.fullname)).setText(aOrder.fullname);
        ((TextView) this.findViewById(R.id.phone)).setText(getString(R.string.tel) + " : " + aOrder.getPhone());
        ((TextView) this.findViewById(R.id.mobile)).setText(getString(R.string.mobile) + " : " + aOrder.getMobile());
        ((TextView) this.findViewById(R.id.date_reg)).setText(getString(R.string.date_reg) + " : " + aOrder.pesianDateTime);
        ((TextView) this.findViewById(R.id.date_send)).setText(getString(R.string.date_send) + " : " + aOrder.pesianShippingDate);
        ((TextView) this.findViewById(R.id.address)).setText(getString(R.string.address) + " : " + aOrder.address);
        ((TextView) this.findViewById(R.id.description)).setText(getString(R.string.description) + " : " + aOrder.description);

       // ((TextView) this.findViewById(R.id.totalprice)).setText(df.format((int) aOrder.totalprice) + " " + getString(R.string.toman));
       // ((TextView) this.findViewById(R.id.shipping_cost)).setText(df.format((int) aOrder.getShippingCost()) + " " + getString(R.string.toman));
       // ((TextView) this.findViewById(R.id.vat)).setText(df.format((int) aOrder.vat) + " " + getString(R.string.toman));
       // ((TextView) this.findViewById(R.id.payment_type)).setText(aOrder.getPaymentType());
       // ((TextView) this.findViewById(R.id.service_charge)).setText(df.format((int) aOrder.servicecharge) + " " + getString(R.string.toman));
        //((TextView) this.findViewById(R.id.final_price)).setText(df.format((int) aOrder.getFinalPrice()) + " " + getString(R.string.toman));


        FragmentOrder.aOrderDetails = aOrder.lstOrderDetails;
        this.findViewById(R.id.buy_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetialsOrdersActivityReject.this, DetialsSubOrdersActivity.class));
            }
        });

        this.findViewById(R.id.reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DFragment alertdFragment = new DFragment();
                alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                alertdFragment.show(getSupportFragmentManager(), "");

            }
        });


        this.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setData(aOrder.orderId, true, "");

            }
        });

        if (!isNew) {
            this.findViewById(R.id.reject).setVisibility(View.GONE);
            this.findViewById(R.id.submit).setVisibility(View.GONE);
        }

        HideShow(View.GONE, View.VISIBLE);


    }


    public void setLang() {

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

    TextView atxt;

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        setTitle("");
        atxt = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        atxt.setText(getString(R.string.details));


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        // mDrawerToggle.syncState();

    }


    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_item, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        } else if (menuItem.getItemId() == R.id.action_back) {
            finish();
        }

        return super.onOptionsItemSelected(menuItem);

    }

    public void onDestroy() {
        super.onDestroy();
        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
            aBasketModel.count = 0;
    }

    class DFragment extends DialogFragment {

        boolean enableChangeListener = true;

        public DFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View dialog = inflater.inflate(R.layout.dialogz, container, false);

            final EditText aEdt = (EditText) dialog.findViewById(R.id.value);


            dialog.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (aEdt.getText().toString().trim().length() > 0) {
                        setData(aOrder.orderId, false, aEdt.getText().toString());
                        dismiss();

                        hideKeyboardFrom(context, aEdt);
                    }

                }
            });


            return dialog;
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setData(final int orderId, final boolean IsAccepted, final String Description) {

        HideShow(View.VISIBLE, View.GONE);


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(context) != null)
                        aSoapObject = aConnectionPool.ConnectCheckedReciveOrders(orderId, IsAccepted, Description);


                    if (aSoapObject == null) {
                        noServerResponse();
                        return;
                    }

                    if (aSoapObject.toString().toLowerCase().equals("true")) {
                        msg(context.getString(R.string.done));
                    } else {
                        msg(context.getString(R.string.not_done));
                    }

                    if (IsAccepted) {
                        OrderActivity.context.fragmentOrderSubmit.isInit = false;
                        OrderActivity.context.fragmentOrderSubmit.init();

                        if (OrderActivity.context == null)
                            return;

                        ((Activity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                OrderActivity.context.viewPager.setCurrentItem(2);

                            }
                        });

                    } else {
                        OrderActivity.context.fragmentOrderReject.isInit = false;
                        OrderActivity.context.fragmentOrderReject.init();
                        if (OrderActivity.context == null)
                            return;

                        ((Activity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                OrderActivity.context.viewPager.setCurrentItem(1);

                            }
                        });
                    }

                    finish();


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }

    public void msg(final String msg) {

        if (OrderActivity.context == null)
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                final Style customStyle = new Style();
                customStyle.animations = SuperToast.Animations.SCALE;
                customStyle.background = SuperToast.Background.BLUE;
                customStyle.textColor = Color.parseColor("#ffffff");
                customStyle.buttonTextColor = Color.WHITE;
                customStyle.dividerColor = Color.WHITE;

                SuperActivityToast superActivityToast = new SuperActivityToast(OrderActivity.context, customStyle);
                superActivityToast.setDuration(SuperToast.Duration.MEDIUM);
                superActivityToast.setText(msg);
                superActivityToast.show();

            }
        });

    }

    public void noServerResponse() {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null)
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                findViewById(R.id.no_server_response).setVisibility(View.VISIBLE);
                findViewById(R.id.no_server_response_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.no_server_response).setVisibility(View.GONE);
                        //HideShow(View.VISIBLE, View.GONE);
                        //pay();

                    }
                });

            }
        });

    }

    public void HideShow(final int pro, final int layout) {
        if (context == null)
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(aProgress!=null) aProgress.setVisibility(pro);
                aLayout.setVisibility(layout);
            }
        });
    }


}
