package com.zhangqiang.instancerestore;

import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;

public class InstanceRestore {

    public static void restore(Object object, Bundle savedInstance) {
        if (savedInstance == null) {
            return;
        }
        try {
            Class<?> targetClass = object.getClass();
            Class<?> aClass1 = getRestoreClass(targetClass);
            while (aClass1 != null) {

                aClass1.getMethod("restoreInstance", targetClass, Bundle.class).invoke(null, object, savedInstance);
                targetClass = targetClass.getSuperclass();
                aClass1 = getRestoreClass(targetClass);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void save(Object object, Bundle bundle) {

        try {
            Class<?> targetClass = object.getClass();
            Class<?> aClass1 = getRestoreClass(targetClass);
            while (aClass1 != null) {

                aClass1.getMethod("saveInstance", targetClass, Bundle.class).invoke(null, object, bundle);
                targetClass = targetClass.getSuperclass();
                aClass1 = getRestoreClass(targetClass);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static Class getRestoreClass(Class<?> aClass) {

        if (aClass == null) {
            return null;
        }
        Package aPackage = aClass.getPackage();
        if (aPackage == null) {
            return null;
        }
        try {
            return Class.forName(aPackage.getName() + ".InstanceRestore_" + aClass.getSimpleName());
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        }
        return null;
    }
}
