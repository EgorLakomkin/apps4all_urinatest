package org.opencv.samples.tutorial1;
import org.opencv.android.CameraBridgeViewBase;

import android.app.Activity;
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
        mWebView.loadUrl("http://21dfe7b3.ngrok.com/last_analysis?user_id=M5mMzrLAcU");
        Button analys = (Button)findViewById(R.id.analys);
        Button stat = (Button)findViewById(R.id.stat);
        OnClickListener analysList = new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    Intent intent = new Intent(Activity1.this, Tutorial1Activity.class);
			    startActivity(intent);
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
		        mWebView.loadUrl("http://21dfe7b3.ngrok.com/statistics?user_id=M5mMzrLAcU");
		        
			}
		};
		stat.setOnClickListener(statsList);
   }

}
