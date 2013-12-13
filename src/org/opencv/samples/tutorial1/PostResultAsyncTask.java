package org.opencv.samples.tutorial1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class PostResultAsyncTask extends AsyncTask<String, String, String>{

	private Activity activity;
	
	public PostResultAsyncTask(Activity _activity)
	{
		activity = _activity;
	}
	
	@Override
    protected String doInBackground(String... uri) {
    	HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpResponse response;
        String responseString = null;
        try {
        	HttpGet httpget= new HttpGet(uri[0]);
           	       	
            response = httpclient.execute(httpget);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO This exception have seen better days
        } catch (IOException e) {
            //TODO This exception have seen better days
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
    	
    	Toast toast = Toast.makeText(activity, "Received", Toast.LENGTH_LONG);
        toast.show();
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result",result);
		activity.setResult(Constants.RESULT_OK,returnIntent);     
		activity.finish();  	
    	
    }
}
