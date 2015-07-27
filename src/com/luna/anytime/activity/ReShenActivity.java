package com.luna.anytime.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
 * 热身
 * @author Administrator
 *
 */
public class ReShenActivity extends AnyTimeActivity implements OnClickListener{
	private TextView tvLeft;
	private TextView tvTop;
	private TextView tvRight;
	
	private String id;
	private ListView listView;
	private List<AVObject> avObjects;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reshen);
//		Bundle extras = getIntent().getExtras();
//		id = extras.getString("id","");
		init();
		initTopTitle();
	}
	private void init() {
		listView= (ListView) findViewById(R.id.lv_reshen);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String exerciseName = (String) avObjects.get(position).get("exerciseName");
				String objectId = getUserId();
				AVService.addExerciseName(exerciseName,objectId,AVService.ptmId,new SaveCallback() {
					@Override
					public void done(AVException arg0) {
						if(arg0==null){
							Toast.makeText(ReShenActivity.this, "添加成功！", 2000).show();
							ReShenActivity.this.finish();
						}else{
							Toast.makeText(ReShenActivity.this, "添加失败！", 2000).show();
						}
					}
				});
				addReshenRecord();
			}

		
		});
		AVQuery<AVObject> query = new AVQuery<AVObject>("reshen");
		query.findInBackground(new FindCallback<AVObject>() {
		    public void done(List<AVObject> a, AVException e) {
		        if (e == null) {
		        	avObjects=a;
		        	listView.setAdapter(new MyAdapter());
		        } else {
		        	
		        }
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
				ReShenActivity.this.finish();
			}
		});
		tvTop.setText("训练");
		tvRight.setText("");
		tvRight.setVisibility(View.INVISIBLE);
		tvRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
					}
				}).start();
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
		default:
			break;
		}
	}
	
	
	private void addReshenRecord() {
		
	}
	
	private class MyAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		public MyAdapter(){
			inflater=LayoutInflater.from(ReShenActivity.this);
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
			if(view==null){
				view=inflater.inflate(R.layout.item_reshen, null);
			}
			TextView name = (TextView) view.findViewById(R.id.name);
			name.setText(avObject.getString("exerciseName"));
			return view;
		}
		
	}
}
