package com.oppo.transfer.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class SystemUtil {
	
	public static String getRealPathFromURL(Context mContext,String Url) {
		Uri uri = Uri.parse(Url);
		return getRealPathFromURI(mContext, uri);
	}
	
	public static String getRealPathFromURI(Context mContext,Uri contentUri) {
	    String[] proj = { MediaStore.Images.Media.DATA };
	    CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
	    Cursor cursor = loader.loadInBackground();
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	
	/*//从 uri 获取真实 path 的老方法
	public static String getPathfromUrl(String Url){
		Url = "content://media/external/images/media/***";  
		Uri uri = Uri.parse(Url);  

		String[] proj = { MediaStore.Images.Media.DATA };     
		Cursor cursor = Activity.managedQuery(uri,proj,null,null,null);    
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);     
		cursor.moveToFirst();

		String path = cursor.getString(index);    
		//File file = new File(path);  
		//Uri fileUri = Uri.fromFile(file);  
	}
	*/
	
	
	/**
	 * 屏幕分辨率相关
	 */
	private static int scrWidth = 0;
	private static int scrHeight = 0;
	
	public static int getWidth(Context ctx) {
		if(scrWidth == 0) {
			WindowManager wm  = (WindowManager)ctx.getSystemService("window");
		      //具体返回对象是WindowMangerIml类
		    Display display =   wm.getDefaultDisplay(); 
		      
		    DisplayMetrics dm = new DisplayMetrics();   
		    display .getMetrics(dm);
		    scrWidth = dm.widthPixels;
		} 
		return scrWidth;
	}
	
	public static int getHeight(Context ctx){
		if(scrHeight == 0) {
			WindowManager wm  = (WindowManager)ctx.getSystemService("window");
		      //具体返回对象是WindowMangerIml类
		    Display display =   wm.getDefaultDisplay(); 
		      
		    DisplayMetrics dm = new DisplayMetrics();   
		    display .getMetrics(dm);
		    scrHeight = dm.heightPixels;
		} 
		return scrHeight;
	}
	
	public static int getSy(){
		return 0;
	}
	
	

}
