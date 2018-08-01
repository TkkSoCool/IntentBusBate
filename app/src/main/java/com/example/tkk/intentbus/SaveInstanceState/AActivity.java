package com.example.tkk.intentbus.SaveInstanceState;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.api.InjectParam;
import com.example.tkk.intentbus.R;

public class AActivity extends AppCompatActivity {
    @InjectParam
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        AActivity__JumpCenter.bind(this);
        Log.d("AActivity", ">>>onCreate---" + id);
        final TextView textView  = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("点击了");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
