package ir.tahasystem.music.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.Order;


public class MapTocuhActivity extends AppCompatActivity {

    public static Kala aKala;
    Toolbar mToolbar;

    public static MapTocuhActivity context;
    public static Order aOrder;

    LinearLayout aLayout;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    public static boolean isNew;
    private Location location;
    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("----->>>>>>");

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_map_touch);


        findViewById(R.id.submit_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        initToolbar();

        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) findViewById(R.id.list_load);
        aLayout = (LinearLayout) findViewById(R.id.layout);


    }

    public boolean isInit = false;

    public void init() {

        if (isInit || context == null)
            return;

        isInit = true;


        SupportMapFragment fm = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map));

        if (fm != null)
            googleMap = fm.getMap();
        ;

        if (googleMap == null)
            return;


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        LatLng latLngHome;
        if (Values.peykLatLng == null)
            latLngHome = new LatLng(Values.lat, Values.lng);
        else {

            latLngHome = Values.peykLatLng;
            googleMap.addMarker(new MarkerOptions().position(Values.peykLatLng));
        }

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(latLngHome);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                Values.peykLatLng = point;

                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(point));
            }
        });

        aProgress.setVisibility(View.GONE);


    }

    public void onResume() {
        super.onResume();

        init();

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
        atxt.setText(getString(R.string.position));


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


}
