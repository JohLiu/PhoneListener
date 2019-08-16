# PhoneListener
来电拦截实现来电秀

#### 功能简介

案例通过对来电信息的广播拦截，来实现 企业通讯录识别（来电秀）的效果。
> 未对去电信息拦截处理。

#### 1.来电状态监听

要获取来电信息，首先需要设置读取电话状态的权限。
> Android9.0之前只需要 `READ_PHONE_STATE` 权限即可,
> 9.0后要从手机状态中读取电话号码，请根据您的用例更新应用以请求必要的权限：
> 要通过 PHONE_STATE Intent 操作读取电话号码，同时需要 READ_CALL_LOG 权限和 READ_PHONE_STATE 权限。
> 要从 onCallStateChanged() 中读取电话号码，只需要 READ_CALL_LOG 权限。 不需要 READ_PHONE_STATE 权限
```
    <!--读取电话的状态信息的权限-->
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <!--读取通话记录的权限-->
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
```

然后设置来电监听获取来电信息，`PhoneStateListener`的`onCallStateChanged`
方法可获取来电状态和来电号码，并根据不同来电状态作相应处理
```
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        // state 当前来电状态 
        // incomingNumber 来电号码
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            // 挂断
            case TelephonyManager.CALL_STATE_IDLE:
                Log.e(TAG, "当前状态：挂断");
                break;
            // 通话中
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.e(TAG, "当前状态：通话中");
                break;
            // 电话响铃
            case TelephonyManager.CALL_STATE_RINGING:
                Log.e(TAG, "当前状态：响铃中");
                break;
            default:
                break;
        }
    }
```

#### 2.广播与服务
创建广播接收者`Receiver`,接收系统来电广播，并在`Receiver`中根据来电监听作处理
```
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
                                WindowsUtils.showPopupWindow(mcontext, "移动开发部-Joh\n" + number);
                            }
                        } else {
                            WindowsUtils.showPopupWindow(mcontext, "移动开发部-Joh\n" + number);
                        }
                    }
                });
                tm.listen(phoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        }
    }
```
创建来电监听服务并绑定
```
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
    
    // 在Activity中绑定服务
    Intent intent = new Intent(getApplicationContext(), PhoneListenService.class);
    startService(intent);
```
最后在`manifest`中声明广播与服务
```
    <!--广播-->
    <receiver android:name=".PhoneReceiver">
        <intent-filter>
             <action android:name="android.intent.action.PHONE_STATE" />
        </intent-filter>
    </receiver>

    <!--服务-->
    <service
        android:name=".PhoneListenService"
        android:enabled="true"
        android:exported="true" />
```
#### 3.来电识别悬浮窗
来电监听已经可以拿到来电状态和来电号码，可根据来电信息识别是否为企业人员，如果是展示悬浮窗标识来电者信息。
展示悬浮窗时需设置权限,并且需要用户手动设置悬浮窗是否开启。
> Android8.0设备出现从设置悬浮窗是否开启页面返回应用时无法通过Settings.canDrawOverlays(context)判断到悬浮窗是未开启的情况，所以需对
> 8.0设置做特殊处理（即需要用户跳转到悬浮窗设置列表页找到对应的应用，然后开启悬浮窗）
```
    //配置文件权限声明
    <!-- 允许 弹出系统级别的AlterDialog -->
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <!-- 在 屏幕最顶部显示 view-->
    <uses-permission android:name="android.permission.SYSTEM_OVERLAY_WINDOW" />
    
    // 在Activity中跳转系统悬浮窗设置页面
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // 由于8.0对系统弹唱权限的限制，需要用户进去设置中找到对应应用设置弹窗权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            //8.0
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
        } else {
            // 6.0、7.0、9.0
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
        }
    }
```
设置悬浮窗展示页面，将悬浮窗封装一下可以随时使用
```
/**
 * 来电监听窗口工具类
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
```
