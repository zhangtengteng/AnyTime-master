package com.luna.anytime;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

public class AnyTimeApplication extends Application {
	public static final String KEY_CLIENT_ID = "client_id";
	static SharedPreferences preferences;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
//		AVOSCloud.useAVCloudCN();
		// U need your AVOS key and so on to run the code.
		AVOSCloud.initialize(this,
				"7r7befs8zwsgicuijnjac9bg1qk7h7y41gkf1bnlya1qkrbw",
				"dr40i7je00jecx81g0emifx06iipcwn8qiwjubnbnqbhorkx");
		AVOSCloud.setDebugLogEnabled(true);
		
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,
				new com.example.test.MessageHandler(this));
	}
	
	public static String getClientIdFromPre() {
		return preferences.getString(KEY_CLIENT_ID, "");
	}

	public static void setClientIdToPre(String id) {
		preferences.edit().putString(KEY_CLIENT_ID, id).apply();
	}

	public static AVIMClient getIMClient() {
		return AVIMClient.getInstance(getClientIdFromPre());
	}
}
