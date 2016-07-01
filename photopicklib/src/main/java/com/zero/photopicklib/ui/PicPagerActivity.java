package com.zero.photopicklib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zero.photopicklib.R;
import com.zero.photopicklib.adapter.PhotoPagerAdapter;
import com.zero.photopicklib.util.StatusBarCompat;
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

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pic_pager);

        initToolbar();

        initData();
        initView();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        StatusBarCompat.compat(this, ActivityCompat.getColor(this, android.R.color.transparent));
        mToolbar.setNavigationIcon(R.drawable.ic_go_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ActionBar mActionBar = getSupportActionBar();
        if (null != mActionBar) {
            mActionBar.setElevation(0);
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
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
