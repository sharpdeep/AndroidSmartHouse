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
		//ȡ��������,������setContetxtView����֮ǰ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		tv_version = (TextView)findViewById(R.id.tv_activity_main_version);
		//ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		tv_version.setText("�汾�ţ�"+getVersion());
		
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
	
	//�õ�Ӧ�ó���İ汾
	private String getVersion()
	{
		try
        {
                PackageManager packageManager = getPackageManager();
                //�ܹ��õ�AndroidManifest.XML�������������
                PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
                return packageInfo.versionName;
        }
        catch (NameNotFoundException e)
        {
                e.printStackTrace();
                return "�汾��δ֪";
        }
	}

	@Override
	protected void onDestroy() {
		serviceintent.setClass(WelcomeActivity.this, DataService.class);
		startService(serviceintent);
		super.onDestroy();
	}

}
