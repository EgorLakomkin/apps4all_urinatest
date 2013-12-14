package org.opencv.samples.tutorial1;
import org.opencv.android.CameraBridgeViewBase;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class Activity1 extends Activity {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial2_surface_view);
        WebView mWebView = (WebView) findViewById(R.id.webView1);
        
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.loadUrl(Constants.servAddress+"/last_analysis?user_id=M5mMzrLAcU");
        Button analys = (Button)findViewById(R.id.analys);
        Button stat = (Button)findViewById(R.id.stat);
        OnClickListener analysList = new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    Intent intent = new Intent(Activity1.this, Tutorial1Activity.class);
			    startActivityForResult(intent, Constants.ACTIVITY_RESULT_ID );
			}
		};
		analys.setOnClickListener(analysList);
        OnClickListener statsList = new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WebView mWebView = (WebView) findViewById(R.id.webView1);
		        
		        WebSettings settings = mWebView.getSettings();
		        settings.setJavaScriptEnabled(true);
		        settings.setDefaultTextEncodingName("utf-8");
		        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		        mWebView.loadUrl(Constants.servAddress+"/statistics?user_id=M5mMzrLAcU");
		        
			}
		};
		stat.setOnClickListener(statsList);
   }

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	  if (requestCode == Constants.ACTIVITY_RESULT_ID) {
		 if(resultCode == Constants.RESULT_OK){
			  WebView mWebView = (WebView) findViewById(R.id.webView1);
		        
		      mWebView.loadUrl(Constants.servAddress+"/last_analysis?user_id=M5mMzrLAcU");
			  Log.d("UrinaTest", "RESULT_OK");
	     }
	    
		 
	  }
	}
	
}
