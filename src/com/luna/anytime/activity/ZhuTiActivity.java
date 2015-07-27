package com.luna.anytime.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.luna.anytime.AVService;
import com.luna.anytime.AnyTimeActivity;
import com.luna.anytime.R;

/**
 * 主题
 * 
 * @author Administrator
 * 
 */
public class ZhuTiActivity extends AnyTimeActivity implements OnClickListener {
	private TextView tvLeft;
	private TextView tvTop;
	private TextView tvRight;

	private String id;
	private GridView gv;
	private List<AVObject> avObjects;

	private MyAdapter myAdapter;
	private Map<String,TextView> map = new HashMap<String,TextView>();
	private String currentMap;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_zhuti);
		Bundle extras = getIntent().getExtras();
		id = extras.getString("id", "");
		init();
		initTopTitle();
	}

	private void init() {
		gv = (GridView) findViewById(R.id.gv_zhuti);
		AVQuery<AVObject> query = new AVQuery<AVObject>("zhuti");
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> a, AVException e) {
				if (e == null) {
					avObjects = a;
					if (myAdapter == null) {
						myAdapter = new MyAdapter();
						gv.setAdapter(myAdapter);
					} else {
						myAdapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(ZhuTiActivity.this, "获取主题标签失败！", 2000)
							.show();
				}
			}
		});
		
		gv.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				switch (map.size()) {
				case 0:
					break;
				case 1:
					
					break;
				case 2:
					
					break;

				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				
			}
		});
		
	}

	private void initTopTitle() {
		tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvTop = (TextView) findViewById(R.id.tvTop);
		tvRight = (TextView) findViewById(R.id.tvRight);

		tvLeft.setVisibility(View.VISIBLE);
		tvLeft.setText("取消");
		tvLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ZhuTiActivity.this.finish();
			}
		});
		tvTop.setText("主题");
		tvRight.setText("完成");
		tvRight.setVisibility(View.VISIBLE);
		tvRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						addZhuTi();
					}
				}).start();
			}
		});
	}

	
	private void addZhuTi() {
		String title1=(String) (map.get("0")!=null?map.get("0").getText():"");
		String title2=(String) (map.get("1")!=null?map.get("1").getText():"");
		String title3=(String) (map.get("2")!=null?map.get("2").getText():"");
		AVService.addZhuTiName(title1,title2,title3, getUserId(), AVService.ptmId, new SaveCallback() {
			@Override
			public void done(AVException arg0) {
				if(arg0==null){
					Toast.makeText(ZhuTiActivity.this, "添加主题成功！", 2000).show();
					ZhuTiActivity.this.finish();
				}else{
					Toast.makeText(ZhuTiActivity.this, "添加主题失败！", 2000).show();
				}
			}
		});
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tvLeft:
			ZhuTiActivity.this.finish();
			break;
		case R.id.tvRight:
			break;
		default:
			break;
		}
	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyAdapter() {
			inflater = LayoutInflater.from(ZhuTiActivity.this);
		}

		@Override
		public int getCount() {
			return avObjects.size();
		}

		@Override
		public Object getItem(int arg0) {
			return avObjects.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			AVObject avObject = avObjects.get(position);
			if (view == null) {
				view = inflater.inflate(R.layout.item_zhuti2, null);
			}
			final TextView name = (TextView) view.findViewById(R.id.tv_zhuti_name);
			name.setText(avObject.getString("themeTitle").toString());
			name.setTag(avObject.getObjectId());
			name.setOnClickListener(new OnClickListener() {
				
				@SuppressLint("ResourceAsColor") @Override
				public void onClick(View arg0) {
					if(currentMap==null || currentMap==""){
						map.put("0", name);
						currentMap="0";
					}else if(currentMap=="0"){
						if(map.get("1")!=null){
							map.get("1").setBackgroundColor(Color.parseColor("#e0e0e1"));
							map.get("1").setTextColor(Color.parseColor("#ff000000"));
						}
						map.put("1", name);
						currentMap="1";
					}else if(currentMap=="1"){
						if(map.get("2")!=null){
							map.get("2").setBackgroundColor(Color.parseColor("#e0e0e1"));
							map.get("2").setTextColor(Color.parseColor("#ff000000"));
						}
						map.put("2", name);
						currentMap="2";
					}else if(currentMap=="2"){
						if(map.get("0")!=null){
							map.get("0").setBackgroundColor(Color.parseColor("#e0e0e1"));
							map.get("0").setTextColor(Color.parseColor("#ff000000"));
						}
						map.put("0", name);
						currentMap="0";
					}
					name.setBackgroundColor(Color.parseColor("#990000"));
					name.setTextColor(Color.parseColor("#FFFFFFFF"));
				}
			});
			return view;
		}

	}
}
