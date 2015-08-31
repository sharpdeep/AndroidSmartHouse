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

	private static final int Max_NUM = 50;//得到service最大数量 


	/**
	 * 得到版本号，如果得到不到则返回“版本号未知”
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
			return "版本号未知";
		}
	}

	/**
	 * 开启一个后台服务，开启前可以先判断服务是否已经在运行
	 * @param context
	 * @param cls
	 */
	public static <T> void startService(Context context, Class<T> cls){
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startService(intent);
	}
	

	/**
	 * 转到另一个Activity
	 * @param context
	 * @param cls
	 */
	public static <T> void startActivity(Activity context, Class<T> cls){
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startActivity(intent);
	}
	/**
	 * 判断服务是否正在运行
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
	 * 判断是否打开网络
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
	 * @param newActivity 如果不想跳转，可以传入null
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
	 * 根据名称结束一个Sevice，会先检查Sevcie是否存在，所以不用检查了
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
