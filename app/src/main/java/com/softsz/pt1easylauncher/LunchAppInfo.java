package com.softsz.pt1easylauncher;

import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;

/**
 * 
 * @author xph
 * class for get launcher activity info
 */
public class LunchAppInfo {
	private LauncherActivityInfo mLauncherActivityInfo;

	LunchAppInfo(LauncherActivityInfo launcherActivityInfo) {
		mLauncherActivityInfo = launcherActivityInfo;
	}

	public ComponentName getComponentName() {
		return mLauncherActivityInfo.getComponentName();
	}

	public UserHandle getUser() {
		return mLauncherActivityInfo.getUser();
	}

	public CharSequence getLabel() {
		return mLauncherActivityInfo.getLabel();
	}

	public Drawable getIcon(int density) {
		return mLauncherActivityInfo.getIcon(density);
	}

	public ApplicationInfo getApplicationInfo() {
		return mLauncherActivityInfo.getApplicationInfo();
	}

	public long getFirstInstallTime() {
		return mLauncherActivityInfo.getFirstInstallTime();
	}

	public Drawable getBadgedIcon(int density) {
		return mLauncherActivityInfo.getBadgedIcon(density);
	}
}
