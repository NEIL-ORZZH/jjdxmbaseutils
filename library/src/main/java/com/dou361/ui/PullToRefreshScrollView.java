package com.dou361.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * ========================================
 * <p/>
 * 版 权：深圳市晶网电子科技有限公司 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2015-9-29 下午8:30:16
 * <p/>
 * 描 述：ScrollView上拉下拉的阻尼效果。
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class PullToRefreshScrollView extends PullToRefreshView {

	public PullToRefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshScrollView(Context context) {
		super(context);
	}
	
	@Override
	protected void init(Context context) {
		super.init(context);
	}

	/** 活动内容view */
	public ScrollView getContentView() {
		return mScrollView;
	}
	
	@Override
	protected boolean hasContentView() {
		return true;
	}
	
	private ScrollView mScrollView;
	
	protected void addContentView(Context context) {
		mScrollView = new ScrollView(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addView(mScrollView, params);
	}


}
