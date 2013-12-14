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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

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

		StringBuilder parameters = new StringBuilder();

		parameters.append("image=");
		try {
			parameters.append(URLEncoder.encode(new String(array),"UTF-8"));
		} catch (UnsupportedEncodingException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
			Log.e("MYAPP","ex",e4);
		}
		URL url = null;
		try {
			url = new URL(req_url);
		} catch (MalformedURLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			Log.e("MYAPP","ex",e3);
		} 
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset","UTF-8");
			connection.setRequestProperty("Content-Length",Integer.toString(parameters.toString().getBytes().length));
			
			DataOutputStream wr = null;
			
			wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(parameters.toString());
			wr.flush();
			wr.close();
			connection.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("MYAPP","ex",e);
			e.printStackTrace();
		}	
					
		
		return null;
	}

	@Override
    protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}
	
}
