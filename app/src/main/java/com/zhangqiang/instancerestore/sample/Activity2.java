package com.zhangqiang.instancerestore.sample;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhangqiang.permissionrequest.PermissionRequestHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

                            Log.i("Test", "=============" + permission + "======" + 0);

                        }
                        for (int grantResult : grantResults) {
                            Log.i("Test", "=============" + grantResult);
                        }
                    }
                });


        View tvTitle = findViewById(R.id.tv_title);
        tvTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Activity2.this, "Test", Toast.LENGTH_SHORT).show();
            }
        }, 3000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Activity2.this, "Test", Toast.LENGTH_SHORT).show();
                    }
                }, 10000);
            }
        }).start();
        finish();

        ToastFix.fix();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Test", "=======onDestroy=========");
    }
}
