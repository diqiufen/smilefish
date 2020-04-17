package com.smle.fish.interfaces;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Target(ElementType.FIELD)
 * @Retention(RetentionPolicy.RUNTIME)
 * @PACKAGE_NAME：com.smle.fish.interfaces
 * @user：yj
 * @date：2020/4/15
 * @版本：
 * @功能描述：
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {

    int viewId() default -1;
}
