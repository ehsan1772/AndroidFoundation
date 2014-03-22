package com.foundation.restful;

public interface DataManager {
	public int fetchMeData(String url);
	public int getStatus(int refId);
	public int cacheSizeCount();
	public int refreshData(String url);
	public int refreshData(int refId);
	public void cancel(int refId);
	public <T> T getData(int refId, Class<T> clazz);
}
