package ir.tahasystem.music.app.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;

import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.ModelHolderService;
import ir.tahasystem.music.app.Model.NotifyModel;
import ir.tahasystem.music.app.Model.NotifyModelNew;
import ir.tahasystem.music.app.Model.VersionModel;
import ir.tahasystem.music.app.NotifyActivity;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.update.UpdatePanel;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Serialize;
import ir.tahasystem.music.app.utils.SharedPref;


public class Services extends Service {

    int mStartMode;
    IBinder mBinder;
    boolean mAllowRebind;
    private Alarm aAlarm;
    private static Context context;
    public static int interval;

    UpdatePanel updatePanel;


    @Override
    public void onCreate() {

        this.context = this;

        interval=1000 * 60 *SharedPref.readIntNotify(context,"notifytime");

        try{

        aAlarm = new Alarm();
        aAlarm.SetAlarm(this, interval);

        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("SERVICE CREATE->");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (SharedPref.readBoolTrue(context, "notifyfull")) {
            System.out.println("########### SERVICE NOTIFY START ###########");
            System.out.println("########### " + " Interval " + interval + " milisec ###########");
            LoginHolder.getInstance().init(context);
            getDataNotify();
        }


        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("SERVICE ########### NOTIFY DESTROYED");

    }


    public synchronized void getDataNotify() {

        if (NetworkUtil.getConnectivityStatusString(context) != null && LoginHolder.getInstance().getLogin() != null)
            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        System.out.println("NOTIFY CHECK");

                        ConnectionPool aConnectionPool = new ConnectionPool();

                        SoapPrimitive aSoapObject = null;
                        if (NetworkUtil.getConnectivityStatusString(context) != null)
                            aSoapObject = aConnectionPool.ConnectGetNotifications();


                        if (aSoapObject == null || aSoapObject.toString() == null) {

                            return;
                        }

                        NotifyModelNew aNotifyModelNew;

                        try {

                            aNotifyModelNew = new Gson().fromJson(aSoapObject.toString(), NotifyModelNew.class);

                        }catch (Exception e){
                            e.printStackTrace();
                            return;
                        }


                        if (aNotifyModelNew.neworder > 0 && LoginHolder.getInstance().getLogin().hasRole) {

                            Kala aKala = new Kala();
                            aKala.isOrder = true;
                            aKala.isOrderSeen = false;
                            aKala.id = 0;
                            aKala.neworder = aNotifyModelNew.neworder;
                            aKala.name = context.getString(R.string.new_orders);
                            aKala.description = context.getString(R.string.you_have) + " " + aNotifyModelNew.neworder + " " + context.getString(R.string.for_submit);

                            Kala bKala = ModelHolderService.getInstance().getKalaOrder(context);
                            if (bKala==null ||aNotifyModelNew.neworder != bKala.neworder || !bKala.isOrderSeen)
                                Notify(aKala, 10000);


                            ModelHolderService.getInstance().setKalaOrder(context, aKala);
                        }


                        NotifyModel aNotifyModel = new NotifyModel();

                        VersionModel aVersionModel = new VersionModel();
                        aVersionModel.apkVer = aNotifyModelNew.version;
                        aVersionModel.apkName = Values.companyId + ".apk";
                        aVersionModel.apkFile = Values.companyId + ".apk";
                        aVersionModel.apkDir = "/CAPK/";
                        aVersionModel.apkIsCan = true;
                        aNotifyModel.ver = aVersionModel;


                        aNotifyModel.notify = aNotifyModelNew.news;


                        if (aNotifyModel == null)
                            return;

                        new Serialize().saveToFile(context, aNotifyModel, "aNotifyModelNew");

                        List<Kala> newKalaList = aNotifyModel.getNotify();
                        if (newKalaList == null)
                            return;

                        List<Kala> oldKalaList = ModelHolderService.getInstance().getKalaList(context);

                        List<Kala> tmpKalaList = new ArrayList<Kala>();

                        if (oldKalaList == null) {

                            oldKalaList = new ArrayList<Kala>();
                            tmpKalaList = newKalaList;

                        } else {

                            for (Kala newKala : newKalaList) {
                                Kala hasModel = newKala;
                                for (Kala oldKala : oldKalaList) {
                                    if (oldKala.id == newKala.id)
                                        hasModel = null;
                                }
                                if (hasModel != null)
                                    tmpKalaList.add(hasModel);
                            }
                        }


                        for (Kala tmpKala : tmpKalaList)
                            Notify(tmpKala);


                        oldKalaList.addAll(tmpKalaList);

                        ModelHolderService.getInstance().setKalaList(context, oldKalaList);


                        if (tmpKalaList.size() > 0 && MainActivity.context != null)
                            MainActivity.context.updateNotify();

                        if (tmpKalaList.size() > 0 && NotifyActivity.context != null &&
                                NotifyActivity.context.fragmentNotify.isInit)
                            NotifyActivity.context.fragmentNotify.getNotify();


                        // #check for Update
                         updatePanel = new UpdatePanel(context, aNotifyModel.getVer());
                         updatePanel.update();


                        // #Response notify
                        for (Kala tmpKala : tmpKalaList)
                            aConnectionPool.ConnectNotificationsShown(tmpKala.id);


                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } finally {

                    }

                }
            }).start();

    }

    public int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str = "" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }


    private int numMessages = 0;
    private NotificationManager mNotificationManager;

    public synchronized void Notify(Kala aKala) {

        if (android.os.Build.VERSION.SDK_INT < 11) {
            NotifyForyo(aKala.name, aKala.description, aKala.name, aKala.id);
        } else {
            NotifyJelly(aKala.name, aKala.description, aKala.name, aKala.id);
        }

    }

    public synchronized void Notify(Kala aKala, int id) {

        if (android.os.Build.VERSION.SDK_INT < 11) {
            NotifyForyo(aKala.name, aKala.description, aKala.name, id);
        } else {
            NotifyJelly(aKala.name, aKala.description, aKala.name, id);
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

		/* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(context, NotifyActivity.class);

        resultIntent.putExtra("keyz", "has");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotifyActivity.class);

		/* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        int requestID = (int) System.currentTimeMillis();

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT);

        if (!SharedPref.readBool(context, "notifynotihng"))
            mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_VIBRATE | Notification.FLAG_AUTO_CANCEL);
        else
            mBuilder.setDefaults(Notification.FLAG_AUTO_CANCEL);

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		/* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(id, mBuilder.build());
    }

    public synchronized void NotifyForyo(String title, String text, String ticker, int id) {

        Intent resultIntent = new Intent(context, NotifyActivity.class);
        resultIntent.putExtra("key", "has");

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

    //##


}