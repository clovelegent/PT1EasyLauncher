package com.softsz.pt1easylauncher;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public final class LoadAppsUtil {

	public static final String KEY_SAVEAPPCN = "key_save_app_component_name";
	public static final String SAVEAPPSPREFNAME = "easyLanucherapps";
	public static List<ItemInfo> mAppsData;

	public static boolean isAppInstalled(Context context, String packageName) {
		final PackageManager pm = context.getPackageManager();
		List<PackageInfo> pInfo = pm.getInstalledPackages(0);
		List<String> pName = new ArrayList<String>();
		if (pInfo != null) {
			for (int i = 0; i < pInfo.size(); i++) {
				String pn = pInfo.get(i).packageName;
				pName.add(pn);
			}
		}
		return pName.contains(packageName);
	}

	/*
	 * public static boolean initAppCompName(Context context) { StringBuilder sb
	 * = new StringBuilder(); SharedPreferences appComponentNamePref =
	 * context.getSharedPreferences(SAVEAPPSPREFNAME, Context.MODE_PRIVATE);
	 * SharedPreferences.Editor editor = appComponentNamePref.edit(); String[]
	 * value = context.getResources().getStringArray(R.array.residentApps); for
	 * (int i = 0; i < value.length; i++) { Log.d("xph_LoadAppsUtil", value[i]);
	 * sb.append(value[i]+";"); } editor.putString(KEY_SAVEAPPCN,sb.toString());
	 * return editor.commit(); }
	 */

	public static String getSavedAppCompName(Context context) {
		SharedPreferences appComponentNamePref = context.getSharedPreferences(SAVEAPPSPREFNAME, Context.MODE_PRIVATE);
		String appsCompNm = appComponentNamePref.getString(KEY_SAVEAPPCN, null);
		return appsCompNm;
	}

	public static boolean addAppCompNm(Context context, String value) {
		SharedPreferences appComponentNamePref = context.getSharedPreferences(SAVEAPPSPREFNAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = appComponentNamePref.edit();
		String saved = getSavedAppCompName(context);
		if (value == null)
			return false;
		if (value.trim().isEmpty())
			return false;
		if (saved != null) {
			if (saved.contains(value))
				return false;
		}
		String newPrefApps;
		if (saved == null) {
			newPrefApps = value + ";";
		} else {
			newPrefApps = saved + value + ";";
		}
		editor.putString(KEY_SAVEAPPCN, newPrefApps);
		return editor.commit();
	}
	
	public static boolean updateCompNmSort(Context context, String value) {
		SharedPreferences appComponentNamePref = context.getSharedPreferences(SAVEAPPSPREFNAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = appComponentNamePref.edit();
		editor.putString(KEY_SAVEAPPCN, value);
		return editor.commit();
	}

	public static boolean removeAppCompName(Context context, String value) {
		SharedPreferences appComponentNamePref = context.getSharedPreferences(SAVEAPPSPREFNAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = appComponentNamePref.edit();
		String saved = getSavedAppCompName(context);
		if (value == null)
			return false;
		if (value.trim().isEmpty())
			return false;
		if (!saved.contains(value))
			return false;
		String newPrefApps = saved.replace(value + ";", "");
		editor.putString(KEY_SAVEAPPCN, newPrefApps);
		return editor.commit();
	}

	public static String[] getSharePrefAppsCompNameArray(Context context) {
		String[] apps = new String[] {};
		String saved = getSavedAppCompName(context);
		if (saved == null) {
			apps = null;
		} else if (saved.trim().isEmpty()) {
			apps = null;
		} else {
			apps = saved.split(";");
		}
		return apps;
	}
}
