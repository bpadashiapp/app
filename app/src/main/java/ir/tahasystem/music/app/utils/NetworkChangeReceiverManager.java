package ir.tahasystem.music.app.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ir.tahasystem.music.app.services.ServicesSocketManager;

public class NetworkChangeReceiverManager extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil2.getConnectivityStatusString(context);
        Log.e("network reciever", "Sulod sa network reciever");
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if(status== NetworkUtil2.NETWORK_STATUS_NOT_CONNECTED){
                //new ForceExitPause(context).execute();
            }else{
               // new ResumeForceExitPause(context).execute();
                context.startService(new Intent(context, ServicesSocketManager.class));
            }

       }
    }






}