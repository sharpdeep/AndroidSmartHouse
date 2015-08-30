package com.sharpdeep.android_smarthouse;
//温度监控界面
//待完善：网络未连接时不显示
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class TemparetureActivity extends Activity{

	private SharedPreferences tempareturepreferences;
	private TextView tempareturetext;
	private boolean tempareturekey;
	private String tempareture;
	private boolean outtoastflag = false;//拔出的Toast提示的标志
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tempareture);
		
		tempareturekey = true;
		tempareturetext = (TextView)findViewById(R.id.tempareturetext);
		tempareturepreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		//开启一个线程不断读取温度
		TemparetureThread tempareturethread = new TemparetureThread();
		tempareturethread.start();
	}
	
	class TemparetureThread extends Thread{

		@Override
		public void run() {
			//从sharepreferences中读取，并用runonuithread方法显示
			while(tempareturekey)
			{
				tempareture = tempareturepreferences.getString("tempareture", "----");
				
				runOnUiThread(new Runnable() {
					public void run() {
						if(tempareture.equals("----")){
							if(!outtoastflag)//未提示过
							{
								Toast.makeText(TemparetureActivity.this, "温度传感器未连接", Toast.LENGTH_SHORT).show();
								outtoastflag = true;//已经提示过的标志
								tempareturetext.setText(tempareture);
							}
						}
						else{
							tempareturetext.setText(tempareture+"\u2103");
							outtoastflag = false; //因为又插入传感器有了数据，所以再次拔出将会有Toast
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
		//关闭线程的循环读取
		tempareturekey =false;
		super.onDestroy();
	}
	
	
}
