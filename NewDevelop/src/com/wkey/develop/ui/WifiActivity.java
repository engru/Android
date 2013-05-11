package com.wkey.develop.ui;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.wkey.develop.R;
import com.wkey.develop.config.Constants;
import com.wkey.develop.config.FormDataPostUtils;
import com.wkey.develop.http.HttpClient;
import com.wkey.develop.http.HttpException;
import com.wkey.develop.http.Response;
import com.wkey.develop.http.ResponseException;
import com.wkey.develop.task.HttpGetTask;
import com.wkey.develop.task.HttpPostTask;
import com.wkey.develop.ui.widget.adapter.ListViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

public class WifiActivity extends Activity {
	protected HttpClient http = null;
	private Button mBtn_wifi,mBtn_Query;
	private ImageView mImg_code;
	private EditText mEdit_code;
	private TextView mRequest,mResponse;
	private WebView mWebView;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   /* StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()   // or .detectAll() for all detectable problems
        .penaltyLog()
        .build());
	    
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
		.penaltyLog() //打印logcat
		.penaltyDeath()
		.build());
		*/
	    http = new HttpClient();
	    setContentView(R.layout.wifi_acti_info);
	    
	    mBtn_wifi = (Button)findViewById(R.id.btn_wifi_req);
	    mBtn_wifi.setOnClickListener(mBtnListener);
	    mBtn_Query= (Button)findViewById(R.id.btn_wifi_query);
	    mBtn_Query.setOnClickListener(mBtnListener);
	    
	    mImg_code = (ImageView) findViewById(R.id.randCode);
	    mImg_code.setOnClickListener(mBtnListener);
	    
	    mEdit_code = (EditText)findViewById(R.id.randCodeInput);
	    
	    mRequest = (TextView) findViewById(R.id.request);
	    mResponse = (TextView) findViewById(R.id.response);
	    
