package com.example.test;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

/**
 * Created by zhangxiaobo on 15/4/15.
 */
public class Application extends android.app.Application {
	public static final String KEY_CLIENT_ID = "client_id";
	static SharedPreferences preferences;

	@Override
	public void onCreate() {
		super.onCreate();
		AVOSCloud.setDebugLogEnabled(true);
		// 杩欐槸浣跨敤缇庡浗鑺傜偣鐨� app 淇℃伅锛屽鏋滀笉浣跨敤缇庡浗鑺傜偣锛岃 comment 杩欎袱琛�
		// AVOSCloud.useAVCloudUS();
		// AVOSCloud.initialize(this,
		// "l8j5lm8c9f9d2l90213i00wsdhhljbrwrn6g0apptblu7l90",
		// "b3uyj9cmk84s5t9n6z1rqs9pvf2azofgacy9bfigmiehhheg");

		// 杩欐槸浣跨敤涓浗鑺傜偣鐨� app 淇℃伅锛屽鏋滀娇鐢ㄤ腑鍥借妭鐐癸紝璇� uncomment 杩欎袱琛�
		// 杩欐槸鐢ㄤ簬 SimpleChat 鐨� app id 鍜� app key锛屽鏋滄洿鏀瑰皢涓嶈兘杩涘叆 demo
		// 涓浉搴旂殑鑱婂ぉ瀹�
		AVOSCloud.initialize(this,
				"9p6hyhh60av3ukkni3i9z53q1l8yy3cijj6sie3cewft18vm",
				"nhqqc1x7r7r89kp8pggrme57i374h3vyd0ukr2z3ayojpvf4");
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 蹇呴』鍦ㄥ惎鍔ㄧ殑鏃跺�欐敞鍐� MessageHandler
		// 搴旂敤涓�鍚姩灏变細閲嶈繛锛屾湇鍔″櫒浼氭帹閫佺绾挎秷鎭繃鏉ワ紝闇�瑕� MessageHandler 鏉ュ鐞�
		AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,
				new MessageHandler(this));
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
