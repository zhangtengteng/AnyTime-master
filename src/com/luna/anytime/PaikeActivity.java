package com.luna.anytime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PaikeActivity extends Activity {

	TextView nameTextView;
	TextView registerTimeTextView;

	public PaikeActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_gerenzhongxin);
//		nameTextView = (TextView)findViewById(R.id.textView_userName);
//		registerTimeTextView = (TextView)findViewById(R.id.textView_register_time);
//		
//		AVUser currentUser = AVUser.getCurrentUser();
//		nameTextView.setText(currentUser.getUsername());
//		String date = DateFormat.format("yyyy-MM-dd HH:mm",
//				currentUser.getCreatedAt()).toString();
//		registerTimeTextView.setText(getString(R.string.person_register_time)
//				.replace("{0}", date));
		
		
		TextView loginout = (TextView) findViewById(R.id.tv_logout);
		
		loginout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				AVService.logout();
				
			}
		});
	}
	
}
