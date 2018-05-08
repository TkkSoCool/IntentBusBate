package com.example.api;

import java.io.Serializable;

/**
 * Created  on 2018-05-03
 *
 * @author 唐开阔
 * @describe 在注解处理器中，通过反射获取class对象，会null
 */
public class Utils {
    public static boolean typeIsisAssignableFromSerializable(Class targerClass) {
        return Serializable.class.isAssignableFrom(targerClass);
    }
    public static boolean typeIsisAssignableFromParcelable(Class targerClass,Class parcelableClass) {
        return parcelableClass.isAssignableFrom(targerClass);
    }
}
