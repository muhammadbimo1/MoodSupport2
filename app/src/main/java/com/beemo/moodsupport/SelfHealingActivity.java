package com.beemo.moodsupport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelfHealingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_healing);
    }
    public void startbreath(View v) {
        Intent intent = new Intent(SelfHealingActivity.this, BreathingActivity.class);
        startActivity(intent);
        //todo : finished
    }
}
