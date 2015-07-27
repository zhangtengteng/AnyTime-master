package com.luna.anytime.activity;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.luna.anytime.AnyTimeActivity;
import com.luna.anytime.MainActivity;
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

public class AddMemberActivity3 extends AnyTimeActivity implements OnClickListener{
	private TextView tvRight;
	private TextView tvLeft;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_member3);
		init();
	}
	private void init() {
		tvRight=(TextView) findViewById(R.id.tvRight);
		tvRight.setOnClickListener(this);
		tvLeft=(TextView) findViewById(R.id.tvLeft);
		tvLeft.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tvLeft:
			AddMemberActivity3.this.finish();
			break;
		case R.id.tvRight:
			AddMemberActivity3.this.finish();
			break;
		default:
			break;
		}
	}
}
