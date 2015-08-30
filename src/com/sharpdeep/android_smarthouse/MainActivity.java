package com.sharpdeep.android_smarthouse;
/*
 * �������ܣ���������е�activity���������ܱ���ʱע��
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

				"�¶ȼ��","ʪ�ȼ��","ú�����",
				"�������","������","������",
				"�������"
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
					Toast.makeText(MainActivity.this, "��"+position+"��"+"��ѡ��", Toast.LENGTH_SHORT).show();
					switch (position) {
					case 0://�¶ȼ��
						intent = new Intent();
						intent.setClass(MainActivity.this, TemparetureActivity.class);
						startActivity(intent);
						break;
					case 1://ʪ�ȼ��
						intent = new Intent();
						intent.setClass(MainActivity.this, HumidityActivity.class);
						startActivity(intent);
						break;
					case 2://ú�����
						intent = new Intent();
						intent.setClass(MainActivity.this, COActivity.class);
						startActivity(intent);
						break;
					case 3://�������
						intent = new Intent();
						intent.setClass(MainActivity.this, GuardActivity.class);
						startActivity(intent);
						break;
					case 4://������
						intent = new Intent();
						intent.setClass(MainActivity.this, CHActivity.class);
						startActivity(intent);
						break;
					case 5://������
						intent = new Intent();
						intent.setClass(MainActivity.this, VideoActivity.class);
						startActivity(intent);
						break;
					case 6://�������
//						Toast.makeText(MainActivity.this, "����ϵ�����߿���", Toast.LENGTH_SHORT).show();
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
			menu.add(0, Constant.MENU_ITEM1, 0, "����");
			menu.add(0, Constant.MENU_ITEM2, 0, "����");
			menu.add(0, Constant.MENU_ITEM3, 0, "�˳�");
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
				builder.setMessage("			���ܼҾ�-ħ��С��APP\n			��Ȩ��4-bits���Шr(�s���t)�q\n			��ϵ��ʽ���󺰼���( # �� # )\n");
				builder.setPositiveButton("ȷ��", null);
				builder.create().show();
				break;
			case Constant.MENU_ITEM3:
				AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
				builder1.setIcon(R.drawable.app_icon);
				builder1.setTitle("�˳�");
				builder1.setMessage("			��ȷ��Ҫ�˳���(��_��)?");
				builder1.setPositiveButton("�����ˣ��Ҿ�����", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
				builder1.setNegativeButton("������˼���ּ����", null);
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
									connetlayout.setVisibility(8);//����
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
									connetlayout.setVisibility(0);//�ɼ�
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
