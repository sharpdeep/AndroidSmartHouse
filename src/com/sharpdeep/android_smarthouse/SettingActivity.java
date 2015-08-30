package com.sharpdeep.android_smarthouse;


import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.text.format.Time;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SettingActivity extends Activity{
	
	private SharedPreferences settingpreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
		settingpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//设置内容自动存储	
		Calendar calendar = Calendar.getInstance();
//		Toast.makeText(SettingActivity.this, calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
	}
	
	void SaveTime(int type, int hour, int minute){
		SharedPreferences.Editor editor = settingpreferences.edit();
		switch(type)
		{
		case Constant.TYPE_LEFT_HOME_TIME:
			editor.putInt("lefthomehour", hour);
			editor.putInt("lefthomeminute", minute);
			break;
		case Constant.TYPE_COME_BACK_TIME:
			editor.putInt("comebackhour", hour);
			editor.putInt("comebackminute", minute);
			break;
		case Constant.TYPE_SLEEP_TIME:
			editor.putInt("sleephour", hour);
			editor.putInt("sleepminute", minute);
			break;
		case Constant.TYPE_GET_UP_TIME:
			editor.putInt("getuphour", hour);
			editor.putInt("getupminute", minute);
			break;
		}
		editor.commit();
	}
	
	@Override
	protected void onDestroy() {
		if(settingpreferences.getBoolean("connectstate", false))//网络已经连接成功，需要关闭socket先
		{
			Message msg = SocketThread.mhandler.obtainMessage();
			msg.what = Constant.CLOSE_CONNECT_OF_SOCKET;
			SocketThread.mhandler.sendMessage(msg);//销毁socket线程，断开连接
		}
		
		Message msg1 = DataService.servicehandler.obtainMessage();
		msg1.what = Constant.CONNECT_FAILED;
		DataService.servicehandler.sendMessage(msg1);
		super.onDestroy();
	}



	@SuppressLint("ValidFragment")
	class SettingFragment extends PreferenceFragment{
		private CheckBoxPreference lefthomemodel;
		private CheckBoxPreference nightmodel;
		private CheckBoxPreference timer;
		private CheckBoxPreference outside;
		private EditTextPreference outsideip;
		private Preference lefthometime;
		private Preference comebacktime;
		private Preference sleeptime;
		private Preference getuptime;
		private Calendar calendar;
		
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.activity_setting);
			
			lefthomemodel = (CheckBoxPreference)findPreference("lefthome");
			nightmodel = (CheckBoxPreference)findPreference("night");
			timer = (CheckBoxPreference)findPreference("timer");
			outside = (CheckBoxPreference)findPreference("outside");
			outsideip = (EditTextPreference)findPreference("outsideip");
			lefthometime = (Preference)findPreference("lefthometime");
			comebacktime = (Preference)findPreference("comebacktime");
			sleeptime = (Preference)findPreference("sleeptime");
			getuptime = (Preference)findPreference("getuptime");
			calendar = Calendar.getInstance();
			if(timer.isChecked()){
				lefthomemodel.setChecked(false);
				nightmodel.setChecked(false);
				lefthomemodel.setEnabled(false);
				nightmodel.setEnabled(false);
				
				lefthometime.setEnabled(true);
				comebacktime.setEnabled(true);
				sleeptime.setEnabled(true);
				getuptime.setEnabled(true);
				
				lefthometime.setSummary(settingpreferences.getInt("lefthomehour", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + 
										settingpreferences.getInt("lefthomeminute", calendar.get(Calendar.MINUTE)));

				comebacktime.setSummary(settingpreferences.getInt("comebackhour", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + 
										settingpreferences.getInt("comebackminute", calendar.get(Calendar.MINUTE)));

				sleeptime.setSummary(settingpreferences.getInt("sleephour", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + 
									 settingpreferences.getInt("sleepminute", calendar.get(Calendar.MINUTE)));

				getuptime.setSummary(settingpreferences.getInt("getuphour", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + 
									 settingpreferences.getInt("getupminute", calendar.get(Calendar.MINUTE)));
			}
			else{
				lefthomemodel.setEnabled(true);
				nightmodel.setEnabled(true);
				
				lefthometime.setEnabled(false);
				comebacktime.setEnabled(false);
				sleeptime.setEnabled(false);
				getuptime.setEnabled(false);
				
				lefthometime.setSummary("");
				comebacktime.setSummary("");
				sleeptime.setSummary("");
				getuptime.setSummary("");
			}
			
			if(settingpreferences.getBoolean("outside", false)){
				outsideip.setEnabled(true);
				outsideip.setSummary(settingpreferences.getString("outsideip", ""));
			}
			else{
				outsideip.setEnabled(false);
				outsideip.setSummary("");
			}
			
			if(lefthomemodel.isChecked())
			{
				nightmodel.setEnabled(false);
				nightmodel.setChecked(false);
			}
			else
			{
				nightmodel.setEnabled(true);
			}
			
			//离家模式监听
			lefthomemodel.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if(lefthomemodel.isChecked())
					{
						nightmodel.setEnabled(false);
						nightmodel.setChecked(false);
					}
					else
					{
						nightmodel.setEnabled(true);
					}
					return false;
				}
			});
			
			outside.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if(outside.isChecked()){
						outsideip.setEnabled(true);
						outsideip.setSummary(settingpreferences.getString("outsideip", ""));
					}
					else{
						outsideip.setEnabled(false);
						outsideip.setSummary("");
					}
					return false;
				}
			});
			
			//是否开启定时模式监听
			timer.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if(timer.isChecked())
					{
						lefthomemodel.setChecked(false);
						nightmodel.setChecked(false);
						lefthomemodel.setEnabled(false);
						nightmodel.setEnabled(false);
						
						lefthometime.setEnabled(true);
						comebacktime.setEnabled(true);
						sleeptime.setEnabled(true);
						getuptime.setEnabled(true);
						
						lefthometime.setSummary(settingpreferences.getInt("lefthomehour", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + 
												settingpreferences.getInt("lefthomeminute", calendar.get(Calendar.MINUTE)));
						
						comebacktime.setSummary(settingpreferences.getInt("comebackhour", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + 
												settingpreferences.getInt("comebackminute", calendar.get(Calendar.MINUTE)));
						
						sleeptime.setSummary(settingpreferences.getInt("sleephour", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + 
											 settingpreferences.getInt("sleepminute", calendar.get(Calendar.MINUTE)));
						
						getuptime.setSummary(settingpreferences.getInt("getuphour", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + 
											 settingpreferences.getInt("getupminute", calendar.get(Calendar.MINUTE)));

					}
					else
					{
						lefthomemodel.setEnabled(true);
						nightmodel.setEnabled(true);
						
						lefthometime.setEnabled(false);
						comebacktime.setEnabled(false);
						sleeptime.setEnabled(false);
						getuptime.setEnabled(false);
						
						lefthometime.setSummary("");
						comebacktime.setSummary("");
						sleeptime.setSummary("");
						getuptime.setSummary("");
					}
					return false;
				}
			});
			
			lefthometime.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					TimePickerDialog dialog = new TimePickerDialog(SettingActivity.this, null, settingpreferences.getInt("lefthomehour", calendar.HOUR_OF_DAY),
							settingpreferences.getInt("lefthomeminute", calendar.MINUTE) , true){
								@Override
								public void onTimeChanged(TimePicker view,
										int hourOfDay, int minute) {
									super.onTimeChanged(view, hourOfDay, minute);
									SaveTime(Constant.TYPE_LEFT_HOME_TIME, hourOfDay, minute);
								}
						
					};
					dialog.setTitle("离家时间");
					dialog.show();
					return false;
				}
			});
			
			comebacktime.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					TimePickerDialog dialog = new TimePickerDialog(SettingActivity.this, null, settingpreferences.getInt("comebackhour", calendar.HOUR_OF_DAY),
							settingpreferences.getInt("comebackminute", calendar.MINUTE) , true){
								@Override
								public void onTimeChanged(TimePicker view,
										int hourOfDay, int minute) {
									super.onTimeChanged(view, hourOfDay, minute);
									SaveTime(Constant.TYPE_COME_BACK_TIME, hourOfDay, minute);
								}
						
					};
					dialog.setTitle("回家时间");
					dialog.show();
					return false;
				}
			});
			
			sleeptime.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					TimePickerDialog dialog = new TimePickerDialog(SettingActivity.this, null, settingpreferences.getInt("sleephour", calendar.HOUR_OF_DAY),
							settingpreferences.getInt("sleepminute", calendar.MINUTE) , true){
								@Override
								public void onTimeChanged(TimePicker view,
										int hourOfDay, int minute) {
									super.onTimeChanged(view, hourOfDay, minute);
									SaveTime(Constant.TYPE_SLEEP_TIME, hourOfDay, minute);
								}
						
					};
					dialog.setTitle("睡觉时间");
					dialog.show();
					return false;
				}
			});
			
			getuptime.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					TimePickerDialog dialog = new TimePickerDialog(SettingActivity.this, null, settingpreferences.getInt("getuphour", calendar.HOUR_OF_DAY),
							settingpreferences.getInt("getupminute", calendar.MINUTE) , true){
								@Override
								public void onTimeChanged(TimePicker view,
										int hourOfDay, int minute) {
									super.onTimeChanged(view, hourOfDay, minute);
									SaveTime(Constant.TYPE_GET_UP_TIME, hourOfDay, minute);
								}
						
					};
					dialog.setTitle("起床时间");
					dialog.show();
					return false;
				}
			});
		}
		
	}
	
	void init(){
		
	}
}
