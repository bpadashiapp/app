package ir.tahasystem.music.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.Locale;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.Company;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.Order;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Serialize;


public class ContactusActivity extends AppCompatActivity {

    Toolbar mToolbar;

    public static ContactusActivity context;
    public static Order aOrder;

    LinearLayout aLayout;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    public static boolean isNew;
    private boolean isFill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_contactus);
        initToolbar();

        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) findViewById(R.id.list_load);
        aLayout = (LinearLayout) findViewById(R.id.layout);


    }

    public void onStart() {
        super.onStart();

        getData();
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
        atxt.setText(getString(R.string.contactus));


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


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    Company aCompany;

    private void getData() {

        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                Company cListKala = (Company) new Serialize().readFromFile(context,
                        "aListKalaCompanyC");

                aCompany=  cListKala;

                if (cListKala != null) {
                    // aListKala.add(cListKala);
                    isFill = true;
                    setupView(cListKala);

                } else {
                    isFill = false;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) == null)
                        noServerResponse();

                }

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectGetCompanyInfo(Values.companyId);


                    if (aSoapObject == null || aSoapObject.toString() == null) {
                        noServerResponse();
                        return;
                    }

                    aCompany = new Gson().fromJson(aSoapObject.toString(), Company.class);



                    if (aCompany!=null) {


                        if (!isFill) {
                            setupView(aCompany);

                        } else {
                            HideShow(View.GONE, View.VISIBLE);
                        }

                        new Serialize().saveToFile(context,aCompany,
                                "aListKalaCompanyC");

                    } else {
                        HideShow(View.GONE, View.VISIBLE);
                    }

                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }



    public void setupView(final Company company) {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null)
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                if (context == null)
                    return;

                ImageView imageView= (ImageView)findViewById(R.id.image);

                Picasso.with(context)
                        .load(company.img).skipMemoryCache()
                        .into(imageView);

                ((TextView)findViewById(R.id.name)).setText(company.companyName);
               // ((TextView)findViewById(R.id.description)).setText(company.shortDescription);
                ((TextView)findViewById(R.id.address)).setText(company.address);



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


    public void phone(View v){

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + aCompany.phone));
        startActivity(intent);

    }

    public void mobile(View v){

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + aCompany.Mobile));
        startActivity(intent);

    }






}
