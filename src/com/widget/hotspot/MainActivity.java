package com.widget.hotspot;

import java.net.ContentHandler;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";
	private Button mBtn1, mBtn2;
	private WifiAdmin mWifiAdmin;
	private Context mContext = null;
	private WifiManager wifiManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		mBtn1 = (Button) findViewById(R.id.button1);
		mBtn2 = (Button) findViewById(R.id.button2);
		mBtn1.setText("点击连接Wifi");
		mBtn2.setText("点击创建Wifi热点");
		mBtn1.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mWifiAdmin = new WifiAdmin(mContext) {

					@Override
					public void myUnregisterReceiver(BroadcastReceiver receiver) {
						// TODO Auto-generated method stub
						MainActivity.this.unregisterReceiver(receiver);
					}

					@Override
					public Intent myRegisterReceiver(BroadcastReceiver receiver, IntentFilter filter) {
						// TODO Auto-generated method stub
						MainActivity.this.registerReceiver(receiver, filter);
						return null;
					}

					@Override
					public void onNotifyWifiConnected(String serveraddress) {
						// TODO Auto-generated method stub
						Log.v(TAG, "have connected success!" + serveraddress);
						wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						if (wifiManager != null) {
							DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
							Log.v(TAG, "server address:" + Formatter.formatIpAddress(dhcpInfo.serverAddress));
						}
					}

					@Override
					public void onNotifyWifiConnectFailed() {
						// TODO Auto-generated method stub
						Log.v(TAG, "have connected failed!");
						Log.v(TAG, "###############################");
					}
				};
				mWifiAdmin.openWifi();
				mWifiAdmin.addNetwork(mWifiAdmin.createWifiInfo("goopai_wifiap", "12345678", WifiAdmin.TYPE_WPA));
			}
		});

		mBtn2.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				WifiApAdmin wifiAp = new WifiApAdmin(mContext);
				wifiAp.startWifiAp("goopai_wifiap", "12345678");
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.d("Rssi", "Registered");
	}

	@Override
	public void onPause() {
		super.onPause();

		Log.d("Rssi", "Unregistered");
	}
}
