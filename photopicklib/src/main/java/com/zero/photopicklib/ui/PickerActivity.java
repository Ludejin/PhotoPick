package com.zero.photopicklib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.zero.photopicklib.R;
import com.zero.photopicklib.adapter.PhotoAdapter;
import com.zero.photopicklib.data.PhotoRepository;
import com.zero.photopicklib.entity.Photo;
import com.zero.photopicklib.entity.PhotoDir;
import com.zero.photopicklib.mvp.PickerContract;
import com.zero.photopicklib.mvp.PickerPresenter;
import com.zero.photopicklib.util.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin_ on 2016/6/11
 * 邮箱：dejin_lu@creawor.com
 */
public class PickerActivity extends AppCompatActivity implements PickerContract.View {

    private PickerPresenter mPresenter;

    private RecyclerView mRcyPhoto;
    private PhotoAdapter mPhotoAdapter;

    private List<Photo> mPhotos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_picker);

        StatusBarCompat.compat(this, ActivityCompat.getColor(this, R.color.colorPrimary));
        mRcyPhoto = (RecyclerView) findViewById(R.id.rcy_picker);

        mPresenter = new PickerPresenter(new PhotoRepository(),this,
                this, false, false);

        initToolbar();

        initRecycleView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("图片选择");
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setElevation(0);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    private void initRecycleView() {
        mPhotoAdapter = new PhotoAdapter(this, mPhotos, 6);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRcyPhoto.setLayoutManager(layoutManager);
        mRcyPhoto.setAdapter(mPhotoAdapter);
        mRcyPhoto.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picker, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("选择长度", "" + mPhotoAdapter.getSelectedImages().size());
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void getPhotoSuc(List<PhotoDir> photoDirs) {
        Log.i("文件夹：", "" + photoDirs.get(0).getPhotos().size());
        mPhotos.clear();
        mPhotos.addAll(photoDirs.get(0).getPhotos());
        mPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(PickerPresenter presenter) {
        mPresenter = presenter;
    }
}
