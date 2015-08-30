package com.sharpdeep.android_smarthouse;


import android.R.bool;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class DataService extends Service{

	private SharedPreferences sharepreferences;
	private SocketThread socketthread;
	public static Handler servicehandler;
	private boolean dangerinflag = true;
	private boolean fireinflag = true;
	private boolean chinflag = true;
	private boolean coinflag = true;
	private boolean dangeralarm = false;
	private boolean firealarm = false;
	private boolean coalarm = false;
	private boolean chalarm = false;
	private AlarmThread alarmthread;
	private boolean dangernotification = false;
	private boolean firenotification = false;
	private boolean conotification = false;
	private boolean chnotification = false;
	private Toast mtoast;
	private boolean failedtoastflag = false;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		//�洢Service��״̬
		super.onCreate();
		sharepreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SaveServiceState(Constant.SERVICE_STATE_CREATE);
		Toast.makeText(getApplicationContext(), "Service Create", Toast.LENGTH_SHORT).show();
		socketthread = new SocketThread( getApplicationContext());
		socketthread.start();
		alarmthread = new AlarmThread();
		alarmthread.start();
		InitServiceHandler();
		
	}

	void SaveConnetedState(boolean flag){
		SharedPreferences.Editor editor = sharepreferences.edit();
		editor.putBoolean("connectstate", flag);
		editor.commit();
	}
	
	void SaveServiceState(int state){
		SharedPreferences.Editor editor = sharepreferences.edit();
		editor.putInt("Service_State", state);
		editor.commit();
	}
	
	@Override
	public void onDestroy() {
		SaveServiceState(Constant.SERVICE_STATE_CREATE);
		//�������ٵ�ʱ��ر�socket�͸�����
		Message msg	= socketthread.mhandler.obtainMessage(Constant.CLOSE_CONNECT_OF_SOCKET);
		socketthread.mhandler.sendMessage(msg);
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(getApplicationContext(), "Service Start, and ID is:"+startId, Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}
	
	void InitServiceHandler(){
		servicehandler = new Handler(){

			@SuppressWarnings("deprecation")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch(msg.what)
				{
				case Constant.CONNECT_SUCCESS:
					Toast.makeText(getApplicationContext(), "���ӷ������ɹ�",Toast.LENGTH_SHORT).show();
					failedtoastflag = false;
					if(!sharepreferences.getBoolean("timer", false))//��ʱģʽδ����
					{
						SharedPreferences.Editor editor = sharepreferences.edit();
						editor.putBoolean("settingedit", true);
						editor.commit();
					}
					SaveConnetedState(true);
					break;
				case Constant.CONNECT_FAILED:
					if(!failedtoastflag)
					{
						Toast.makeText(getApplicationContext(), "���ӷ�����ʧ�ܣ����������С���", Toast.LENGTH_SHORT).show();
						failedtoastflag =true;
						SaveConnetedState(false);
					}
					if(!socketthread.isAlive())
					{
						socketthread = new SocketThread(getApplicationContext());
						socketthread.start();
					}
					Message msg1 = socketthread.mhandler.obtainMessage(Constant.CLOSE_CONNECT_AND_CONNECT_AGAIN);
					socketthread.mhandler.sendMessageDelayed(msg1, Constant.CONNECT_AGAIN_DELAY);
					break;	
				case Constant.DANGER_OUT:
					dangerinflag = false;
					break;
				case Constant.DANGER_IN:
					dangerinflag = true;
					break;
				case Constant.FIRE_OUT:
					fireinflag =false;
					break;
				case Constant.FIRE_IN:
					fireinflag = true;
					break;
				case Constant.COOUT:
					coinflag =false;
					break;
				case Constant.COIN:
					coinflag = true;
					break;
				case Constant.CHOUT:
					chinflag =false;
					break;
				case Constant.CHIN:
					chinflag = true;
				}
			}
			
		};
	}
	
	class AlarmThread extends Thread{

		public void run() {
			while(true)
			{
				if(dangerinflag || fireinflag || coinflag || chinflag)//��һ������������
				{
					dangeralarm = sharepreferences.getBoolean("dangeralarm", false);
					firealarm = sharepreferences.getBoolean("firealarm", false);
					coalarm = sharepreferences.getBoolean("coalarm", false);
					chalarm = sharepreferences.getBoolean("chalarm", false);
					
					if(dangeralarm && !dangernotification)//��Σ�ղ���δ֪ͨ
					{
						BuildeNotification(Constant.DANGER_NOTIFICATION);
						dangernotification = true;
						//֪ͨ��������Ч
					}
					
					if(!dangeralarm)//Σ�վ������
					{
						dangernotification =false;//�ɽ��еڶ���֪ͨ
					}
					
					if(coalarm && !conotification)//��Σ�ղ���δ֪ͨ
					{
						BuildeNotification(Constant.CO_NOTIFICATION);
						conotification = true;
						//֪ͨ��������Ч
					}
					
					if(!coalarm)
					{
						conotification = false;
					}
					
					if(chalarm && !chnotification)//��Σ�ղ���δ֪ͨ
					{
						BuildeNotification(Constant.CH_NOTIFICATION);
						chnotification = true;
						//֪ͨ��������Ч
					}
					
					if(!chalarm)
					{
						chnotification = false;
					}
					
				}
				if(sharepreferences.getBoolean("extra", false))
				{
					BuildeNotification(Constant.EXTRA_NOTIFICATION);
					SharedPreferences.Editor editor = sharepreferences.edit();
					editor.putBoolean("extra", false);
					editor.commit();
				}
			}
		}
		
	}
	
	void BuildeNotification(int alarm_type){
		
		switch(alarm_type)
		{
		case Constant.DANGER_NOTIFICATION:
			Uri uri = null; 
	    	//���֪ͨ������
	        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	        //����һ��֪ͨ����(��Ҫ���ݵĲ���������,�ֱ���ͼ��,����� ʱ��)
	        Notification notification = new Notification(R.drawable.myic,"����",System.currentTimeMillis());
	        Intent intent = new Intent(DataService.this,MainActivity.class);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        PendingIntent pendingIntent = PendingIntent.getActivity(DataService.this, 0, intent, 0);    
	        notification.setLatestEventInfo(getApplicationContext(), "Σ�վ���", "��������", pendingIntent);
	        notification.flags = Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;
	        notification.sound = uri.parse("android.resource://" + getPackageName() + "/" +R.raw.danger);
	        manager.notify(Constant.DANGER_NOTIFICATION_ID, notification);//����֪ͨ,id���Լ�ָ����ÿһ��Notification��Ӧ��Ψһ��־
	        //��ʵ�����idû�б�Ҫ����,ֻ��Ϊ������Ҫ�õ����Ž���������
			break;
		case Constant.EXTRA_NOTIFICATION:
			Uri uri2 = null; 
	        NotificationManager manager2 = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	        Notification notification2 = new Notification(R.drawable.myic,"����",System.currentTimeMillis());
	        Intent intent2 = new Intent(DataService.this,MainActivity.class);
	        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        PendingIntent pendingIntent2 = PendingIntent.getActivity(DataService.this, 0, intent2, 0);    
	        notification2.setLatestEventInfo(getApplicationContext(), "���⾯��", "��Ҳ��֪������ʲô����", pendingIntent2);
	        notification2.flags = Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;
	        notification2.sound = uri2.parse("android.resource://" + getPackageName() + "/" +R.raw.fire);
	        manager2.notify(Constant.FIRE_NOTIFICATION_ID, notification2);
			break;
		case Constant.CO_NOTIFICATION:
			Uri uri3 = null; 
	        NotificationManager manager3 = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	        Notification notification3 = new Notification(R.drawable.myic,"����",System.currentTimeMillis());
	        Intent intent3 = new Intent(DataService.this,MainActivity.class);
	        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        PendingIntent pendingIntent3 = PendingIntent.getActivity(DataService.this, 0, intent3, 0);    
	        notification3.setLatestEventInfo(getApplicationContext(), "ú������", "���ܷ�����ú��й©��", pendingIntent3);
	        notification3.flags = Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;
	        notification3.sound = uri3.parse("android.resource://" + getPackageName() + "/" +R.raw.fire);
	        manager3.notify(Constant.FIRE_NOTIFICATION_ID, notification3);
			break;
		case Constant.CH_NOTIFICATION:
			Uri uri4 = null; 
	        NotificationManager manager4 = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	        Notification notification4 = new Notification(R.drawable.myic,"����",System.currentTimeMillis());
	        Intent intent4 = new Intent(DataService.this,MainActivity.class);
	        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        PendingIntent pendingIntent4 = PendingIntent.getActivity(DataService.this, 0, intent4, 0);    
	        notification4.setLatestEventInfo(getApplicationContext(), "��Ȼ������", "���ܷ�����Ȼ��й©��", pendingIntent4);
	        notification4.flags = Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;
	        notification4.sound = uri4.parse("android.resource://" + getPackageName() + "/" +R.raw.fire);
	        manager4.notify(Constant.FIRE_NOTIFICATION_ID, notification4);
			break;
		}
	}

}
