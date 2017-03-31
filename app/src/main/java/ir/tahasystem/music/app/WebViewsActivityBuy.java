package ir.tahasystem.music.app;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import ir.onlinefood.app.factory.R;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.custom.LiveWebView;


public class WebViewsActivityBuy extends AppCompatActivity {

    TextView addressTxt;
    private WebView web;
    private Toolbar toolbar;

    public static WebViewsActivityBuy context;

    ProgressBarCircularIndeterminate aProgress;
    private boolean buyDone = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.context = this;

        setContentView(R.layout.activity_webview);

        aProgress = (ProgressBarCircularIndeterminate) findViewById(R.id.list_load);
        aProgress.setVisibility(View.VISIBLE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setTitle("");
        TextView atxt = (TextView) toolbar.findViewById(R.id.toolbar_title);
        atxt.setText(getString(R.string.buy));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        addressTxt = (TextView) this.findViewById(R.id.web_view_address);
        addressTxt.setVisibility(View.GONE);

        String url;

        url = getIntent().getStringExtra("url");

        web = new LiveWebView(this);

        web.setInitialScale(100);
        web.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        // web.getSettings().setLoadWithOverviewMode(true) ;
        web.getSettings().setUseWideViewPort(true);

        web.requestFocus(View.FOCUS_DOWN);
        web.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

        web.requestFocusFromTouch();
        web.getSettings().setUseWideViewPort(true);

        web.getSettings().setJavaScriptEnabled(true);

        web.setWebViewClient(new Callback());

        web.getSettings().setBuiltInZoomControls(true);
        web.setVerticalScrollBarEnabled(true);
        web.setHorizontalScrollBarEnabled(true);

        web.freeMemory();

        int frac;

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Configuration config = getResources().getConfiguration();

        LinearLayout aLinearLayout = (LinearLayout) findViewById(R.id.web_view);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(false);
        aLinearLayout.addView(web);
        CookieManager.getInstance().setAcceptCookie(true);


        System.out.println("GOTO->" + url);

        web.loadUrl(url);
    }

    private class Callback extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

        public void onPageStarted(WebView view, final String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);

            System.out.println("-->" + url);

            if (url == null)
                return;

            if (url.toLowerCase().contains("onlinepakhsh.com/wservices/ResultOk".toLowerCase())) {

                System.out.println("-#->" + url);

                done(url);

            } else if (url.toLowerCase().contains("onlinepakhsh.com/wservices/ResultFail".toLowerCase())) {

                finish();

            }


            web.setInitialScale(100);

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

            System.out.println("-->" + url);

            if (url == null)
                return;

            if (url.toLowerCase().contains("onlinepakhsh.com/wservices/ResultOk".toLowerCase())) {

                System.out.println("-#->" + url);

                done(url);

            } else if (url.toLowerCase().contains("onlinepakhsh.com/wservices/ResultFail".toLowerCase())) {

                finish();

            }

            aProgress.setVisibility(View.GONE);

        }

        @Override
        public void onLoadResource(WebView view, String urlz) {

            final String url = view.getUrl();

            System.out.println("-->" + urlz);

            if (url == null)
                return;

            if (url.toLowerCase().contains("onlinepakhsh.com/wservices/ResultOk".toLowerCase())) {


                System.out.println("-#->" + url);

                done(url);

            } else if (url.toLowerCase().contains("onlinepakhsh.com/wservices/ResultFail".toLowerCase())) {

                finish();

            }

            System.out.println("-->" + url + "-->" + urlz);

        }
    }

    public int PxToDpi(float dipValue) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
        return px;

    }

    private void setVisiblez(final MenuItem mItem, final boolean b) {

        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                mItem.setVisible(b);

            }
        });

    }

    @Override
    protected void onPause() {
        web.onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        web.onResume();
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        web.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {

        ModelHolder.getInstance().clearBuying();

        if (buyDone) {
            // Intent aIntent = new Intent(this, CoponActivity.class);
            // startActivity(aIntent);
            // BasketActivity.context.finish();
        } //else {

        //Intent aIntent = new Intent(this, BasketActivity.class);
        //startActivity(aIntent);
        //}

        web.destroy();
        web = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_item, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {


        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        } else if (menuItem.getItemId() == R.id.action_back) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    boolean isShow = false;

    public synchronized void done(String url) {

        if (isShow)
            return;

        isShow = true;

        buyDone = true;
        ModelHolder.getInstance().buyDone();
        finish();


    }


}
