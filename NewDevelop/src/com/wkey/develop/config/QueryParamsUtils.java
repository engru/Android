package com.wkey.develop.config;

public class QueryParamsUtils {
	String TAG = "FormDataPostUtils:";
	PostParams params;
	
	
	public QueryParamsUtils(){
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
	
	public String getQueryParams(){
		final StringBuilder buf = new StringBuilder();
		buf.append("?");
		for(int i=0;i<Constants.ParamNames.length-1;i++){
			buf.append(Constants.ParamNames[i])
				.append("=").append(params.getString(Constants.ParamNames[i])).append("&");
		}
		buf.append(Constants.ParamNames[Constants.ParamNames.length-1])
			.append("=").append(params.getString(Constants.ParamNames[Constants.ParamNames.length-1]));
		return buf.toString();
	}
	/*
	public ArrayList<BasicNameValuePair> getFormData_Pair(){
		ArrayList<BasicNameValuePair> mParams = new ArrayList<BasicNameValuePair>();
		for(int i=0;i<Constants.ParamNames.length;i++){
			mParams.add(new BasicNameValuePair(Constants.ParamNames[i],params.getString(Constants.ParamNames[i])));
			//System.out.println(Constants.ParamNames[i]+"="+params.getString(Constants.ParamNames[i])+"&");
		}
		//System.out.println(TAG+mParams.size());
		return mParams;
	}*/
}
