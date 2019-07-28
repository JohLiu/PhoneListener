package com.phone.listen;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 来去电监听服务
 */

public class PhoneListenService extends Service {

    public static final String TAG = PhoneListenService.class.getSimpleName();

    public static final String ACTION_REGISTER_LISTENER = "action_register_listener";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand action: " + intent.getAction() + " flags: " + flags + " startId: " + startId);
        String action = intent.getAction();
        if (action.equals(ACTION_REGISTER_LISTENER)) {
            registerPhoneStateListener();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void registerPhoneStateListener() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind action: " + intent.getAction());
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind action: " + intent.getAction());
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e(TAG, "onRebind action: " + intent.getAction());
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    private PhoneStateListener listener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //state 当前状态 incomingNumber
            super.onCallStateChanged(state, incomingNumber);
            Log.e(PhoneListenService.TAG, "CustomPhoneStateListener state: " + state + " incomingNumber: " + incomingNumber);

            switch (state) {
                // 挂断
                case TelephonyManager.CALL_STATE_IDLE:
                    /**
                     * 1.电话挂断
                     * 隐藏弹窗
                     */
                    Log.e(PhoneListenService.TAG, "CustomPhoneStateListener" + "电话挂断");
                    break;
                // 接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    /**
                     * 2.接听状态
                     * 隐藏弹窗
                     * incomingNumber 通话方的号码
                     */
                    WindowsUtils.hidePopupWindow();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    // 电话响铃
                    Log.e(PhoneListenService.TAG, "CustomPhoneStateListener" + "电话响铃");
                    break;
                default:
                    break;
            }
        }
    };
}
