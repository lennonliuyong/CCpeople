package com.ccdrive.peopledetail.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class AppUtil {

	private Context mContext;

	public AppUtil(Context mContext) {
		this.mContext = mContext;
	}

	public boolean isInstall(String appName) {
		int count;
		boolean isInstall = false;
		PackageManager pckMan = mContext.getPackageManager();
		List<PackageInfo> packs = pckMan.getInstalledPackages(0);
		count = packs.size();
		String name;
		int installedNum = 0;
		for (int i = 0; i < count; i++) {
			PackageInfo p = packs.get(i);
			if (p.versionName == null) {
				continue;
			}
			// 判断该软件包是否�?data/app目录�?
			ApplicationInfo appInfo = p.applicationInfo;
			name = p.applicationInfo.packageName;
			if ((appName.trim()).equals(name)) {
				isInstall = true;
				break;
			}
		}

		return isInstall;
	}

	// 获取�?��的非系统应用
	public ArrayList<PackageInfo> getMapPackages() {
		ArrayList<PackageInfo> list = new ArrayList<PackageInfo>();
		PackageManager packageManager = mContext.getPackageManager();
		List<PackageInfo> lPackageInfo = packageManager.getInstalledPackages(0);
		// 过滤系统或用�?
		for (PackageInfo packageInfo : lPackageInfo) {
			if (!isSystemPackage(packageInfo)) {
				list.add(packageInfo);
			}
		}
		return list;
	}

	public boolean isSystemPackage(PackageInfo packageInfo) {
		// 系统应用
		if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)// !=0
			return true;
		// 系统升级应用
		else if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) > 0)// !=0
			return true;
		// 用户安装应用
		else
			return false;
	}
}
