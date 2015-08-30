package com.sharpdeep.android_smarthouse;

import android.R.bool;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

public class HumidityActivity extends Activity{

	private boolean humiditykey;
	private String humidity;
	private TextView humiditytext;
	private SharedPreferences humiditypreferences;
	private boolean outtoastflag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huminity);
		
		humiditykey = true;
		humiditytext = (TextView)findViewById(R.id.huminitytext);
		humiditypreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		HumidityThread humiditythread = new HumidityThread();
		humiditythread.start();
		
	}
	
	class HumidityThread extends Thread{

		@Override
		public void run() {
			while(humiditykey)
			{
				humidity = humiditypreferences.getString("humidity", "----");
				runOnUiThread(new Runnable() {
					public void run() {
						if(humidity.equals("----")){
							if(!outtoastflag)
							{
								Toast.makeText(HumidityActivity.this, "湿度传感器未连接", Toast.LENGTH_SHORT).show();
								humiditytext.setText(humidity);
								outtoastflag = true;
							}
						}
						else{
							humiditytext.setText(humidity + "%rh");
							outtoastflag =false;
						}
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			super.run();
		}
		
	}

	@Override
	protected void onDestroy() {
		humiditykey =false;
		super.onDestroy();
	}


}
