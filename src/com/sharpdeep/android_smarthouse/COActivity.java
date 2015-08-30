package com.sharpdeep.android_smarthouse;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class COActivity extends Activity{

	private boolean alarm_co_key;
	private ImageButton cobutton;
	private boolean iscoin;
	private boolean co;
	private boolean key;
	private SharedPreferences copreferences;
	private COAlarmThread coalarmthread;
	private COThread cothread;
	private boolean intoast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_co);
		cobutton = (ImageButton)findViewById(R.id.cobutton);
		copreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		coalarmthread = new COAlarmThread();
		key = true;
		intoast =false;
		cothread = new COThread();
		cothread.start();
		
		cobutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alarm_co_key = false;
				cobutton.setImageResource(R.drawable.normal_co);
				SharedPreferences.Editor editor = copreferences.edit();
				editor.putBoolean("coalarm", false);
				editor.commit();
			}
		});
	}
	
	
	
	@Override
	protected void onDestroy() {
		key = false;
		alarm_co_key = false;
		super.onDestroy();
	}



	class COThread extends Thread{
		public void run() {
			while(key)
			{
				iscoin = copreferences.getBoolean("iscoin", true);
				co = copreferences.getBoolean("coalarm", false);
				runOnUiThread(new Runnable() {
					public void run() {
						if(!iscoin && !intoast){
							Toast.makeText(COActivity.this, "CO传感器未连接", Toast.LENGTH_SHORT).show();
							intoast =true; 
						}
						else{
							if(co){
								if(!coalarmthread.isAlive()){
									alarm_co_key = true;
									coalarmthread.start();
								}
							}
							if(!co && alarm_co_key){
								alarm_co_key = false;
								cobutton.setImageResource(R.drawable.normal_fire);
								SharedPreferences.Editor editor = copreferences.edit();
								editor.putBoolean("coalarm", false);
								editor.commit();
							}
						}
					}
				});
			}
			super.run();
		}
		
	}

	class COAlarmThread extends Thread{

		@Override
		public void run() {
			while(alarm_co_key)
			{
			super.run();
			runOnUiThread(new Runnable() {
				public void run() {
					cobutton.setImageResource(R.drawable.alarm_co_1);
				}
			});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			runOnUiThread(new Runnable() {
				public void run() {
					cobutton.setImageResource(R.drawable.alarm_co_2);
				}
			});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
	}
}
