package com.sharpdeep.object;

import com.sharpdeep.model.MyObject;

public class AlertObject extends MyObject{
	
	private Boolean mValue;

	public AlertObject(int id, Boolean isCtorlable) {
		super(id, isCtorlable);
		mValue = false;
	}

	public Boolean getValue() {
		return mValue;
	}

	public void setValue(Boolean value) {
		this.mValue = value;
	}
	
	

}
