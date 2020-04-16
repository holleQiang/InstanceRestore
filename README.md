# InstanceRestore
当activity和fragment重建时，恢复保存的值。使用apt实现，性能高，支持Bundle所有类型

在Activity或者Fragment使用下面的方式来使用，推荐在你的BaseActivity或者BaseFragment中使用

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
