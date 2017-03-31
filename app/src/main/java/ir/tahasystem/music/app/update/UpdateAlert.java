package ir.tahasystem.music.app.update;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import java.io.File;

import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.VersionModel;
import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.utils.FontUtils;
import ir.tahasystem.music.app.utils.SharedPref;


public class UpdateAlert {

    private Context context;
    VersionModel versionModel;

    public UpdateAlert(final Context context, VersionModel versionModel) {

        this.versionModel = versionModel;
        this.context = context;

    }

    public void show(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "irfontnumbold.ttf");

        builder.setTitle(FontUtils.typeface(typeface,context.getString(R.string.update)));
        builder.setMessage(FontUtils.typeface(typeface,context.getString(R.string.updates)));
        builder.setCancelable(versionModel.apkIsCan);

        builder.setPositiveButton(context.getString(R.string.update), null);

        String cancelStr=context.getString(R.string.cancel);
        if (!versionModel.apkIsCan)
            context.getString(R.string.exit_app);

        builder.setNegativeButton(cancelStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                if (!versionModel.apkIsCan)
                    MainActivity.context.finish();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(context, SharedPref.readNull(context, "app" + versionModel.apkVer));
            }
        });


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
