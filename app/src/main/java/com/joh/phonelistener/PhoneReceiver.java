package com.joh.phonelistener;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 来电广播捕获
 *
 * @author : Joh Liu
 * @date : 2019/8/15 11:14
 */
public class PhoneReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneReceiver";

    private Context mcontext;
    private MyPhoneCallListener phoneCallListener = new MyPhoneCallListener();

    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        // 非拨出状态
        if (intent.getAction() != null && !intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            // 设置一个监听器
            if (tm != null) {
                phoneCallListener.setPhoneListener(number -> {
                    if (!TextUtils.isEmpty(number)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (Settings.canDrawOverlays(mcontext)) {
                                WindowsUtils.showPopupWindow(mcontext, "信息部-龚守辉\n" + number);
                            }
                        } else {
                            WindowsUtils.showPopupWindow(mcontext, "信息部-龚守辉\n" + number);
                        }
                    }
                });
                tm.listen(phoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        }
    }

}

