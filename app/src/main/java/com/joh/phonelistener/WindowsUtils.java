package com.joh.phonelistener;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 来电监听窗口工具类
 *
 * @author : Joh Liu
 * @date : 2019/8/15 11:14
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // 注意TYPE_SYSTEM_ALERT从Android8.0开始被舍弃了
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            // 从Android8.0开始悬浮窗要使用TYPE_APPLICATION_OVERLAY
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

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
