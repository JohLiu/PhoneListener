package com.joh.phonelistener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 来电监听
 *
 * @author : Joh Liu
 * @date : 2019/8/15 10:30
 */
public class MyPhoneCallListener extends PhoneStateListener {

    private static final String TAG = "MyPhoneCallListener";
    protected PhoneListener listener;

    /**
     * 返回电话状态
     * <p>
     * CALL_STATE_IDLE 挂断
     * CALL_STATE_OFFHOOK 通话中
     * CALL_STATE_RINGING 响铃
     */
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        //state 当前状态 incomingNumber
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            // 挂断
            case TelephonyManager.CALL_STATE_IDLE:
                Log.e(TAG, "当前状态：挂断");
                WindowsUtils.hidePopupWindow();
                break;
            // 通话中
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.e(TAG, "当前状态：通话中");
                WindowsUtils.hidePopupWindow();
                break;
            // 电话响铃
            case TelephonyManager.CALL_STATE_RINGING:
                Log.e(TAG, "当前状态：响铃中");
                listener.onCallStateRinging(incomingNumber);
                break;
            default:
                break;
        }
    }

    public void setPhoneListener(PhoneListener phoneListener) {
        this.listener = phoneListener;
    }

    public interface PhoneListener {
        void onCallStateRinging(String number);
    }
}
