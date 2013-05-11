package com.wkey.develop.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;

public class Constants {
	//12306 login params 9
	public static final String LOGIN_RAND		= "loginRand" ;				//1
	public static final String REFUND_LOGIN 	= "refundLogin" ;			//2
	public static final String REFUND_FLAG 		= "refundFlag" ;			//3
	public static final String LOGIN_USER 		= "loginUser.user_name" ;	//4
	public static final String NAME_ERROR_FOCUS = "nameErrorFocus" ;		//5
	public static final String USER_PASSWORD 	= "user.password" ;			//6
	public static final String PASSWORD_ERROR 	= "passwordErrorFocus" ;	//7
	public static final String RAND_CODE 		= "randCode" ;				//8
	public static final String RAND_ERROR_FOCUS = "randErrorFocus" ;		//9
	
	public static String[] ParamNames = {
			LOGIN_RAND,REFUND_LOGIN,
			REFUND_FLAG,
			LOGIN_USER,
			NAME_ERROR_FOCUS,
			USER_PASSWORD,
			PASSWORD_ERROR,
			RAND_CODE,
			RAND_ERROR_FOCUS,
			//refundLoginCheck:Y
	};
	
	public static String[] defValue={
			"592"		,	//1 loginRand
			"N"			,	//2 
			"Y"			,	//3 Flag
			"wkeycn"	,	//4   �˺�
			""			,	//5 
			"a123456"	,	//6   ����
			""			,	//7 
			"NTU4"		,	//8 randCode
			""				//9 
			
	};
	
	public static String COOKIE = "";
	public static CookieStore cookieStore  =new BasicCookieStore();
	
	
	
	/**
	 * URL
	 */
	public static final String url_randCode	//��֤��
		="https://dynamic.12306.cn/otsweb/passCodeAction.do?rand=sjrand&0.577396409586072";
	public static final String url_loginCode 	//��½��
		= "https://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest";	//��֤��ַ
	public static final String url_login		//��½��ַ
		= "https://dynamic.12306.cn/otsweb/loginAction.do?method=login";				//��¼��ַ
	
	/*���˹���*/
	//����
		//δ��ɶ���
		//������ѯ
		//ԤԼ��ѯ
		//���Ŵ���
		//��Ʊ
		//��ǩ

	/*������Ϣ*/
	public static final String url_getPagePassengerAll //������ϵ��
		= "https://dynamic.12306.cn/otsweb/passengerAction.do?method=getPagePassengerAll";

		//��������
		//�����޸�
		//�����˻�
	
	//ͨ��
}
