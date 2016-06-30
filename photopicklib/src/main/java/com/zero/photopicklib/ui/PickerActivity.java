package com.zero.photopicklib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.zero.photopicklib.R;
import com.zero.photopicklib.adapter.PhotoAdapter;
import com.zero.photopicklib.adapter.PopupDirAdapter;
import com.zero.photopicklib.data.PhotoRepository;
import com.zero.photopicklib.entity.Photo;
import com.zero.photopicklib.entity.PhotoDir;
import com.zero.photopicklib.mvp.PickerContract;
import com.zero.photopicklib.mvp.PickerPresenter;
import com.zero.photopicklib.util.CaptureManager;
import com.zero.photopicklib.util.PickConfig;
import com.zero.photopicklib.util.StatusBarCompat;

import java.io.IOException;
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
    private PopupDirAdapter mDirAdapter;

    private ListPopupWindow mPopupWindow;
    private CaptureManager mCaptureManager;

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private AppCompatButton btnDir;
    public static int COUNT_MAX = 4;

    private List<Photo> mPhotos = new ArrayList<>();
    private ArrayList<String> selImages = new ArrayList<>();
    private List<PhotoDir> mDirs = new ArrayList<>();

    private String addPath;

    private int spanCount;
    private int maxPickSize;
    private int toolbarColor;
    private boolean showCamera;
    private boolean showGif;
    private Bundle mBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_picker);


        StatusBarCompat.compat(this, ActivityCompat.getColor(this, R.color.colorPrimary));

        mRcyPhoto = (RecyclerView) findViewById(R.id.rcy_picker);
        btnDir = (AppCompatButton) findViewById(R.id.btn_dir);

        mPresenter = new PickerPresenter(new PhotoRepository(), this,
                this, false, false);

        mPresenter.subscribe();

        mCaptureManager = new CaptureManager(this);

        initData();

        initToolbar();

        initRecycleView();

        initPopupWin();
        initEvent();
    }

    private void initData() {
        mBundle = getIntent().getBundleExtra(PickConfig.EXTRA_PICK_BUNDLE);
        spanCount = mBundle.getInt(PickConfig.EXTRA_SPAN_COUNT, PickConfig.DEFAULT_SPAN_COUNT);
        maxPickSize = mBundle.getInt(PickConfig.EXTRA_MAX_SIZE, PickConfig.DEFAULT_PICK_SIZE);
        toolbarColor = mBundle.getInt(PickConfig.EXTRA_TOOLBAR_COLOR, PickConfig.DEFALUT_TOOLBAR_COLOR);
        selImages = mBundle.getStringArrayList(PickConfig.EXTRA_SEL_IMAGE);
    }

    private void initEvent() {
        mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopupWindow.dismiss();

                mPhotoAdapter.setCurrDirIndex(position);

                PhotoDir directory = mDirs.get(position);
                btnDir.setText(directory.getName());
                mPhotos.clear();
                mPhotos.addAll(directory.getPhotos());
                mPhotoAdapter.notifyDataSetChanged();
            }
        });
        btnDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else if (!PickerActivity.this.isFinishing()) {
                    adjustHeight();
                    mPopupWindow.show();
                }
            }
        });
    }

    private void initPopupWin() {
        mDirAdapter = new PopupDirAdapter(mDirs, this);

        mPopupWindow = new ListPopupWindow(this);
        mPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        mPopupWindow.setAnchorView(btnDir);
        mPopupWindow.setAdapter(mDirAdapter);
        mPopupWindow.setModal(true);
        mPopupWindow.setDropDownGravity(Gravity.BOTTOM);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("图片选择");
        setSupportActionBar(toolbar);

        StatusBarCompat.compat(this, ActivityCompat.getColor(this, toolbarColor));
        toolbar.setBackgroundResource(toolbarColor);
        toolbar.setNavigationIcon(R.drawable.ic_go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void initRecycleView() {
        mPhotoAdapter = new PhotoAdapter(this, mPhotos, selImages, maxPickSize, spanCount,
                true, toolbar, toolbarTitle);
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mRcyPhoto.setLayoutManager(layoutManager);
        mRcyPhoto.setAdapter(mPhotoAdapter);
        mRcyPhoto.setItemAnimator(new DefaultItemAnimator());

        mPhotoAdapter.setOnCameraClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    intent = mCaptureManager.dispatchTakePictureIntent();
                    startActivityForResult(intent, CaptureManager.REQUEST_TAKE_PHOTO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            mCaptureManager.galleryAddPic();
            if (mDirs.size() > 0) {
                addPath = mCaptureManager.getCurrentPhotoPath();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mCaptureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCaptureManager.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void adjustHeight() {
        if (null == mDirAdapter) return;
        int count = mDirAdapter.getCount();
        count = count < COUNT_MAX ? count : COUNT_MAX;
        if (mPopupWindow != null) {
            mPopupWindow.setHeight(count * getResources()
                    .getDimensionPixelOffset(R.dimen.item_directory_height));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picker, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<String> selectedImages = mPhotoAdapter.getSelectedImages();
        if (selectedImages.size() == 0) {
            Toast.makeText(this, "Please Select Photo", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAY_LIST, selectedImages);
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != addPath && !TextUtils.isEmpty(addPath)) {
            PhotoDir directory = mDirs.get(0);
            directory.getPhotos().add(0, new Photo(addPath.hashCode(), addPath));
            directory.setCoverPath(addPath);

            mPhotos.clear();
            mPhotos.addAll(directory.getPhotos());
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void getPhotoSuc(List<PhotoDir> photoDirs) {
        mDirs.clear();
        mDirs.addAll(photoDirs);
        mDirAdapter.notifyDataSetChanged();

        mPhotos.clear();
        mPhotos.addAll(photoDirs.get(0).getPhotos());
        mPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(PickerPresenter presenter) {
        mPresenter = presenter;
    }
}
