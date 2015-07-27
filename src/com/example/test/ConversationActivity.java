package com.example.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.Conversation;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.luna.anytime.AnyTimeApplication;
import com.luna.anytime.R;

/**
 * Created by zhangxiaobo on 15/4/16.
 */
public class ConversationActivity extends BaseActivity implements
		View.OnClickListener {
	// 杩欐槸浣跨敤涓浗鑺傜偣鏃朵娇鐢ㄧ殑 瀵硅瘽 id銆傚鏋滀笉浣跨敤缇庡浗鑺傜偣锛岃 uncomment 杩欎竴琛屻��
	public static final String CONVERSATION_ID = "551a2847e4b04d688d73dc54";
	private static final String TAG = ConversationActivity.class
			.getSimpleName();
	// 杩欐槸浣跨敤缇庡浗鑺傜偣鏃朵娇鐢ㄧ殑 瀵硅瘽 id銆傚鏋滀笉浣跨敤缇庡浗鑺傜偣锛岃 comment 杩欎竴琛屻��
	// public static final String CONVERSATION_ID = "55489bd9e4b065597b2061d6";

	private TextView clientIdTextView;
	private EditText otherIdEditText;

	AVUser currentUser = AVUser.getCurrentUser();
	private String phone = (String) currentUser.get("mobilePhoneNumber");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);
		AnyTimeApplication.setClientIdToPre(phone);
		AVIMClient imClient = AVIMClient.getInstance(phone);
		imClient.open(new AVIMClientCallback() {
			
			@Override
			public void done(AVIMClient arg0, AVException arg1) {
				
			}
		});
		// init
		clientIdTextView = (TextView) findViewById(R.id.client_id);
		otherIdEditText = (EditText) findViewById(R.id.otherIdEditText);

		clientIdTextView.setText(getString(R.string.welcome) + " "
				+ AnyTimeApplication.getClientIdFromPre());

		findViewById(R.id.join_conversation).setOnClickListener(this);

		findViewById(R.id.logout).setOnClickListener(this);
		findViewById(R.id.chat_with_other).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logout:
			AnyTimeApplication.setClientIdToPre("");
			finish();
			break;
		case R.id.join_conversation:
			AVIMConversation conversation = AnyTimeApplication.getIMClient()
					.getConversation(CONVERSATION_ID);
			if (conversation.getMembers().contains(
					AnyTimeApplication.getClientIdFromPre())) {
				ChatActivity.startActivity(ConversationActivity.this,
						CONVERSATION_ID);
			} else {
				conversation.join(new AVIMConversationCallback() {
					@Override
					public void done(AVException e) {
						if (filterException(e)) {
							ChatActivity.startActivity(
									ConversationActivity.this, CONVERSATION_ID);
						}
					}
				});
			}
			break;
		case R.id.chat_with_other:
			String otherId = otherIdEditText.getText().toString();
			if (!TextUtils.isEmpty(otherId)) {
				fetchConversationWithClientIds(Arrays.asList(otherId),
						ConversationType.OneToOne,
						new AVIMConversationCreatedCallback() {
							@Override
							public void done(AVIMConversation conversation,
									AVException e) {
								if (filterException(e)) {
									ChatActivity.startActivity(
											ConversationActivity.this,
											conversation.getConversationId());
								}
							}
						});
			}
			break;
		}
	}

	private void fetchConversationWithClientIds(List<String> clientIds,
			final ConversationType type,
			final AVIMConversationCreatedCallback callback) {
		final AVIMClient imClient = AnyTimeApplication.getIMClient();
		final List<String> queryClientIds = new ArrayList<String>();
		queryClientIds.addAll(clientIds);
		if (!clientIds.contains(imClient.getClientId())) {
			queryClientIds.add(imClient.getClientId());
		}
		AVIMConversationQuery query = imClient.getQuery();
		query.whereEqualTo(Conversation.ATTRIBUTE_MORE + ".type",
				type.getValue());
		query.whereContainsAll(Conversation.COLUMN_MEMBERS, queryClientIds);
		query.findInBackground(new AVIMConversationQueryCallback() {
			@Override
			public void done(List<AVIMConversation> list, AVException e) {
				if (e != null) {
					callback.done(null, e);
				} else {
					if (list == null || list.size() == 0) {
						Map<String, Object> attributes = new HashMap<String, Object>();
						attributes.put(ConversationType.KEY_ATTRIBUTE_TYPE,
								type.getValue());
						imClient.createConversation(queryClientIds, attributes,
								callback);
					} else {
						callback.done(list.get(0), null);
					}
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AnyTimeApplication.getIMClient().close(new AVIMClientCallback() {
			@Override
			public void done(AVIMClient avimClient, AVException e) {
				if (e == null) {
					Log.d(TAG, "閫�鍑鸿繛鎺�");
				} else {
					Toast.makeText(ConversationActivity.this, e.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
