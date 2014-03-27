package com.foundation.data;


/**
 * 
 * A self destructive object that holds an object (value) and kills itself if not accessed within a specific time
 * @author ehsan.barekati
 *
 */
public class CacheObject {
	private MemoryCache owner;
	private Object value;
	private long lifeTime;
	private int key;
	
	private Thread timerThread; 
	
	public CacheObject(MemoryCache owner, Object value, long lifeTime, int key) {
		this.owner = owner;
		this.value = value;
		this.lifeTime = lifeTime;
		this.key = key;
		startTimer();
	}
	
	private void startTimer() {
		timerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(lifeTime);
					owner.remove(key);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		timerThread.start();
	}
	
	private void resetTimer() {
		timerThread.interrupt();
		startTimer();
	}

	public Object getValue() {
		resetTimer();
		return value;
	}
	
}
