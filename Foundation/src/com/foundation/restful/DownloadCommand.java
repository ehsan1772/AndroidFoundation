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
	    ByteArrayOutputStream out = null;
	    int status = 0;
		try {
			response = httpclient.execute(new HttpGet(uri));
		    StatusLine statusLine = response.getStatusLine();
		    status = statusLine.getStatusCode();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		    } else{
		        //Closes the connection.
		        response.getEntity().getContent().close();
		    }
		} catch (IOException e) {
			out = null;
			e.printStackTrace();
		} finally {
			listener.deliverDownloadResult(urlString, status, out);
		}
	}
	
	static public interface DownloadListener {
		public void deliverDownloadResult(String urlString, int status, ByteArrayOutputStream result);
	}

}
