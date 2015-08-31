package com.sharpdeep.object;

import com.sharpdeep.model.MyObject;

public class MonitorObject extends MyObject{
	
	private float mValue;

	public MonitorObject(int id, Boolean isCtorlable) {
		super(id, isCtorlable);
		mValue = -1;
	}

	public float getValue() {
		return mValue;
	}

	public void setValue(int value) {
		this.mValue = value;
	}
	
	
	
}
