package ir.tahasystem.music.app.custom;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LiveWebView extends WebView {

	Context mContext;
	public LiveWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		setWebViewClient();
	}
	
	public LiveWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		setWebViewClient();
	}

	@Override
	public boolean onCheckIsTextEditor() {
		return true; 
	}
	
	boolean setWebViewClient()
	{
		
		setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
		setFocusable(true);
    	setFocusableInTouchMode(true);
    	requestFocus(View.FOCUS_DOWN);
    	
		WebSettings webSettings = getSettings();
		webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
		//addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
		

        setOnTouchListener(new OnTouchListener() {

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
    	
		
		this.setWebViewClient(new WebViewClient()
		{
            ProgressDialog dialog = new ProgressDialog(mContext);
           
			   @Override  
			   public boolean shouldOverrideUrlLoading(WebView view, String url)  
			   {  
				System.out.println("+-------------------------------");
				System.out.println("|	shouldOverrideUrlLoading : url:  "+ url);
				System.out.println("+-------------------------------");
					loadUrl(url);
					
					return true;
			    }  
			   
			   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				     Toast.makeText(mContext, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			   }
			   
			   public void onPageStarted(WebView view, String url, Bitmap favicon)
			   {
				   if(dialog!=null)
				   {
			            dialog.setMessage("Loading...");
			            dialog.setIndeterminate(true);
			            dialog.setCancelable(true);
			            dialog.show();
				   }

			   }
			   
			   public void onPageFinished(WebView view, String url)
			   {
				   if(dialog!=null)
				   {
					   dialog.cancel();
				   }
			   }
		});  
		
		this.setWebChromeClient(new WebChromeClient() {
			   public void onProgressChanged(WebView view, int progress) {
			     // Activities and WebViews measure progress with different scales.
			     // The progress meter will automatically disappear when we reach 100%
			   }
			   
			   @Override
		        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
	   				System.out.println("+-------------------------------");
	   				System.out.println("|WebChromeClient onJsAlert	" + message);
	   				System.out.println("+-------------------------------");
		            result.confirm();
		            return true;
		        }
		});
		
		
		
		loadUrl("http://www.google.com");
		
		return true;
	}


}