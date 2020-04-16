package com.zhangqiang.instancerestore.sample;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangqiang.instancerestore.InstanceRestore;
import com.zhangqiang.instancerestore.annotations.Instance;
import com.zhangqiang.permissionrequest.PermissionRequestHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Instance("ssssssss")
    int saveData = 1;
    @Instance
    int[] saveData1;
    @Instance
    String text;
    @Instance
    String[] text1;
    @Instance
    float a;
    @Instance
    float[] a1;
    @Instance
    double b;
    @Instance
    double[] b1;
    @Instance
    long c;
    @Instance
    long[] c1;
    @Instance
    Bundle d;
    @Instance
    boolean e;
    @Instance
    boolean[] e1;
    @Instance
    char f;
    @Instance
    char[] f1;
    @Instance
    UserBean userBean;
    @Instance
    Size g;
    @Instance
    byte h;
    @Instance
    byte[] i;
    @Instance
    CharSequence k;
    @Instance
    CharSequence[] l;
    @Instance
    boolean m;
    @Instance
    boolean[] m1;
    @Instance
    ArrayList<String> n;
    @Instance
    ArrayList<Integer> o;
    @Instance
    ArrayList<CharSequence> p;
    @Instance
    SizeF q;
    @Instance
    UserBean[] r;
    @Instance
    FlowerBean s;
    @Instance
    ArrayList<UserBean> userBeans;
    @Instance
    LinkedList<FlowerBean> flowerBeans;
//    @Instance
    String input;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = (EditText) findViewById(R.id.et_input);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                input = s.toString();
                textView.setText(input);

            }
        });

        textView = (TextView) findViewById(R.id.tv_title);
        textView.setText(input);
    }


}
