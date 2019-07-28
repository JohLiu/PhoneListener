package com.phone.listen;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Title : 来电监听窗口工具类
 * Description :
 * Author :  Joh      date : 2017/12/7 11:17
 * Update :                         date :
 * Version : 1.0.0
 * Copyright : copyright(c) 浙江亚厦股份有限公司 2017 ~ 2020 版权所有
 */
public class WindowsUtils {
    private static final String LOG_TAG = "WindowUtils";
    private static View mView = null;
    private static WindowManager mWindowManager = null;
    private static Context mContext = null;
    public static Boolean isShown = false;

    /**
     * 显示弹出框
     *
     * @param context
     */
    public static void showPopupWindow(final Context context, String name) {
        if (isShown) {
            return;
        }
        isShown = true;
        // 获取应用的Context
        mContext = context.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mView = setUpView(context, name);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = dp2px(70);
        params.format = PixelFormat.RGBA_8888;

        mWindowManager.addView(mView, params);
    }

    public static int dp2px(final float dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow() {
        if (isShown && null != mView) {
            mWindowManager.removeView(mView);
            isShown = false;
        }
    }

    private static View setUpView(Context context, String name) {
        View view = LayoutInflater.from(context).inflate(R.layout.phone_alert,
                null);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(name);
        return view;
    }
}
