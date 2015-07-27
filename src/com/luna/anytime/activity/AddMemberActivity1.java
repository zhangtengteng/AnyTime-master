package com.luna.anytime.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diy.widget.CircularImage;
import com.luna.anytime.AnyTimeActivity;
import com.luna.anytime.R;

public class AddMemberActivity1 extends AnyTimeActivity implements
		OnClickListener {
	private TextView tvRight;
	private TextView tvLeft;
	private CircularImage icon;
	private EditText username;
	private EditText usersex;
	private EditText userbirth;
	
	private LinearLayout ll_icon;
	private String name;
	private String sex;
	private String birth;

	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private byte[] byteArrays;
	private static final String IMAGE_FILE_LOCATION = Environment
			.getExternalStorageDirectory() + "/temp.jpg";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_member1);
		init();
	}

	private void init() {
		tvRight = (TextView) findViewById(R.id.tvRight);
		tvRight.setOnClickListener(this);
		tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvLeft.setOnClickListener(this);
		icon = (CircularImage) findViewById(R.id.cover_user_photo);
		icon.setImageDrawable(getResources().getDrawable(R.drawable.camera));
		username = (EditText) findViewById(R.id.et_name);
		usersex = (EditText) findViewById(R.id.et_sex);
		userbirth = (EditText) findViewById(R.id.et_birth);
		ll_icon=(LinearLayout) findViewById(R.id.ll_icon);
		ll_icon.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tvLeft:
			AddMemberActivity1.this.finish();
			break;
		case R.id.tvRight:
			// startActivity(new
			// Intent(AddMemberActivity1.this,AddMemberActivity2.class));
			if(username.getText().toString()==null||userbirth.getText().toString()==null||usersex.getText().toString()==null||byteArrays==null){
				Toast.makeText(AddMemberActivity1.this, "请完善资料！", 2000).show();
				return ;
			}
			Intent intent = new Intent(AddMemberActivity1.this,
					AddMemberActivity2.class);
			intent.putExtra("name", username.getText().toString());
			intent.putExtra("birth", userbirth.getText().toString());
			if(usersex.getText().toString().equals("女")){
				intent.putExtra("sex","♀" );
			}else{
				intent.putExtra("sex","♂" );
			}
			intent.putExtra("icon", byteArrays);
			startActivity(intent);
			AddMemberActivity1.this.finish();
			break;
		case R.id.ll_icon:
			//拍照
			photo();
			break;
		default:
			break;
		}
	}
	
	public void photo() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), "addMember.jpg")));
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
					+ "/addMember.jpg");
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
				icon.setImageBitmap(photo);
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
