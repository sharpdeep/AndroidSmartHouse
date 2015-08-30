package com.sharpdeep.android_smarthouse;
/*
 * 创建socket连接，并将inputstream中的数据存储在sharepreferences中，key为message_input
 * 目前还差数据的处理部分
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SocketThread extends Thread{

	private String ip;
	private int port = Constant.DATA_URL_PORT;
	private OutputStream outputstream;
	private InputStream inputstream;
	private Socket socket;
	private SocketAddress address;
	public static Handler mhandler;
	private boolean key = false;
	private SharedPreferences sharepreferences;
	private Context context;
	private boolean connet_key = false;

	public SocketThread(Context context) {
		
		this.context = context;
		sharepreferences = PreferenceManager.getDefaultSharedPreferences(context);
		
	}
	
	

	public void connect(){
		if(!sharepreferences.getBoolean("outside", false) || (sharepreferences.getBoolean("inside", false)&&sharepreferences.getBoolean("outside", false)))//第二个不选时,或者都选
		{
			ip = Constant.DATA_URL_IP;
		}
		if(!sharepreferences.getBoolean("inside", false) && sharepreferences.getBoolean("outside", false))
		{
			ip = sharepreferences.getString("outsideip", "");
		}
		address= new InetSocketAddress(ip, port);
		socket = new Socket();
		
		try {
			socket.connect(address, 5000);
			outputstream = socket.getOutputStream();
			inputstream = socket.getInputStream();
			connet_key = true;
			SendMessage(Constant.CONNECT_SUCCESS);
			
			key = true;
			
			RXThread rxthread = new RXThread();
			rxthread.start();
			
			TxThread txthread = new TxThread();
			txthread.start();
			
			TimeThread timethread = new TimeThread();
			timethread.start();

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SendMessage(Constant.CONNECT_FAILED);
		}
	}
	
	/*
	 * 发送消息给Service
	 */
	void SendMessage(int what)
	{
		Message msg = DataService.servicehandler.obtainMessage(what);
		DataService.servicehandler.sendMessage(msg);
	}
	
	

	@Override
	public void run() {
		connect();
		InitHandler();
	}
	
	void InitHandler(){
		
		Looper.prepare();
		mhandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				int flag = msg.what;
				try {
					switch(flag)
					{
					case Constant.SEND_MESSAGE_BY_SOCKET:
						outputstream.write(((String)msg.obj).getBytes());
						outputstream.flush();
						break;
					case Constant.CLOSE_CONNECT_OF_SOCKET:
						key = false;
						outputstream.close();
						inputstream.close();
						socket.close();
						mhandler.getLooper().quit();//结束消息队列
						break;
					case Constant.CLOSE_CONNECT_AND_CONNECT_AGAIN:
						//重新连接服务器直到成功连接
						connect();
						break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.handleMessage(msg);
			}
			
		};
		Looper.loop();
	}
	
	void  SavePreferences (String key, String str){
		SharedPreferences.Editor editor = sharepreferences.edit();
		editor.putString(key, str);
		editor.commit();
	}
	
	void  SavePreferences (String key, boolean alarm){
		SharedPreferences.Editor editor = sharepreferences.edit();
		editor.putBoolean(key, alarm);
		editor.commit();
	}
	
	void SavePreferences(int type,String str)
	{
		switch(type)
		{
		case Constant.TYPE_TEMPARETURE:
			SavePreferences("tempareture", str);
			break;
			
		case Constant.TYPE_HUMIDITY:
			SavePreferences("humidity", str);
			break;
		}
	}
	
	void SavePreferences(int type, boolean flag)
	{
		switch(type)
		{
		case Constant.TYPE_DANGER:
			SavePreferences("dangeralarm", flag);
			break;
			
		case Constant.TYPE_FIRE:
			SavePreferences("firealarm", flag);
			break;
			
		case Constant.TYPE_ISDANGERIN:
			SavePreferences("isdangerin", flag);
			break;
			
		case Constant.TYPE_ISFIREIN:
			SavePreferences("isfirein", flag);
			break;
			
		case Constant.TYPE_EXTRA:
			SavePreferences("extra", flag);
			break;
			
		case Constant.TYPE_CO:
			SavePreferences("coalarm", flag);
			break;
			
		case Constant.TYPE_ISCOIN:
			SavePreferences("iscoin", flag);
			break;
			
		case Constant.TYPE_ISCHIN:
			SavePreferences("ischin", flag);
			break;
			
		case Constant.TYPE_CH:
			SavePreferences("chalarm", flag);
			break;
		}
	}
	//模式处理判断是否要报警
	boolean isAlarm(){
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		boolean flag = false;
		if(sharepreferences.getBoolean("timer", false))//如果开启定时模式
		{
			//如果目前时间大于回家时间但是小于睡觉时间则不报警
			if(hour >= sharepreferences.getInt("comebackhour", 0) && hour <= sharepreferences.getInt("sleephour", 0)){
				if(hour == sharepreferences.getInt("comebackhour", 0) && minute <= sharepreferences.getInt("comebackminute", 0))
				{
					flag = true;
				}
				if(hour == sharepreferences.getInt("sleephour", 0) && minute >= sharepreferences.getInt("sleepminute", 0))
				{
					flag = true;
				}
				else
					flag = false;
			}
			//如果当前时间大于起床时间并且小于离家时间则不报警
			if(hour >= sharepreferences.getInt("getuphour", 0) && hour <= sharepreferences.getInt("lefthomehour", 0)){
				if(hour == sharepreferences.getInt("getuphour", 0) && minute <= sharepreferences.getInt("getupminute", 0))
				{
					flag = true;
				}
				if(hour == sharepreferences.getInt("lefthomehour", 0) && minute >= sharepreferences.getInt("lefthourminute", 0))
				{
					flag = true;
				}
				else
					flag = false;
			}
			//其他情况均会正常报警
			else
				flag = true;
			return flag;
		}
		//如果不开启定时模式
		else{
			//如果在睡觉或者离开家都会开启报警
			if(sharepreferences.getBoolean("night", true) || sharepreferences.getBoolean("lefthome", true))
			{
				return true;
			}
			//其他情况不报警
			return false;
		}
	}
	
	class RXThread extends Thread{

		byte[] readbyte = new byte[1024];
		@Override
		public void run() {
			while(key)
			{
				try {
					int readsize = inputstream.read(readbyte);
					if(readsize == 2)//两个字节数组
					{
						switch(readbyte[0])
						{
						case 0x70:
							//温度传感器拔出,拔出统一存储为"----"
							SavePreferences(Constant.TYPE_TEMPARETURE, "----");
							break;
						case 0x71:
							//温度传感器插入，两个字节数据，存储
							Integer temp = (int) readbyte[1];
							SavePreferences(Constant.TYPE_TEMPARETURE, temp.toString());
							break;
						case 0x20:
							SavePreferences(Constant.TYPE_HUMIDITY, "----");
							//湿度传感器拔出，存储为"----"
							break;
						case 0x21:
							//湿度传感器插入，两个字节数据,存储
							Integer temp1 = (int) readbyte[1];
							SavePreferences(Constant.TYPE_HUMIDITY, temp1.toString());
							break;
						case 0x30:
							//人体红外拔出
							SendMessage(Constant.DANGER_OUT);
							SavePreferences(Constant.TYPE_ISDANGERIN, false);//拔出
							break;
						case 0x31:
							SendMessage(Constant.DANGER_IN);
							SavePreferences(Constant.TYPE_ISDANGERIN, true);//插入
							if(readbyte[1] == 0x01)
							{
								if(isAlarm())
								{
									SavePreferences(Constant.TYPE_DANGER, true);
								}
							}
							if(readbyte[1] == 0x00)
							{
								SavePreferences(Constant.TYPE_DANGER, false);
							}
							//如果出现警报，存储
							break;
						case 0x40:
							SendMessage(Constant.COOUT);
							SavePreferences(Constant.TYPE_ISCOIN, false);
							break;
						case 0x41:
							SendMessage(Constant.COIN);
							SavePreferences(Constant.TYPE_ISCOIN, true);
							if(readbyte[1] == 0x01)
							{
								SavePreferences(Constant.TYPE_CO, true);
							}
							
							if(readbyte[1] == 0x00)
							{
								SavePreferences(Constant.TYPE_CO, false);
							}
							//如果出现警报，存储
							break;
						case 0x50://天然气
							SendMessage(Constant.CHOUT);
							SavePreferences(Constant.TYPE_ISCHIN, false);
							break;
						case 0x51:
							SendMessage(Constant.CHIN);
							SavePreferences(Constant.TYPE_ISCHIN, true);
							if(readbyte[1] == 0x01)
							{
								SavePreferences(Constant.TYPE_CH, true);
							}
							if(readbyte[1] == 0x00)
							{
								SavePreferences(Constant.TYPE_CH, false);
							}
							//如果出现警报，存储
							break;
						case 0x61://紧急开关被按下
							if(readbyte[1] == 0x01)
							{
								SavePreferences(Constant.TYPE_EXTRA, true);
							}
							if(readbyte[1] == 0x00)//解除警报
							{
								SavePreferences(Constant.TYPE_EXTRA, false);
							}
							break;
							
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	class TxThread extends Thread{
		@Override
		public void run() {
			while(key)
			{
				try {
					//设置命令发送
					if(sharepreferences.getBoolean("settingedit", false))//改在提示成功后存入true
					{
						Thread.sleep(1000);
						SavePreferences("settingedit", false);
						byte[] sendbyte = new byte[1];
						//如果开启定时模式
						if(sharepreferences.getBoolean("timer", false)){
							//离家命令
							if(sharepreferences.getBoolean("lefthomecommand", false)){
								sendbyte[0] = 0x1E;
								SharedPreferences.Editor editor = sharepreferences.edit();
								editor.putBoolean("lefthomecommand",false);
								editor.commit();
							}
							else if(sharepreferences.getBoolean("comebackcommand", false)){
								sendbyte[0] = 0x1D;
								SharedPreferences.Editor editor = sharepreferences.edit();
								editor.putBoolean("comebackcommand",false);
								editor.commit();
							}
						}
						//未开启定时模式则需要进一步判断
						else{
							if(!sharepreferences.getBoolean("lefthome", false))
							{
								if(sharepreferences.getBoolean("night", false))
								{
									sendbyte[0] = 0x1D;
								}
								else
								{
									sendbyte[0] = 0x1E;
								}
							}
							else
							{
								sendbyte[0] = 0x1E;
							}
						}
						outputstream.write(sendbyte);
						outputstream.flush();
					}
					//小车控制命令
					if(sharepreferences.getBoolean("buttonclick", false))
					{
						SavePreferences("buttonclick", false);
						outputstream.write(sharepreferences.getString("way", null).getBytes());
						outputstream.flush();
					}
					//清除警报
					if(sharepreferences.getBoolean("ClearWarnning", false)){
						SharedPreferences.Editor editor = sharepreferences.edit();
						editor.putBoolean("ClearWarnning", false);
						editor.commit();
						outputstream.write(0x0f);
						outputstream.flush();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	class TimeThread extends Thread{
		@Override
		public void run() {
			while(key){
				Calendar calendar = Calendar.getInstance();
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
	
				//如果开启定时模式
				if(sharepreferences.getBoolean("timer", false))
				{
					//到了离家时间
					if(hour == sharepreferences.getInt("lefthomehour", 0) && minute == sharepreferences.getInt("lefthomeminute", 0))
					{
						//准备发出离家指令
						SharedPreferences.Editor editor = sharepreferences.edit();
						editor.putBoolean("settingedit", true);
						editor.putBoolean("lefthomecommand",true);
						editor.commit();
					}
					//到了回家时间
					if(hour == sharepreferences.getInt("comebackhour", 0) && minute == sharepreferences.getInt("comebackminute", 0)){
						
						SharedPreferences.Editor editor = sharepreferences.edit();
						editor.putBoolean("settingedit", true);
						editor.putBoolean("comebackcommand", true);
						editor.commit();
					}
				}
			}
		}
		
	}
}

	
