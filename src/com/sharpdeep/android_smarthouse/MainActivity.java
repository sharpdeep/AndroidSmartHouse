package com.sharpdeep.android_smarthouse;
/*
 * 新增功能：添加了所有的activity，重连功能被暂时注释
 */
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity{

	private int[] resId = {
			R.drawable.item1,R.drawable.item2,R.drawable.item3,
			R.drawable.item4,R.drawable.item5,R.drawable.item6,
			R.drawable.item7
		};
		
		private String[] name = {

				"温度监控","湿度监控","煤气监控",
				"防盗监控","甲烷监控","摄像监控",
				"清除警报"
		};
		
		private GridView gridview;
		private Intent intent;
		
		private LinearLayout connetlayout;
		private boolean key;
		private SharedPreferences connectpreferences;
		private CheckConnectThread checkconnectthread;
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_main);
			
			key = true;
			connectpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			
			gridview = (GridView)findViewById(R.id.gridview);
			connetlayout = (LinearLayout)findViewById(R.id.connectlayout);
			
			
			MainAdapter adapter = new MainAdapter(this, name, resId);
			gridview.setAdapter(adapter);
			gridview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					Toast.makeText(MainActivity.this, "第"+position+"个"+"被选中", Toast.LENGTH_SHORT).show();
					switch (position) {
					case 0://温度监控
						intent = new Intent();
						intent.setClass(MainActivity.this, TemparetureActivity.class);
						startActivity(intent);
						break;
					case 1://湿度监控
						intent = new Intent();
						intent.setClass(MainActivity.this, HumidityActivity.class);
						startActivity(intent);
						break;
					case 2://煤气监控
						intent = new Intent();
						intent.setClass(MainActivity.this, COActivity.class);
						startActivity(intent);
						break;
					case 3://防盗监控
						intent = new Intent();
						intent.setClass(MainActivity.this, GuardActivity.class);
						startActivity(intent);
						break;
					case 4://甲烷监控
						intent = new Intent();
						intent.setClass(MainActivity.this, CHActivity.class);
						startActivity(intent);
						break;
					case 5://摄像监控
						intent = new Intent();
						intent.setClass(MainActivity.this, VideoActivity.class);
						startActivity(intent);
						break;
					case 6://清除警报
//						Toast.makeText(MainActivity.this, "请联系开发者开发", Toast.LENGTH_SHORT).show();
						SharedPreferences.Editor editor = connectpreferences.edit();
						editor.putBoolean("ClearWarnning", true);
						editor.commit();
						break;
					default:
						break;
					}
				}
				
			});
			checkconnectthread = new CheckConnectThread();
			checkconnectthread.start();
			
		}

		@Override
		protected void onDestroy() {
			key =false;
			super.onDestroy();
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			menu.add(0, Constant.MENU_ITEM1, 0, "设置");
			menu.add(0, Constant.MENU_ITEM2, 0, "关于");
			menu.add(0, Constant.MENU_ITEM3, 0, "退出");
			return true;
		}

		@Override
		public boolean onMenuItemSelected(int featureId, MenuItem item) {
			switch (item.getItemId()) {
			case Constant.MENU_ITEM1:
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SettingActivity.class);
				startActivity(intent);
				break;
			case Constant.MENU_ITEM2:
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setIcon(R.drawable.app_icon);
				builder.setTitle("About");
				builder.setMessage("			智能家居-魔方小盒APP\n			版权由4-bits所有╮(╯▽╰)╭\n			联系方式：大喊即可( # ▽ # )\n");
				builder.setPositiveButton("确定", null);
				builder.create().show();
				break;
			case Constant.MENU_ITEM3:
				AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
				builder1.setIcon(R.drawable.app_icon);
				builder1.setTitle("退出");
				builder1.setMessage("			你确定要退出吗？(⊙_⊙)?");
				builder1.setPositiveButton("别问了，我决定了", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
				builder1.setNegativeButton("不好意思，手贱点错", null);
				builder1.create().show();
				break;
			}
			return super.onMenuItemSelected(featureId, item);
		}

		class CheckConnectThread extends Thread{

			@Override
			public void run() {
				while(key)
				{
					if(connectpreferences.getBoolean("connectstate", false))
					{
						if(0 == connetlayout.getVisibility())
						{
							runOnUiThread(new Runnable() {
								public void run() {
									connetlayout.setVisibility(8);//隐藏
								}
							});
						}
					}
					else
					{
						if(8 == connetlayout.getVisibility())
						{
							runOnUiThread(new Runnable() {
								public void run() {
									connetlayout.setVisibility(0);//可见
								}
							});
						}
							
					}
				}
				super.run();
			}
		}

		@Override
		public void onBackPressed() {
			
		}
		
		
	}
