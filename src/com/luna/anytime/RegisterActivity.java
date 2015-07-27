package com.luna.anytime;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.luna.anytime.activity.AddMemberActivity1;

public class RegisterActivity extends AnyTimeActivity {
	Button registerButton;
	EditText userName;
	EditText userCode;
	EditText userPassword;
	Button sencodeMss;
	String phone;
	String code ;
	private ProgressDialog progressDialog;
	
	private TextView tvLeft;
	private TextView tvTop;
	private TextView tvRight;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
		
		
		tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvTop = (TextView) findViewById(R.id.tvTop);
		tvRight = (TextView) findViewById(R.id.tvRight);
		tvLeft.setVisibility(View.VISIBLE);
		tvTop.setText("注册");
		tvRight.setText("下一步");
		tvRight.setVisibility(View.INVISIBLE);
//		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		sencodeMss = (Button) findViewById(R.id.btn_send_code);
		registerButton = (Button) findViewById(R.id.button_i_need_register);
		userName = (EditText) findViewById(R.id.editText_register_userphone);
		userCode = (EditText) findViewById(R.id.editText_register_code);
		userPassword = (EditText) findViewById(R.id.editText_register_userPassword);
		tvLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				RegisterActivity.this.finish();
			}
		});
		sencodeMss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					public void run() {
						try {
							AVOSCloud.requestSMSCode(userName.getText()
									.toString().trim(), "aaa", "注册", 10);
						} catch (AVException e) {
							e.printStackTrace();
						}
					}

				}).start();
				;
			}
		});

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 next();
				// if (userPassword.getText().toString()
				// .equals(userPasswordAgain.getText().toString())) {
				if (!userPassword.getText().toString().isEmpty()) {
					if (!userName.getText().toString().isEmpty()) {
						if (!userCode.getText().toString().isEmpty()) {
							 phone = userName.getText().toString();
							 code = userCode.getText().toString();
							 verifyCode(code);
//							AVOSCloud.verifySMSCodeInBackground(code, phone,
//									new AVMobilePhoneVerifyCallback() {
//										@Override
//										public void done(AVException e) {
//											if (e == null) {
//												// 发送成功
//												next();
//
//											} else {
//												Toast.makeText(
//														RegisterActivity.this,
//														"验证码不正确",
//														Toast.LENGTH_LONG)
//														.show();
//											}
//
//										}
//									});
							
							
							// progressDialogShow();
						
						} else {
							showError(activity
									.getString(R.string.error_register_email_address_null));
						}
					} else {
						showError(activity
								.getString(R.string.error_register_user_name_null));
					}
				} else {
					showError(activity
							.getString(R.string.error_register_password_null));
				}
			
			}
		});
	}


	  private void verifyCode(String code) {
		    AVOSCloud.verifySMSCodeInBackground(code, phone,
		        new AVMobilePhoneVerifyCallback() {
		          @Override
		          public void done(AVException e) {
		            if (e == null) {
		            	 next();
		            	  Toast.makeText(
									RegisterActivity.this,
									"验证正确",
									Toast.LENGTH_LONG)
									.show();
		            } else {
		              e.printStackTrace();
		              Toast.makeText(
								RegisterActivity.this,
								"验证码不正确",
								Toast.LENGTH_LONG)
								.show();
		            }
		          }
		        });
		  }
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent LoginIntent = new Intent(this, LoginActivity.class);
			startActivity(LoginIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void next() {
		Intent mainIntent = new Intent(activity, NextActivity.class);
		String password = userPassword.getText().toString();
		String phone = userName.getText().toString();
		if(password.isEmpty()||phone.isEmpty()){
			Toast.makeText(RegisterActivity.this, "请完善资料！", 2000).show();
			return;
		}
		mainIntent.putExtra("phone", phone);
		mainIntent.putExtra("password", password);
		startActivity(mainIntent);
		RegisterActivity.this.finish();
	}

	// public void register() {
	// SignUpCallback signUpCallback = new SignUpCallback() {
	// public void done(AVException e) {
	// progressDialogDismiss();
	// if (e == null) {
	// showRegisterSuccess();
	// Intent mainIntent = new Intent(activity, MainActivity1.class);
	// startActivity(mainIntent);
	// activity.finish();
	// } else {
	// switch (e.getCode()) {
	// case 202:
	// showError(activity
	// .getString(R.string.error_register_user_name_repeat));
	// break;
	// case 203:
	// showError(activity
	// .getString(R.string.error_register_email_repeat));
	// break;
	// default:
	// showError(activity
	// .getString(R.string.network_error));
	// break;
	// }
	// }
	// }
	// };
	// String username = userName.getText().toString();
	// String password = userPassword.getText().toString();
	// String email = userEmail.getText().toString();
	//
	// AVService.signUp(username, password, email, signUpCallback);
	// }

//	private void progressDialogDismiss() {
//		if (progressDialog != null)
//			progressDialog.dismiss();
//	}

	// ��ȴ�
//	private void progressDialogShow() {
//		progressDialog = ProgressDialog
//				.show(activity,
//						activity.getResources().getText(
//								R.string.dialog_message_title),
//						activity.getResources().getText(
//								R.string.dialog_text_wait), true, false);
//	}
	// ע��ɹ�

	private void showRegisterSuccess() {
		new AlertDialog.Builder(activity)
				.setTitle(
						activity.getResources().getString(
								R.string.dialog_message_title))
				.setMessage(
						activity.getResources().getString(
								R.string.success_register_success))
				.setNegativeButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}
}
