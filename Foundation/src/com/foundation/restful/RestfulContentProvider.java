package com.foundation.restful;


/**
 * 
 * This class is our first contact point for for getting to RESTful resources
 * it will work with the memory cache, SQLite database and ResfulService to provide data  
 * 
 * 
 * @author ehsan.barekati
 *
 */
public class RestfulContentProvider<T> {

	static void test() {
		RestfulContentProvider<String> testClass = new RestfulContentProvider<String>();
	}
	public int fetchData(String url) {
		return url.hashCode();
	}

}
