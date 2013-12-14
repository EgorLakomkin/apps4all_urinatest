package org.opencv.samples.tutorial1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;



import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class ByteArrayPost extends AsyncTask<String, String, String> {

	Activity activity;
	String req_url;
	byte[] array;
	
	public ByteArrayPost(Activity _activity, String _url, byte[] _array)
	{
		activity = _activity;
		req_url = _url;
		array = _array;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(req_url);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		ByteArrayBody bab = new ByteArrayBody(array,  "image/jpeg", "submission.jpg");

		reqEntity.addPart("image", bab);
		
		postRequest.setEntity(reqEntity);
		try {
			HttpResponse response = httpClient.execute(postRequest);
			Log.v("MYAPP","OK");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

	@Override
    protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}
	
}
