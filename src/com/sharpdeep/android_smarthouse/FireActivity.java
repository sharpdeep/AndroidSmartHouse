package com.sharpdeep.android_smarthouse;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class FireActivity extends Activity{

	private boolean alarm_fire_key;
	private ImageButton firebutton;
	private boolean isfirein;
	private boolean fire;
	private boolean key;
	private SharedPreferences firepreferences;
	private FireAlarmThread firealarmthread;
	private FireThread firethread;
	private boolean intoast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fire);
		firebutton = (ImageButton)findViewById(R.id.firebutton);
		firepreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		firealarmthread = new FireAlarmThread();
		key = true;
		intoast =false;
		firethread = new FireThread();
		firethread.start();
		
		firebutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alarm_fire_key = false;
				firebutton.setImageResource(R.drawable.normal_fire);
				SharedPreferences.Editor editor = firepreferences.edit();
				editor.putBoolean("firealarm", false);
				editor.commit();
			}
		});
	}
	
	
	
	@Override
	protected void onDestroy() {
		key = false;
		alarm_fire_key = false;
		super.onDestroy();
	}



	class FireThread extends Thread{
		public void run() {
			while(key)
			{
				isfirein = firepreferences.getBoolean("isfirein", true);
				fire = firepreferences.getBoolean("firealarm", false);
				runOnUiThread(new Runnable() {
					public void run() {
						if(!isfirein && !intoast){
							Toast.makeText(FireActivity.this, "气体传感器未连接", Toast.LENGTH_SHORT).show();
							intoast =true; 
						}
						else{
							if(fire){
								if(!firealarmthread.isAlive()){
									alarm_fire_key = true;
									firealarmthread.start();
								}
							}
							if(!fire && alarm_fire_key){
								alarm_fire_key = false;
								firebutton.setImageResource(R.drawable.normal_fire);
								SharedPreferences.Editor editor = firepreferences.edit();
								editor.putBoolean("firealarm", false);
								editor.commit();
							}
						}
					}
				});
			}
			super.run();
		}
		
	}

	class FireAlarmThread extends Thread{

		@Override
		public void run() {
			while(alarm_fire_key)
			{
			super.run();
			runOnUiThread(new Runnable() {
				public void run() {
					firebutton.setImageResource(R.drawable.alarm_fire_1);
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
					firebutton.setImageResource(R.drawable.alarm_fire_2);
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
