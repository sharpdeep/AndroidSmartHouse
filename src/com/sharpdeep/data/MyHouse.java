package com.sharpdeep.data;

import java.util.HashMap;
import java.util.Map;

import android.R.integer;

import com.sharpdeep.model.MyObject;
import com.sharpdeep.object.AlertObject;
import com.sharpdeep.object.MonitorObject;

public class MyHouse {

	private MonitorObject mTemp;
	private MonitorObject mHumi;
	private AlertObject mFireAlert;
	private AlertObject mGuardAlert;
	private AlertObject mCoAlert;
	private AlertObject mChAlert;

	Map<Integer, MyObject> mDevices;
	
	public static final int TEMP_ID = 0;
	public static final int HUMI_ID = 1;
	public static final int FIREALERT_ID = 2;
	public static final int GUARDALERT_ID = 3;
	public static final int COALERT_ID = 4;
	public static final int CHALERT_ID = 5;
	
	public static final int TYPE_MONITOR = 1000;
	public static final int TYPE_ALERT = 1001;
	
	private static MyHouse mMyHouse = new MyHouse();
	
	private MyHouse(){
		initData();
	}
	/**
	 * 初始化数据，把数据放到map，方便管理
	 */
	private void initData(){
		if(mDevices == null){
			mDevices = new HashMap<Integer, MyObject>();
		}
		mDevices.put(TEMP_ID, (mTemp = new MonitorObject(TEMP_ID, false)));
		mDevices.put(HUMI_ID, (mHumi = new MonitorObject(HUMI_ID, false)));
		mDevices.put(FIREALERT_ID, (mFireAlert = new AlertObject(FIREALERT_ID, true)));
		mDevices.put(GUARDALERT_ID, (mGuardAlert = new AlertObject(GUARDALERT_ID, true)));
		mDevices.put(COALERT_ID, (mCoAlert = new AlertObject(COALERT_ID, true)));
		mDevices.put(CHALERT_ID, (mChAlert = new AlertObject(CHALERT_ID, true)));
		//此处可以添加添加设备代码（上次已编辑的设备）
	}
	
	public static MyHouse getInstance(){
		return mMyHouse;
	}
	
	/**
	 * 返回id对应的设备对象，找不到的话会返回null
	 * @param id
	 * @return
	 */
	public MyObject getDeviceById(int id){
		MyObject device = mDevices.get(id);
		return device;
	}
	
	public Integer getDeviceTypeById(int id){
		if(getDeviceById(id) instanceof MonitorObject){
			return TYPE_MONITOR;
		}
		else if(getDeviceById(id) instanceof AlertObject){
			return TYPE_ALERT;
		}
		return null;
	}
	
	/**
	 * 由id得到设备数据，若设备不存在返回null
	 * 返回的类型为Object，需自己根据设备类型转型
	 * @param id
	 * @return
	 */
	public Object getMonitorDeviceDataById(int id){
		Object data = null;
		if(getDeviceTypeById(id) == TYPE_MONITOR){
			data = ((MonitorObject)getDeviceById(id)).getValue();
		}else if(getDeviceTypeById(id) == TYPE_ALERT){
			data = ((AlertObject)getDeviceById(id)).getValue();
		}
		if(getDeviceById(id) != null){
			data = ((MonitorObject)getDeviceById(id)).getValue();
		}
		return data;
	}
	
	public void setDeviceDataById(int id, Object data){
		if(getDeviceTypeById(id) == TYPE_MONITOR){
			((MonitorObject)getDeviceById(id)).setValue((Integer)data);
		}else if(getDeviceTypeById(id) == TYPE_ALERT){
			((AlertObject)getDeviceById(id)).setValue((Boolean)data);
		}
	}
	
}
