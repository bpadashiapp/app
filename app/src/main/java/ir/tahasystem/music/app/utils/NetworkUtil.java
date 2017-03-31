package ir.tahasystem.music.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ir.tahasystem.music.app.MyApplication;

public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public synchronized static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public synchronized static String getConnectivityStatusString(Context context) {


        int conn = NetworkUtil.getConnectivityStatus(MyApplication.getContext());


        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI)
            status = "Not connected to Internet";

        if (conn == NetworkUtil.TYPE_MOBILE)
            status = "Not connected to Internet";

        return status;


    }
}