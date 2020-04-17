# InstanceRestore
[![](https://jitpack.io/v/holleQiang/InstanceRestore.svg)](https://jitpack.io/#holleQiang/InstanceRestore)

### 当activity和fragment重建时，恢复保存的值。使用apt实现，性能高，支持Bundle所有类型

#### 在Activity或者Fragment中使用InstanceRestore.restore来恢复实例，使用InstanceRestore.save来保存实例，推荐在你的BaseActivity或者BaseFragment中按下面的方式来使用。
#### 使用@Instance来标记需要恢复的实例

```
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
```
