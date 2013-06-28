package com.oppo.transfer.core.utils;

public class Constants {
	//控制字符
	public static final String REQSEND	= "REQUESTSEND";
	public static final String ALLOW	= "ALLOW";
	public static final String DENY		= "DENY";
	public static final String END		= "COMPLETE";
	public static final String EXCEP	= "EXCEPTION";
	public static final String FILE		= "FILE";
	public static final String DIR		= "DIR";
	public static final String DEFINED	= "DEFINDED";
	
	public static final String ONLYFILE	= "ONLYFILE";
	
	
	public final static int BUFFER_SIZE = 1024 * 10 * 10;//100k
	
	//默认参数
	public static String CTR_STATE 	= null;
	public static String P2P_IP 	= "192.168.43.147";
	public static int DEFAULT_PORT 	= 13267;

	
	public static TransInfo transInfo = new TransInfo();
	
}
