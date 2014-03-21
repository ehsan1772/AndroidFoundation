package com.foundation.restful;

import android.util.LruCache;

/**
 * 
 * This class will represent a simple memory cache, 
 * Should be instantiated by a service 
 * 
 * @author ehsan.barekati
 */

public class MemoryCache {

	LruCache<Integer, Object> mBitmapLRUCache;
}
