package com.dou361.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dou361.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * 创建日期：2015/11/11
 * <p/>
 * 描 述：精仿iOSAlertViewController控件
 * 点击取消按钮返回 －1，其他按钮从0开始算
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class AlertView {
    public static enum Style {
        ActionSheet,//底部
        ActionCenter,//中部
        Alert//中间
    }

    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );
    public static final int HORIZONTAL_BUTTONS_MAXCOUNT = 2;
    public static final String OTHERS = "others";
    public static final String DESTRUCTIVE = "destructive";
    public static final String CANCEL = "cancel";
    public static final String TITLE = "title";
    public static final String MSG = "msg";
    public static final int CANCELPOSITION = -1;//点击取消按钮返回 －1，其他按钮从0开始算

    private String title;
    private String msg;
    private List<String> mDestructive;
    private List<String> mOthers;
    private String cancel;
    private ArrayList<String> mDatas = new ArrayList<String>();

    private Context context;
    private ViewGroup contentContainer;
    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;//AlertView 的 根View
    private ViewGroup loAlertHeader;//窗口headerView

    private Style style = Style.Alert;

    private OnDismissListener onDismissListener;
    private OnItemClickListener onItemClickListener;
    private boolean isDismissing;

    private Animation outAnim;
    private Animation inAnim;
    private int gravity = Gravity.CENTER;

    /***
     * @param title               标题
     * @param msg                 消息
     * @param cancel              取消
     * @param destructive         高亮列表
     * @param others              非高亮列表
     * @param context             上下文
     * @param style               样式
     * @param onItemClickListener 点击监听
     */
    public AlertView(String title, String msg, String cancel, String[] destructive, String[] others, Context context, Style style, OnItemClickListener onItemClickListener) {
        this.context = context;
        if (style != null) this.style = style;
        this.onItemClickListener = onItemClickListener;

        initData(title, msg, cancel, destructive, others);
        initViews();
        init();
        initEvents();
    }

    /**
     * 获取数据
     */
    protected void initData(String title, String msg, String cancel, String[] destructive, String[] others) {

        this.title = title;
        this.msg = msg;
        if (destructive != null) {
            this.mDestructive = Arrays.asList(destructive);
            this.mDatas.addAll(mDestructive);
        }
        if (others != null) {
            this.mOthers = Arrays.asList(others);
            this.mDatas.addAll(mOthers);
        }
        if (cancel != null) {
            this.cancel = cancel;
            if (style == Style.Alert && mDatas.size() < HORIZONTAL_BUTTONS_MAXCOUNT) {
                this.mDatas.add(0, cancel);
            }
        }

    }

    protected void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(ResourceUtils.getResourceIdByName("layout", "layout_alertview"), decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        contentContainer = (ViewGroup) rootView.findViewById(ResourceUtils.getResourceIdByName("id", "content_container"));
        int margin_alert_left_right = 0;
        switch (style) {
            case ActionSheet:
                params.gravity = Gravity.BOTTOM;
                margin_alert_left_right = context.getResources().getDimensionPixelSize(ResourceUtils.getResourceIdByName("dimen", "margin_actionsheet_left_right"));
                params.setMargins(margin_alert_left_right, 0, margin_alert_left_right, margin_alert_left_right);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.BOTTOM;
                initActionSheetViews(layoutInflater);
                break;
            case ActionCenter:
                params.gravity = Gravity.CENTER;
                margin_alert_left_right = context.getResources().getDimensionPixelSize(ResourceUtils.getResourceIdByName("dimen", "margin_actionsheet_left_right"));
                params.setMargins(margin_alert_left_right, 0, margin_alert_left_right, margin_alert_left_right);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.CENTER;
                initActionSheetViews(layoutInflater);
                break;
            case Alert:
                params.gravity = Gravity.CENTER;
                margin_alert_left_right = context.getResources().getDimensionPixelSize(ResourceUtils.getResourceIdByName("dimen", "margin_alert_left_right"));
                params.setMargins(margin_alert_left_right, 0, margin_alert_left_right, 0);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.CENTER;
                initAlertViews(layoutInflater);
                break;
        }
    }

    protected void initHeaderView(ViewGroup viewGroup) {
        loAlertHeader = (ViewGroup) viewGroup.findViewById(ResourceUtils.getResourceIdByName("id", "loAlertHeader"));
        //标题和消息
        TextView tvAlertTitle = (TextView) viewGroup.findViewById(ResourceUtils.getResourceIdByName("id", "tvAlertTitle"));
        TextView tvAlertMsg = (TextView) viewGroup.findViewById(ResourceUtils.getResourceIdByName("id", "tvAlertMsg"));
        if (title != null) {
            tvAlertTitle.setText(title);
        } else {
            tvAlertTitle.setVisibility(View.GONE);
        }
        if (msg != null) {
            tvAlertMsg.setText(msg);
        } else {
            tvAlertMsg.setVisibility(View.GONE);
        }
    }

    protected void initListView() {
        ListView alertButtonListView = (ListView) contentContainer.findViewById(ResourceUtils.getResourceIdByName("id", "alertButtonListView"));
        //把cancel作为footerView
        if (cancel != null && style == Style.Alert) {
            View itemView = LayoutInflater.from(context).inflate(ResourceUtils.getResourceIdByName("layout", "item_alertbutton"), null);
            TextView tvAlert = (TextView) itemView.findViewById(ResourceUtils.getResourceIdByName("id", "tvAlert"));
            tvAlert.setText(cancel);
            tvAlert.setClickable(true);
            tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
            tvAlert.setTextColor(context.getResources().getColor(ResourceUtils.getResourceIdByName("color", "textColor_alert_button_cancel")));
            tvAlert.setBackgroundResource(ResourceUtils.getResourceIdByName("drawable", "bg_alertbutton_bottom"));
            tvAlert.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
            alertButtonListView.addFooterView(itemView);
        }
        int hideLine = 0;
        if (title == null && msg == null) {
            hideLine = 1;
        }
        AlertViewAdapter adapter = new AlertViewAdapter(mDatas, mDestructive, hideLine);
        alertButtonListView.setAdapter(adapter);
        alertButtonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(AlertView.this, position);
                dismiss();
            }
        });
    }

    protected void initActionSheetViews(LayoutInflater layoutInflater) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(ResourceUtils.getResourceIdByName("layout", "layout_alertview_actionsheet"), contentContainer);
        initHeaderView(viewGroup);

        initListView();
        TextView tvAlertCancel = (TextView) contentContainer.findViewById(ResourceUtils.getResourceIdByName("id", "tvAlertCancel"));
        if (cancel != null) {
            tvAlertCancel.setVisibility(View.VISIBLE);
            tvAlertCancel.setText(cancel);
        }
        tvAlertCancel.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
    }

    protected void initAlertViews(LayoutInflater layoutInflater) {

        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(ResourceUtils.getResourceIdByName("layout", "layout_alertview_alert"), contentContainer);
        initHeaderView(viewGroup);

        int position = 0;
        //如果总数据小于等于HORIZONTAL_BUTTONS_MAXCOUNT，则是横向button
        if (mDatas.size() <= HORIZONTAL_BUTTONS_MAXCOUNT) {
            ViewStub viewStub = (ViewStub) contentContainer.findViewById(ResourceUtils.getResourceIdByName("id", "viewStubHorizontal"));
            viewStub.inflate();
            LinearLayout loAlertButtons = (LinearLayout) contentContainer.findViewById(ResourceUtils.getResourceIdByName("id", "loAlertButtons"));
            for (int i = 0; i < mDatas.size(); i++) {
                //如果不是第一个按钮
                if (i != 0) {
                    //添加上按钮之间的分割线
                    View divier = new View(context);
                    divier.setBackgroundColor(context.getResources().getColor(ResourceUtils.getResourceIdByName("color", "bgColor_divier")));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) context.getResources().getDimension(ResourceUtils.getResourceIdByName("dimen", "size_divier")), LinearLayout.LayoutParams.MATCH_PARENT);
                    loAlertButtons.addView(divier, params);
                }
                View itemView = LayoutInflater.from(context).inflate(ResourceUtils.getResourceIdByName("layout", "item_alertbutton"), null);
                TextView tvAlert = (TextView) itemView.findViewById(ResourceUtils.getResourceIdByName("id", "tvAlert"));
                tvAlert.setClickable(true);

                //设置点击效果
                if (mDatas.size() == 1) {
                    tvAlert.setBackgroundResource(ResourceUtils.getResourceIdByName("drawable", "bg_alertbutton_bottom"));
                } else if (i == 0) {//设置最左边的按钮效果
                    tvAlert.setBackgroundResource(ResourceUtils.getResourceIdByName("drawable", "bg_alertbutton_left"));
                } else if (i == mDatas.size() - 1) {//设置最右边的按钮效果
                    tvAlert.setBackgroundResource(ResourceUtils.getResourceIdByName("drawable", "bg_alertbutton_right"));
                }
                String data = mDatas.get(i);
                tvAlert.setText(data);

                //取消按钮的样式
                if (data == cancel) {
                    tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
                    tvAlert.setTextColor(context.getResources().getColor(ResourceUtils.getResourceIdByName("color", "textColor_alert_button_cancel")));
                    tvAlert.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
                    position = position - 1;
                }
                //高亮按钮的样式
                else if (mDestructive != null && mDestructive.contains(data)) {
                    tvAlert.setTextColor(context.getResources().getColor(ResourceUtils.getResourceIdByName("color", "textColor_alert_button_destructive")));
                }

                tvAlert.setOnClickListener(new OnTextClickListener(position));
                position++;
                loAlertButtons.addView(itemView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            }
        } else {
            ViewStub viewStub = (ViewStub) contentContainer.findViewById(ResourceUtils.getResourceIdByName("id", "viewStubVertical"));
            viewStub.inflate();
            initListView();
        }
    }

    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    protected void initEvents() {
    }

    public AlertView addExtView(View extView) {
        loAlertHeader.addView(extView);
        return this;
    }

    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        decorView.addView(view);
        contentContainer.startAnimation(inAnim);
    }

    /**
     * 添加这个View到Activity的根视图
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        onAttached(rootView);
    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        View view = decorView.findViewById(ResourceUtils.getResourceIdByName("id", "outmost_container"));
        return view != null;
    }

    public void dismiss() {
        if (isDismissing) {
            return;
        }

        //消失动画
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        //从activity根视图移除
                        decorView.removeView(rootView);
                        isDismissing = false;
                        if (onDismissListener != null) {
                            onDismissListener.onDismiss(AlertView.this);
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        contentContainer.startAnimation(outAnim);
        isDismissing = true;
    }

    public Animation getInAnimation() {
        int res = getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        int res = getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public AlertView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    class OnTextClickListener implements View.OnClickListener {

        private int position;

        public OnTextClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(AlertView.this, position);
            dismiss();
        }
    }

    /**
     * 主要用于拓展View的时候有输入框，键盘弹出则设置MarginBottom往上顶，避免输入法挡住界面
     */
    public void setMarginBottom(int marginBottom) {
        int margin_alert_left_right = context.getResources().getDimensionPixelSize(ResourceUtils.getResourceIdByName("dimen", "margin_alert_left_right"));
        params.setMargins(margin_alert_left_right, 0, margin_alert_left_right, marginBottom);
        contentContainer.setLayoutParams(params);
    }

    public AlertView setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(ResourceUtils.getResourceIdByName("id", "outmost_container"));

        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        } else {
            view.setOnTouchListener(null);
        }
        return this;
    }

    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    private int getAnimationResource(int gravity, boolean isInAnimation) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? ResourceUtils.getResourceIdByName("anim", "alertview_slide_in_bottom") : ResourceUtils.getResourceIdByName("anim", "alertview_slide_out_bottom");
            case Gravity.CENTER:
                return isInAnimation ? ResourceUtils.getResourceIdByName("anim", "alertview_fade_in_center") : ResourceUtils.getResourceIdByName("anim", "alertview_fade_out_center");
        }
        return -1;
    }

    public class AlertViewAdapter extends BaseAdapter {
        private List<String> mDatas;
        private List<String> mDestructive;
        private int hideLine;

        public AlertViewAdapter(List<String> datas, List<String> destructive, int hideLine) {
            this.mDatas = datas;
            this.mDestructive = destructive;
            this.hideLine = hideLine;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String data = mDatas.get(position);
            Holder holder = null;
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(ResourceUtils.getResourceIdByName("layout", "item_alertbutton"), null);
                holder = creatHolder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            holder.UpdateUI(parent.getContext(), data, position);
            return view;
        }

        public Holder creatHolder(View view) {
            return new Holder(view);
        }

        class Holder {
            private TextView tvAlert;
            private View v_line;

            public Holder(View view) {
                tvAlert = (TextView) view.findViewById(ResourceUtils.getResourceIdByName("id", "tvAlert"));
                v_line = view.findViewById(ResourceUtils.getResourceIdByName("id", "v_line"));
            }

            public void UpdateUI(Context context, String data, int position) {
                if (position == 0) {
                    v_line.setVisibility(View.GONE);
                } else {
                    v_line.setVisibility(View.VISIBLE);
                }
                tvAlert.setText(data);
                if (mDestructive != null && mDestructive.contains(data)) {
                    tvAlert.setTextColor(context.getResources().getColor(ResourceUtils.getResourceIdByName("color", "textColor_alert_button_destructive")));
                } else {
                    tvAlert.setTextColor(context.getResources().getColor(ResourceUtils.getResourceIdByName("color", "textColor_alert_button_others")));
                }
            }
        }
    }

    public interface OnDismissListener {
        public void onDismiss(Object o);
    }

    public interface OnItemClickListener {
        public void onItemClick(Object o, int position);
    }
}
