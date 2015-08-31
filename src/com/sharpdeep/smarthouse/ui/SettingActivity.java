package com.sharpdeep.smarthouse.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

public class SettingActivity extends Activity{
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
	}
	
}
