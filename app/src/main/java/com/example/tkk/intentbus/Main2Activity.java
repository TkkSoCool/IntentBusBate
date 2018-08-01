package com.example.tkk.intentbus;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.api.InjectParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Main2Activity extends AppCompatActivity {
    /**
     * 八种基本数据类型
     */
    @InjectParam
    byte byteBase;
//    @InjectParam
//    short shortBase;
//    @InjectParam
//    int intBase;
//    @InjectParam
//    long longBase;
//    @InjectParam
//    float floatBase;
//    @InjectParam
//    double doubleBase;
//    @InjectParam
//    char charBase;
//    @InjectParam
//    boolean booleanBase;
//    /**
//     * 基本类型数组
//     */
//    @InjectParam
//    byte[] byteArr;
//    @InjectParam
//    short[] shortArr;
//    @InjectParam
//    int[] intArr;
//    @InjectParam
//    long[] longArr;
//    @InjectParam
//    float[] floatArr;
//    @InjectParam
//    double[] doubleArr;
//    @InjectParam
//    char[] charArr;
//    @InjectParam
//    boolean[] booleanArr;
//
//    /**
//     * 四种ArrayList
//     * ArrayList<Integer>
//     * ArrayList<CharSequence>
//     * ArrayList<String>
//     * ArrayList<Parcelable>
//     */
//    @InjectParam
//    ArrayList<Integer> integerArrayList;
//    @InjectParam
//    ArrayList<CharSequence> charSequenceArrayList;
//    @InjectParam
//    ArrayList<String> stringArrayList;
    @InjectParam
    ArrayList<UserParcelable> parcelableArrayList;
//    @InjectParam
//    HashMap<String,Parcelable> stringParcelableMap;
//    @InjectParam
//    User user;
//    @InjectParam
//    UserParcelable userParcelable;
    @InjectParam
    UserSerializable userSerializable;

//    @InjectParam
//    User[] userArr;
//    @InjectParam
//    UserParcelable[] userParcelableArr;
    @InjectParam
    UserSerializable[] userSerializableArr;

    /**
     * 特殊类型
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Main2Activity__JumpCenter.bind(this);
    }

    private boolean typeIsisAssignableFromSerializable(String className) {
        Class targerClass;

        try {
            targerClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return Serializable.class.isAssignableFrom(targerClass);
    }
}
