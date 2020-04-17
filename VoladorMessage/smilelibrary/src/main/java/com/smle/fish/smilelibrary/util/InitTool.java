package com.smle.fish.smilelibrary.util;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;

/**
 * @PACKAGE_NAME：com.smle.fish.smilelibrary.util
 * @user：yj
 * @date：2020/4/15
 * @版本：
 * @功能描述：
 */
public class InitTool {

    public static void init(Object initObject, Class annotationClass, @NonNull InitCallBack initCallBack) throws IllegalAccessException {
        Class<?> cla = initObject.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                Object objectValue = initCallBack.onCallBack(field.getAnnotation(annotationClass));
                if (objectValue == null) {
                    throw new NullPointerException("Call Back result null");
                } else {
                    field.setAccessible(true);
                    field.set(initObject, objectValue);
                }
            }
        }
    }

    public interface InitCallBack<T> {
        Object onCallBack(T annotationObject);
    }
}
