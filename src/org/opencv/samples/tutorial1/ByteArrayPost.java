package org.opencv.samples.tutorial1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;



import android.app.Activity;
import android.os.AsyncTask;
import android.util.Base64;
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

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(req_url);
		MultipartEntity multiPart = new MultipartEntity();
		
		multiPart.addPart("file", new ByteArrayBody(array, "image.jpg"));
		post.setEntity(multiPart);
		
		try {
			HttpResponse httpResponse = client.execute(post);
			HttpEntity entity = httpResponse.getEntity();
			InputStream is = entity.getContent();
			String res = IOUtil.convertStreamToString(is).trim();
			int glukoze_value = Integer.valueOf(res);
			
			
			List<Integer> prebuild = Arrays.asList(new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0, 0});
			List<Integer> resultAnalysis = new LinkedList<Integer>();
			for(Integer i : prebuild)
				resultAnalysis.add(i);
			resultAnalysis.add(glukoze_value);
			Tutorial1Activity.sendAnalysisData(activity, resultAnalysis);
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
