package com.foundation.restful;

/**
 * A data object to keep track of download status
 * 
 * @author Ehsan
 */
public class RestfulDownloadRequest implements DataRequest {
	public enum DownloadStatus{FAILED, SUCCEED, RUNNING, CREATED, FAILED_FINAL};
	
	private String url;
	private int downloadAttemptCounter;
	private Long requestReceivedTime;
	private long lastAttemptTime;
	private long lastRecievedTime;
	private DownloadStatus status;
	
	public RestfulDownloadRequest(String url) {
		this.url = url;
		requestReceivedTime = System.currentTimeMillis();
		status = DownloadStatus.CREATED;
	}
	
	@Override
	public int getId(){
		return url.hashCode();
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDownloadAttemptCounter() {
		return downloadAttemptCounter;
	}

	@Override
	public void setDownloadAttemptCounter(int downloadAttemptCounter) {
		this.downloadAttemptCounter = downloadAttemptCounter;
	}

	@Override
	public long getRequestReceivedTime() {
		return requestReceivedTime;
	}
	public void setRequestReceivedTime(long requestReceivedTime) {
		this.requestReceivedTime = requestReceivedTime;
	}

	@Override
	public long getLastAttemptTime() {
		return lastAttemptTime;
	}

	@Override
	public void setLastAttemptTime(long lastAttemptTime) {
		this.lastAttemptTime = lastAttemptTime;
	}

	@Override
	public long getLastRecievedTime() {
		return lastRecievedTime;
	}

	@Override
	public void setLastRecievedTime(long lastRecievedTime) {
		this.lastRecievedTime = lastRecievedTime;
	}

	@Override
	public DownloadStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(DownloadStatus status) {
		this.status = status;
	}

	@Override
	public int compareTo(DataRequest o) {
		return this.requestReceivedTime.compareTo(o.getRequestReceivedTime());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RestfulDownloadRequest) {
			return ((RestfulDownloadRequest) obj).getUrl().equals(this.getUrl());
		}
		return false;
	}

}
