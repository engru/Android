package com.wkey.develop.task;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;

import com.wkey.develop.http.HttpClient;
import com.wkey.develop.http.Response;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HttpPostTask extends AsyncTask<String, Integer, String> implements Observer {
	protected HttpClient http=null;
	Handler handler;
	ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	
	public HttpPostTask(Handler mhandler) {
		// TODO Auto-generated constructor stub
		//super();
		handler = mhandler;
	}
	
	public HttpPostTask(Handler mhandler,ArrayList<BasicNameValuePair> params) {
		// TODO Auto-generated constructor stub
		//super();
		handler = mhandler;
		if(params!=null)
			this.params = params;
	}
	
	protected String getRequestBody() {
		final StringBuilder buf = new StringBuilder();
		buf.append("<request local_version=\"1\">");
		buf.append("<uid>5217</uid>");
		buf.append("<size>30</size>");
		buf.append("<start_position>1</start_position>");
		buf.append("<platform>10</platform>");
		buf.append("<theme_version>3</theme_version>");
		buf.append("<system_type>0</system_type>");
		buf.append("<os_version>2.3.4</os_version>");
		buf.append("<screen_size>800#480</screen_size>");
		buf.append("<mobile_name>msm8660_surf</mobile_name>");
		buf.append("</request>");
		return buf.toString();
	}
	
	@Override
	protected String doInBackground(String... url) {
		// TODO Auto-generated method stub
		http = new HttpClient();
		try{
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
			
			//ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();;
			//params.add(new BasicNameValuePair("loginRand", "592"));
			//params.add(new BasicNameValuePair("refundLogin", "N"));
			//params.add(new BasicNameValuePair("refundFlag", "Y"));
			//params.add(new BasicNameValuePair("loginUser.user_name", "wkeycn"));
			//params.add(new BasicNameValuePair("nameErrorFocus", ""));
			//params.add(new BasicNameValuePair("user.password", "123456"));
			//params.add(new BasicNameValuePair("randCode", "NTU4"));
			//params.add(new BasicNameValuePair("randErrorFocus", ""));

			 Response res1=http.post(url[0],params);
			 System.out.println("22222222222");
			 
			 		HttpResponse res = res1.getHttpResponse();
					Header[] headers = res.getAllHeaders();
					for(int i=0; i<headers.length;i++){
						System.out.println("\n"+headers[i].getName()+":"+headers[i].getValue());
					}
					
					
					
			 System.out.println("222222222222222222222222222");
			 return res1.asString();//.toString();
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}

	}
	// @Override
	public void onPostExecute(String res) {
		Message msg;
		try{
			if(res == null)
				return;
			
			System.out.println("task:2");
			msg = handler.obtainMessage(200);
			Bundle bundle = msg.getData();
			//bundle.putBoolean("json", );
			bundle.putString("res", res);
			handler.sendMessage(msg);
			System.out.println("task:3"+res);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}

}
