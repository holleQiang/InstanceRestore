package com.zhangqiang.instancerestore.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.zhangqiang.permissionrequest.PermissionRequestHelper;

public class Activity2 extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PermissionRequestHelper.requestPermission(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE}, new PermissionRequestHelper.Callback() {
                    @Override
                    public void onPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
                        for (String permission : permissions) {
                            boolean garant = ActivityCompat.checkSelfPermission(Activity2.this.getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;

                            Log.i("Test","=============" + permission + "======" + 0);

                        }
                        for (int grantResult : grantResults) {
                            Log.i("Test","=============" + grantResult);
                        }
                    }
                });
    }
}
