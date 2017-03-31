package ir.tahasystem.music.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.DialogBox.LoadBox;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.Company;
import ir.tahasystem.music.app.Model.MapModel;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.Order;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.utils.DirectionsJSONParser;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Serialize;


public class MapActivity extends AppCompatActivity {

    Toolbar mToolbar;

    public static MapActivity context;
    public static Order aOrder;

    LinearLayout aLayout;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    public static boolean isNew;
    private Location location;

    TextView txtKm;

    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";

    public String MODE = "driving";

    FloatingActionButton aFabUpR;
    FloatingActionButton aFabUpL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("----->>>>>>");

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_map);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        System.out.println(locationManager + "####XXXX");


        initToolbar();

        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) findViewById(R.id.list_load);
        aLayout = (LinearLayout) findViewById(R.id.layout);

        txtKm = (TextView) findViewById(R.id.map_km);


        aFabUpL = (FloatingActionButton) findViewById(R.id.fab_up_l);
        aFabUpL.setVisibility(View.VISIBLE);

        aFabUpR = (FloatingActionButton) findViewById(R.id.fab_up_r);
        aFabUpR.setVisibility(View.VISIBLE);

       /* if (MODE.equals(MODE_DRIVING)) {
            aFabUp.setImageResource(R.drawable.drive_icon);
        } else {
            aFabUp.setImageResource(R.drawable.walk_icon);
        }*/

        aFabUpL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                synchronized (this) {

                        MODE = MODE_DRIVING;
                        getData();

                }
            }
        });

        aFabUpR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                synchronized (this) {

                        MODE = MODE_WALKING;
                        getData();
                }


            }
        });

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

        setupViewInit();

        getData();
        getMyLocation();

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


    private void getData() {

        LoadBox.ShowLoad(context,getString(R.string.plz_wait));

        HideShow(View.VISIBLE, View.GONE);


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectGetCompanyInfo(Values.companyId);


                    if (aSoapObject == null || aSoapObject.toString() == null) {
                        if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null) {
                            noServerResponse();
                        } else
                            HideShow(View.GONE, View.VISIBLE);
                        return;
                    }

                    Company aCompany = new Gson().fromJson(aSoapObject.toString(), Company.class);


                    latLngFactroy = new LatLng(aCompany.Yposition, aCompany.Xposition);

                    if (location == null)
                        latLngHome = new LatLng(Values.lat, Values.lng);
                    else
                        latLngHome = new LatLng(location.getLatitude(), location.getLongitude());


                    setupView(aCompany.Yposition, aCompany.Xposition, aCompany.companyName, aCompany.address);


                    String url = getDirectionsUrl(latLngHome, latLngFactroy);

                    if (url == null)
                        return;

                    if (NetworkUtil.getConnectivityStatusString(MapActivity.context) == null) {
                        return;
                    }


                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }

    GoogleMap googleMap;
    LatLng latLngFactroy;
    LatLng latLngHome;

    public void setupView(final double latitude, final double longitude, final String companyName, final String address) {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null)
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                if (context == null)
                    return;


                latLngFactroy = new LatLng(latitude, longitude);

                if (location == null)
                    latLngHome = new LatLng(Values.lat, Values.lng);
                else
                    latLngHome = new LatLng(location.getLatitude(), location.getLongitude());


                //LatLngBounds.Builder builder = new LatLngBounds.Builder();

                //builder.include(latLngFactroy);
                // builder.include(latLngHome);


                //final LatLngBounds bounds = builder.build();
                //final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, (int) context.getResources().getDimension(R.dimen.map_padding));
                final CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLngHome, 18);

                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition arg0) {
                        googleMap.moveCamera(cu);
                        googleMap.setOnCameraChangeListener(null);
                    }
                });

                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(companyName));
                marker.showInfoWindow();

                System.out.println(".showInfoWindow()2" + latitude + " ," + longitude);


            }
        });


    }


    public void setupViewInit() {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null)
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                MapModel mapModel = (MapModel) new Serialize().readFromFile(context,
                        "aListKalaMap");

                System.out.println("aListKalaMap" + "------>>>>>" + mapModel);

                if (mapModel == null || mapModel.result == null)
                    return;

                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();

                // Traversing through all the routes
                for (int i = 0; i < mapModel.result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = mapModel.result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(getResources().getColor(R.color.color_primary));
                }

                float distance = 0;

                if (points != null) {

                    for (int i = 0; i < points.size(); i++) {

                        if ((i + 1) < points.size()) {

                            distance += calDistance(points.get(i), points.get(i + 1));
                        }
                    }

                    txtKm.setText(getString(R.string.dis_to_Comany) + " " + getString(R.string.app_name) +
                            " " + Math.round(distance) + " " + getString(R.string.km));

                }

                // Drawing polyline in the Google Map for the i-th route
                googleMap.clear();

                if (lineOptions == null)
                    return;

                googleMap.addPolyline(lineOptions);


                LatLng latLngHome = new LatLng(mapModel.latHome, mapModel.lngHome);

                CameraUpdate center =
                        CameraUpdateFactory.newLatLng(latLngHome);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);

                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);

                LatLng latLngFactroy = new LatLng(mapModel.latFactroy, mapModel.lngFactroy);

                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLngFactroy)
                        .title(context.getString(R.string.app_name)));
                marker.showInfoWindow();

                System.out.println(".showInfoWindow()1");


            }


        });


    }


    public void noServerResponse() {

        HideShow(View.GONE, View.VISIBLE);
        LoadBox.Hide();


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
                        HideShow(View.VISIBLE, View.GONE);
                        getData();

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
                if (aProgress != null) aProgress.setVisibility(pro);
                aLayout.setVisibility(layout);
            }
        });
    }


    private void addMarkers() {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(latLngFactroy)
                    .title("First Point"));
            googleMap.addMarker(new MarkerOptions().position(latLngHome)
                    .title("Second Point"));
        }
    }


    //###


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        if (origin == null || dest == null)
            return null;

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + "mode=" + MODE;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            LoadBox.Hide();
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                LoadBox.Hide();
                e.printStackTrace();
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(getResources().getColor(R.color.color_primary));
            }

            float distance = 0;

            if (points != null) {

                for (int i = 0; i < points.size(); i++) {

                    if ((i + 1) < points.size()) {

                        distance += calDistance(points.get(i), points.get(i + 1));
                    }
                }

                txtKm.setText(getString(R.string.dis_to_Comany) + " " + getString(R.string.app_name) +
                        " " + Math.round(distance) + " " + getString(R.string.km));

            }

            // Drawing polyline in the Google Map for the i-th route

            if (location == null)
                return;

            if (lineOptions == null)
                return;

            googleMap.clear();

            googleMap.addPolyline(lineOptions);


            latLngHome = new LatLng(location.getLatitude(), location.getLongitude());

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(latLngHome);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLngFactroy)
                    .title(context.getString(R.string.app_name)));
            marker.showInfoWindow();

            System.out.println(".showInfoWindow()1");

            MapModel mapModel = new MapModel();
            mapModel.latFactroy = latLngFactroy.latitude;
            mapModel.lngFactroy = latLngFactroy.longitude;

            mapModel.latHome = latLngHome.latitude;
            mapModel.lngHome = latLngHome.longitude;

            mapModel.result = result;

            LoadBox.Hide();


            new Serialize().saveToFile(context, mapModel,
                    "aListKalaMap");



        }
    }

    public float calDistance(LatLng l1, LatLng l2) {


        Location crntLocation = new Location("crntlocation");
        crntLocation.setLatitude(l1.latitude);
        crntLocation.setLongitude(l1.longitude);

        Location newLocation = new Location("newlocation");
        newLocation.setLatitude(l2.latitude);
        newLocation.setLongitude(l2.longitude);


        return crntLocation.distanceTo(newLocation) / 1000;
    }

    private boolean isGPSEnabled, isNetworkEnabled;
    private boolean canGetLocation;
    LocationManager locationManager;

    private static final long MIN_TIME_BW_UPDATES = 1;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 100;


    public void getMyLocation() {

        System.out.println(locationManager + "####");

        try {
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            this.canGetLocation = true;

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }


            if (isNetworkEnabled) {

                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, aLocationListener);

                if (locationManager != null)
                    onLocationChanged(locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

            }
            synchronized (this) {
                if (isGPSEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, aLocationListener);

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, aLocationListener);


                    if (locationManager != null)
                        onLocationChanged(locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER));

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLocationChanged(Location location) {

        MapActivity.this.location = location;

        synchronized (this) {

            if (NetworkUtil.getConnectivityStatusString(MapActivity.context) == null) {
                return;
            }


            String url = getDirectionsUrl(latLngHome, latLngFactroy);

            if (url == null)
                return;

            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);


        }
    }


    LocationListener aLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            if (location == null)
                return;

            MapActivity.this.location = location;


            LatLng latLngHome = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdateFactory.newLatLng(latLngHome);
            CameraUpdateFactory.zoomTo(17);


            System.out.println("LOCATION #### CHANGE########");

            if (NetworkUtil.getConnectivityStatusString(MapActivity.context) == null) {
                return;
            }


            synchronized (this) {


                String url = getDirectionsUrl(latLngHome, latLngFactroy);

                if (url == null)
                    return;


                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);


            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


}
