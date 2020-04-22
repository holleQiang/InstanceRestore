package com.zhangqiang.instancerestore.sample;

import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ToastFix {

    public static void fix() {

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.N_MR1) {
            return;
        }
        try {

            Class<?> mINotificationManagerClass = Class.forName("android.app.INotificationManager");
            Field sServiceField = Toast.class.getDeclaredField("sService");
            sServiceField.setAccessible(true);
            Object notificationManager = sServiceField.get(null);
            if (notificationManager == null) {

                Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
                getServiceMethod.setAccessible(true);
                notificationManager = getServiceMethod.invoke(null);
            }
            if (notificationManager == null) {
                return;
            }
            final Object finalNotificationManager = notificationManager;
            Object notificationManagerProxy = Proxy.newProxyInstance(notificationManager.getClass().getClassLoader(), new Class[]{mINotificationManagerClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    String methodName = method.getName();
                    if ("enqueueToast".equals(methodName) && args != null) {
                        for (Object arg : args) {
                            if (arg == null) {
                                continue;
                            }
                            Class<?> aClass = arg.getClass();
                            if ("android.widget.Toast.TN".equals(aClass.getCanonicalName())) {

                                Field mHandlerField = aClass.getDeclaredField("mHandler");
                                mHandlerField.setAccessible(true);
                                mHandlerField.set(arg,new SilentlyHandler(arg));
                            }
                        }
                    }
                    return method.invoke(finalNotificationManager, args);
                }
            });
            sServiceField.set(null, notificationManagerProxy);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static class SilentlyHandler extends Handler {

        private Object toastTN;

        SilentlyHandler(Object toastTN) {
            this.toastTN = toastTN;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                IBinder token = (IBinder) msg.obj;
                Method handleShowMethod = toastTN.getClass().getDeclaredMethod("handleShow", IBinder.class);
                handleShowMethod.setAccessible(true);
                handleShowMethod.invoke(toastTN, token);

                Log.i("Test","=======handleShow========");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
