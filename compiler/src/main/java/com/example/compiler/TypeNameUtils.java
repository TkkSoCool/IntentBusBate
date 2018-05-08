package com.example.compiler;

import com.squareup.javapoet.ClassName;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;

/**
 * Created  on 2018/1/26
 *
 * @author 唐开阔
 * @describe 对象类型信息
 */

public class TypeNameUtils {
    public static final ClassName UTILS = ClassName.get("com.example.api","Utils");
    public static final ClassName INTEGER = ClassName.get("java.lang", "Integer");
    public static final ClassName CHAR_SEQUENCE = ClassName.get("java.lang", "CharSequence");
    public static final ClassName STRING = ClassName.get("java.lang", "String");
    public static final ClassName PARCELABLE = ClassName.get("android.os", "Parcelable");
    public static final ClassName ARRAYLIST = ClassName.get(ArrayList.class);
    public static final ClassName CONTEXT = ClassName.get("android.content","Context");
    public static final ClassName BUILDER = ClassName.get("","Builder");
    public static final ClassName INTENT = ClassName.get("android.content","Intent");
    public static ClassName THIS(String name){
      return   ClassName.get("",name);
    }
}
