package com.dou361.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dou361.application.BaseApplication;

public class UIUtils {

    /**
     * 获取全局的上下文
     */
    public static Context getContext() {
        return UtilsManager.getInstance().getAppContext();
    }

    /**
     * 获取主线程
     */
    public static Thread getMainThread() {
        return BaseApplication.getMainThread();
    }

    /**
     * 获取主线程Id
     */
    public static long getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return BaseApplication.getMainThreadHandler();
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 从主线程looper里面移除runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    /**
     * 获得填充器
     */
    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /**
     * 获取资源对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取资源对象对应的Id（如布局：“id”；图片：“drawable”）
     */
    public static int getIdByName(String idName, String idType) {
        return getResources().getIdentifier(idName, idType,
                getContext().getPackageName());
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取数字
     */
    public static int getInteger(int resId) {
        return getResources().getInteger(resId);
    }

    /**
     * 获取数字数组
     */
    public static int[] getIntArray(int resId) {
        return getResources().getIntArray(resId);
    }

    /**
     * 获取TypedArray数组可用到drawable和color的数组
     */
    public static TypedArray getTypedArray(int resId) {
        return getResources().obtainTypedArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    /**
     * 判断当前的线程是不是在主线程
     */
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    /**
     * 运行到主线程
     */
    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    /**
     * 获取屏幕的分辨率
     */
    @SuppressWarnings("deprecation")
    public static int[] getResolution() {
        Context context = getContext();
        if (null == context) {
            return null;
        }
        WindowManager windowMgr = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int[] res = new int[2];
        res[0] = windowMgr.getDefaultDisplay().getWidth();
        res[1] = windowMgr.getDefaultDisplay().getHeight();
        return res;
    }

    /**
     * 获得设备的横向dpi
     */
    public static float getWidthDpi() {
        Context context = getContext();
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = null;
        try {
            if (context != null) {
                dm = new DisplayMetrics();
                dm = context.getApplicationContext().getResources()
                        .getDisplayMetrics();
            }

            return dm.densityDpi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获得设备的纵向dpi
     */
    public static float getHeightDpi() {
        Context context = getContext();
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.ydpi;
    }

    /**
     * 获取屏幕宽度(像素)
     */
    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度(像素)
     */
    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * pxz转换dip
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 把自身从父View中移除
     */
    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * 请求View树重新布局，用于解决中层View有布局状态而导致上层View状态断裂
     */
    public static void requestLayoutParent(View view, boolean isAll) {
        ViewParent parent = view.getParent();
        while (parent != null && parent instanceof View) {
            if (!parent.isLayoutRequested()) {
                parent.requestLayout();
                if (!isAll) {
                    break;
                }
            }
            parent = parent.getParent();
        }
    }

    /**
     * 判断触点是否落在该View上
     */
    public static boolean isTouchInView(MotionEvent ev, View v) {
        int[] vLoc = new int[2];
        v.getLocationOnScreen(vLoc);
        float motionX = ev.getRawX();
        float motionY = ev.getRawY();
        return motionX >= vLoc[0] && motionX <= (vLoc[0] + v.getWidth())
                && motionY >= vLoc[1] && motionY <= (vLoc[1] + v.getHeight());
    }

    /**
     * FindViewById的泛型封装，减少强转代码
     */
    public static <T extends View> T findViewById(View layout, int id) {
        return (T) layout.findViewById(id);
    }

    /**
     * 获取屏幕的比例
     *
     * @return
     */
    public static float getScaledDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float value = dm.scaledDensity;
        return value;
    }

    /**
     * 适用于Adapter中简化ViewHolder相关代码
     *
     * @param convertView
     * @param id
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T obtainView(View convertView, int id) {
        SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
        if (holder == null) {
            holder = new SparseArray<View>();
            convertView.setTag(holder);
        }
        View childView = holder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            holder.put(id, childView);
        }
        return (T) childView;
    }

    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredWidth(View view) {
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 设置多击事件 @params clickTimes 多击次数 @params view 多击view @params l 多击完成响应的监听
     */
    public static void setOnMultiClickListener(final int clickTimes, final View view, final OnMultiClickListener l) {

        view.setOnClickListener(new View.OnClickListener() {
            /**
             * 配置存储时间的数组
             */
            long[] mHits = new long[clickTimes];

            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 600)) {
                    l.onMultiClick(view);
                }
            }
        });

    }

    /**
     * 多击事件的监听
     */
    public interface OnMultiClickListener {
        void onMultiClick(View v);
    }

}
