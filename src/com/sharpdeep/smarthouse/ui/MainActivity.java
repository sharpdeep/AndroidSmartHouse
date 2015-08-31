package com.sharpdeep.smarthouse.ui;


import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sharpdeep.broadcastreceiver.DataReceiver;
import com.sharpdeep.model.MyCard;
import com.sharpdeep.model.UpdateInfo;
import com.sharpdeep.utils.AndroidUtil;
import com.sharpdeep.utils.ToastUtil;

public class MainActivity extends SlidingFragmentActivity{

	private CardUI mViewCard;
	private CanvasTransformer mCanvasTransformer;

	private MyCard mViewTCard;//温度
	private MyCard mViewHCard;//湿度

	//测试用代码
	private int testData = 1;
	private CardStack testStack;
	private MyCard testCard;
	private boolean testFlag = true;


	private final static String TAG = "MainActivityTag";
	public final static String MY_INTENT_ACTION = "com.sharpdeep.smarthouse.ui.MainActivity";
	
	private DataReceiver mReceiver;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.project_name);
		setSlidingActionBarEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.myic);
		
		startReceiver();
		initAnimation();
		initMenu();
		setContentView(R.layout.activity_main);
		initCard();
		update();
	}
	/**
	 * 动画效果
	 */
	private void initAnimation(){
		mCanvasTransformer = new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float)(percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
			}
		};
	}
	/**
	 * 初始化SlidingMenu
	 */
	private void initMenu(){
		setBehindContentView(R.layout.menu_fragment);

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setBehindCanvasTransformer(mCanvasTransformer);

	}
	/**
	 * 初始化卡片布局
	 */
	private void initCard(){
		mViewCard = (CardUI) findViewById(R.id.cardsview);
		mViewCard.setSwipeable(false);

		mViewTCard = new MyCard("温度", 25+"\u2103");
		mViewHCard = new MyCard("湿度", 60+"%rh");

		// 此处变量赋值可优化
		CardStack environmentStack = new CardStack();
		environmentStack.setTitle("室内环境监控");
		mViewCard.addStack(environmentStack);

		mViewCard.addCard(mViewTCard);
		mViewCard.addCard(mViewHCard);

		testData = 0;
		testStack = new CardStack();
		testStack.setTitle("Test");
		testStack.add(testCard = new MyCard("测试卡片", testData+""));
		mViewCard.addStack(testStack);

		mViewCard.refresh();

		//以下为测试源码
		Timer timer = new Timer();
		TestTimerTask task = new TestTimerTask();
		timer.schedule(task, 1000);
	}
	
	/**
	 * 注册一个广播，用于监听数据变化
	 */
	private void startReceiver(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(MY_INTENT_ACTION);
		mReceiver = new DataReceiver();
		registerReceiver(mReceiver, filter);
	}

	//以下为测试类
	class TestTimerTask extends TimerTask{
		@Override
		public void run() {
			while(testFlag){
				runOnUiThread(new Runnable() {
					public void run() {
							testData++;
							testCard.setDescription(testData+"");
							mViewCard.refresh();
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
	/**
	 * 判断是否需要更新
	 */
	
	private void update(){
		UpdateInfo info = UpdateInfo.newInstance();
		if(info.isNeedUpdate()){
			//需要更新
			AlertDialog.Builder builder = new Builder(MainActivity.this);
			builder.setTitle("版本更新");
			builder.setMessage(info.getDescription());
			builder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//此处获取url下载安装
				}
			});
			builder.setNegativeButton("取消", null);
			builder.show();
		}
	}

	/**
	 * 当SlidingMenu中选项被选择的时候调用,此处处理菜单事件
	 */
	public void onSlidingMenuItemClick(int position){
		ToastUtil.toast(MainActivity.this, position+"");
		switch(position){
		case 0:
			break;
		case 1:
			AndroidUtil.startActivity(this, SettingActivity.class);
			break;
		case 2:
			showAboutMessage();
			break;
		case 3:
			finish();
			AndroidUtil.stopSevice(this, getResources().getString(R.string.dataService_name));
			break;
		}
	}
	
	public void showAboutMessage(){
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setTitle(getResources().getString(R.string.about_title));
		builder.setMessage(getResources().getString(R.string.about_message_line1)+"\n\n"+getResources().getString(R.string.about_message_line2));
		builder.setPositiveButton("确定", null);
		if(getSlidingMenu().isMenuShowing()){
			toggle();
		}
		builder.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			toggle();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_MENU:
			toggle();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		//测试用代码
		testFlag = false;
		
		unregisterReceiver(mReceiver);
		
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
	}

}
