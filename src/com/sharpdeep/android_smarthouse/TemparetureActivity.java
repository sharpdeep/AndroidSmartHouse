package com.sharpdeep.android_smarthouse;
//�¶ȼ�ؽ���
//�����ƣ�����δ����ʱ����ʾ
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class TemparetureActivity extends Activity{

	private SharedPreferences tempareturepreferences;
	private TextView tempareturetext;
	private boolean tempareturekey;
	private String tempareture;
	private boolean outtoastflag = false;//�γ���Toast��ʾ�ı�־
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tempareture);
		
		tempareturekey = true;
		tempareturetext = (TextView)findViewById(R.id.tempareturetext);
		tempareturepreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		//����һ���̲߳��϶�ȡ�¶�
		TemparetureThread tempareturethread = new TemparetureThread();
		tempareturethread.start();
	}
	
	class TemparetureThread extends Thread{

		@Override
		public void run() {
			//��sharepreferences�ж�ȡ������runonuithread������ʾ
			while(tempareturekey)
			{
				tempareture = tempareturepreferences.getString("tempareture", "----");
				
				runOnUiThread(new Runnable() {
					public void run() {
						if(tempareture.equals("----")){
							if(!outtoastflag)//δ��ʾ��
							{
								Toast.makeText(TemparetureActivity.this, "�¶ȴ�����δ����", Toast.LENGTH_SHORT).show();
								outtoastflag = true;//�Ѿ���ʾ���ı�־
								tempareturetext.setText(tempareture);
							}
						}
						else{
							tempareturetext.setText(tempareture+"\u2103");
							outtoastflag = false; //��Ϊ�ֲ��봫�����������ݣ������ٴΰγ�������Toast
						}
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			super.run();
		}
	}

	@Override
	protected void onDestroy() {
		//�ر��̵߳�ѭ����ȡ
		tempareturekey =false;
		super.onDestroy();
	}
	
	
}
