package com.zero.photopicklib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zero.photopicklib.R;
import com.zero.photopicklib.adapter.PhotoPagerAdapter;
import com.zero.photopicklib.widget.PinchImageViewPager;

import java.util.ArrayList;


/**
 * Created by Jin_ on 2016/6/30
 * 邮箱：dejin_lu@creawor.com
 */
public class PicPagerActivity extends AppCompatActivity {

    public static final String EXTRA_PATH = "extra_path";
    public static final String EXTRA_ITEM = "extra_item";

    private ArrayList<String> mPaths;
    private int mCurrItem = 0;
    private PinchImageViewPager mViewPager;
    private PhotoPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pic_pager);

        initData();
        initView();
    }

    private void initView() {
        mViewPager = (PinchImageViewPager) findViewById(R.id.vp_pic_item);
        mPagerAdapter = new PhotoPagerAdapter(mPaths, mViewPager, this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrItem);
    }

    private void initData() {
        mPaths = getIntent().getStringArrayListExtra(EXTRA_PATH);
        mCurrItem = getIntent().getIntExtra(EXTRA_ITEM, 0);
    }
}
