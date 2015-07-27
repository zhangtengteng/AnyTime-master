package com.luna.anytime.activity;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.luna.anytime.AVService;
import com.luna.anytime.AnyTimeActivity;
import com.luna.anytime.MainActivity;
import com.luna.anytime.NextActivity;
import com.luna.anytime.R;
import com.luna.anytime.adapter.AnytimeUserResponseListAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddMemberActivity2 extends AnyTimeActivity implements
		OnClickListener {
	private TextView tvRight;
	private TextView tvLeft;
	private EditText userphone;
	private EditText useremaile;
	private String phone;
	private String emaile;
	private String Stringname, Stringsex, Stringbirth;
	private byte[] byteArrays;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_member2);
		init();
	}

	private void init() {
		tvRight = (TextView) findViewById(R.id.tvRight);
		tvRight.setOnClickListener(this);
		tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvLeft.setOnClickListener(this);
		userphone = (EditText) findViewById(R.id.et_phone);
		useremaile = (EditText) findViewById(R.id.et_email);

		Intent intent = getIntent();
		Stringname = intent.getStringExtra("name");
		Stringsex = intent.getStringExtra("sex");
		Stringbirth = intent.getStringExtra("birth");
		byteArrays = intent.getByteArrayExtra("icon");

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tvLeft:
			AddMemberActivity2.this.finish();
			break;
		case R.id.tvRight:
			register();
			break;
		default:
			break;
		}
	}

	public void register() {
		if(useremaile.getText().toString().isEmpty()||useremaile.getText().toString().isEmpty()){
			Toast.makeText(AddMemberActivity2.this, "请完善资料！", 2000).show();
			return;
		}
		SaveCallback sCallback = new SaveCallback() {
			public void done(AVException e) {
				progressDialogDismiss();
				if (e == null) {
					startActivity(new Intent(AddMemberActivity2.this,
							AddMemberActivity3.class));
					AddMemberActivity2.this.finish();
				} else {
					switch (e.getCode()) {
					case 202:
						Toast.makeText(AddMemberActivity2.this, "202", 1000);
						break;
					case 203:
						Toast.makeText(AddMemberActivity2.this, "203", 1000);
						break;
					default:
						showError(AddMemberActivity2.this
								.getString(R.string.network_error));
						break;
					}
				}
			}
		};
		progressDialogShow();
		AVService.pTMemberinfo(Stringname, Stringsex, Stringbirth, userphone
				.getText().toString(), useremaile.getText().toString(),
				getUserId(), byteArrays,sCallback);

	}
	
}
