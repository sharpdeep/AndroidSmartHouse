package com.sharpdeep.android_smarthouse;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class CHActivity extends Activity{
	
	private boolean alarm_ch_key;
	private ImageButton chbutton;
	private boolean ischin;
	private boolean ch;
	private boolean key;
	private SharedPreferences chpreferences;
	private CHAlarmThread chalarmthread;
	private CHThread chthread;
	private boolean intoast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ch);
		chbutton = (ImageButton)findViewById(R.id.chbutton);
		chpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		chalarmthread = new CHAlarmThread();
		key = true;
		intoast =false;
		chthread = new CHThread();
		chthread.start();
		
		chbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alarm_ch_key = false;
				chbutton.setImageResource(R.drawable.normal_ch);
				SharedPreferences.Editor editor = chpreferences.edit();
				editor.putBoolean("chalarm", false);
				editor.commit();
			}
		});
	}
	
	
	
	@Override
	protected void onDestroy() {
		key = false;
		alarm_ch_key = false;
		super.onDestroy();
	}



	class CHThread extends Thread{
		public void run() {
			while(key)
			{
				ischin = chpreferences.getBoolean("ischin", true);
				ch = chpreferences.getBoolean("chalarm", false);
				runOnUiThread(new Runnable() {
					public void run() {
						if(!ischin && !intoast){
							Toast.makeText(CHActivity.this, "天然气传感器未连接", Toast.LENGTH_SHORT).show();
							intoast =true; 
						}
						else{
							if(ch){
								if(!chalarmthread.isAlive()){
									alarm_ch_key = true;
									chalarmthread.start();
								}
							}
							if(!ch && alarm_ch_key){
								alarm_ch_key = false;
								chbutton.setImageResource(R.drawable.normal_fire);
								SharedPreferences.Editor editor = chpreferences.edit();
								editor.putBoolean("chalarm", false);
								editor.commit();
							}
						}
					}
				});
			}
			super.run();
		}
		
	}

	class CHAlarmThread extends Thread{

		@Override
		public void run() {
			while(alarm_ch_key)
			{
			super.run();
			runOnUiThread(new Runnable() {
				public void run() {
					chbutton.setImageResource(R.drawable.alarm_ch_1);
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
					chbutton.setImageResource(R.drawable.alarm_ch_2);
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
