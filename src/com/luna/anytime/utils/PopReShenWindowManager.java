package com.luna.anytime.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.luna.anytime.R;
import com.luna.anytime.diy.PickerView;
import com.luna.anytime.diy.PickerView.onSelectListener;

public class PopReShenWindowManager {
	private PopupWindow pop;
	private View popView;
	private PickerView pickerView;
	private TextView tvOk;
	private static volatile PopReShenWindowManager instance;
	List<String> tBrand;
	List<String> data = new ArrayList<String>();
	private Context context;

	private PopReShenWindowManager() {
	}

	/**
	 * 创建单例类，提供静态方法调用
	 * 
	 * @return ActivityManager
	 */
	public static PopReShenWindowManager getInstance() {
		if (instance == null) {
			instance = new PopReShenWindowManager();
		}
		return instance;
	}

	/***
	 * popWindow初始化方法
	 * 
	 * @param context
	 * @param w
	 */
	public void init(Context context, final int width, int height, int id) {
		this.context = context;
		// 创建PopupWindow对象
		pop = new PopupWindow(setPopView(context, id),
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		setPopWidth(context, width, height);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		
	}

	public PopupWindow getPopView() {
		return pop;
	}

	public View setPopView(Context context, int id) {
		if (popView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			// 引入窗口配置文件
			popView = inflater.inflate(id, null);
			popView.getBackground().setAlpha(110);
		}
		return popView;
	}

	public void showPopAsDropDown(View view, int x, int y) {
		if (pop != null) {
			pop.showAsDropDown(view, x, y);
		} else {
		}
	}

	public void showPopAllLocation(View parent, int gravity, int x, int y) {
		if (pop != null) {
			pop.showAtLocation(parent, gravity, x, y);
		} else {
		}
	}

	public void dismissPop() {
		pop.dismiss();
	}

	public void setPopWidth(Context context, final int width, final int height) {
		if (popView == null) {
			return;
		}
		popView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// 动态设置pop的宽度
						FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) popView
								.getLayoutParams();
						linearParams.width = width;
						linearParams.height = height;
					}
				});
	}

	public void changeOnClick(View view, OnClickListener onClickListener) {

	}

	public void changeOnSelect(onSelectListener selectListener) {
		pickerView.setOnSelectListener(selectListener);
	}
	
	public void changeTvOKOnClick(OnClickListener onClickListener){
		if(tvOk==null){
			tvOk = (TextView) popView.findViewById(R.id.tv_ok);
		}
		tvOk.setOnClickListener(onClickListener);
	}
	
	public void setPickViewData(List<String> data) {
		this.data = data;
		if (pickerView == null) {
			pickerView = (PickerView) popView.findViewById(R.id.minute_pv);
			tvOk = (TextView) popView.findViewById(R.id.tv_ok);
			popView.findViewById(R.id.tv_cancle).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dismissPop();
				}
			});
		}
		pickerView.setData(data);
	}
}
