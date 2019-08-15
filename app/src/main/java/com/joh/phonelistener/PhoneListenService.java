package com.joh.phonelistener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneListenService extends Service {

    private static final String TAG = "PhoneListenService";

    // 电话管理者对象
    private TelephonyManager mTelephonyManager;
    // 电话状态监听者
    private MyPhoneStateListener myPhoneStateListener;

    @Override
    public void onCreate() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }


    /**
     * 自定义内部类对来电的电话状态进行监听
     */
    class MyPhoneStateListener extends PhoneStateListener {
        // 重写电话状态改变时触发的方法
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "响铃:" + incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG, "接听");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG, "挂断");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // 取消来电的电话状态监听服务
        if (mTelephonyManager != null && myPhoneStateListener != null) {
            mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }
}
