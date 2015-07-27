package com.luna.anytime;

import java.util.LinkedList;

import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.test.ConversationActivity;
import com.example.test.MessageMainActivity;
import com.luna.anytime.activity.AddMemberActivity1;
import com.luna.anytime.data.CommonData;
import com.luna.anytime.diy.SimpleActivity;

public class MainActivity extends ActivityGroup implements OnClickListener {

	/**
	 * 上下文
	 */
	private Context mContext;

	/**
	 * home布局
	 */
	private View mHomeView;

	/**
	 * order布局
	 */
	private View mOrderView;

	/**
	 * shop布局
	 */
	private View mShopView;

	private View mSelectedView;

	/**
	 * 第一次点击back键的时间
	 */
	private long mExitTime;

	/**
	 * 选中模块下标
	 */
	private int currentIndex = CommonData.TAB_HOME_INDEX;

	/**
	 * 存放activity的栈
	 */
	public static LinkedList<String> mActivityStack = new LinkedList<String>();

	/**
	 * 内容根布局
	 */
	private FrameLayout mContainer = null;
	
	private TextView tvTop;
	
	private ProgressDialog progressDialog = null;
	

	// 顶部标题栏
	private TextView tvLeft;
	private TextView tvRight;
	private ImageButton btnDown;

	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {

				default :   
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		initViews();
		initTopViews(1);
		bindListener();
		loadDefault();

	}

	
	
	
	


	/***
	 * 初始化顶部标题栏
	 */
	private void initTopViews(int type) {
		tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvLeft.setVisibility(View.INVISIBLE);
		tvRight = (TextView) findViewById(R.id.tvRight);
//		btnDown = (ImageButton) findViewById(R.id.ibLeftImag);
		tvTop=(TextView) findViewById(R.id.tvTop);
		if(type==1){
			tvTop.setText("排课");
			tvRight.setText("+");
			tvRight.setVisibility(View.VISIBLE);
			tvRight.setOnClickListener(this);
		}else if(type==2){
			tvTop.setText("聊天");
			tvRight.setVisibility(View.INVISIBLE);
		}else if(type==3){
			tvTop.setText("个人中心");
			tvRight.setVisibility(View.INVISIBLE);
		}
		
		
	}

	private void initViews() {
		mContainer = (FrameLayout) findViewById(R.id.ysh_main_content_frame);
		mHomeView = findViewById(R.id.tab_home_layout);
		mOrderView = findViewById(R.id.tab_order_layout);
		mShopView = findViewById(R.id.tab_shop_layout);

		tvLeft = (TextView) findViewById(R.id.tvLeft);
//		btnDown = (ImageButton) findViewById(R.id.ibLeftImag);

	}

	private void bindListener() {
		mHomeView.setOnClickListener(this);
		mOrderView.setOnClickListener(this);
		mShopView.setOnClickListener(this);
		tvLeft.setOnClickListener(this);
//		btnDown.setOnClickListener(this);
	}

	/**
	 * 默认加载 home 界面
	 */
	private void loadDefault() {
		setSelected(CommonData.TAB_HOME_INDEX);
		loadActivity(SimpleActivity.class.getName(), SimpleActivity.class, null);
	}

	/**
	 * 装载container容器中的Activity <一句话功能简述> <功能详细描述>
	 * 
	 * @param id
	 * @param cls
	 * @param params
	 *            [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void loadActivity(String id, Class<?> cls, Bundle params) {
		// 判断当前页面id与跳转页面id是否相同，若是，则不执行跳转
		if (!mActivityStack.isEmpty() && id != null
				&& mActivityStack.getLast().equals(id)) {
			return;
		}
		mActivityStack.add(id);
		// 跳转前设置intent的内容
		Intent intent = new Intent(MainActivity.this, cls);

		if (params != null) {
			intent.putExtras(params);
		}

		// 执行跳转
		Window subWindow = getLocalActivityManager().startActivity(id, intent);
		View subView = null;
		if (subWindow == null) {
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			subView = getLocalActivityManager().startActivity(id, intent)
					.getDecorView();
		} else {
			subView = subWindow.getDecorView();
		}

		// 切换contain的视图
		mContainer.removeAllViews();

		mContainer.addView(subView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mContainer.setFocusable(true);
		mContainer.requestFocusFromTouch();
	}

	private void setSelected(int index) {
		if (mSelectedView != null) {
			mSelectedView.setSelected(false);
		}

		mHomeView.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		mOrderView.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		mShopView.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));

		switch (index) {
			case CommonData.TAB_HOME_INDEX :
				mSelectedView = mHomeView;
				mHomeView.setBackgroundResource(R.color.white_color);
				break;

			case CommonData.TAB_ORDER_INDEX :
				mSelectedView = mOrderView;
				mOrderView.setBackgroundResource(R.color.white_color);
				break;

			case CommonData.TAB_SHOP_INDEX :
				mSelectedView = mShopView;
				mShopView.setBackgroundResource(R.color.white_color);
				break;
			default :
				break;
		}
		currentIndex = index;
		mSelectedView.setSelected(true);
	}

	@Override
	public void onClick(View v) {
		mActivityStack.clear();
		switch (v.getId()) {
		// home
			case R.id.tab_home_layout :
				if (currentIndex == CommonData.TAB_HOME_INDEX) {
					return;
				}
				loadDefault();
				initTopViews(1);
				break;
			// order
			case R.id.tab_order_layout :
				if (currentIndex == CommonData.TAB_ORDER_INDEX) {
					return;
				}
				setSelected(CommonData.TAB_ORDER_INDEX);
				initTopViews(2);
				loadActivity(ConversationActivity.class.getName(), ConversationActivity.class,
						null);
				break;
			// shop
			case R.id.tab_shop_layout :
				initTopViews(3);
				if (currentIndex == CommonData.TAB_SHOP_INDEX) {
					return;
				}
				setSelected(CommonData.TAB_SHOP_INDEX);
				loadActivity(PersonActivity.class.getName(),PersonActivity.class,
						null);
				break;

			// 顶部文字
			case R.id.tvLeft :
				Intent intent1 = new Intent(MainActivity.this,
						RegisterActivity.class);
				startActivity(intent1);
				break;
				
				// 添加会员
			case R.id.tvRight :
				startActivity(new Intent(MainActivity.this,AddMemberActivity1.class));
				break;

//			// 顶部文字
//			case R.id.ibLeftImag :
//				Intent intent2 = new Intent(MainActivity.this,
//						RegActivity.class);
//				startActivity(intent2);
//				break;
			default :
				break;
		}
	}

}
