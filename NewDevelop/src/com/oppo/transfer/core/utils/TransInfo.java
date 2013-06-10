package com.oppo.transfer.core.utils;

import java.util.List;

public class TransInfo {
	//传输信息又几个方面
	/**
	 * 1、 选择的文件，（包括文件、文件夹、自定义文件）（路径和路径列表）
	 * 2、 网络，对方的网络IP
	 * 3、 端口号，端口号本来可以使用默认，但内网情况下，需要使用协商
	 * 
	 */
	
	String 			Path ;
	List<String>	Paths ;
	String 			IP ;
	long 			Port ;
	
	
}
