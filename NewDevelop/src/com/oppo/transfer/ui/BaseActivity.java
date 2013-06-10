package com.oppo.transfer.ui;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.util.Log;

public class BaseActivity extends Activity {
	String TAG = "BaseActivity";
	
	
	ActionListener discoverPeersActionListener = new ActionListener() {

		@Override
		public void onSuccess() {
			  Log.e(TAG,"Success");
		}

		@Override
		public void onFailure(int reasonCode) {
			  Log.e(TAG,"Failure");
		}
    };;
}
