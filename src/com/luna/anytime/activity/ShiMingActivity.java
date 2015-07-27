package com.luna.anytime.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luna.anytime.MainActivity;
import com.luna.anytime.R;
import com.luna.anytime.utils.Utils;
/***
 * 实名认证
 * @author Administrator
 *
 */
public class ShiMingActivity extends Activity{
	private TextView tvLeft;
	private TextView tvTop;
	private TextView tvRight;
	private ImageView icon;
	
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	
	private static final int TAKE_PICTURE = 0x000001;
	private static final String IMAGE_FILE_LOCATION= Utils.getSDPath()+"head2.jpg";
	private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(
				R.layout.activity_shiming, null);
		setContentView(parentView);
		initTopTitle();
		init();
	}
	private void initTopTitle() {
		tvLeft=(TextView) findViewById(R.id.tvLeft);
		tvTop=(TextView) findViewById(R.id.tvTop);
		tvRight=(TextView) findViewById(R.id.tvRight);
		tvLeft.setVisibility(View.INVISIBLE);
		tvTop.setText("实名认证");
		tvRight.setText("跳过/完成");
		tvRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(ShiMingActivity.this,MainActivity.class));
				ShiMingActivity.this.finish();
		
			}
		});
	}
	private void init() {
		icon=(ImageView) findViewById(R.id.icon);
		
		icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!pop.isShowing()) {
					pop.showAtLocation(parentView, Gravity.CENTER, 0, 0);
				}
			}
		});
	pop = new PopupWindow(ShiMingActivity.this);
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
//			pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
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
//			pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			// ll_popup.clearAnimation();
		}
	});
	bt3.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			pop.dismiss();
			// ll_popup.clearAnimation();
		}
	});
	
	}
	
	
	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE,imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}	

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			if (resultCode == RESULT_OK) {
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				icon.setImageBitmap(bm);
				
				// FileUtils.saveBitmap(bm, fileName);
				// ImageItem takePhoto = new ImageItem();
				// takePhoto.setBitmap(bm);
				// Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}
	
}
