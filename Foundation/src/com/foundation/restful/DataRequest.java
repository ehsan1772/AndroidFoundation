package com.foundation.restful;

import com.foundation.restful.RestfulDownloadRequest.DownloadStatus;

public interface DataRequest extends Comparable<DataRequest> {

	public abstract int getId();

	public abstract void setDownloadAttemptCounter(int downloadAttemptCounter);

	public abstract long getRequestReceivedTime();

	public abstract long getLastAttemptTime();

	public abstract void setLastAttemptTime(long lastAttemptTime);

	public abstract long getLastRecievedTime();

	public abstract void setLastRecievedTime(long lastRecievedTime);

	public abstract DownloadStatus getStatus();

	public abstract void setStatus(DownloadStatus status);

}