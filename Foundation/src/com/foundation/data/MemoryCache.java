package com.foundation.data;


import android.util.LruCache;

/**
 * 
 * This class will represent a simple memory cache, 
 * Should be instantiated by a service 
 * 
 * @author ehsan.barekati
 */

public class MemoryCache {

	private LruCache<Integer, CacheObject> dataLRUCache;
	private MemoryCacheOwner theOwner;
	public enum MeasuringType {SIZE, COUNT};
	public final static long LIFE_TIME = 10 * 60 * 1000;
	
	public MemoryCache(MemoryCacheOwner owner, int size, MeasuringType type) {
		theOwner = owner;
		switch (type) {
		case COUNT :
			dataLRUCache = new LruCache<Integer, CacheObject>(size);
			break;
		case SIZE :
			dataLRUCache = new LruCache<Integer, CacheObject>(size) {
				@Override
				protected int sizeOf(Integer key, CacheObject value) {
					// TODO change it to the correct implementation
					return super.sizeOf(key, value);
				}
			};
			break;
		}
	}
	

	public void remove(int key) {
		dataLRUCache.remove(key);
		if (dataLRUCache.size() == 0) {
			theOwner.cacheIsEmpty();
		}
	}
	
	public void add(int key, Object object) {
		CacheObject cacheObject = new CacheObject(this, object, LIFE_TIME, key);
		dataLRUCache.put(key, cacheObject);
	}
	
	public CacheObject retrieve(int key) {
		return dataLRUCache.get(key);
	}
	
	public Object retrieveData(int key) {
		return dataLRUCache.get(key).getValue();
	}
}





