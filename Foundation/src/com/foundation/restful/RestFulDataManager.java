package com.foundation.restful;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.http.HttpStatus;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.foundation.restful.DownloadCommand.DownloadListener;
import com.foundation.restful.RestfulDownloadRequest.DownloadStatus;

/** 
 * A service to download data from internet that should be declared in apps manifest that want to use it
 * it manages downloading data and Caching it
 * 
 * @author Ehsan
 *
 */
// we are not extending IntentService because we want to control our threadpool size
public class RestFulDataManager extends Service implements DataManager, MemoryCacheOwner, DownloadListener {
	private static final String TAG = RestFulDataManager.class.getSimpleName();
	private static final int THREAD_POOL_SIZE = 5;
	private MemoryCache memoryCache;
	private SizedArrayList<RestfulDownloadRequest> downloadQueue;
	//private int downLoadQueueSize;
	private WeakReference<LocalBroadcastManager> broadcastManagerReference;
	private int threadSize;
	
	public final static String REQUESTED_URL = "requested_url";
	public static String REF_KEY = "REF_KEY";
	
	private final static int DOWNLOAD_QUEUE_SIZE = 500;
	private final static int THREAD_SIZE = 5;
	private final static int CACHE_SIZE = 100;
	
	private static RestFulDataManager instance;
	
	public static RestFulDataManager getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		memoryCache = new MemoryCache(this, CACHE_SIZE, MemoryCache.MeasuringType.COUNT);
		downloadQueue = new SizedArrayList<RestfulDownloadRequest>(DOWNLOAD_QUEUE_SIZE);
		instance = this;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "Request Received");
		String url = intent.getExtras().getString(REQUESTED_URL);
		
		if (!url.isEmpty()) {
			fetchMeData(url);
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void cacheIsEmpty() {
		// there is no data in the cache so we can shut down the service
		stopSelf();
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
		Intent intent = new Intent("com.ovenbits.chucknorris.RESULT");
		intent.putExtra(REF_KEY, refId);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
	}

	private void addToDownloadQueue(String url) {
		RestfulDownloadRequest request = new RestfulDownloadRequest(url);
		downloadQueue.add(request);
		attemptDownload();
	}
	
	private void attemptDownload() {
		if (threadSize < THREAD_POOL_SIZE) {
			DataRequest request =  downloadQueue.getNextRequest();
			if (request != null) {
				RestfulDownloadRequest restRequest = (RestfulDownloadRequest) request;
				//we don't want the object to be deleted by SizedArrayList
				//TODO Consider synchronizing SizedArrayList methods
				synchronized (request) { 
					try {
						DownloadCommand command = new DownloadCommand(restRequest.getUrl(), this);
						threadSize++;
						new Thread(command).start();
					} catch (URISyntaxException e) {
						Log.e(TAG, "Invalid url : " + downloadQueue.get(0).getUrl());
						e.printStackTrace();
					}
				}
			}
		}
	}
	
//	private DataRequest nextRequest() {
//		for (DataRequest request : downloadQueue) {
//			if (request.getStatus() == DownloadStatus.CREATED || 
//				request.getStatus() == DownloadStatus.FAILED) {
//				return request;
//			}
//		}
//		return null;
//	}

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

		Object result = memoryCache.retrieveData(refId);
		if (result != null) {
			T t = (T) result;
			return t;
		} 
		
		return null;
	}

	@Override
	public void deliverDownloadResult(String urlString, int status, ByteArrayOutputStream result) {
		threadSize--;
		int refId = urlString.hashCode();
		
		RestfulDownloadRequest request = downloadQueue.findById(urlString.hashCode());
		
		if (request == null) {
			Log.e(TAG, "Request has expierd");
			return;
		}
		
		request.setLastRecievedTime(System.currentTimeMillis());
		
		if (result == null) {
			Log.e(TAG, "Download Attemp failed");
			return;
		}
		
		

		if (status == HttpStatus.SC_OK) {
			request.setStatus(DownloadStatus.SUCCEED);
			memoryCache.add(refId, result.toString());
			broadCastUpdate(refId);
		} else {
			request.setStatus(DownloadStatus.FAILED);
		}
		attemptDownload();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// This service may live longer than the application who is calling it so we don't allow binding
		return null;
	}
}
