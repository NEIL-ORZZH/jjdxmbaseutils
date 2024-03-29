package com.dou361.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;

public class DrawableUtils {

	/**
	 * 创建一个图片
	 * 
	 * @param contentColor
	 *            内部填充颜色
	 * @param strokeColor
	 *            描边颜色
	 * @param radius
	 *            圆角
	 */
	public static GradientDrawable createDrawable(int contentColor,
			int strokeColor, int radius) {
		GradientDrawable drawable = new GradientDrawable(); // 生成Shape
		drawable.setGradientType(GradientDrawable.RECTANGLE); // 设置矩形
		drawable.setColor(contentColor);// 内容区域的颜色
		drawable.setStroke(1, strokeColor); // 四周描边,描边后四角真正为圆角，不会出现黑色阴影。如果父窗体是可以滑动的，需要把父View设置setScrollCache(false)
		drawable.setCornerRadius(radius); // 设置四角都为圆角
		return drawable;
	}

	/**
	 * 创建一个图片选择器
	 * 
	 * @param normalState
	 *            普通状态的图片
	 * @param pressedState
	 *            按压状态的图片
	 */
	public static StateListDrawable createSelector(Drawable normalState,
			Drawable pressedState) {
		StateListDrawable bg = new StateListDrawable();
		bg.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, pressedState);
		bg.addState(new int[] { android.R.attr.state_enabled }, normalState);
		bg.addState(new int[] {}, normalState);
		return bg;
	}

	/** 获取图片的大小 */
	@SuppressLint("NewApi")
	public static int getDrawableSize(Drawable drawable) {
		if (drawable == null) {
			return 0;
		}
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		} else {
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}
	
	/** 获取图片的大小 */
	@SuppressLint("NewApi")
	public static int getBitmapSize(Bitmap bitmap) {
		if (bitmap == null) {
			return 0;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		} else {
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}
	
	public static Bitmap zoomDrawable(Bitmap bit, int w, int h) {

		if (bit != null) {

			int width = bit.getWidth();
			int height = bit.getHeight();
			Bitmap oldbmp = bit;
			Matrix matrix = new Matrix();
			float scaleWidth = ((float) w / width);
			float scaleHeight = ((float) h / height);
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
					matrix, true);
			return newbmp;
		}
		return null;
	}
	
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}
}
