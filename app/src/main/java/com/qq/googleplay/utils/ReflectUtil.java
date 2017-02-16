package com.qq.googleplay.utils;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：GooglePlay
 * Package_Name：com.qq.googleplay
 * Version：1.0
 * time：2016/2/16 13:33
 * des ：${TODO}
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
public class ReflectUtil {

    private static Object invoke(Class<?> cls, Object receiver, String methodname, Class<?>[] clsArr
            , Object[] objArr) throws Exception {
        Method method = null;
        if (objArr == null || objArr.length == 0) {
            method = cls.getMethod(methodname, new Class[0]);
            method.setAccessible(true);
            return method.invoke(receiver, new Object[0]);
        }
        method = cls.getMethod(methodname, clsArr);
        method.setAccessible(true);
        return method.invoke(receiver, objArr);
    }

    private static Object invoke(Class<?> cls, Object receiver, String methodname, Object[] objArr)
            throws Exception {
        Method method = null;
        if (objArr == null || objArr.length == 0) {
            method = cls.getMethod(methodname, new Class[0]);
            method.setAccessible(true);
            return method.invoke(receiver, new Object[0]);
        }
        method = cls.getMethod(methodname, getParameterTypes(objArr));
        method.setAccessible(true);
        return method.invoke(receiver, objArr);
    }

    public static Object getDeclaredField(Object obj, Class<?> cls, String fieldName)
            throws NoSuchFieldException {
        if (obj == null || cls == null || fieldName == null) {
            throw new IllegalArgumentException("parameter can not be null!");
        }
        try {
            Field declaredField = cls.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField.get(obj);
        } catch (Exception e) {
            throw new NoSuchFieldException(fieldName);
        }
    }

    public static Object getDeclaredField(String classame, String fieldName) throws NoSuchFieldException {
        if (classame == null || fieldName == null) {
            throw new IllegalArgumentException("parameter can not be null!");
        }
        try {
            Class cls = Class.forName(classame);
            return getSupperField(cls, cls, fieldName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("className not found");
        }
    }

    public static Object invoke(String className, String methodName, Class<?>[] clsArr, Object[] objArr) throws Exception {
        Class cls = Class.forName(className);
        return invoke(cls, cls, methodName, clsArr, objArr);
    }

   public static Object invoke(String classname, String methodname, Object[] objArr) throws Exception {
        Class cls = Class.forName(classname);
        return invoke(cls, (Object) cls, methodname, objArr);
    }

    private static Class<?>[] getParameterTypes(Object[] objArr) {
        Class<?>[] clsArr = new Class[objArr.length];
        for (int i = 0; i < clsArr.length; i++) {
            clsArr[i] = objArr[i].getClass();
        }
        return clsArr;
    }

    private static Object getSupperField(Object obj, Class<?> cls, String fieldName) throws NoSuchFieldException {
        Class superclass = cls.getSuperclass();
        while (superclass != null) {
            try {
                return getDeclaredField(obj, superclass, fieldName);
            } catch (NoSuchFieldException e) {
                try {
                    superclass = superclass.getSuperclass();
                } catch (Exception e2) {
                    superclass = null;
                }
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    public static String getSystemProperties(Context context, String str) throws IllegalArgumentException {
        try {
            Class loadClass = context.getClassLoader().loadClass("android.os.SystemProperties");
            return (String) loadClass.getMethod("get", String.class).invoke(loadClass, str);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e2) {
            return "";
        }
    }

}