package com.joh.phonelistener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 来电监听服务
 *
 * @author : Joh Liu
 * @date : 2019/8/15 16:44
 */
public class PhoneListenService extends Service {

    private static final String TAG = "PhoneListenService";

    // 电话管理者对象
    private TelephonyManager mTelephonyManager;
    // 电话状态监听者
    private MyPhoneCallListener phoneCallListener;

    @Override
    public void onCreate() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneCallListener = new MyPhoneCallListener();
        phoneCallListener.setPhoneListener(number -> {
            if (!TextUtils.isEmpty(number)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(getApplicationContext())) {
                        WindowsUtils.showPopupWindow(getApplicationContext(), "移动开发部-Joh\n" + number);
                    }
                } else {
                    WindowsUtils.showPopupWindow(getApplicationContext(), "移动开发部-Joh\n" + number);
                }
            }
        });
        mTelephonyManager.listen(phoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    public void onDestroy() {
//        // 取消来电的电话状态监听服务
//        if (mTelephonyManager != null && myPhoneStateListener != null) {
//            mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
//        }
//        super.onDestroy();
//    }
}
