package com.luna.anytime.diy;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.chehui.afinal.FinalBitmap;
import com.diy.widget.CircularImage;
import com.luna.anytime.AVService;
import com.luna.anytime.AnyTimeActivity;
import com.luna.anytime.R;
import com.luna.anytime.activity.ShangKeActivity;

/***
 * 排课列表页面
 * 
 * @author Administrator
 * 
 */
public class SimpleActivity extends AnyTimeActivity {
	List<AVObject> avObjects;
	private List<ApplicationInfo> mAppList;
	private AppAdapter mAdapter;
	private SwipeMenuListView mListView;
	private CircularImage cover_user_photo;
	private FinalBitmap finalBitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		init();

		mAppList = getPackageManager().getInstalledApplications(0);
		mListView = (SwipeMenuListView) findViewById(R.id.listView);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Intent intent = new Intent(SimpleActivity.this,
						ShangKeActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("id",avObjects.get(position).getString("objectId"));
//				intent.putExtras(bundle);
				AVObject avObject = avObjects.get(position);
				String ptmid = avObject.getObjectId();
//				System.out.println("ptmid=========="+ptmid);
				AVService.ptmId=ptmid;
				startActivity(intent);
			}
		});
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("SMS");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				openItem.setTitleColor(Color.WHITE);
//				openItem.setTitle("CALL");
				// set a icon
				deleteItem.setIcon(R.drawable.zhanghao);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				// ApplicationInfo item = mAppList.get(position);
				AVObject avObject = avObjects.get(position);
				switch (index) {
				case 0:
					// 发送短信
					sendSMS(avObject.get("ptmPhone").toString(), "你好！");

					// 发送结果提示
					Toast.makeText(SimpleActivity.this, "发送成功",
							Toast.LENGTH_LONG).show();

					// open
					// open(item);
					break;
				case 1:
					// 打电话
					// 用intent启动拨打电话
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + avObject.getString("ptmPhone")));
					startActivity(intent);
					Toast.makeText(SimpleActivity.this, avObject.getString("ptmPhone"),
							Toast.LENGTH_LONG).show();
					// delete
					// delete(item);
					// mAppList.remove(position);
					// mAdapter.notifyDataSetChanged();
					break;
				}
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
		// listView.setCloseInterpolator(new BounceInterpolator());

		// test item long click
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						position + " long click", 0).show();
				return false;
			}
		});

	}

	private void init() {
		finalBitmap=finalBitmap.create(SimpleActivity.this);
		// 查询PTMember表
		getPTMember();

	}

	/**
	 * // 查询PTMember表
	 */
	private void getPTMember() {
		AVQuery<AVObject> query = new AVQuery<AVObject>("PTMember");
		query.whereEqualTo("puid",getUserId());
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> a, AVException e) {
				if (e == null) {
					avObjects = a;
					if (mAdapter == null) {
						mAdapter = new AppAdapter();
						mListView.setAdapter(mAdapter);
					} else {
						mAdapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(SimpleActivity.this,
							R.string.dialog_error_title, 2000).show();
				}
			}
		});
	}

	private void delete(ApplicationInfo item) {
		// delete app
		try {
			Intent intent = new Intent(Intent.ACTION_DELETE);
			intent.setData(Uri.fromParts("package", item.packageName, null));
			startActivity(intent);
		} catch (Exception e) {
		}
	}

	private void open(ApplicationInfo item) {
		// open app
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(item.packageName);
		List<ResolveInfo> resolveInfoList = getPackageManager()
				.queryIntentActivities(resolveIntent, 0);
		if (resolveInfoList != null && resolveInfoList.size() > 0) {
			ResolveInfo resolveInfo = resolveInfoList.get(0);
			String activityPackageName = resolveInfo.activityInfo.packageName;
			String className = resolveInfo.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName componentName = new ComponentName(
					activityPackageName, className);

			intent.setComponent(componentName);
			startActivity(intent);
		}
	}

	class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return avObjects.size();
		}

		@Override
		public AVObject getItem(int position) {
			return avObjects.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AVObject avObject = avObjects.get(position);
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.activity_content_item, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			// holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
			holder.tv_name.setText(avObject.getString("ptmNickName"));
			holder.gender.setText(avObject.getString("ptmGender") +" "+avObject.getString("ptmBirth"));
			holder.time.setText(avObject.getString("time"));
			holder.theme1.setText(avObject.getString("themeTitle1"));
			holder.theme2.setText(avObject.getString("themeTitle2"));
			String url = avObject.getAVFile("ptmIcon").getUrl();
			System.out.println("url ====="+url);
			finalBitmap.display(holder.iv_icon, url);
//			holder.iv_icon.setImageDrawable(getResources().getDrawable(
//					R.drawable.camera));
			return convertView;
		}

		class ViewHolder {
			CircularImage iv_icon;
			TextView tv_name;
			TextView gender;
			TextView createdAt;
			TextView time;
			TextView theme1;
			TextView theme2;

			public ViewHolder(View view) {
				iv_icon = (CircularImage) view
						.findViewById(R.id.cover_user_photo);
				tv_name = (TextView) view.findViewById(R.id.name);
				gender = (TextView) view.findViewById(R.id.gender);
				time = (TextView) view.findViewById(R.id.time);
				theme1 = (TextView) view.findViewById(R.id.tv_theme1);
				theme2 = (TextView) view.findViewById(R.id.tv_theme2);

				view.setTag(this);
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	/**
	 * 直接调用短信接口发短信
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public void sendSMS(String phoneNumber, String message) {
		// 获取短信管理器
		android.telephony.SmsManager smsManager = android.telephony.SmsManager
				.getDefault();
		// 拆分短信内容（手机短信长度限制）
		List<String> divideContents = smsManager.divideMessage(message);
		for (String text : divideContents) {
			smsManager.sendTextMessage(phoneNumber, null, text, null, null);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPTMember();
	}
}
