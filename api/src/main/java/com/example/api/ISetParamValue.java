package com.example.api;

/**
 * Created  on 2018/4/13
 *
 * @author 唐开阔
 * @describe 设置对应参数的值
 */
public interface ISetParamValue<T> {
    /**
     * 传统intent赋值，传递对象需要序列化
     */
    void setValueByIntent(T target);

    /**
     * 直接传递对象的引用，不需要序列化
     */
    void setValueByMemory(T target);

}
