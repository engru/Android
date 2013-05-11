package com.wkey.develop.task;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;

import android.graphics.Bitmap;
import com.wkey.develop.http.HttpClient;
import com.wkey.develop.http.Response;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HttpGetTask extends AsyncTask<String, Integer, String> implements Observer {
	protected HttpClient http = null;
	Handler handler;
	boolean Image = false;
	
	public HttpGetTask(Handler mhandler) {
		// TODO Auto-generated constructor stub
		//super();
		handler = mhandler;
	}
	
	public HttpGetTask(Handler mhandler,boolean bl) {
		// TODO Auto-generated constructor stub
		//super();
		handler = mhandler;
		Image = bl;
	}


	protected String doInBackground(String... url) {
		// TODO Auto-generated method stub
		System.out.println("task:1");
		 http = new HttpClient();
		 try{
				//return http.get(url[0], false).asString();
				//return http.get(url[0],false).getHttpResponse();
				Response res1 = http.get(url[0],true);
				
				HttpResponse res = res1.getHttpResponse();
				Header[] headers = res.getAllHeaders();
				for(int i=0; i<headers.length;i++){
					System.out.println("\n"+headers[i].getName()+":"+headers[i].getValue());
				}
				
				if(Image){
					handleImage(res1.asStream());
					return null;
				}
				
				 
				return res1.asString();
				
		 }catch(Exception ex){
			 ex.printStackTrace();
			 return null;
		 }finally{
			 
		 }

		// handler.sendMessage(msg);
	}
	// @Override
		public void onPostExecute(String res) {
			if(res == null){
				System.out.println("res == null");
				return;
			}
			Message msg;
			//System.out.printl
			System.out.println(res.length());

			try{
				System.out.println("task:2");
				msg = handler.obtainMessage(200);
				Bundle bundle = msg.getData();
				bundle.putString("res", res);//.asString());//res.toString());
					//	(Message)res.toString();
				handler.sendMessage(msg);
				System.out.println("task:3"+res+":");
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		
		/**
		 * JSON fun
		 * @param bin
		 * @return
		 *//*
		public List<Info> JsonParser(Response res){
			JSONArray list = res.asJSONArray();
			int size = list.length();
			List<Info> info = new ArrayList<Info>(size);
			for(int i=0;i<size;i++){
				info.add()
			}
			
			return null;
		}
		*/
		
//////////////////////////////////////////////////////////
		
		public static String bin2hex(String bin) { 
	        char[] digital = "0123456789ABCDEF".toCharArray(); 
	        StringBuffer sb = new StringBuffer(""); 
	        byte[] bs = bin.getBytes(); 
	        int bit; 
	        for (int i = 0; i < bs.length; i++) { 
	            bit = (bs[i] & 0x0f0) >> 4; 
	            sb.append(digital[bit]); 
	            bit = bs[i] & 0x0f; 
	            sb.append(digital[bit]); 
	        } 
	        return sb.toString(); 
	    }
	// @Override
	public void onPostExecute1(HttpResponse res1) {
		Message msg;
		try{
			//if(res.asString() == null)
			//	return;
			/*
			String res = res1.getProtocolVersion().toString();
			
			//Header[] headers = res1.getAllHeaders();
			//for(int i=0; i<headers.length;i++){
			//	res +="\n"+headers[i].getName()+":"+headers[i].getValue()+"\n";
			//}
			
			res1.getFirstHeader("Content-Encoding").getValue();

			res1.getEntity().getContentType().toString();
			 
			 */

			String res="";
			BufferedInputStream bis = null;
			bis = new BufferedInputStream(res1.getEntity().getContent());


			int i = -1;
			byte[] b = new byte[1024];
			StringBuffer sb = new StringBuffer();
			while ((i = bis.read(b)) != -1) {
			    res +=new String(b)+"\n";//.toString();//sb.append(new String(b, 0, i));
			}
			//res = sb.toString();

			
			System.out.println("task:2");
			msg = handler.obtainMessage(200);
			Bundle bundle = msg.getData();
			bundle.putString("res", res);//.asString());//res.toString());
				//	(Message)res.toString();
			handler.sendMessage(msg);
			System.out.println("task:3"+res+":");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

/****
 *  This function just for test in URLConnection , 
 */

    public String getImage(String str){
		String res="";
    	try{
    		InputStream in =null;
    		URL url = new URL(str);
    		//
    		//InputStream in=url.openStream();

    		URLConnection conn = url.openConnection();

    		HttpURLConnection httpconn = (HttpURLConnection)conn;
    		httpconn.setRequestMethod("GET");
    		httpconn.setConnectTimeout(6*1000);
    		
    		System.out.println("wkeytest");
    		if(httpconn.getResponseCode() == 200)
    		{	in= httpconn.getInputStream();
    		
				
				BufferedInputStream bis = null;
				bis = new BufferedInputStream(in);
	
	
				int i = -1;
				byte[] b = new byte[1024];
				StringBuffer sb = new StringBuffer();
				while ((i = bis.read(b)) != -1) {
				    res+=new String(b)+"\n";//.toString();//sb.append(new String(b, 0, i));
				}
				return res;

    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		System.out.println("exception");
    	}
    	return res;
    	
    }
    
    
    void handleImage(InputStream is){
		Message msg;
		
		Bitmap bm = BitmapFactory.decodeStream(is);
		System.out.println("Bitmap:"+((bm==null)?"true":"false"));
		msg = handler.obtainMessage(300);
		Bundle bundle = msg.getData();
		bundle.putParcelable("image", bm);
		//bundle.putString("res", res);//.asString());//res.toString());
			//	(Message)res.toString();
		handler.sendMessage(msg);
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
}
