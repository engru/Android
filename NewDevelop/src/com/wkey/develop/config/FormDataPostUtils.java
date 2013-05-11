package com.wkey.develop.config;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;


public class FormDataPostUtils {
	String TAG = "FormDataPostUtils:";
	PostParams params;
	/**Form Data:
	 * loginRand=592
	 * refundLogin=N
	 * refundFlag=Y
	 * loginUser.user_name=wkeycn
	 * nameErrorFocus=
	 * user.password=*****
	 * passwordErrorFocus=
	 * randCode=NTU4
	 * randErrorFocus=
	 */
	
	/** Moved to Constans.java 
	int loginRand;
	String[] ParamNames={
			"loginRand"		,		//1
			"refundLogin"	,		//2
			"refundFlag"	,		//3
			"loginUser.user_name",	//4
			"nameErrorFocus",		//5
			"user.password"	,		//6
			"passwordErrorFocus",	//7
			"randCode"		,		//8
			"randErrorFocus"		//9
			
	};
	
	String[] defValue={
			"592"		,	//1 loginRand
			"N"			,	//2 
			"Y"			,	//3 Flag
			"username"	,	//4   ’À∫≈
			""			,	//5 
			"password"	,	//6   √‹¬Î
			""			,	//7 
			"NTU4"		,	//8 randCode
			""				//9 
			
	};
	*/
	
	
	public FormDataPostUtils(){
		params = new PostParams();
		for(int i=0;i<Constants.ParamNames.length;i++){
			params.put(Constants.ParamNames[i], Constants.defValue[i]);
		}
	}
	
	public void put(String key, Object value) {
		params.put(key, value);
	}
	
	public Object get(String key) {
		return params.get(key);
	}
	
	public String getFormData(){
		final StringBuilder buf = new StringBuilder();
		for(int i=0;i<Constants.ParamNames.length;i++){
			buf.append(Constants.ParamNames[i]).append("=").append(params.getString(Constants.ParamNames[i])).append("&");
		}
		return buf.toString();
	}
	
	public ArrayList<BasicNameValuePair> getFormData_Pair(){
		ArrayList<BasicNameValuePair> mParams = new ArrayList<BasicNameValuePair>();
		for(int i=0;i<Constants.ParamNames.length;i++){
			mParams.add(new BasicNameValuePair(Constants.ParamNames[i],params.getString(Constants.ParamNames[i])));
			//System.out.println(Constants.ParamNames[i]+"="+params.getString(Constants.ParamNames[i])+"&");
		}
		//System.out.println(TAG+mParams.size());
		return mParams;
		
	}
	

}
