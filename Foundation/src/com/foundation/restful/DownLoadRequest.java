package com.foundation.restful;

/**
 * A data object to keep track of download status
 * 
 * @author Ehsan
 */
public class DownLoadRequest {
	public enum DownloadStatus{FAILED, SUCCEED, RUNNING};
	
	private String url;
	private int downloadAttemptCounter;
	private long requestReceivedTime;
	private long lastAttemptTime;
	private long lastRecievedTime;
	private DownloadStatus status;
	
	public DownLoadRequest(String url) {
		this.url = url;
		requestReceivedTime = System.currentTimeMillis();
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
	public void setDownloadAttemptCounter(int downloadAttemptCounter) {
		this.downloadAttemptCounter = downloadAttemptCounter;
	}
	public long getRequestReceivedTime() {
		return requestReceivedTime;
	}
	public void setRequestReceivedTime(long requestReceivedTime) {
		this.requestReceivedTime = requestReceivedTime;
	}
	public long getLastAttemptTime() {
		return lastAttemptTime;
	}
	public void setLastAttemptTime(long lastAttemptTime) {
		this.lastAttemptTime = lastAttemptTime;
	}
	public long getLastRecievedTime() {
		return lastRecievedTime;
	}
	public void setLastRecievedTime(long lastRecievedTime) {
		this.lastRecievedTime = lastRecievedTime;
	}
	public DownloadStatus getStatus() {
		return status;
	}
	public void setStatus(DownloadStatus status) {
		this.status = status;
	}
	
	

}
