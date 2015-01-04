package com.example.filedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 
 * 类名：MyReciver.java
 *	
 * 陕西朗恩科技信息有限公司
 *
 * 标准版
 * 
 * 功能说明：xxxx
 * 
 * @author duweibin
 *
 * 2014-12-18 下午12:46:59
 *
 */
public class MyReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("baadoo", "action:"+intent.getAction());
		 //接收安装广播 
		String packageName = intent.getDataString();   
	    if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {   
	        Log.i("baadoo", "安装了:"+packageName);
	    }else if (intent.getAction().equals(Intent.ACTION_PACKAGE_FIRST_LAUNCH)) {
	    	 Log.i("baadoo", "安装了:ACTION_PACKAGE_FIRST_LAUNCH"+packageName);
		}else if (intent.getAction().equals(Intent.ACTION_PACKAGE_RESTARTED)) {
			Log.i("baadoo", "安装了:ACTION_PACKAGE_RESTARTED"+packageName);
		}     
	}

}
