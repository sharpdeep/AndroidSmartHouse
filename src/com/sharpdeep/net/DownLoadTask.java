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
	 * -2 socket�ر��쳣��-1 socket���ӳ�ʱ�� 1 ���ӳɹ�
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
				if(new String(bytes, 0, 4).equals("AABB")){	//֡ͷ
					MyHouse house = MyHouse.getInstance();
					int id = bytes[4];
					switch(house.getDeviceTypeById(id)){
					case MyHouse.TYPE_ALERT:
						//�����ݼ��뵽MyHouse��
						break;
					case MyHouse.TYPE_MONITOR:
						//�����ݼ��뵽MyHouse��
						break;
					}
				}
			}else{
				//�����ݶ�ʧ
			}
		}
		
		return result;
	}
/**
 * �������ص���������͹㲥���ڹ㲥�д�����ʾ���û����Ӧ����Ϣ
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
			break;		//ʧ��
		case 1:
			if(mContext != null){
				Intent intent = new Intent();
				intent.setAction(MainActivity.MY_INTENT_ACTION);
				intent.putExtra(DOWNLOAD_STATE, true);
				mContext.sendBroadcast(intent);
			}
			break;		//�ɹ�
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	

}
