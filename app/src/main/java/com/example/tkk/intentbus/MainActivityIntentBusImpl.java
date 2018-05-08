package com.example.tkk.intentbus;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.example.api.ISetParamValue;

/**
 * Created  on 2018/4/13
 *
 * @author 唐开阔
 * @describe
 */
public class MainActivityIntentBusImpl implements ISetParamValue<MainActivity> {
    private Builder builder;
    private Context mContent;
    private static MainActivityIntentBusImpl instance;

    private MainActivityIntentBusImpl(Builder builder) {
        this.builder = builder;
        mContent = builder.context;
    }
    public static MainActivityIntentBusImpl getInstance() {
        return instance;
    }

    public static void bind(MainActivity mainActivity) {
        if (instance == null) {
            return;
        }
        if (instance.builder.isDataByIntent)
            instance.setValueByIntent(mainActivity);
        else
            instance.setValueByMemory(mainActivity);
    }
    /**
     * 进行跳转和传值
     */
    public void go() {
        Intent intent = new Intent(mContent, MainActivity.class);
        setIntent(intent);
        mContent.startActivity(intent);
    }
    public static Builder builder(Context from) {
        return new Builder(from);
    }

    private void setIntent(Intent intent){
        if (builder.isDataByIntent) {
            intent.putExtra("num", builder.num);
            intent.putExtra("info", builder.info);
            intent.putExtra("user", (Parcelable) builder.user);
        }
    }

    //下面两个是获取值得方法
    @Override
    public void setValueByIntent(MainActivity target) {
//        target.num =  target.getIntent().getIntExtra("num", -1);
//        target.info = target.getIntent().getStringExtra("info");
//        target.user = target.getIntent().getParcelableExtra("user");
    }

    @Override
    public void setValueByMemory(MainActivity target) {
//        target.num = builder.num;
//        target.info = builder.info;
//        target.user = builder.user;
    }

    public static class Builder {
        private Context context;
        private Boolean isDataByIntent = true;


        private int num;
        private String info;
        private UserParcelable user;
        private Builder(Context from) {
            this.context = from;
        }
        public Builder setDataByIntent(Boolean dataByIntent) {
            this.isDataByIntent = dataByIntent;
            return this;
        }
        public Builder setNum(int num) {
            this.num = num;
            return this;
        }
        public String getInfo() {
            return info;
        }
        public Builder setInfo(String info) {
            this.info = info;
            return this;
        }
        public Builder setUser(UserParcelable user) {
            this.user = user;
            return this;
        }
        public MainActivityIntentBusImpl create() {
            instance = new MainActivityIntentBusImpl(this);
            return instance;
        }
    }
}
