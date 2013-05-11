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

import java.lang.reflect.Field;

import org.igeek.android.pluginframework.beans.Plugin;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * @author 作�? E-mail:hangxin1940@gmail.com
 * @version 创建时间�?011-12-16 上午10:45:04
 * 插件描述�?
 */
public class PluginDescription <T> {
	private Class<T> clazz;
	
	public PluginDescription(Class<T> clazz) {
		this.clazz=clazz;
	}
	
	/**
	 * 获得自定义的插件描述�?
	 * @param context 主程序句�?
	 * @param plugin
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws NameNotFoundException 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public T getDescription(Context context,Plugin plugin) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NameNotFoundException, IllegalArgumentException, SecurityException, NoSuchFieldException{
		Context targetContext=context.createPackageContext(plugin.getPkgInfo().packageName, Context.CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY);
		
		Class<?> c=targetContext.getClassLoader().loadClass(plugin.getDescription());
		Object des=c.newInstance();
		
		T sor=clazz.newInstance();
		
		
		//获取�?��字段
		Field[] fields=clazz.getSuperclass().getDeclaredFields();
		for(Field f:fields){
			f.setAccessible(true);
			Field tf=c.getSuperclass().getDeclaredField(f.getName());
			tf.setAccessible(true);
			//获得注解
			//填充bean
			Log.e("org.igeek.android-plugin", " -: "+tf.getName()+" -> "+f.getName()+"  = "+tf.get(des));
			f.set(sor, tf.get(des));
			
			
		}
		
		
		return  sor;
	}
}
