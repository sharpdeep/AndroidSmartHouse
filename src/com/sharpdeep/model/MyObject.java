package com.sharpdeep.model;

public class MyObject {

	private int mId;
	private Boolean mIsCtorlable;
	
	public  MyObject(int id, Boolean isCtorlable) {
		this.mId = id;
		this.mIsCtorlable = isCtorlable;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}


	public Boolean getIsCtorlable() {
		return mIsCtorlable;
	}

	public void setIsCtorlable(Boolean isCtorlable) {
		this.mIsCtorlable = isCtorlable;
	}
	
	
}
