package ir.tahasystem.music.app.DialogBox;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import ir.onlinefood.app.factory.R;

public class LoadBox {

    public static ProgressDialog progressbar;
    public static Toast toast;
    private static Context context;
    private static int proTotal = 1;
    private static int pro = 1;

    public static synchronized void ShowLoadPro(Context context, int max, String msg) {

        LoadBox.context = context;

        proTotal = 0;
        pro = 0;

        LoadBox.context = context;
        progressbar = new ProgressDialog(context, R.style.MyDialogThemeProgress);
        progressbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressbar.setMessage(msg);
        progressbar.setMax(max);
        progressbar.setCancelable(false);

        if (progressbar != null && !progressbar.isShowing())
            progressbar.show();
    }


    public static void setProTotal(int i) {

        LoadBox.proTotal = LoadBox.proTotal + i;

    }

    public static synchronized void setPro() {

        System.out.println(pro + "/######/" + proTotal + "//" + (int) ((pro * 100) / proTotal));

        if (context == null || progressbar == null || !progressbar.isShowing())
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                pro++;
                if (progressbar != null)
                    progressbar.setProgress((int) ((pro * 100) / proTotal));

            }
        });

    }

    public static void setPro(final String msg) {

        if (context == null || progressbar == null || !progressbar.isShowing())
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                progressbar.setMessage(msg);

            }
        });

    }

    public static void ShowLoad(Context context, String text) {
        LoadBox.context = context;
        progressbar = new ProgressDialog(context);
        progressbar.setCancelable(true);
        progressbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressbar.setMessage(text);

        if (progressbar != null && !progressbar.isShowing())
            progressbar.show();
    }


    public static void ShowLoadStay(Context context, String text) {
        LoadBox.context = context;
        progressbar = new ProgressDialog(context);
        progressbar.setCancelable(false);
        progressbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressbar.setMessage(text);

        if (progressbar != null && !progressbar.isShowing())
            progressbar.show();
    }

    public static boolean isShow() {
        return progressbar.isShowing();
    }

    public static void Hide() {

        if (context == null || progressbar == null || !progressbar.isShowing())
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (progressbar != null) {
                    progressbar.dismiss();
                }

            }
        });


    }


}
