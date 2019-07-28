package com.phone.listen;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 来去电监听
 */

public class CustomPhoneStateListener extends PhoneStateListener {

    private Context mContext;

    public CustomPhoneStateListener(Context context) {
        mContext = context;
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        super.onServiceStateChanged(serviceState);
        Log.e(PhoneListenService.TAG, "CustomPhoneStateListener onServiceStateChanged: " + serviceState);
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        Log.e(PhoneListenService.TAG, "CustomPhoneStateListener state: " + state + " incomingNumber: " + incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                // 电话挂断
                /**
                 * 1.电话挂断
                 * 隐藏弹窗
                 */
                Log.e(PhoneListenService.TAG, "CustomPhoneStateListener" + "电话挂断");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                // 来电接通 或者 去电，去电接通  但是没法区分
                /**
                 * 2.接听状态
                 * 隐藏弹窗
                 * incomingNumber 通话方的号码
                 */
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                // 电话响铃
                Log.e(PhoneListenService.TAG, "CustomPhoneStateListener" + "电话响铃");
                //HangUpTelephonyUtil.isColleague(mContext,incomingNumber);
                break;
            default:
                break;
        }

    }
}
