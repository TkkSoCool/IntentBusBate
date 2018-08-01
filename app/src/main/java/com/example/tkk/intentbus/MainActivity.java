package com.example.tkk.intentbus;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.api.InjectActivity;
import com.example.api.InjectParam;
import com.example.tkk.intentbus.SaveInstanceState.AActivity__JumpCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@InjectActivity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_to_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AActivity__JumpCenter.builder(MainActivity.this).setId(1).create().go();
            }
        });
    }

    public void goActivity2(View view) {
        Main2Activity__JumpCenter.builder(this).create().go();
    }
}
