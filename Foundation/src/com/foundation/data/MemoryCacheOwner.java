package com.foundation.data;

/**
 * this interface should be implemented by any class that in an instance of
 * MemoryCache, the methods will be invoked by memorycache to update memory cache on the status 
 * 
 * @author ehsan.barekati
 *
 */
public interface MemoryCacheOwner {
	public void cacheIsEmpty();
}
