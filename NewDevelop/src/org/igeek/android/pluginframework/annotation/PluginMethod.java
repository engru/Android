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

package org.igeek.android.pluginframework.annotation;

/**
 * @author 作�? E-mail:hangxin1940@gmail.com
 * @version 创建时间�?011-12-16 下午11:53:21
 * 描述插件的方�?
 */
public @interface PluginMethod {
	public String description() default "";
	public boolean need_context() default false;
}
