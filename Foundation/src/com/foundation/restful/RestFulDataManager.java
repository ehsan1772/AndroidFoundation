package com.foundation.restful;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.LinkedList;

import org.apache.http.HttpStatus;

import com.foundation.restful.DownloadCommand.DownloadListener;

import android.app.Activity;
import android.util.Log;

/** 
 * A class with singleton behavior that should be instantiated in apps
 * Application and manages downloading data and Caching it
 * 
 * @author Ehsan
 *
 */
public class RestFulDataManager implements DataManager, MemoryCacheOwner, DownloadListener {
	private static String TAG = RestFulDataManager.class.getSimpleName();
	private MemoryCache memoryCache;
	private LinkedList<String> downloadQueue;
	private int downLoadQueueSize;
	//private LocalBroadcastManager ll;
	
	public RestFulDataManager(int cacheSize, int downLoadQueueSize) {
		memoryCache = new MemoryCache(this, cacheSize, MemoryCache.MeasuringType.COUNT);
		downloadQueue = new LinkedList<String>();
		this.downLoadQueueSize = downLoadQueueSize;
	}

	@Override
	public void cacheIsEmpty() {
		// TODO Auto-generated method stub
		
	}

	//TODO the hashCode won't be unique for all entries
	@Override
	public int fetchMeData(String url) {
		int refId = url.hashCode();
		if (memoryCache.retrieve(refId) == null) {
			addToDownloadQueue(url);
		} else {
			broadCastAvailability(refId);
		}
		return refId;
	}

	private void broadCastAvailability(int refId) {
		// TODO Auto-generated method stub
		
	}

	private void addToDownloadQueue(String url) {
		downloadQueue.add(url);
		if (downloadQueue.size() < downLoadQueueSize) {
			try {
				DownloadCommand command = new DownloadCommand(downloadQueue.poll(), this);
				new Thread(command).start();
			} catch (URISyntaxException e) {
				Log.e(TAG, "Invalid url : " + url);
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getStatus(int refId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int cacheSizeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int refreshData(String url) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int refreshData(int refId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void cancel(int refId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T getData(int refId, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deliverDownloadResult(String urlString, int status, ByteArrayOutputStream result) {
		if (result != null && status == HttpStatus.SC_OK) {
			int refId = urlString.hashCode();
			memoryCache.add(refId, result.toString());
			broadCastAvailability(refId);
		} else {
			
		}
		
	}

}