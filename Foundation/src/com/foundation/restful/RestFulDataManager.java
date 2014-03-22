package com.foundation.restful;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.http.HttpStatus;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.foundation.restful.DownloadCommand.DownloadListener;
import com.foundation.restful.DownloadRequest.DownloadStatus;

/** 
 * A class with singleton behavior that should be instantiated in apps
 * Application and manages downloading data and Caching it
 * 
 * @author Ehsan
 *
 */
public class RestFulDataManager implements DataManager, MemoryCacheOwner, DownloadListener {
	private static final String TAG = RestFulDataManager.class.getSimpleName();
	private static final int THREAD_POOL_SIZE = 5;
	private MemoryCache memoryCache;
	private SizedArrayList<DownloadRequest> downloadQueue;
	private int downLoadQueueSize;
	private WeakReference<LocalBroadcastManager> broadcastManagerReference;
	private int threadSize;
	
	public static String REF_KEY = "REF_KEY";
	
	public RestFulDataManager(int cacheSize, int downLoadQueueSize, LocalBroadcastManager broadcastManager) {
		broadcastManagerReference = new WeakReference<LocalBroadcastManager>(broadcastManager);
		memoryCache = new MemoryCache(this, cacheSize, MemoryCache.MeasuringType.COUNT);
		downloadQueue = new SizedArrayList<DownloadRequest>(downLoadQueueSize);
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
			broadCastUpdate(refId);
		}
		return refId;
	}

	private void broadCastUpdate(int refId) {
		Intent intent = new Intent();
		intent.putExtra(REF_KEY, refId);
		broadcastManagerReference.get().sendBroadcast(intent);
	}

	private void addToDownloadQueue(String url) {
		DownloadRequest request = new DownloadRequest(url);
		downloadQueue.add(request);
		attemptDownload();
	}
	
	private void attemptDownload() {
		if (threadSize < THREAD_POOL_SIZE) {
			DownloadRequest request =  downloadQueue.get(0); 
			//we don't want the object to be deleted by SizedArrayList
			//TODO Consider synchronizing SizedArrayList methods
			synchronized (request) { 
				try {
					DownloadCommand command = new DownloadCommand(downloadQueue.get(0).getUrl(), this);
					threadSize++;
					new Thread(command).start();
				} catch (URISyntaxException e) {
					Log.e(TAG, "Invalid url : " + downloadQueue.get(0).getUrl());
					
					e.printStackTrace();
				}
			}
		}
	}
	
	private DownloadRequest nextRequest() {
		for (DownloadRequest request : downloadQueue) {
			if (request.getStatus() == DownloadStatus.CREATED || 
				request.getStatus() == DownloadStatus.FAILED) {
				return request;
			}
		}
		return null;
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
		threadSize--;
		
		downloadQueue
		
		int refId = urlString.hashCode();
		if (result != null && status == HttpStatus.SC_OK) {
			memoryCache.add(refId, result.toString());
		}
		broadCastUpdate(refId);
	}

}
