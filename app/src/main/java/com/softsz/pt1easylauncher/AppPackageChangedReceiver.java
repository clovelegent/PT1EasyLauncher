package com.softsz.pt1easylauncher;

import java.nio.MappedByteBuffer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppPackageChangedReceiver extends BroadcastReceiver {
	private static final int PACKAGE_NAME_START_INDEX = 8;
	private static final String TAG = "AppInsertReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		
		String Action = intent.getAction();
		Log.d(TAG, "AppInsertReceiver Action = "+Action);
		
		//install app
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
			String data = intent.getDataString();

			if (data == null || data.length() <= PACKAGE_NAME_START_INDEX) {
				return;
			}
			
			String packageName = data.substring(PACKAGE_NAME_START_INDEX);
			Log.d(TAG, "AppInsertReceiver add data = "+data+",packageName = "+packageName);
			
			String[] residentApps = context.getResources().getStringArray(R.array.residentApps);
			for (int i = 0; i < residentApps.length; i++) {
				String appCompName[] = residentApps[i].split("/");
				if(packageName.equals(appCompName[0])){
					ItemInfo ii = EasyLauncher.createItemInfo(true, residentApps[i]);
					if((!LoadAppsUtil.mAppsData.contains(ii)) && LoadAppsUtil.mAppsData.size()<12){
						LoadAppsUtil.mAppsData.add(ii);
					}
				}
			}	
			
		}
		
		//remove app
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
			String data = intent.getDataString();
			
			if (data == null || data.length() <= PACKAGE_NAME_START_INDEX) {
				return;
			}
			
			String packageName = data.substring(PACKAGE_NAME_START_INDEX);
			Log.d(TAG, "AppInsertReceiver remove data = "+data+",packageName = "+packageName);
			for(int i = 0;i<LoadAppsUtil.mAppsData.size();i++){
				if(LoadAppsUtil.mAppsData.get(i).toString().contains(packageName))
					LoadAppsUtil.mAppsData.remove(i);
			}
			
		}
		
		

	}
}
