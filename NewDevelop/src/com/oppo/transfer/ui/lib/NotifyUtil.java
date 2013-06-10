package com.oppo.transfer.ui.lib;

import com.oppo.transfer.core.utils.StateListener;
import com.oppo.transfer.utils.Constants;
import com.wkey.develop.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotifyUtil implements StateListener{
	private NotificationManager mNotificationManager=null;
	private Context mContext;
	
	NotificationCompat.Builder mCBuilder;
	Notification.Builder mBuilder;

	//只需在主线程中初始话一下即可
	
	public NotifyUtil(Context mContext){
		this.mContext = mContext;
		if(mNotificationManager==null)
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		
	}
	
	//默认
	
	private void OlderNotification(){
		Notification mNotification = new Notification();
		mNotification.icon = R.drawable.default_icon;
		mNotification.tickerText = "test";
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent i = new Intent();
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, i, 0);
		mNotification.setLatestEventInfo(mContext, "title", "content", contentIntent);
	}
	
	private void CreateNotificationBuilder(){
		mBuilder = new Notification.Builder(mContext)
			    .setSmallIcon(R.drawable.default_icon)//R.drawable.notification_icon)
			    .setContentTitle("My notification")
			    .setContentText("Hello World!")
			    .setAutoCancel(true)
			    .setTicker("oll")
			    ;
	}
	
	private PendingIntent DefineNotificationAction(){
		Intent resultIntent = new Intent(mContext, Activity.class);
		//TO DO:
		
		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		return 
		    PendingIntent.getActivity(mContext,0,resultIntent, 0//PendingIntent.FLAG_UPDATE_CURRENT
		    		);
	}
	
	
	private void SetNotificationClickBehavior(PendingIntent resultPendingIntent){
		mBuilder.setContentIntent(resultPendingIntent);
	}
	
	public void IssueNotification(){
		//NotificationCompat.Builder mBuilder = null;
		CreateNotificationBuilder();
		SetNotificationClickBehavior(DefineNotificationAction());
		
		// Sets an ID for the notification
		mNotificationManager.notify(R.id.btn_send, mBuilder.getNotification());
		
	}
	
	
	//自定义
	
	
	
	
	
	
	int historyProgress = 0;
	static Notification.Builder mBuilderProgress = null;
	public void updateNotification(int progressValue){
		if(mBuilderProgress == null)
			mBuilderProgress = new Notification.Builder(mContext)
				.setContentTitle("progess")
				.setContentText("content prog")
				.setSmallIcon(R.drawable.arrow);
		if(progressValue == 100)
			mBuilderProgress.setProgress(0, 0, false);
		else
			mBuilderProgress.setProgress(100, progressValue, false);
		
		Notification mNotification = mBuilder.getNotification();
		if (historyProgress != progressValue) {
			if (mNotification == null || mNotificationManager == null) {
				return;
			}
			mNotification.contentView.setTextViewText(
					R.drawable.default_icon,//R.id.notification_progress,
					progressValue
							+ mContext.getResources().getString(R.string.app_name));
									//R.string.notification_progress_sign));
			mNotification.contentView.setProgressBar(R.id.app_name_in_app,//R.id.notification_pb,
					100, progressValue, false);
			
			
			historyProgress = progressValue;
			mNotificationManager.notify(Constants.APPLICATION_NOTIFICATION_ID, mBuilderProgress.getNotification());
		}
	}
	
	
	private void ModifyNotification(){
		mNotificationManager =
		        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		// Sets an ID for the notification, so it can be updated
		int notifyID = 1;
		NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(mContext)
		    .setContentTitle("New Message")
		    .setContentText("You've received new messages.")
		    .setSmallIcon(0);//R.drawable.ic_notify_status)
		int numMessages = 0;
		// Start of a loop that processes data and then notifies the user
		//...
		    mNotifyBuilder.setContentText("")//currentText)
		        .setNumber(++numMessages);
		    // Because the ID remains unchanged, the existing notification is
		    // updated.
		    mNotificationManager.notify(
		            notifyID,
		            mNotifyBuilder.build());
		//...
	}
	
	private void RemoveNotification(){
		
	}

	@Override
	public void updateState(int state,int progressValue) {
		// TODO Auto-generated method stub
		updateNotification(progressValue);
	}
	
	
}
