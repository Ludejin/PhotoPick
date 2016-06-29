package com.zero.photopick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zero.photopicklib.util.PickConfig;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mSelImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        JniUtil jni = new JniUtil();
//        Log.i("结果", jni.append("你好", "世界"));
    }

    public void tiaozhuan(View view) {
        new PickConfig.Builder(this)
                .maxPickSize(5)
                .showGif(true)
                .spanCount(3)
                .setSelImage(mSelImages)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }

        if (PickConfig.PICK_REQUEST_CODE == requestCode) {
            mSelImages = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
            Log.i("选择长度", mSelImages.size() + "");
        }
    }
}
