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

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.ModelHolderService;
import ir.tahasystem.music.app.MsgActivityOrder;
import ir.tahasystem.music.app.OrderActivity;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.SharedPref;


public class ServicesSocketManager extends Service {

    int mStartMode;
    IBinder mBinder;
    boolean mAllowRebind;
    private static Context context;

    AlarmSocketManager aAlarm;


    @Override
    public void onCreate() {

        context = this;

        LoginHolder.getInstance().init(context);

        int interval = 1000 * 3;

        try{

        aAlarm = new AlarmSocketManager();
        aAlarm.SetAlarm(this, interval);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("SERVICE SOCKET ########### MSG %%%%%%%%%%%%%%%%%%");

        runSocket();

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
        System.out.println("SERVICE SOCKET ########### NOTIFY DESTROYED");

    }


    public static WebSocketClient mWebSocketClient;


    public synchronized void runSocket() {

        if (mWebSocketClient != null && (mWebSocketClient.isOpen() || mWebSocketClient.isConnecting())) {
            //mWebSocketClient.send(param);
            return;
        }


        LoginHolder.getInstance().init(context);

        if (NetworkUtil.getConnectivityStatusString(context) == null)
            return;



        if (NetworkUtil.getConnectivityStatusString(context) != null)
            new Thread(new Runnable() {

                @Override
                public void run() {

                    // if (NetworkUtil.getConnectivityStatusString(context) != null)


                    URI uri;
                    try {
                        uri = new URI("ws://91.98.102.126:8081/services2/getways2");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        return;
                    }

                    mWebSocketClient = new WebSocketClient(uri, 20000) {

                        @Override
                        public void onWebsocketPing(WebSocket conn, Framedata f) {
                            FramedataImpl1 resp = new FramedataImpl1(f);
                            resp.setOptcode(Framedata.Opcode.PONG);
                            conn.sendFrame(resp);

                            System.out.println("PINGGGGGGGGGGGGGGGGGGGG");
                        }

                        @Override
                        public void onOpen(ServerHandshake serverHandshake) {
                            System.out.println("Websocket Opened");

                            if(!LoginHolder.getInstance().getLogin().hasRole){
                                System.out.println("Websocket : no has Role");
                                return;
                            }

                            Kala aModel = new Kala();
                            aModel.uid = UUID.randomUUID().toString();
                            aModel.sender = LoginHolder.getInstance().getLogin().uid + "-" + Values.companyId;
                            aModel.receiver = "";
                            aModel.date = "";
                            aModel.text = ServicesSocketManager.this.getString(R.string.connected);
                            aModel.command = "just";
                            aModel.isRec = 0;
                            aModel.name = ServicesSocketManager.this.getString(R.string.manager_part)+" "+ LoginHolder.getInstance().getLogin().uid;
                            aModel.isServer=2;
                            aModel.numServer= Integer.parseInt(LoginHolder.getInstance().getLogin().uid.substring(0,1));
                            aModel.companyId= Values.companyId;

                            mWebSocketClient.send(new Gson().toJson(aModel).toString());


                           /* if (MsgActivityOrder.context != null &&
                                    MsgActivityOrder.context.fragmentMsg != null &&
                                    MsgActivityOrder.context.fragmentMsg.recyclerAdapterH != null)
                                MsgActivityOrder.context.fragmentMsg.recyclerAdapterH.clearItem();*/

                            aModel = new Kala();
                            aModel.uid = UUID.randomUUID().toString();
                            aModel.sender = LoginHolder.getInstance().getLogin().uid + "-" + Values.companyId;
                            aModel.receiver ="";
                            aModel.date = "";
                            aModel.text = "";
                            aModel.command = "list";
                            aModel.isRec = 0;
                            aModel.name = ServicesSocketManager.this.getString(R.string.manager_part)+" "+ LoginHolder.getInstance().getLogin().uid;
                            aModel.isServer=2;
                            aModel.numServer= Integer.parseInt(LoginHolder.getInstance().getLogin().uid.substring(0,1));
                            aModel.companyId= Values.companyId;
                            mWebSocketClient.send(new Gson().toJson(aModel));
                        }


                        @Override
                        public void onMessage(String message) {
                            System.out.println("Websocket : " + message);

                            if(!LoginHolder.getInstance().getLogin().hasRole){
                                System.out.println("Websocket : no has Role");
                                return;
                            }


                            Kala aModel = new Gson().fromJson(message, Kala.class);





                            if (aModel.command.equals("addlist")) {
                                if (MsgActivityOrder.context != null &&
                                        MsgActivityOrder.context.fragmentMsg.isInit)
                                    MsgActivityOrder.context.fragmentMsg.setupViewH(aModel);

                                return;
                            }


                            //DELETE
                            aModel.command = "delete";
                            mWebSocketClient.send(new Gson().toJson(aModel));

                            if (aModel.isServer == 2) {
                                return;
                            }

                            ModelHolderService.getInstance().setKalaListChatManager(context, aModel);

                            if (OrderActivity.context != null)
                                OrderActivity.context.updateMsg();


                            if (MsgActivityOrder.context != null &&
                                    MsgActivityOrder.context.fragmentMsg.isInit)
                                MsgActivityOrder.context.fragmentMsg.getNotify();

                            Notify(aModel);


                        }

                        @Override
                        public void onClose(int i, String s, boolean b) {
                            System.out.println("Websocket" + "Closed " + s);
                            mWebSocketClient = null;

                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            System.out.println("Websocket" + "Error " + e.getMessage());
                            mWebSocketClient = null;
                        }
                    };
                    mWebSocketClient.connect();


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
            NotifyForyo(aKala.sender, aKala.text, aKala.text, 100);
        } else {
            NotifyJelly(aKala.sender, aKala.text, aKala.text, 100);
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

        System.out.println(SharedPref.readBool(context, "notifysilent")+"-"+ SharedPref.readBool(context, "notifynotihng"));


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (!SharedPref.readBool(context, "notifysilent") && !SharedPref.readBool(context, "notifynotihng"))
         mBuilder.setSound(uri);


        Intent resultIntent = new Intent(getApplicationContext(),
               MsgActivityOrder.class);



		/* Creates an explicit intent for an Activity in your app */
        //Intent resultIntent = new Intent(context, MsgActivityOrder.class);

       // resultIntent.putExtra("keyz", "has");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MsgActivityOrder.class);

		/* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        int requestID = (int) System.currentTimeMillis();

       // PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(),0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

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

        Intent resultIntent = new Intent(context, MsgActivityOrder.class);
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