package ir.tahasystem.music.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import ir.tahasystem.music.app.findMap.Fragment1;


public class ServiceReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                System.out.println("incomingNumber : " + incomingNumber);

                if(incomingNumber.contains("+98719712009")) {


                    System.out.println("incomingNumber GETTT");
                    if(Fragment1.context!=null)
                        Fragment1.context.getDataF();

                }

            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

}