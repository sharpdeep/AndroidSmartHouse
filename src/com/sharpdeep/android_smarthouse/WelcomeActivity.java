package com.sharpdeep.android_smarthouse;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.ServiceState;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

	private TextView tv_version;
	private Intent serviceintent = new Intent();
	private Intent mainactivityintent = new Intent();
	private SharedPreferences sharepreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取消标题栏,必须在setContetxtView方法之前
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		tv_version = (TextView)findViewById(R.id.tv_activity_main_version);
		//全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		tv_version.setText("版本号："+getVersion());
		
		sharepreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			
		new Thread(){
			public void run(){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mainactivityintent.setClass(WelcomeActivity.this, MainActivity.class);
				WelcomeActivity.this.startActivity(mainactivityintent);
				WelcomeActivity.this.finish();
			}
		}.start();
		
		
		
	}
	
	//得到应用程序的版本
	private String getVersion()
	{
		try
        {
                PackageManager packageManager = getPackageManager();
                //能够得到AndroidManifest.XML里面的所有内容
                PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
                return packageInfo.versionName;
        }
        catch (NameNotFoundException e)
        {
                e.printStackTrace();
                return "版本号未知";
        }
	}

	@Override
	protected void onDestroy() {
		serviceintent.setClass(WelcomeActivity.this, DataService.class);
		startService(serviceintent);
		super.onDestroy();
	}

}
