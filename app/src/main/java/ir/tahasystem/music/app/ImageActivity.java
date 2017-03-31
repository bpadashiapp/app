package ir.tahasystem.music.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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


public class ImageActivity extends AppCompatActivity {

    Toolbar mToolbar;

    public static ImageActivity context;
    public static Order aOrder;

    LinearLayout aLayout;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    public static String img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_image);
        initToolbar();

        ImageView imageView= (ImageView) this.findViewById(R.id.image);
        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) findViewById(R.id.list_load);
        aLayout = (LinearLayout) findViewById(R.id.layout);

        Picasso.with(this)
                .load(img)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                        HideShow(View.GONE, View.VISIBLE);

                    }

                    @Override
                    public void onError() {

                        noServerResponse();

                    }
                });


    }

    public void onStart() {
        super.onStart();

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
        atxt.setText("");


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


}
