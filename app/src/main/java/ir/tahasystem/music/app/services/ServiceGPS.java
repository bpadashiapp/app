package ir.tahasystem.music.app.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.GpsModel;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.LoginManager;
import ir.tahasystem.music.app.MsgActivity;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.utils.SharedPref;

public class ServiceGPS extends Service {
    private static final String TAG = "GPS->>>";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000 * 1;
    private static final float LOCATION_DISTANCE = 1;

    private AlarmGPS aAlarm;
    public static final int interval = 1000 * 1;
    private ServiceGPS context;


    private class LocationListener implements android.location.LocationListener {

        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);


            Kala aKala = new Kala();
            aKala.appType = 1;
            aKala.DeviceId = 2;
            aKala.Yposition = String.valueOf(location.getLatitude());
            aKala.Xposition = String.valueOf(location.getLongitude());
            aKala.username = username;
            if (LoginHolder.getInstance().getLogin() == null || LoginHolder.getInstance().getLogin().user == null) {
                Log.e(TAG, "onLocationChanged: NULL USERNAME");
                aKala.companyName = null;
                return;
            } else {
                aKala.companyName = Values.companyName;
            }

            aKala.companyId = Values.companyId;

            if (ServicesSocketMap.mWebSocketClient != null && ServicesSocketMap.mWebSocketClient.isOpen()) {
                ServicesSocketMap.mWebSocketClient.send(new Gson().toJson(aKala));

                System.out.println("SEND+> " + new Gson().toJson(aKala));
            } else {
                System.out.println("WRNING SOCKET NOT OPEN ##################");
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);

            Notify(context.getString(R.string.app_name), context.getString(R.string.enable_gps_to_contniue), 10);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);

            Notify(context.getString(R.string.app_name), context.getString(R.string.gps_enabled), 10);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public String username;

    @Override
    public void onCreate() {

        this.context = this;

        username = LoginHolder.getInstance().getLoginId();

        try{

        aAlarm = new AlarmGPS();
        aAlarm.SetAlarm(this, interval);

        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("SERVICE GPS CREATE ##################->");

        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private int numMessages = 0;
    private NotificationManager mNotificationManager;

    public synchronized void Notify(Kala aKala) {

        if (android.os.Build.VERSION.SDK_INT < 11) {
            NotifyForyo(aKala.name, aKala.text, aKala.name, 100);
        } else {
            NotifyJelly(aKala.name, aKala.text, aKala.name, 100);
        }

    }

    public synchronized void Notify(String name, String description, int id) {

        if (android.os.Build.VERSION.SDK_INT < 11) {
            NotifyForyo(name, description, name, id);
        } else {
            NotifyJelly(name, description, name, id);
        }

    }


    public synchronized void NotifyJelly(String title, String text, String ticker, int id) {
        Log.i("Start", "notification");

		/* Invoking the default notification service */
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setContentTitle(Html.fromHtml(title));
        mBuilder.setContentText(Html.fromHtml(text));
        mBuilder.setTicker(Html.fromHtml(ticker));
        mBuilder.setSmallIcon(R.drawable.ic_launcher);

		/* Increase notification number every time a new notification arrives */
        // mBuilder.setNumber(++numMessages);

        mBuilder.setAutoCancel(true);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (!SharedPref.readBool(context, "notifysilent") && !SharedPref.readBool(context, "notifynotihng"))
            mBuilder.setSound(uri);

        Intent resultIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);



		/* Creates an explicit intent for an Activity in your app */
        //Intent resultIntent = new Intent(context, MsgActivity.class);

        //resultIntent.putExtra("keyz", "has");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MsgActivity.class);

		/* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        int requestID = (int) System.currentTimeMillis();

        // PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (!SharedPref.readBool(context, "notifysilent"))
            mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_VIBRATE | Notification.FLAG_AUTO_CANCEL);
        else
            mBuilder.setDefaults(Notification.FLAG_AUTO_CANCEL);

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		/* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(id, mBuilder.build());
    }

    public synchronized void NotifyForyo(String title, String text, String ticker, int id) {

        Intent resultIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = null;

        if (!SharedPref.readBool(context, "notifysilent")) {
            mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher).setContentTitle(Html.fromHtml(title))
                    .setContentText(Html.fromHtml(text)).setContentIntent(resultPendingIntent)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL);

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(uri);

        } else {
            mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher).setContentTitle(Html.fromHtml(title))
                    .setContentText(Html.fromHtml(text)).setContentIntent(resultPendingIntent)
                    .setDefaults(Notification.FLAG_AUTO_CANCEL);
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(id, mBuilder.build());
    }


}