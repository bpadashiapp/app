package ir.tahasystem.music.app.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmSocketManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("ALARAM SOCKET->FIREED!!!");

        if(ServicesSocketManager.mWebSocketClient==null || ServicesSocketManager.mWebSocketClient.isClosed()) {
            Intent aIntent = new Intent(context, ServicesSocket.class);
            context.startService(aIntent);
        }

    }

    public void SetAlarm(Context context, int interval) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmSocketManager.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                interval, pi); // Millisec * Second * Minute

        System.out.println("ALARAM->SET");
    }

    public void CancelAlarm(Context context) {
        System.out.println("ALARAM->CANCEDLD");
        Intent intent = new Intent(context, AlarmSocketManager.class);
        PendingIntent sender = PendingIntent
                .getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}