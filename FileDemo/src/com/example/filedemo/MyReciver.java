package com.example.filedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
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
	private String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("weitu", "action:"+intent.getAction());
		 //接收安装广播 
		String packageName = intent.getDataString();   
	    if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {   
	        Log.i("weitu", "安装了:"+packageName);
	        Log.i("weitu", "安装了:"+packageName+";size:"+FileObserverService.apks.size());
	        for (int i = 0; i < FileObserverService.apks.size(); i++) {
				String apkName = getApkName(targetPath+FileObserverService.apks.get(i),context);
				if (apkName.equals(packageName)) {
					Log.i("weitu", "same:"+packageName);
				}
			}
	    }else if (intent.getAction().equals(Intent.ACTION_PACKAGE_FIRST_LAUNCH)) {
	    	 Log.i("weitu", "安装了:ACTION_PACKAGE_FIRST_LAUNCH"+packageName);
		}else if (intent.getAction().equals(Intent.ACTION_PACKAGE_RESTARTED)) {
			Log.i("weitu", "安装了:ACTION_PACKAGE_RESTARTED"+packageName);
		}     
	}
	public String getApkName(String filePath,Context context) {
		String archiveFilePath = filePath;// 安装包路径
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			String appName = pm.getApplicationLabel(appInfo).toString();
			String packageName = appInfo.packageName; // 得到安装包名称
			String version = info.versionName; // 得到版本信息
//			Log.i("weitu", "packageName:" + packageName+";appName:"+appName+";version:"+version);
			return packageName;
		}
		return "";
	}
}
