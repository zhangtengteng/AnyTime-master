package com.luna.anytime.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.luna.anytime.LoginActivity;
import com.luna.anytime.R;
import com.luna.anytime.RegisterActivity;

public class LoadingActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		findViewById(R.id.btn_star).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(LoadingActivity.this,LoginActivity.class));
				LoadingActivity.this.finish();
			}
		});
	}
}
