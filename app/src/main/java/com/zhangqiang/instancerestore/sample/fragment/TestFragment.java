package com.zhangqiang.instancerestore.sample.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zhangqiang.instancerestore.InstanceRestore;
import com.zhangqiang.instancerestore.annotations.Instance;

public class TestFragment extends Fragment {

    @Instance
    String aaaa;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InstanceRestore.restore(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        InstanceRestore.save(this, outState);
    }
}
