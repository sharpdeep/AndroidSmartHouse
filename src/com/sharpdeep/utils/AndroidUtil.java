package com.sharpdeep.utils;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;

public class AndroidUtil {

	private static final int Max_NUM = 50;//�õ�service������� 


	/**
	 * �õ��汾�ţ�����õ������򷵻ء��汾��δ֪��
	 * @param context
	 */
	public static String getVersion(Context context){
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String versionName = info.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "�汾��δ֪";
		}
	}

	/**
	 * ����һ����̨���񣬿���ǰ�������жϷ����Ƿ��Ѿ�������
	 * @param context
	 * @param cls
	 */
	public static <T> void startService(Context context, Class<T> cls){
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startService(intent);
	}
	

	/**
	 * ת����һ��Activity
	 * @param context
	 * @param cls
	 */
	public static <T> void startActivity(Activity context, Class<T> cls){
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startActivity(intent);
	}
	/**
	 * �жϷ����Ƿ���������
	 * @param context
	 * @param className
	 * @return
	 */
	public static Boolean isServiceRunning(Context context, String className){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> list = (ArrayList<RunningServiceInfo>) manager.getRunningServices(Max_NUM);
		for(RunningServiceInfo info : list){
			if(info.service.getClassName().equals(className)){
				return true;
			}
		}
		return false;
	}
	/**
	 * �ж��Ƿ������
	 * @param context
	 * @return
	 */
	public static Boolean isOpenNetWork(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(manager.getActiveNetworkInfo() != null){
			return manager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}
	/**
	 * 
	 * @param context
	 * @param smallIcon
	 * @param largeIcon
	 * @param title
	 * @param content
	 * @param newActivity ���������ת�����Դ���null
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Notification getNotification(Context context, int smallIcon, int largeIcon,
			String title, String content, Class newActivity){
		
		Notification.Builder builder = new Builder(context);
		Notification notification;
		builder.setSmallIcon(smallIcon);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setWhen(System.currentTimeMillis());
		if(newActivity != null){
			Intent intent = new Intent(context, newActivity);
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
			builder.setContentIntent(pIntent);
		}
		notification = builder.build();
		return notification;
	}
	
	/**
	 * �������ƽ���һ��Sevice�����ȼ��Sevcie�Ƿ���ڣ����Բ��ü����
	 * @param context
	 * @param name
	 */
	public static void stopSevice(Context context, String name) {
		// TODO Auto-generated method stub
		if(isServiceRunning(context, name)){
			Intent intent = new Intent();
			try {
				intent.setClass(context, context.getClass().forName(name));
				context.stopService(intent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
