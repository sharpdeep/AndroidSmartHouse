package com.sharpdeep.backstage;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sharpdeep.data.Constant;
import com.sharpdeep.smarthouse.ui.MainActivity;
import com.sharpdeep.smarthouse.ui.R;
import com.sharpdeep.utils.AndroidUtil;
import com.sharpdeep.utils.ToastUtil;

public class DataService extends Service{

	private Notification mNotification;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ToastUtil.debugToast(DataService.this, "Service Create");
		//		DownLoadTask task = new DownLoadTask();
		//		task.execute(this);
		/*
		new Thread(){

			@Override
			public void run() {
				SocketClient client = SocketClient.getInstance();
				client.connect(SocketClient.DEFAULT_URL, SocketClient.DEFAULT_PORT);
				MyLog.e("DEBUG", "连接成功");
				while(true){
					if(client.isClose()){
						MyLog.e("DEBUG", "socket关闭");
					}
					if(!client.isConnect()){
						MyLog.e("DEBUG", "断开连接");
					}
					else{
						byte[] bytes = client.getMessage();
						MyLog.e("DEBUG", new String(bytes));
					}
				}
			};

		}.start();
		 */
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mNotification = AndroidUtil.getNotification(this, R.drawable.myic, R.drawable.myic, 
				getResources().getString(R.string.project_name), getResources().getString(R.string.notification_tip),MainActivity.class);
		
		startForeground(Constant.MAIN_NOTIFICATION_ID, mNotification);//移到前台，防止被kill
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		stopForeground(true);//退出时擦屁股
		super.onDestroy();
	}
}
