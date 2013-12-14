package org.opencv.samples.tutorial1;

import java.io.IOException;

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

public class ByteArrayPost extends AsyncTask<String, String, String> {

	Activity activity;
	String url;
	byte[] array;
	
	public ByteArrayPost(Activity _activity, String _url, byte[] _array)
	{
		activity = _activity;
		url = _url;
		array = _array;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new ByteArrayEntity(array));           
		try {
			HttpResponse response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
    protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}
	
}
