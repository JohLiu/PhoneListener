package com.phone.listen;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermissions();
        findViewById(R.id.register_service).setOnClickListener(this);
        findViewById(R.id.register_set).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        switch (viewId) {
            case R.id.register_service:
                registerPhoneStateListener();
                break;
            case R.id.register_set:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 1001);
                }
                break;
            default:
                break;
        }
    }

    private void registerPhoneStateListener() {
        Intent intent = new Intent(this, PhoneListenService.class);
        intent.setAction(PhoneListenService.ACTION_REGISTER_LISTENER);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private void initPermissions() {
        String[] perms = {Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE};
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, 1001, perms)
                        .setRationale("权限申请")
                        .setPositiveButtonText("确定")
                        .setNegativeButtonText("取消")
                        .build());

    }
}
