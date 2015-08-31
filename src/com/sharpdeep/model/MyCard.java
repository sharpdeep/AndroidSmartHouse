package com.sharpdeep.model;

import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.RecyclableCard;
import com.sharpdeep.smarthouse.ui.R;

public class MyCard extends RecyclableCard{
	
	private String mTitle;
	private String mDescription;
	
	public MyCard(String title, String description) {
		super(title, description);
		mTitle = title;
		mDescription = description;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView)convertView.findViewById(R.id.title)).setText(mTitle);
		((TextView)convertView.findViewById(R.id.description)).setText(mDescription);
	}

	@Override
	protected int getCardLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.card_simple;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String Title) {
		this.mTitle = Title;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String Description) {
		this.mDescription = Description;
	}
	
	

}
