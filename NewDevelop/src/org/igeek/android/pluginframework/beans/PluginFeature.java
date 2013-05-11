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

/**
 * @author 作�? E-mail:hangxin1940@gmail.com
 * @version 创建时间�?011-12-15 下午01:01:45
 * 插件功能，也就相当于插件提供的类
 */
public class PluginFeature {
	private String featureName; //类名
	
	private List<PluginFeatureMethod> methods; //方法集合
	
	/**
	 * 插件�?
	 */
	public PluginFeature() {
		methods=new ArrayList<PluginFeatureMethod>();
	}

	/**
	 * 获得功能�?类名)
	 * @return
	 */
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * 设置功能�?类名)
	 * @param featureName
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	/**
	 * 获得方法列表
	 * @return
	 */
	public List<PluginFeatureMethod> getMethods() {
		return methods;
	}

	/**
	 * 设置方法列表
	 * @param methods
	 */
	public void addMethod(PluginFeatureMethod method) {
		methods.add(method);
	}
	
	
}
