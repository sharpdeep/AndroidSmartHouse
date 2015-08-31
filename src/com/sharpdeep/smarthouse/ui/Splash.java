package com.sharpdeep.smarthouse.ui;

import com.sharpdeep.backstage.DataService;
import com.sharpdeep.data.Constant;
import com.sharpdeep.model.UpdateInfo;
import com.sharpdeep.utils.AndroidUtil;
import com.sharpdeep.utils.HttpUtils;
import com.sharpdeep.utils.MyLog;
import com.sharpdeep.utils.XMLParser;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Splash extends Activity {
	
	private Handler mHandler;
	private LinearLayout mViewSplash;
	private TextView mViewVersionName;
	private String currentVersion;
	
	private final static String CHECK_UPDATE_URL = "http://sharpdeep.sinaapp.com/update.xml"; 
	private final static String SPLASH_TAG = "SPLASH";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐去标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, //全屏
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		
		mViewSplash = (LinearLayout) findViewById(R.id.splash_main);
		mViewVersionName = (TextView) findViewById(R.id.version);
		
		//出现动画
		Animation anim = AnimationUtils.loadAnimation(Splash.this, R.anim.splash_alpha_present);
		mViewSplash.startAnimation(anim);
		
		//得到版本号并显示
		currentVersion  = AndroidUtil.getVersion(Splash.this);
		mViewVersionName.setText("版本号：" + currentVersion);
		
		//定时转到主界面
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				AndroidUtil.startActivity(Splash.this, MainActivity.class);
				finish();
			}
		}, 3000);
		
		Thread thread = new Thread(){
			@Override
			public void run() {
				
				System.out.println("thread running");
				
				UpdateInfo updateInfo = UpdateInfo.newInstance();
				
				if(XMLParser.parseXML2Info(HttpUtils.getByHttp(CHECK_UPDATE_URL)) == Constant.XML_PARSE_SUCESS){
					if(!currentVersion.equals(updateInfo.getVersion())){
						//需要升级
						updateInfo.setNeedUpdate(true);
						MyLog.e(SPLASH_TAG, "需要升级");
					}
				}
				super.run();
			}
		};
		thread.start();
		
		if(!AndroidUtil.isServiceRunning(Splash.this, getResources().getString(R.string.dataService_name))){
			AndroidUtil.startService(Splash.this, DataService.class);
		}
	}
}
