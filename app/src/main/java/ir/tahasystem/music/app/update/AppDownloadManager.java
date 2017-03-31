package ir.tahasystem.music.app.update;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.NotifyModel;
import ir.tahasystem.music.app.Model.VersionModel;
import ir.tahasystem.music.app.utils.Serialize;
import ir.tahasystem.music.app.utils.SharedPref;


public class AppDownloadManager {

    private static final String DOMIN ="http://onlinepakhsh.com" ;

    private AppDownloadManager() {
    }

    private static class SingletonHelper {
        private static final AppDownloadManager INSTANCE = new AppDownloadManager();
    }

    public static AppDownloadManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private boolean isRun;
    private VersionModel versionModel;
    private long downloadID;
    private DownloadManager downloadManager;


    private IntentFilter downloadCompleteIntentFilter = new IntentFilter(
            DownloadManager.ACTION_DOWNLOAD_COMPLETE);


    public void init(Context context, VersionModel versionModel) {

        if (isRun)
            return;

        this.versionModel = versionModel;

        context.registerReceiver(downloadCompleteReceiver, downloadCompleteIntentFilter);

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(DOMIN + versionModel.apkDir + versionModel.apkFile));

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(getPath(context),context.getString(R.string.app_name) );
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(true);


        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);

        isRun=true;

        System.out.println("DOWNLOAD-> "+DOMIN + versionModel.apkDir + versionModel.apkFile + "DIR> " + getPath(context) + "FILE> " + versionModel.apkFile);


    }


    public BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {


                Query query = new Query();
                query.setFilterById(downloadID);
                Cursor c = downloadManager.query(query);

                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);

                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                        // #COMPLETE
                        System.out.println("DOWNLOAD COMPLETE->");
                        isRun = false;

                        String filePath = c.getString(c
                                .getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

                        File file=new File(filePath);
                        if(file.length()<1410312){
                            System.out.println("DOWNLOAD ERROR-> "+file.length() +"INVALID");
                            return;
                        }

                        System.out.println("DOWNLOAD FILE PATH-> "+filePath);
                        System.out.println("DOWNLOAD FILE SIZE-> "+file.length());

                        if (versionModel == null)
                            versionModel = (VersionModel) new Serialize().readFromFile(context,
                                    "VersionModel");

                        if (versionModel != null)
                            SharedPref.write(context, "app" + versionModel.apkVer, filePath);


                        if (context == null)
                            return;

                        unRegDownload(context);

                        //##
                        NotifyModel aNotifyModel = (NotifyModel) new Serialize().readFromFile(context,
                                "aNotifyModelNew");

                        if (aNotifyModel == null || aNotifyModel.getVer() == null)
                            return;


                        UpdatePanel updatePanel = new UpdatePanel(context, aNotifyModel.getVer());
                        updatePanel.update();


                    }
                }
            }


        }
    };


    public void unRegDownload(Context context) {
        try {
            if (downloadManager != null)
                context.unregisterReceiver(downloadCompleteReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DOWNLOAD DESTROYED->");
    }

    public String getPath(Context context) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();

        File fileTarget = new File(root + "/Download");

        if (!fileTarget.exists())
            fileTarget.mkdirs();

        return "/Download";

    }

    protected void openFile(Context context, String filePath) {

        if(filePath==null)
            return;

        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.fromFile(new File(filePath)),
                "application/vnd.android.package-archive");
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context != null)
            ((Activity) context).startActivityForResult(install,
                    1009);



    }


}
