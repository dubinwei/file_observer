package com.example.filedemo;

import java.util.LinkedList;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;

public class FileObserverService extends Service {
	MyFileObserver observer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}


	public static String getCurrentPk(Context context){
		// 当前正在运行的应用的包名
		ActivityManager am = (ActivityManager) context.getSystemService("activity");
		String currentrunningpk = am.getRunningTasks(1).get(0).topActivity.getPackageName();
		return currentrunningpk;
	}
	private String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("weitu", "service启动了");
		if (observer == null) {
			observer = new MyFileObserver(targetPath);
			observer.startWatching();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	private int writeCounts = 0 ;
	private boolean isAccess = false;
	public static LinkedList<String> apks = new LinkedList<String>();
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
				if (!apks.contains(path)) {
					apks.add(path);
				}
				switch (el) {
				case FileObserver.CREATE:
					Log.i("weitu", "CREATE");
					writeCounts = 0;
					break;
				case FileObserver.CLOSE_WRITE:
					++writeCounts;
					Log.i("weitu", "CLOSE_WRITE:"+writeCounts);
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
