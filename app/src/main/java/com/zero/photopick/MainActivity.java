package com.zero.photopick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zero.photopicklib.ui.PickerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tiaozhuan(View view) {
        Intent intent = new Intent();
        intent.setClass(this, PickerActivity.class);
        startActivity(intent);
    }
}
