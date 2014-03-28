package com.foundation.utils;

import java.util.ArrayList;
/**
 * this class creates a list of conditions and informs the observer when all the
 * conditions are removed, inspired by the publisher/observer pattern
 * 
 * the observer may move to the next stage when all the conditions are removed. For example, it may be used
 * in an android fragment to wait on all the loaders to return before populating the UI
 *
 * @author ehsan.barekati
 */

public class ConditionList extends ArrayList<Condition>{
	private static final long serialVersionUID = -7190508126150986630L;
	private ConditionObserver observer;
	

	public void setObserver(ConditionObserver observer) {
		this.observer = observer;
	}
	
	public void conditionRemoved() {
		checkCondition();
	}
	
	public boolean hasConditions(){
		for (Condition condition : this) {
			if (condition.getStatus() != Condition.REMOVED) {
				return true;
			}
		}
		return false;
	}
	
	private void checkCondition() {
		for (Condition condition : this) {
			if (condition.getStatus() != Condition.REMOVED) {
				return;
			}
		}
		observer.ConditionsRemoved(this);
	}
	public interface ConditionObserver {
		public void ConditionsRemoved(ConditionList list);
	//	public void ConditionsAdded(ConditionList list);
	}
}
