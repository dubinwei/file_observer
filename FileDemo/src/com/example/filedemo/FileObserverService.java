package com.example.filedemo;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;

public class FileObserverService extends Service {
	MyFileObserver observer;
	private HashMap<String, ArrayList<String>> maps = new HashMap<String, ArrayList<String>>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public static File getLatestFile(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
		File newFile = null;
		for (File file : files) {
			if (newFile == null || file.lastModified() > newFile.lastModified()) {
				newFile = file;
			}
		}
		return newFile;
	}

	public String getApkName(String filePath) {
		String archiveFilePath = filePath;// 安装包路径
		PackageManager pm = getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			String appName = pm.getApplicationLabel(appInfo).toString();
			String packageName = appInfo.packageName; // 得到安装包名称
			String version = info.versionName; // 得到版本信息
			Log.i("weitu", "packageName:" + packageName+";appName:"+appName+";version:"+version);
			return packageName;
		}
		return "";
	}
	public static String getCurrentPk(Context context){
		// 当前正在运行的应用的包名
		ActivityManager am = (ActivityManager) context.getSystemService("activity");
		String currentrunningpk = am.getRunningTasks(1).get(0).topActivity.getPackageName();
		return currentrunningpk;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("weitu", "service启动了");
		if (observer == null) {
			observer = new MyFileObserver(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/");
			observer.startWatching();
			IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
			filter.addAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH);
			filter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
			registerReceiver(reciver, filter);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(reciver);
	}
	private ApkAddReciver reciver = new ApkAddReciver();
	private final class ApkAddReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			 //接收安装广播 
			String packageName = intent.getDataString();   
		    if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {   
		        Log.i("weitu", "安装了:"+packageName);
		    }else if (intent.getAction().equals(Intent.ACTION_PACKAGE_FIRST_LAUNCH)) {
		    	 Log.i("weitu", "安装了:ACTION_PACKAGE_FIRST_LAUNCH"+packageName);
			}else if (intent.getAction().equals(Intent.ACTION_PACKAGE_RESTARTED)) {
				Log.i("weitu", "安装了:ACTION_PACKAGE_RESTARTED"+packageName);
			}      
		}
	}
	class MyFileObserver extends FileObserver {

		// 为了防止嵌套消息调用，建议使用 super(String,int) 按需创建监控消息值
		public MyFileObserver(String path) {
			super(path, FileObserver.CLOSE_WRITE | FileObserver.CREATE
					| FileObserver.OPEN | FileObserver.ACCESS);
		}

		@Override
		public void onEvent(int event, String path) {
			synchronized (this) {
				// 这里注意 event 值是与 0x40000000或上后的值，所以需要case时需要先进行
				// &FileObserver.ALL_EVENTS
				int el = event & FileObserver.ALL_EVENTS;
				switch (el) {
				case FileObserver.CREATE:
					File file = getLatestFile(path);
					Log.i("weitu", "CREATE:"+file.getName());
					Log.i("weitu", "CREATE");
					break;
				case FileObserver.CLOSE_WRITE:
					Log.i("weitu", "CLOSE_WRITE");
					break;
				case FileObserver.OPEN:
					Log.i("weitu", "OPEN");
					break;
				case FileObserver.ACCESS:
					Log.i("weitu", "ACCESS");
					break;
				}
			}
		}

	}
}
