package com.sharpdeep.android_smarthouse;

import com.sharpdeep.android_smarthouse.MjpegView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.storage.OnObbStateChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

public class VideoActivity extends Activity{
	
	private MjpegView backgroundView = null;

    private String CameralUrl;

    private final int WIFI_STATE_UNKNOW = 0x3000;
    private final int WIFI_STATE_DISABLED = 0x3001;
    private final int WIFI_STATE_NOT_CONNECTED = 0x3002;
    private final int WIFI_STATE_CONNECTED = 0x3003;
//    private final String WIFI_SSID_PERFIX = "robot";
    
    private Context mContext;
    
	
	private ImageButton forwardbutton;
	private ImageButton backwardbutton;
	private ImageButton leftbutton;
	private ImageButton rightbutton;
	
	private SharedPreferences videopreferences;
	
	private final byte[] forwardcommand = {0x2F};
	private final byte[] backwardcommand = {0x2E};
	private final byte[] leftcommand = {0x2D};
	private final byte[] rightcommand = {0x2C};
	
	private ButtonListener listener;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mContext = this;
		
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//��ȥ���⣨Ӧ�õ����ֱ���Ҫд��setContentView֮ǰ����������쳣��
        //ȫ��
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_video);
		
		forwardbutton = (ImageButton)findViewById(R.id.btnForward);
		backwardbutton = (ImageButton)findViewById(R.id.btnBack);
		leftbutton = (ImageButton)findViewById(R.id.btnLeft);
		rightbutton = (ImageButton)findViewById(R.id.btnRight);
		backgroundView = (MjpegView)findViewById(R.id.mySurfaceView1);
	    videopreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    listener = new ButtonListener();
	    
	    forwardbutton.setOnTouchListener(listener);
	    backwardbutton.setOnTouchListener(listener);
	    leftbutton.setOnTouchListener(listener);
	    rightbutton.setOnTouchListener(listener);
	    
		//����·����
	    connectToRouter();
	}
	
	//��ȡwifi״̬����ʼ��Ϊδ֪��ֻ�д�wifi���ҽ���SSID�к���robot��wifi�Ż᷵�ؿ���״̬
    private int getWifiStatus () {
        int status = WIFI_STATE_UNKNOW;
        WifiManager mWifiMng = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        switch (mWifiMng.getWifiState()) {
        case WifiManager.WIFI_STATE_DISABLED:
        case WifiManager.WIFI_STATE_DISABLING:    
        case WifiManager.WIFI_STATE_ENABLING:
        case WifiManager.WIFI_STATE_UNKNOWN:
            status = WIFI_STATE_DISABLED;
            break;
        case WifiManager.WIFI_STATE_ENABLED:
            status = WIFI_STATE_NOT_CONNECTED;
            ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            State wifiState = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (State.CONNECTED == wifiState) {
                WifiInfo info = mWifiMng.getConnectionInfo();
                if (null != info) {
                    String bSSID = info.getBSSID();
                    String SSID = info.getSSID();
                    Log.i("Socket", "getWifiStatus bssid=" + bSSID + " ssid=" + SSID);
                    if (null != SSID && SSID.length() > 0) {
    //                    if (SSID.toLowerCase().contains(WIFI_SSID_PERFIX)) {
                            status = WIFI_STATE_CONNECTED;
    //                    }
                    }
                }
            }
            break;
        default:
            break;
        }
        return status;
    }
    
	//����·����
    private void connectToRouter() {
        int status = getWifiStatus();
        if (WIFI_STATE_CONNECTED == status) {

            if(!videopreferences.getBoolean("outside", false) || (videopreferences.getBoolean("inside", false)&&videopreferences.getBoolean("outside", false)))//�ڶ�����ѡʱ,���߶�ѡ
    		{
    			CameralUrl = Constant.CAMERALURL;
    		}
    		if(!videopreferences.getBoolean("inside", false) && videopreferences.getBoolean("outside", false))
    		{
    			CameralUrl = "http://"+videopreferences.getString("outsideip", "192.168.1.1")+":8080/?action=stream";
    		}

            if (null != CameralUrl && CameralUrl.length() > 4) {
            	backgroundView.setSource(CameralUrl);//��ʼ��Camera
            }
        }
    }
    
    void SaveCommand(byte[] commandbyte){
    	SharedPreferences.Editor editor = videopreferences.edit();
		editor.putBoolean("buttonclick", true);//��־�Ű�ť����
		editor.putString("way", new String(commandbyte));//���巽��
		editor.commit();
    }
    
    class ButtonListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ImageButton imagebutton = (ImageButton)v;
			int action = event.getAction();
			switch(imagebutton.getId())
			{
			case R.id.btnForward:
				if(action == MotionEvent.ACTION_DOWN)
				{
					byte[] commandbyte = new byte[1];
					forwardbutton.setImageResource(R.drawable.sym_forward_1);
					commandbyte = forwardcommand;
					SaveCommand(commandbyte);
				}
				if(action == MotionEvent.ACTION_UP)
					forwardbutton.setImageResource(R.drawable.sym_forward);
				break;
			case R.id.btnBack:
				if(action == MotionEvent.ACTION_DOWN)
				{
					byte[] commandbyte = new byte[1];
					backwardbutton.setImageResource(R.drawable.sym_backward_1);
					commandbyte = backwardcommand;
					SaveCommand(commandbyte);
				}
				if(action == MotionEvent.ACTION_UP)
					backwardbutton.setImageResource(R.drawable.sym_backward);
				break;
			case R.id.btnLeft:
				if(action == MotionEvent.ACTION_DOWN)
				{
					byte[] commandbyte = new byte[1];
					leftbutton.setImageResource(R.drawable.sym_left_1);
					commandbyte = leftcommand;
					SaveCommand(commandbyte);
				}
				if(action == MotionEvent.ACTION_UP)
					leftbutton.setImageResource(R.drawable.sym_left);
				break;
			case R.id.btnRight:
				if(action == MotionEvent.ACTION_DOWN)
				{
					byte[] commandbyte = new byte[1];
					rightbutton.setImageResource(R.drawable.sym_right_1);
					commandbyte = rightcommand;
					SaveCommand(commandbyte);
				}
				if(action == MotionEvent.ACTION_UP)
					rightbutton.setImageResource(R.drawable.sym_right);
				break;
			}
			return true;
		}  	
    }
    
    
}
