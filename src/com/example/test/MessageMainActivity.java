package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.luna.anytime.AnyTimeApplication;
import com.luna.anytime.R;

public class MessageMainActivity extends BaseActivity {
	EditText clientIdEditText;
	AVUser currentUser = AVUser.getCurrentUser();
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		phone = (String) currentUser.get("mobilePhoneNumber");
		if (!TextUtils.isEmpty(AnyTimeApplication.getClientIdFromPre())) {
			openClient(AnyTimeApplication.getClientIdFromPre());
			return;
		}

		setContentView(R.layout.activity_message_main);
		openClient(phone);
		// clientIdEditText = (EditText) findViewById(R.id.client_id);

		// findViewById(R.id.login).setOnClickListener(new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// final String selfId = clientIdEditText.getText().toString();
		// if (!TextUtils.isEmpty(selfId)) {
		// AnyTimeApplication.setClientIdToPre(phone);
		// openClient(selfId);
		// }
		// }
		// });
	}

	public void openClient(String selfId) {
		AnyTimeApplication.setClientIdToPre(selfId);
		AVIMClient imClient = AVIMClient.getInstance(selfId);
		imClient.open(new AVIMClientCallback() {
			@Override
			public void done(AVIMClient avimClient, AVException e) {
				if (filterException(e)) {
					Intent intent = new Intent(MessageMainActivity.this,
							ConversationActivity.class);
					startActivity(intent);
				}
			}
		});
	}
}
