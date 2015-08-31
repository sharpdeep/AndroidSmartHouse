package com.sharpdeep.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	private static Toast mToast;
	private static boolean DEBUG = true;//µ÷ÊÔÄ£Ê½
	
	private ToastUtil(){
		
	}
	
	
	public static void toast(Context context,String text){
		if(mToast == null){
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		else{
			mToast.setText(text);
		}
		mToast.show();
	}
	
	public static void debugToast(Context context,String text){
		if(DEBUG){
			if(mToast == null){
				mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			}
			else{
				mToast.setText(text);
			}
			mToast.show();
		}
	}
}
