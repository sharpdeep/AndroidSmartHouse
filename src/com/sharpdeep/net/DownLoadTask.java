package com.sharpdeep.net;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sharpdeep.data.MyHouse;
import com.sharpdeep.smarthouse.ui.MainActivity;

public class DownLoadTask extends AsyncTask<Context, Void, Integer>{
	
	Context mContext;
	public static final String DOWNLOAD_STATE = "DownloadState";
	/**
	 * -2 socket关闭异常；-1 socket连接超时； 1 连接成功
	 */
	@Override
	protected Integer doInBackground(Context... context) {
		mContext = context[0];
		SocketClient client = SocketClient.getInstance();
		int result;
		if((result = client.connect(SocketClient.DEFAULT_URL, SocketClient.DEFAULT_PORT)) == 1){
			byte[] bytes = client.getMessage();
			client.disconnet();
			if(bytes.length == 11){
				if(new String(bytes, 0, 4).equals("AABB")){	//帧头
					MyHouse house = MyHouse.getInstance();
					int id = bytes[4];
					switch(house.getDeviceTypeById(id)){
					case MyHouse.TYPE_ALERT:
						//将数据加入到MyHouse中
						break;
					case MyHouse.TYPE_MONITOR:
						//将数据加入到MyHouse中
						break;
					}
				}
			}else{
				//有数据丢失
			}
		}
		
		return result;
	}
/**
 * 根据下载的情况，发送广播，在广播中处理并显示给用户相对应的信息
 * @param result
 */
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		switch(result){
		case -1:
		case -2:
			if(mContext != null){
				Intent intent = new Intent();
				intent.setAction(MainActivity.MY_INTENT_ACTION);
				intent.putExtra(DOWNLOAD_STATE, false);
				mContext.sendBroadcast(intent);
			}
			break;		//失败
		case 1:
			if(mContext != null){
				Intent intent = new Intent();
				intent.setAction(MainActivity.MY_INTENT_ACTION);
				intent.putExtra(DOWNLOAD_STATE, true);
				mContext.sendBroadcast(intent);
			}
			break;		//成功
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	

}
