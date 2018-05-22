package com.softsz.pt1easylauncher;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

public class MyApplication extends Application {

	private static MyApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		//LoadAppsUtil.initAppCompName(this);
	}

	public static MyApplication getInstance() {
		return instance;
	}
}
