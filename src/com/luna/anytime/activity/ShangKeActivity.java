package com.luna.anytime.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.chehui.afinal.FinalBitmap;
import com.diy.widget.CircularImage;
import com.luna.anytime.AVService;
import com.luna.anytime.AnyTimeActivity;
import com.luna.anytime.R;
import com.luna.anytime.diy.PickerView.onSelectListener;
import com.luna.anytime.utils.PopKangWindowManager;
import com.luna.anytime.utils.PopReShenWindowManager;

/**
 * 上课
 * 
 * @author Administrator
 * 
 */
public class ShangKeActivity extends AnyTimeActivity implements OnClickListener {
	private LinearLayout ll_add_reshen;
	private ListView listView;
	private ListView listView2;
	private List<AVObject> reshenRecords;
	private List<AVObject> reshenRecords2;
	private List<AVObject> zhutis;
	private CircularImage cover_user_photo;
	private String id;

	private MyAdapter myAdapter;
	private MyAdapter2 myAdapter2;
	private TextView tvLeft;
	private TextView tvTop;
	private TextView tvRight;
	private TextView ptmName;
	private TextView ptmTime;

	private FinalBitmap finalBitmap;
	private String shangkeId;
	private String kangId;
	private LinearLayout ll_zhuti;

	private TextView zhuti1, zhuti2, zhuti3;
	private String reshenChangeStr, reshenId;
	private String kangFuHe, kangObjectId,kangRepeat,kangCount;
	List<String> data1 = new ArrayList<String>();
	List<String> dataFuHe = new ArrayList<String>();
	List<String> dataReturn = new ArrayList<String>();
	List<String> dataCount = new ArrayList<String>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_shangke);
		// Bundle extras = getIntent().getExtras();
		// id = extras.getString("id", "");
		init();
		initTopTitle();
	}

	private void initTopTitle() {
		tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvTop = (TextView) findViewById(R.id.tvTop);
		tvRight = (TextView) findViewById(R.id.tvRight);

		tvLeft.setVisibility(View.INVISIBLE);
		tvTop.setText("上课内容");
		tvRight.setText("完成");
		tvRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						ShangKeActivity.this.finish();
					}
				}).start();
			}
		});
	}

	private void init() {
		initPop();

		finalBitmap = finalBitmap.create(ShangKeActivity.this);
		ll_add_reshen = (LinearLayout) findViewById(R.id.ll_add_reshen);
		ll_add_reshen.setOnClickListener(this);
		LinearLayout ll_add_kang = (LinearLayout) findViewById(R.id.ll_add_kang);
		ll_add_kang.setOnClickListener(this);
		LinearLayout ll_add_zhuti = (LinearLayout) findViewById(R.id.ll_add_zhuti);
		ll_add_zhuti.setOnClickListener(this);
		ll_zhuti = (LinearLayout) findViewById(R.id.ll_zhuti);

		cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
		// cover_user_photo.setImageDrawable(getResources().getDrawable(R.drawable.camera));
		listView = (ListView) findViewById(R.id.lv_reshen);
		listView2 = (ListView) findViewById(R.id.lv_kang);
		ptmName = (TextView) findViewById(R.id.tv_ptm_name);
		ptmTime = (TextView) findViewById(R.id.tv_ptm_time);

		zhuti1 = (TextView) findViewById(R.id.tv_zhuti1);
		zhuti2 = (TextView) findViewById(R.id.tv_zhuti2);
		zhuti3 = (TextView) findViewById(R.id.tv_zhuti3);
		getData();
		getData2();
		getPtmData();

		setPopReShenWidth();
		setPopKangWidth();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				TextView time = (TextView) view.findViewById(R.id.tv_time);
				setText(time, 0);
				reshenId = reshenRecords.get(position).getObjectId();

			}
		});
		
		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				TextView fuhe = (TextView) view.findViewById(R.id.tv_fuhe);
				TextView repeta = (TextView) view.findViewById(R.id.tv_repeat);
				TextView count = (TextView) view.findViewById(R.id.tv_count);
				kangObjectId=reshenRecords2.get(position).getObjectId();
				setTextKang(fuhe,repeta,count);
			}
		});
	}

	private void initPop() {
		data1.add("1分钟");
		data1.add("2分钟");
		data1.add("3分钟");
		data1.add("4分钟");
		data1.add("5分钟");

		dataFuHe.add("1");
		dataFuHe.add("2");
		dataFuHe.add("3");
		dataFuHe.add("4");
		dataFuHe.add("5");
	}

	/***
	 * 设置pop的宽度
	 */
	private void setPopReShenWidth() {
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		PopReShenWindowManager.getInstance().init(getApplicationContext(),
				width, 500, R.layout.pop_reshen);
		PopReShenWindowManager.getInstance().changeTvOKOnClick(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 更新热身运动的时间
						updateReshen();
						PopReShenWindowManager.getInstance().dismissPop();
					}

				});
	}

	private void setPopKangWidth() {
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		PopKangWindowManager.getInstance().init(getApplicationContext(),
				width, 500, R.layout.pop_kang);
		PopKangWindowManager.getInstance().changeTvOKOnClick(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						//更新kang
						updatekang();
						PopKangWindowManager.getInstance().dismissPop();
					}
				});
	}

	private void updatekang() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				AVObject post = new AVObject("kangRecord");
				AVQuery<AVObject> query = new AVQuery<AVObject>("kangRecord");
				try {
					post = query.get(kangObjectId);
				} catch (AVException e1) {
					e1.printStackTrace();
				}
				if(kangFuHe!=null || kangFuHe!=""){
					post.put("fuhe",kangFuHe );
				}
				if(kangCount!=null || kangCount!=""){
					post.put("count",kangCount );
				}
				if(kangRepeat!=null || kangRepeat!=""){
					post.put("repeat",kangRepeat );
				}
				post.saveInBackground(new SaveCallback() {
					@Override
					public void done(AVException e) {
						if (e == null) {
							Log.i("LeanCloud", "Save successfully.");
						} else {
							Log.e("LeanCloud", "Save failed.");
						}
					}
				});
			}
		}).start();
	}
	
	private void updateReshen() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				AVObject post = new AVObject("reshenRecord");
				AVQuery<AVObject> query = new AVQuery<AVObject>("reshenRecord");
				try {
					post = query.get(reshenId);
				} catch (AVException e1) {
					e1.printStackTrace();
				}
				if(reshenChangeStr==null|| reshenChangeStr==""){
					return ;
				}
				
				post.put("exerciseDuration", reshenChangeStr);
				post.saveInBackground(new SaveCallback() {
					@Override
					public void done(AVException e) {
						if (e == null) {
							Log.i("LeanCloud", "Save successfully.");
						} else {
							Log.e("LeanCloud", "Save failed.");
						}
					}
				});
			}
		}).start();

	}

	/**
	 * 更新reshen时间
	 * 
	 * @param view
	 */
	private void setText(final TextView view, final int i) {
		if (data1 == null) {
			return;
		}
		PopReShenWindowManager.getInstance().setPickViewData(data1);
		PopReShenWindowManager.getInstance().changeOnSelect(
				new onSelectListener() {
					@Override
					public void onSelect(String text) {
						view.setText(text);
						reshenChangeStr = text;
					}
				});
		setPopReShenWidth();
		PopReShenWindowManager.getInstance().showPopAllLocation(view,
				Gravity.CENTER | Gravity.BOTTOM, 0, 0);
	};
	/**
	 * 更新kang时间
	 * 
	 * @param view
	 */
	private void setTextKang(final TextView view1, final TextView view2, final TextView view3) {
		kangFuHe=view1.getText().toString();
		kangRepeat=view2.getText().toString();
		kangCount=view3.getText().toString();
		if (dataFuHe == null) {
			return;
		}
		PopKangWindowManager.getInstance().setPickViewData(dataFuHe);
		PopKangWindowManager.getInstance().changeOnSelect1(
				new onSelectListener() {
					
					@Override
					public void onSelect(String text) {
						view1.setText(text);
						kangFuHe = text;
					}
				});
		PopKangWindowManager.getInstance().changeOnSelect2(
				new onSelectListener() {
					
					@Override
					public void onSelect(String text) {
						view2.setText(text);
						kangRepeat = text;
					}
				});
		PopKangWindowManager.getInstance().changeOnSelect3(
				new onSelectListener() {
					
					@Override
					public void onSelect(String text) {
						view3.setText(text);
						kangCount = text;
					}
				});
		setPopKangWidth();
		PopKangWindowManager.getInstance().showPopAllLocation(view2,
				Gravity.CENTER | Gravity.BOTTOM, 0, 0);
	};

	/**
	 * 获取会员信息
	 */
	private void getPtmData() {
		AVQuery<AVObject> query = new AVQuery<AVObject>("PTMember");
		query.whereEqualTo("objectId", AVService.ptmId);
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> a, AVException e) {
				if (e == null) {
					AVObject avObject = a.get(0);
					ptmName.setText(avObject.getString("ptmNickName"));
					ptmTime.setText(avObject.getCreatedAt().toString());
					String url = avObject.getAVFile("ptmIcon").getUrl();
					finalBitmap.display(cover_user_photo, url);
				} else {
					Toast.makeText(ShangKeActivity.this, "获取PTM失败！", 2000)
							.show();
				}
			}
		});
	}

	/**
	 * 获取热身列表
	 */
	private void getData() {
		AVQuery<AVObject> query = new AVQuery<AVObject>("reshenRecord");
		query.whereEqualTo("uid", getUserId());
		query.whereEqualTo("ptmId", AVService.ptmId);
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> a, AVException e) {
				if (e == null) {
					reshenRecords = a;
					if (myAdapter == null) {
						myAdapter = new MyAdapter();
						listView.setAdapter(myAdapter);
					} else {
						myAdapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(ShangKeActivity.this, "获取reshenRecord失败！",
							2000).show();
				}
			}
		});
	}

	private void getData2() {
		AVQuery<AVObject> query = new AVQuery<AVObject>("kangRecord");
		query.whereEqualTo("uid", getUserId());
		query.whereEqualTo("ptmId", AVService.ptmId);
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> a, AVException e) {
				if (e == null) {
					reshenRecords2 = a;
					if (myAdapter2 == null) {
						myAdapter2 = new MyAdapter2();
						listView2.setAdapter(myAdapter2);
					} else {
						myAdapter2.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(ShangKeActivity.this, "获取kangRecord失败！",
							2000).show();
				}
			}
		});
	}

	private void getData3() {
		AVQuery<AVObject> query = new AVQuery<AVObject>("zhutiRecord");
		query.whereEqualTo("uid", getUserId());
		query.whereEqualTo("ptmId", AVService.ptmId);
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> a, AVException e) {
				if (e == null) {
					zhutis = a;
					AVObject avObject = zhutis.get(zhutis.size() - 1);
					if (avObject != null) {
						if (avObject.getString("themeTitle1").isEmpty()
								|| avObject.getString("themeTitle1").equals("")) {
							zhuti1.setVisibility(View.INVISIBLE);
						} else {
							zhuti1.setVisibility(View.VISIBLE);
							zhuti1.setText(avObject.getString("themeTitle1"));
						}

						if (avObject.getString("themeTitle2").isEmpty()
								|| avObject.getString("themeTitle2").equals("")) {
							zhuti2.setVisibility(View.INVISIBLE);
						} else {
							zhuti2.setVisibility(View.VISIBLE);
							zhuti2.setText(avObject.getString("themeTitle2"));
						}

						if (avObject.getString("themeTitle3").isEmpty()
								|| avObject.getString("themeTitle3").equals("")) {
							zhuti3.setVisibility(View.INVISIBLE);
						} else {
							zhuti3.setVisibility(View.VISIBLE);
							zhuti3.setText(avObject.getString("themeTitle3"));
						}

					}
				} else {
					zhuti1.setVisibility(View.INVISIBLE);
					zhuti2.setVisibility(View.INVISIBLE);
					zhuti3.setVisibility(View.INVISIBLE);
					Toast.makeText(ShangKeActivity.this, "获取zhutiRecord失败！",
							2000).show();
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tvLeft:
			break;
		case R.id.tvRight:
			break;
		case R.id.tv_del_reshen:
			delReShen();
			break;
		case R.id.tv_del_kang:
			delKang();
			break;
		case R.id.ll_add_reshen:
			Intent intent = new Intent(ShangKeActivity.this,
					ReShenActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("id", id);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.ll_add_kang:
			Intent intent2 = new Intent(ShangKeActivity.this,
					KangActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString("id", id);
			intent2.putExtras(bundle2);
			startActivity(intent2);
			break;
		case R.id.ll_add_zhuti:
			Intent intent3 = new Intent(ShangKeActivity.this,
					ZhuTiActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putString("id", id);
			intent3.putExtras(bundle3);
			startActivity(intent3);
			break;
		default:
			break;
		}
	}

	private void delReShen() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				AVQuery<AVObject> query = new AVQuery<AVObject>("reshenRecord");
				query.whereEqualTo("ptmId", AVService.ptmId);
				query.whereEqualTo("uid", getUserId());
				query.whereEqualTo("objectId", shangkeId);
				try {
					query.deleteAllInBackground(new DeleteCallback() {

						@Override
						public void done(AVException arg0) {
							Toast.makeText(ShangKeActivity.this, "删除成功！", 2000)
									.show();
							getData();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void delKang() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				AVQuery<AVObject> query = new AVQuery<AVObject>("kangRecord");
				query.whereEqualTo("ptmId", AVService.ptmId);
				query.whereEqualTo("uid", getUserId());
				query.whereEqualTo("objectId", kangId);
				try {
					query.deleteAllInBackground(new DeleteCallback() {

						@Override
						public void done(AVException arg0) {
							Toast.makeText(ShangKeActivity.this, "删除成功！", 2000)
									.show();
							getData2();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyAdapter() {
			inflater = LayoutInflater.from(ShangKeActivity.this);
		}

		@Override
		public int getCount() {
			return reshenRecords.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			final AVObject avObject = reshenRecords.get(position);
			shangkeId = avObject.getObjectId();
			if (view == null) {
				view = inflater.inflate(R.layout.item_reshen2, null);
			}
			TextView name = (TextView) view.findViewById(R.id.tv_name);
			name.setText(avObject.getString("exerciseName"));
			TextView time = (TextView) view.findViewById(R.id.tv_time);
			if (avObject.getString("exerciseDuration") != ""
					|| avObject.getString("exerciseDuration") != null) {
				time.setText(avObject.getString("exerciseDuration")+"");
			}
			TextView tv_del = (TextView) view.findViewById(R.id.tv_del_reshen);
			tv_del.setOnClickListener(ShangKeActivity.this);
			return view;
		}

	}

	private class MyAdapter2 extends BaseAdapter {
		private LayoutInflater inflater;

		public MyAdapter2() {
			inflater = LayoutInflater.from(ShangKeActivity.this);
		}

		@Override
		public int getCount() {
			return reshenRecords2.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			final AVObject avObject = reshenRecords2.get(position);
			kangId = avObject.getObjectId();
			if (view == null) {
				view = inflater.inflate(R.layout.item_kang2, null);
			}
			TextView name = (TextView) view.findViewById(R.id.tv_name);
			name.setText(avObject.getString("exerciseName"));
			TextView fuhe = (TextView) view.findViewById(R.id.tv_fuhe);
			fuhe.setText(avObject.getString("fuhe"));
			TextView repeat = (TextView) view.findViewById(R.id.tv_repeat);
			repeat.setText(avObject.getString("repeat"));
			TextView count = (TextView) view.findViewById(R.id.tv_count);
			count.setText(avObject.getString("count"));

			TextView tv_del = (TextView) view.findViewById(R.id.tv_del_kang);
			tv_del.setOnClickListener(ShangKeActivity.this);
			return view;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		getData();
		getData2();
		getData3();

	}
}
