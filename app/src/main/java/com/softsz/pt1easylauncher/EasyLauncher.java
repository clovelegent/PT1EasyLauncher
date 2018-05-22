package com.softsz.pt1easylauncher;

import java.util.ArrayList;
import java.util.List;

import com.softsz.pt1easylauncher.SelectAppAddFragmentDialog.SelectedAddListener;
import com.softsz.pt1easylauncher.SelectAppRemoveFragmentDialog.SelectedRemoveListener;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class EasyLauncher extends Activity implements OnItemClickListener, SelectedAddListener, SelectedRemoveListener {

	private static final String TAG = "xph_easylauncher";
	private List<String> needShowAppCompNameList;
	private DragGridView mDragGridView;
	private static PackageManager pm;
	private Resources res;

	/**
	 * if replaceResidentAppsIcon == true we can replace grid Item
	 */
	private boolean replaceResidentAppsIcon = false;

	private int drawableArray[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
		pm = getPackageManager();

		res = getResources();
		initData();

		drawableArray = new int[] { R.drawable.ic_alcohol_check2, R.drawable.ic_message, R.drawable.ic_policy_work,
				R.drawable.ic_recod_illegal, R.drawable.ic_security_join, R.drawable.ic_sound_chat,
				R.drawable.ic_system_setting, R.drawable.ic_take_picture_illegal_park, R.drawable.ic_vedio_chat };

		if (replaceResidentAppsIcon) {
			String residentName[] = res.getStringArray(R.array.residentAppSName);
			for (int i = 0; i < 9; i++) {
				LoadAppsUtil.mAppsData.get(i).setIcon(res.getDrawable(drawableArray[i]));
				LoadAppsUtil.mAppsData.get(i).setLabel(residentName[i]);
			}
		}

		if (LoadAppsUtil.mAppsData.size() < 12) {
			ItemInfo mAddItem = new ItemInfo(false, res.getDrawable(R.drawable.icon_add),
					res.getString(R.string.add_item), null);
			LoadAppsUtil.mAppsData.add(mAddItem);
		}

		setContentView(R.layout.activity_easy_launcher);

		mDragGridView = (DragGridView) findViewById(R.id.gview);
		mDragGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mDragGridView.setAdapter(new MAdapter(LoadAppsUtil.mAppsData, this));
		mDragGridView.setOnItemClickListener(this);
	}

	private void initData() {
		needShowAppCompNameList = initResidentApps();

		List<String> prefApps = initPrefApps();
		if (prefApps != null) {
			for (int i = 0; i < prefApps.size(); i++) {
				needShowAppCompNameList.add(prefApps.get(i));
			}
		}
		LoadAppsUtil.mAppsData = initAppsData(needShowAppCompNameList);
	}

	@Override
	protected void onResume() {
		super.onResume();
		dragGridAdapterNofityData();
		// String appcn =
		// "com.android.browser/com.android.browser.BrowserActivity";
		// addAppData(false,appcn);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			boolean haveRemoveAbleItem = false;
			for (ItemInfo ii : LoadAppsUtil.mAppsData) {
				if (ii.isIsRemoveAble()) {
					haveRemoveAbleItem = true;
					break;
				}
			}
			if (haveRemoveAbleItem) {
				SelectAppRemoveFragmentDialog appRemoveFragmentDialog = new SelectAppRemoveFragmentDialog();
				appRemoveFragmentDialog.show(getFragmentManager(), null);
			}
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}

		if (keyCode == 270) {
			Intent intent = new Intent();
			ComponentName component = new ComponentName("com.android.launcher3", "com.android.launcher3.Launcher");
			intent.setComponent(component);
			intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction("android.intent.action.MAIN");
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private void dragGridAdapterNofityData() {
		ListAdapter adapter = mDragGridView.getAdapter();
		DragGridAdapter<ItemInfo> dragGridAdapter = (DragGridAdapter<ItemInfo>) adapter;
		dragGridAdapter.notifyDataSetChanged();
	}

	// according to needShowAppCompNameList create girdview adapter list
	private List<ItemInfo> initAppsData(List<String> needShowAppCompNameList) {
		List<ItemInfo> mData = new ArrayList<ItemInfo>();
		String initGridAppsCompName[] = res.getStringArray(R.array.residentApps);
		for (String appcn : needShowAppCompNameList) {
			boolean residentApp = false;
			for (int i = 0; i < initGridAppsCompName.length; i++) {
				if (initGridAppsCompName[i].equals(appcn)) {
					residentApp = true;
					break;
				}
			}
			mData.add(createItemInfo(residentApp, appcn));
		}
		return mData;
	}

	/**
	 * create every item info ,label & icon & component name & remove
	 * 
	 * @param contains
	 * @param appcn
	 * @return ItemInfo
	 */
	public static ItemInfo createItemInfo(boolean contains, String appcn) {
		String[] compNms = appcn.split("/");
		Log.d(TAG, "packageName:" + compNms[0] + ",className:" + compNms[1]);
		ComponentName componentName = new ComponentName(compNms[0], compNms[1]);
		ApplicationInfo appInfo = null;
		Drawable appIcon = null;
		String label = null;
		try {
			appInfo = pm.getApplicationInfo(compNms[0], PackageManager.GET_META_DATA);
			label = pm.getApplicationLabel(appInfo).toString();
			appIcon = pm.getApplicationIcon(appInfo);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return new ItemInfo(!contains, appIcon, label, componentName);
	}

	/*
	 * The resident apps R.array.residentApps
	 */
	public List<String> initResidentApps() {
		List<String> needResidentShowAppsCompNameList = new ArrayList<String>();
		String[] residentApps = getResources().getStringArray(R.array.residentApps);
		for (int i = 0; i < residentApps.length; i++) {
			String appCompName[] = residentApps[i].split("/");
			boolean isInstalled = LoadAppsUtil.isAppInstalled(this, appCompName[0]);
			if (isInstalled) {
				needResidentShowAppsCompNameList.add(residentApps[i]);
			}
		}
		return needResidentShowAppsCompNameList;
	}

	/*
	 * Stored in SharePrefrence APP include resident APP and other add App
	 */
	private List<String> initPrefApps() {
		List<String> needShowPrefAppsCompNameList = new ArrayList<String>();
		String[] prefApps = LoadAppsUtil.getSharePrefAppsCompNameArray(this);
		if (prefApps == null)
			return null;
		for (int i = 0; i < prefApps.length; i++) {
			String appCompName[] = prefApps[i].split("/");
			boolean isInstalled = LoadAppsUtil.isAppInstalled(this, appCompName[0]);
			if (isInstalled) {
				needShowPrefAppsCompNameList.add(prefApps[i]);
			}
		}
		return needShowPrefAppsCompNameList;
	}

	class MAdapter extends DragGridAdapter<ItemInfo> {

		public MAdapter(List list, Context context) {
			super(list, context);
		}

		@Override
		protected View getItemView(int position, View convertView, ViewGroup parent) {
			String mLabel = getList().get(position).getLabel();
			Drawable mDrawable = getList().get(position).getIcon();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, null);
			ImageView appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
			appIcon.setImageDrawable(mDrawable);
			TextView appLabel = (TextView) convertView.findViewById(R.id.app_label);
			appLabel.setText(mLabel);
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d("xph_DragGridView", "gridview onItemClick");
		ListAdapter adapter = mDragGridView.getAdapter();
		DragGridAdapter<ItemInfo> dragGridAdapter = (DragGridAdapter<ItemInfo>) adapter;
		ComponentName component = dragGridAdapter.getList().get(position).getComponentName();

		// on click add button the component is null, should show the
		// application list to select which add to grid
		if (component == null) {
			SelectAppAddFragmentDialog selectAppFragmentDialog = new SelectAppAddFragmentDialog();
			selectAppFragmentDialog.show(getFragmentManager(), null);
		} else {
			Intent intent = new Intent();
			intent.setComponent(component);
			intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction("android.intent.action.MAIN");
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				Log.i(TAG, "Not starting activity , " + e);
			}
		}

	}

	@Override
	public void onSelectedAddComplete(ItemInfo itemInfo) {
		LoadAppsUtil.mAppsData.remove(LoadAppsUtil.mAppsData.size() - 1);
		if (LoadAppsUtil.mAppsData.size() < 12) {
			LoadAppsUtil.addAppCompNm(this, itemInfo.getComponentName().getPackageName() + "/"
					+ itemInfo.getComponentName().getClassName());
			LoadAppsUtil.mAppsData.add(itemInfo);
			if (LoadAppsUtil.mAppsData.size() < 12) {
				ItemInfo mAddItem = new ItemInfo(false, res.getDrawable(R.drawable.icon_add),
						res.getString(R.string.add_item), null);
				LoadAppsUtil.mAppsData.add(mAddItem);
			}
		}
		dragGridAdapterNofityData();
	}

	@Override
	public void onSelectedRemoveComplete(ItemInfo itemInfo) {
		boolean havaAddItem = false;
		LoadAppsUtil.removeAppCompName(this, itemInfo.getComponentName().getPackageName() + "/"
				+ itemInfo.getComponentName().getClassName());
		LoadAppsUtil.mAppsData.remove(itemInfo);
		for (ItemInfo ii : LoadAppsUtil.mAppsData) {
			if (ii.getComponentName() == null) {
				havaAddItem = true;
				break;
			}
		}
		if (!havaAddItem) {
			ItemInfo mAddItem = new ItemInfo(false, res.getDrawable(R.drawable.icon_add),
					res.getString(R.string.add_item), null);
			LoadAppsUtil.mAppsData.add(mAddItem);
		}
		dragGridAdapterNofityData();
	}

}
