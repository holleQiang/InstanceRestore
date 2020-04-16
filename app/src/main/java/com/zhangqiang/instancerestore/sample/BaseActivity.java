package com.zhangqiang.instancerestore.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhangqiang.instancerestore.InstanceRestore;
import com.zhangqiang.instancerestore.annotations.Instance;

public abstract class BaseActivity extends AppCompatActivity {

    @Instance
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InstanceRestore.restore(this,savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        InstanceRestore.save(this,outState);
    }
}
