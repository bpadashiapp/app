package ir.tahasystem.music.app.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.VersionModel;
import ir.tahasystem.music.app.utils.Serialize;
import ir.tahasystem.music.app.utils.SharedPref;


public class UpdatePanel {

    private VersionModel versionModel;
    private Context context;

    public UpdatePanel(Context context, VersionModel versionModel) {

        this.context = context;
        this.versionModel = versionModel;

        new Serialize().saveToFile(context, versionModel, "VersionModel");
    }


    public void update() {

        if (versionModel == null || context == null)
            return;

        if (!isNewVersion() || !isDownloadedAllowed())
            return;

        if (isApkDownloaded() != null) {
            if (MainActivity.context != null)
                MainActivity.context.notifyAvaliableUpdate(versionModel);

        } else
            downloadApk();

    }

    public int getVersionCode() {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionCode;
    }

    public boolean isNewVersion() {
        return versionModel.apkVer > this.getVersionCode();
    }

    public boolean isDownloadedAllowed() {
        if (!versionModel.apkIsCan)
            return true;
        return SharedPref.readBoolTrue(context, "notifyupdate");
    }

    public String isApkDownloaded() {
        return SharedPref.readNull(context, "app" + versionModel.apkVer);
    }

    private void downloadApk() {
        AppDownloadManager.getInstance().init(context, versionModel);
    }


}
