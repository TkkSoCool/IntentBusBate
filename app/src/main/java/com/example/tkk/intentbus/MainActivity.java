package com.example.tkk.intentbus;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.api.InjectActivity;
import com.example.api.InjectParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@InjectActivity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goActivity2(View view) {
        Main2Activity_SUBSCRIBE_INFO.builder(this).setByteBase((byte) 1.0).create().go();
    }
}
