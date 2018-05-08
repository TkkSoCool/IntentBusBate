package com.example.api;

/**
 * Created  on 2018/4/13
 *
 * @author 唐开阔
 * @describe
 */
public @interface InjectParam {
    boolean booleanDefault() default false;
    byte byteDefault() default Byte.MAX_VALUE;
    short shortDefault() default Short.MAX_VALUE;
    int intDefault() default Integer.MAX_VALUE;
    long longDefault() default Long.MAX_VALUE;
    float floatDefault() default Float.MAX_VALUE;
    double doubleDefault() default Double.MAX_VALUE;
    char charDefault() default Character.MAX_VALUE;
}
