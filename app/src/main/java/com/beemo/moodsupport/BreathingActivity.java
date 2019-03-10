package com.beemo.moodsupport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BreathingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);
    }
    public void startbreathe(View v) {
        Intent intent = new Intent(BreathingActivity.this, BreathingStart.class);
        startActivity(intent);
        //todo : finished
    }
}
