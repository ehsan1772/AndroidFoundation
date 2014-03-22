package com.foundation.restful;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SizedArrayList<T extends Comparable> extends ArrayList<T> {
	private static final long serialVersionUID = -2684222090808678630L;
	private int maxSize;
	
	public SizedArrayList(int maxSize) {
		super();
		this.maxSize = maxSize;
	}
	
	public void add(int index, T element) {
		while (!hasRoom(1)) {
			makeRoom(1);
		}
		super.add(index, element);
		Collections.sort(this);
	}
	
	//TODO we should first check to make sure it's not running
	private void makeRoom(int i) {
		Collections.sort(this);
		this.removeRange(0, i);
	}

	private boolean hasRoom(int i) {
		if (this.size() + i < maxSize) {
			return true;
		} else {
			return false;
		}
	}

	public boolean add(T e) {
		while (!hasRoom(1)) {
			makeRoom(1);
		}
		boolean result = super.add(e);
		Collections.sort(this);
		return result;
	}
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return false;
	}
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return false;
	}

}
