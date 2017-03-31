package ir.tahasystem.music.app.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by root on 1/2/17.
 */

public class MapIcon extends Drawable {

    Context context;
    String text;
    int imageRes;

    Paint paint;

    public MapIcon(Context context, String text, int imageRes) {

        this.setBounds(0, 0, this.getIntrinsicWidth(), this.getIntrinsicHeight());

        this.context = context;
        this.text=text;
        this.imageRes=imageRes;
    }

    @Override
    public int getIntrinsicWidth() {
        return 500;
    }

    @Override
    public int getIntrinsicHeight() {
        return 500;

    }

    @Override
    public void draw(Canvas canvas) {


        paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setTextSize(13f);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setShadowLayer(6f, 0, 0, Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);

        Rect bounds = new Rect();
        if(text!=null && text.length()>0) {
            paint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawRect(0, 0, bounds.width()+10, bounds.height()+10, paint);


            paint.setColor(Color.WHITE);
            canvas.drawText(text, 5, 5, paint);

            System.out.println("--->DRAW0");

            Resources res = context.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, imageRes);
            canvas.drawBitmap(bitmap, (bounds.width()+10+bitmap.getWidth())/2, bounds.height()+10, null);
        }else{
            System.out.println("--->DRAW");
        }



    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

}
