package com.foundation.utils;

public class Condition {
	private int status;
	public final static int CREATED = 0;
	public final static int REMOVED = -1;
	public final static int PUT = 1;
	
	private ConditionList parent;
	
	public Condition(ConditionList parent) {
		this.parent = parent;
		status = CREATED;
	}
	
	public void remove() {
		status = REMOVED;
	}
	
	public void put() {
		status = PUT;
	}
	
	public int getStatus() {
		return status;
	}
}
