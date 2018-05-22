package com.softsz.pt1easylauncher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.os.UserHandle;

/**
 * 
 * @author xph
 * class for collect launcher APP.
 *
 */
public class QsLunchApps {

	private LauncherApps mLauncherApps;
	
	public QsLunchApps(Context context) {
		mLauncherApps = (LauncherApps) context.getSystemService("launcherapps");
	}
	
	public List<LunchAppInfo> getActivityList(String packageName,UserHandle user) {
        List<LauncherActivityInfo> list = mLauncherApps.getActivityList(packageName,user);
        if (list.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        ArrayList<LunchAppInfo> compatList = new ArrayList<LunchAppInfo>(list.size());
        for (LauncherActivityInfo info : list) {
            compatList.add(new LunchAppInfo(info));
        }
        return compatList;
    }
}
