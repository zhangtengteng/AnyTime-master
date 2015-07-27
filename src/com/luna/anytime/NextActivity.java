package com.luna.anytime;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.luna.anytime.utils.Utils;

public class NextActivity<CircularImage> extends AnyTimeActivity {

	private ImageView userPhoto;
	private EditText userName;
	private EditText userSex;;
	private EditText userbrithday;
	private EditText address;
	private EditText str;
	private String Userphone;
	private String UserPWD;
	private Button btn_reg;

	private TextView tvLeft;
	private TextView tvTop;
	private TextView tvRight;

	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;

	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private byte[] byteArrays;
	private static final String IMAGE_FILE_LOCATION = Environment
			.getExternalStorageDirectory() + "/temp.jpg";

	// + "/head.jpg";
	// private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(R.layout.activity_person_info,
				null);
		setContentView(parentView);
		btn_reg = (Button) findViewById(R.id.rg_btn);
		Userphone = getIntent().getStringExtra("phone");
		UserPWD = getIntent().getStringExtra("password");
		initTopTitle();
		initView();

	}

	private void initTopTitle() {
		tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvTop = (TextView) findViewById(R.id.tvTop);
		tvRight = (TextView) findViewById(R.id.tvRight);
		tvLeft.setVisibility(View.INVISIBLE);
		tvTop.setText("个人信息");
		tvRight.setText("下一步");
		tvRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						register();
					}
				}).start();
			}
		});
	}

	public void initView() {

		userPhoto = (ImageView) findViewById(R.id.cover_user_photo);
		Bitmap loacalBitmap = Utils.getLoacalBitmap(IMAGE_FILE_LOCATION);
		if (loacalBitmap != null) {
			userPhoto.setImageBitmap(loacalBitmap);
		}
		userName = (EditText) findViewById(R.id.ed_name);
		userbrithday = (EditText) findViewById(R.id.ed_birthday);
		userSex = (EditText) findViewById(R.id.ed_sex);
		address = (EditText) findViewById(R.id.ed_dizhi);
		str = (EditText) findViewById(R.id.tv_str);

		userPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				photo();
//				if (!pop.isShowing()) {
//					pop.showAtLocation(parentView, Gravity.CENTER, 0, 0);
//				}
			}
		});

		pop = new PopupWindow(NextActivity.this);
		// ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				// ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				// pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				// ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Intent intent = new Intent(ActivityCertifiedMerchants2.this,
				// AlbumActivity.class);
				// startActivity(intent);
				// overridePendingTransition(R.anim.activity_translate_in,
				// R.anim.activity_translate_out);
				pop.dismiss();
				// pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				// ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				// ll_popup.clearAnimation();
			}
		});

		// btn_reg.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// register();
		//
		// }
		// });

	}

	public void register() {
		SignUpCallback signUpCallback = new SignUpCallback() {
			public void done(AVException e) {
				if (e == null) {
					// showRegisterSuccess();
					Intent mainIntent = new Intent(NextActivity.this,
							MainActivity.class);
					startActivity(mainIntent);
					NextActivity.this.finish();
				} else {
					Toast.makeText(NextActivity.this, "注册失败", 1000).show();
				}
			}
		};
		String username = userName.getText().toString();
		
		// URL userphoto = userPhoto.getClass().getResource("http://");
		String usersex = userSex.getText().toString();
		String userbrit = userbrithday.getText().toString();
		AVService.signUp(UserPWD, Userphone, username, usersex, userbrit, str
				.getText().toString(),byteArrays,true,signUpCallback);
	}

	// private void showRegisterSuccess() {
	// new AlertDialog.Builder(activity)
	// .setTitle(
	// activity.getResources().getString(
	// R.string.dialog_message_title))
	// .setMessage(
	// activity.getResources().getString(
	// R.string.success_register_success))
	// .setNegativeButton(android.R.string.ok,
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int which) {
	// dialog.dismiss();
	// }
	// }).show();
	// }



	public void photo() {
		// Intent openCameraIntent = new
		// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// // 下面这句指定调用相机拍照后的照片存储的路径
		// openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new
		// File(
		// Environment.getExternalStorageDirectory(), "test.jpg")));
		// startActivityForResult(openCameraIntent, TAKE_PICTURE);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), "temp.jpg")));
		System.out.println("============="
				+ Environment.getExternalStorageDirectory());
		startActivityForResult(intent, PHOTOHRAPH);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/temp.jpg");
			System.out.println("------------------------" + picture.getPath());
			startPhotoZoom(Uri.fromFile(picture));
		}

		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 -to
				byteArrays = stream.toByteArray();
				userPhoto.setImageBitmap(photo);
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 64);
		intent.putExtra("outputY", 64);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}
}
