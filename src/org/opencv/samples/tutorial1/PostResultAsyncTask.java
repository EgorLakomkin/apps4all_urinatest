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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.AsyncTask;

public class PostResultAsyncTask extends AsyncTask<String, String, String>{

	private Context context;
	
	public PostResultAsyncTask(Context _context)
	{
		context = _context;
	}
	
	@Override
    protected String doInBackground(String... uri) {
    	HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpResponse response;
        String responseString = null;
        try {
        	HttpPost httppost = new HttpPost(uri[0]);
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            String user_id = uri[1];
            String analysis = uri[2];
           	nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
           	nameValuePairs.add(new BasicNameValuePair("analysis", analysis));            
           	
           	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        	
            response = httpclient.execute(httppost);
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
          	
    	
    }
}
