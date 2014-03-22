package com.foundation.restful;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * This class represents a download class as a runnable
 * download manager instantiated and executes instances of this class based on the 
 * desired queue size
 * 
 * @author Ehsan
 *
 */
public class DownloadCommand implements Runnable {
	private DownloadListener listener;
	private URI uri;
	private String urlString;
	
	public DownloadCommand(String urlString, DownloadListener listener) throws URISyntaxException {
		this.urlString = urlString;
		this.listener = listener;
		uri = new URI(urlString);
	}

	@Override
	public void run() {
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response = null;
		try {
			response = httpclient.execute(new HttpGet(uri));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        listener.deliverDownloadResult(urlString, HttpStatus.SC_OK, out);
		   //     String responseString = out.toString();
		    } else{
		        listener.deliverDownloadResult(urlString, statusLine.getStatusCode(), null);
		        //Closes the connection.
		        response.getEntity().getContent().close();
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public interface DownloadListener {
		public void deliverDownloadResult(String urlString, int status, ByteArrayOutputStream result);
	}

}
