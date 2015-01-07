package com.example.filedemo;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, FileObserverService.class));
//        File latest = FileObserverService.getLatestFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/");
//        Log.i("baadoo", "latest:"+getApkName(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/"+"kao.apk"));
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
