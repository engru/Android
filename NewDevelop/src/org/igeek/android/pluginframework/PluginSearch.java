/*
 * Copyright (C) 2011 hangxin1940@gmail.com  http://hangxin1940.cnblogs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.igeek.android.pluginframework;

import java.util.ArrayList;
import java.util.List;

import org.igeek.android.pluginframework.beans.Plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author ä½è? E-mail:hangxin1940@gmail.com
 * @version åå»ºæ¶é´ï¼?011-12-15 ä¸å11:55:50
 * æ¥æ¾æä»¶
 */
public class PluginSearch {
	
	/**
	 * æ¥æ¾æä»¶
	 */
	public PluginSearch() {
	}
	
	/**
	 * è·åæ?æä»¶
	 * @param context å¥æ
	 * @return
	 * @throws NameNotFoundException
	 */
	public List<Plugin> getPlugins(Context context) throws NameNotFoundException{
		
		List<Plugin> plugins=new ArrayList<Plugin>();
		
		PackageManager  pkgManager=context.getPackageManager();
		String pkgName=context.getPackageName();
		String sharedUID=pkgManager.getPackageInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES).sharedUserId;
		
		List<PackageInfo> pkgs=pkgManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(PackageInfo pkg	:pkgs){
			//æ¥æ¾å·æç¸åsuidçåï¼å¹¶ä¸æé¤èªèº?
			if(!sharedUID.equals(pkg.sharedUserId)||pkgName.equals(pkg.packageName))
				continue;
			
			Plugin plugin=new Plugin();
			plugin.setPkgInfo(pkg);
			plugin.setPluginLable(pkgManager.getApplicationLabel(pkg.applicationInfo).toString());
			plugins.add(plugin);
		
		}
		
		
		return plugins;
	}
}
