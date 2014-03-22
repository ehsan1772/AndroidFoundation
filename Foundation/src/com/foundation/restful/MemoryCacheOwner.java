package com.foundation.restful;

/**
 * this interface should be implemented by any class that in an instance of
 * MemoryCache, the methods will be invoked by memorycache to update memory cache on the status 
 * 
 * @author Ehsan
 *
 */
public interface MemoryCacheOwner {
	public void cacheIsEmpty();
}
