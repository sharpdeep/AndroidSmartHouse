package com.sharpdeep.android_smarthouse;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class GuardActivity extends Activity{

	private ImageButton dangerbutton;
	private boolean alarm_danger_key;
	private boolean key;
	private SharedPreferences guardpreferences;
	private boolean danger;
	private boolean isdangerin;
	private AlarmThread alarmthread;
	private GuardThread guardthread;
	private boolean intoast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guard);
		dangerbutton = (ImageButton)findViewById(R.id.dangerbutton);
		guardpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		alarmthread = new AlarmThread();
		key = true;
		intoast = false;
		guardthread = new GuardThread();
		guardthread.start();

		
		dangerbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//停止警报动画
				alarm_danger_key = false;
				dangerbutton.setImageResource(R.drawable.normal_danger);
				//停止警报
				SharedPreferences.Editor editor = guardpreferences.edit();
				editor.putBoolean("dangeralarm", false);
				editor.commit();
				//此处可打开视频
			}
		});
	}
	



	@Override
	protected void onDestroy() {
		key = false;
		alarm_danger_key = false;
		super.onDestroy();
	}



	class GuardThread extends Thread{
		public void run() {
			while(key)
			{
				isdangerin = guardpreferences.getBoolean("isdangerin", true);
				danger = guardpreferences.getBoolean("dangeralarm", false);
				runOnUiThread(new Runnable() {
					public void run() {
						if(!isdangerin && !intoast){
							Toast.makeText(GuardActivity.this, "人体红外未连接", Toast.LENGTH_SHORT).show();
							intoast = true;
						}
						else{
							if(danger){
								if(!alarmthread.isAlive())
								{
									alarm_danger_key = true;
									alarmthread.start();
								}
							}
							if(!danger && alarm_danger_key){
								//停止警报动画
								alarm_danger_key = false;
								dangerbutton.setImageResource(R.drawable.normal_danger);
								//停止警报
								SharedPreferences.Editor editor = guardpreferences.edit();
								editor.putBoolean("dangeralarm", false);
								editor.commit();
							}
						}
					}
				});
			}
			super.run();
		}
		
	}
	
	class AlarmThread extends Thread{

		@Override
		public void run() {
			while(alarm_danger_key)
			{
			super.run();
			runOnUiThread(new Runnable() {
				public void run() {
					dangerbutton.setImageResource(R.drawable.alarm_danger1);
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
					dangerbutton.setImageResource(R.drawable.alarm_danger2);
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
	