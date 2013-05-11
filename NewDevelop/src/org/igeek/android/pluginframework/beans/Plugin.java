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

package org.igeek.android.pluginframework.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * @author E-mail:hangxin1940@gmail.com
 * @version 创建时间�?011-12-15 上午10:52:51 �?��插件对象
 */
public class Plugin {

	private String pluginLable; // 插件名称

	private PackageInfo pkgInfo; // 插件包信�?

	private String description; // 描述信息，也就是类名

	private List<PluginFeature> features;

	private Context context; // 插件的句�?

	private Map<String, PluginIntent> intents;

	public Plugin() {
		features = new ArrayList<PluginFeature>();
	}

	/**
	 * 获取插件名称
	 * 
	 * @return
	 */
	public String getPluginLable() {
		return pluginLable;
	}

	/**
	 * 设置插件名称
	 * 
	 * @param strPluginLable
	 */
	public void setPluginLable(String pluginLable) {
		this.pluginLable = pluginLable;
	}

	/**
	 * 获取插件包信�?
	 * 
	 * @return
	 */
	public PackageInfo getPkgInfo() {
		return pkgInfo;
	}

	/**
	 * 设置插件包信�?
	 * 
	 * @param pkgInfo
	 */
	public void setPkgInfo(PackageInfo pkgInfo) {
		this.pkgInfo = pkgInfo;
	}

	/**
	 * 获得功能列表
	 * 
	 * @return
	 */
	public List<PluginFeature> getFeatures() {
		return features;
	}

	/**
	 * 添加�?��功能描述
	 */
	public void addFeature(PluginFeature feature) {
		features.add(feature);
	}

	/**
	 * 添加�?��功能描述
	 * 
	 * @param features
	 */
	public void setFeatures(List<PluginFeature> features) {
		this.features = features;
	}

	/**
	 * 设置插件描述，也就是插件的包�?
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取插件描述
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 获取插件的句�?
	 * 
	 * @return
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * 设置插件的句�?
	 * 
	 * @param context
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	public Map<String, PluginIntent> getIntents() {
		return intents;
	}

	public void setIntents(Map<String, PluginIntent> intents) {
		this.intents = intents;
	}

	public void putIntent(String category, PluginIntent intent) {
		this.intents.put(category, intent);
	}

}
