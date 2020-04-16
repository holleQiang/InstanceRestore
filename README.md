# InstanceRestore
当activity和fragment重建时，恢复保存的值。使用apt实现，性能高，支持Bundle所有类型

在Activity或者Fragment使用下面的方式来使用，推荐在你的BaseActivity或者BaseFragment中使用

@Instance<br/>
Bundle bundle;<br/>

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {<br/>
    super.onCreate(savedInstanceState);<br/>
    InstanceRestore.restore(this,savedInstanceState);<br/>
}<br/>

@Override<br/>
protected void onSaveInstanceState(Bundle outState) {<br/>
    super.onSaveInstanceState(outState);<br/>
    InstanceRestore.save(this,outState);<br/>
}<br/>