	    mWebView = (WebView)findViewById(R.id.webView);
		mWebView.getSettings().setJavaScriptEnabled(true);
	}
	
	private OnClickListener mBtnListener = new OnClickListener() {
		//LayoutInflater flater; //= LayoutInflater.from(mContext);
		public String HOST = "http://i.store.nearme.com.cn/";// 姝ｅ紡
		public String STATICS_HOST = "http://i.stat.nearme.com.cn/statistics/";// 姝ｅ紡
		//public static String PB_HOST = "";
		int i = 0;
		String url;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//System.out.println("onclick:"+v.getId());
			switch(v.getId()){
				case R.id.randCode:
					getRandCode();
					break;
				case R.id.btn_wifi_query:
					getpassengerAll();
					break;
				case R.id.btn_wifi_req:
					try{
						if(true){//i%2==1){
							//url = "https://support.cdmatech.com/login/";
							//url = "http://dynamic.12306.cn/otsquery/query/queryRemanentTicketAction.do?method=queryLeftTicket&orderRequest.train_date=2013-04-29&orderRequest.from_station_telecode=SZQ&orderRequest.to_station_telecode=NCG&orderRequest.train_no=&trainPassType=QB&trainClass=QB%23D%23Z%23T%23K%23QT%23&includeStudent=00&seatTypeAndNum=&orderRequest.start_time_str=00%3A00--24%3A00";
							//url ="https://dynamic.12306.cn/otsweb/trainQueryAppAction.do?method=init&where=ypcx";
							url = "https://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest";
							//url = "https://dynamic.12306.cn/otsweb/loginAction.do?method=login";
							//http://www.baidu.com/";
							i++;
						}else{
							url = "http://www.google.com/ig/api?h1=zh-CN&stock=600825";//http://dynamic.12306.cn/otsquery/query/queryRemanentTicketAction.do?method=queryLeftTicket&orderRequest.train_date=2013-04-26&orderRequest.from_station_telecode=SZQ&orderRequest.to_station_telecode=NCG&orderRequest.train_no=&trainPassType=QB&trainClass=QB%23D%23Z%23T%23K%23QT%23&includeStudent=00&seatTypeAndNum=&orderRequest.start_time_str=00%3A00--24%3A00";
							i++;
						}

						/*Param */
						
						/**/
						
						//Response res = post(url,null,false);
						System.out.println("wifi:1");
						//mResponse.setText(res.toString());
						//post(HOST+"MobileAPI/MobileAPI/ListRankingProducts.ashx",null,false);
						//doLogin();
						getLoginRand();
						
						
						System.out.println("wifi:2");
						//mResponse.setText("askdfj");
					}catch(Exception ex){
						ex.printStackTrace();
						mResponse.setText(ex.toString());
						System.out.println("wifi:3");
					}
					System.out.println("wifi:4");
					break;
			}
		}
	};

	
	Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			final Bundle bundle = msg.getData();
			switch (msg.what) {
				case 100:
					break;
				case 200:
					//final Bundle bundle = msg.getData();
					String msg1 = bundle.getString("res");
					mResponse.setText(msg1);
					mResponse.setVisibility(View.GONE);

					mWebView.loadData(msg1,"text/html;charset=UTF-8",null);
					
					try{	
							loginRand=null;
							JSONObject jsonObj = new JSONObject(msg1);
							loginRand = jsonObj.getString(Constants.LOGIN_RAND);
					} catch (JSONException jsone) {
					}
					if(loginRand!=null){
						doLogin();
						mBtn_wifi.setVisibility(View.GONE);
						mBtn_Query.setVisibility(View.VISIBLE);
						//
					}
					break;
				case 300:
					//final Bundle bundle = msg.getData();
					Bitmap bm = bundle.getParcelable("image");
					System.out.println("Bitmap:"+((bm==null)?"true":"false"));
					mImg_code.setImageBitmap(bm);
					//mImg_code.setScaleType(ScaleType.FIT_XY);
					//mImg_code.setScaleX(4);
					//mImg_code.setScaleY(4);
					break;
				default:
					break;
			}
		}
		
	};
	private void getLoginRand(){
		String url = "https://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest";	//验证地址
		post(url,null,false);
	}
	private void getRandCode(){
		String url="https://dynamic.12306.cn/otsweb/passCodeAction.do?rand=sjrand&0.577396409586072";
		getImage(url,null,false);
	}
	
	private String loginRand = null;
	private void doLogin(){
		String url = "https://dynamic.12306.cn/otsweb/loginAction.do?method=login";				//登录地址
		//loginRand = null;
		//解析LoginRand

		/**try {
			synchronized(this){
				while(loginRand==null)
					this.wait(1000);
			}
	    } catch (InterruptedException e) {
	        e.printStackTrace(); 
	    }*/
		FormDataPostUtils fd = new FormDataPostUtils();
		fd.put(Constants.LOGIN_RAND, loginRand);
		fd.put(Constants.RAND_CODE, mEdit_code.getText().toString());
		//mRequest.setText(fd.getFormData_Pair().toString());
		
		//URLEncodedUtils.format(fd.getFormData_Pair());
		/*
		System.out.println("wwwww"+(fd.getFormData_Pair().size()));//==null?"true11":"false11"));
		final StringBuilder result = new StringBuilder();
        for (int i=0;i<fd.getFormData_Pair().size();i++){//final NameValuePair parameter : fd.getFormData_Pair()) {
        	NameValuePair parameter = fd.getFormData_Pair().get(i);
            final String encodedName = parameter.getName();
            final String encodedValue = parameter.getValue();
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(encodedName);
            if (encodedValue != null) {
                result.append("=");
                result.append(encodedValue);
            }
        }
        mRequest.setText(result.toString());
        */
		mRequest.setText(URLEncodedUtils.format(fd.getFormData_Pair(),"UTF-8"));
		
        //for (final BasicNameValuePair parameter : fd.getFormData_Pair()) {
        //    System.out.println(parameter.getName()+"|"+ parameter.getValue());
       // }
		
		post(url,fd.getFormData_Pair(),false);
		//getAllCity();
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
		//拼接参数  
      //  StringBuffer params = new StringBuffer();  
      //  params.append("loginRand="+loginBefore.getLoginRand()).append("&")  
      //          .append("refundLogin=N").append("&")  
      //          .append("refundFlag=Y").append("&")  
      //          .append("loginUser.user_name="+PropertiesUtils.newInstance().getPropertiesValue("username")).append("&")  
      //          .append("nameErrorFocus=&")  
      //          .append("user.password="+PropertiesUtils.newInstance().getPropertiesValue("password")).append("&")  
      //          .append("passwordErrorFocus=&")  
      //          .append("randCode="+randCode).append("&")  
      //          .append("randErrorFocus="); 
	}
	
	private void getpassengerAll(){
		//String url = "https://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest";	//验证地址
		ArrayList<BasicNameValuePair> mParams = new ArrayList<BasicNameValuePair>();
		mParams.add(new BasicNameValuePair("pageIndex", "0"));
		mParams.add(new BasicNameValuePair("pageSize", "7"));
		mParams.add(new BasicNameValuePair("passenger_name", "请输入汉字或拼音首字母"));
		mRequest.setText(URLEncodedUtils.format(mParams,"UTF-8"));
		post(Constants.url_getPagePassengerAll,mParams,true);

	}
	
	private void getAllCity(){
		String url ="https://dynamic.12306.cn/otsweb/registAction.do?method=getAllCity";
		post(url,null,false);
			
	}

	protected synchronized Response get(String url,ArrayList<BasicNameValuePair> params,boolean authenticated) {//throws HttpException{
		new HttpGetTask(mhandler).execute(url);
		return null;
	}
	
	protected synchronized Response getImage(String url,ArrayList<BasicNameValuePair> params,boolean authenticated) {//throws HttpException{
		new HttpGetTask(mhandler,true).execute(url);
		return null;
	}
	
	protected synchronized Response post(String url,ArrayList<BasicNameValuePair> params,boolean authenticated) {//throws HttpException{
		new HttpPostTask(mhandler,params).execute(url);
		return null;
	}
}
