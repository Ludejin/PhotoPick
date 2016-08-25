package com.zero.photopicklib.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.zero.photopicklib.event.OnPhotoClickListener;
import com.zero.photopicklib.mvp.PickerContract;
import com.zero.photopicklib.mvp.PickerPresenter;
import com.zero.photopicklib.util.CaptureManager;
import com.zero.photopicklib.util.PickConfig;
import com.zero.photopicklib.util.StatusBarUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zero.photopicklib.ui.PicPagerActivity.EXTRA_ITEM;
import static com.zero.photopicklib.ui.PicPagerActivity.EXTRA_PATH;

/**
 * Created by Jin_ on 2016/6/11
 */
public class PickerActivity extends AppCompatActivity implements PickerContract.View,
        OnPhotoClickListener {

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
    private int mDirIndex = 0;

    private String addPath;

    private int spanCount;
    private int maxPickSize;
    private int toolbarColor;
    private boolean showCamera;
    private boolean showGif;
    private boolean showAll;
    private Bundle mBundle;

    public PickerActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_picker);

        mRcyPhoto = (RecyclerView) findViewById(R.id.rcy_picker);
        btnDir = (AppCompatButton) findViewById(R.id.btn_dir);

        mCaptureManager = new CaptureManager(this);

        initData();

        mPresenter = new PickerPresenter(new PhotoRepository(), this,
                this, showCamera, showGif, showAll);

        mPresenter.subscribe();

        initToolbar();

        initRecycleView();

        initPopupWin();
        initEvent();
    }

    private void initData() {
        mBundle = getIntent().getBundleExtra(PickConfig.EXTRA_PICK_BUNDLE);
        spanCount = mBundle.getInt(PickConfig.EXTRA_SPAN_COUNT, PickConfig.DEFAULT_SPAN_COUNT);
        maxPickSize = mBundle.getInt(PickConfig.EXTRA_MAX_SIZE, PickConfig.DEFAULT_PICK_SIZE);
        toolbarColor = mBundle.getInt(PickConfig.EXTRA_TOOLBAR_COLOR, PickConfig.DEFAULT_TOOLBAR_COLOR);
        selImages = mBundle.getStringArrayList(PickConfig.EXTRA_SEL_IMAGE);
        showCamera = mBundle.getBoolean(PickConfig.EXTRA_SHOW_CAMERA, PickConfig.DEFAULT_SHOW_CAMERA);
        showAll = mBundle.getBoolean(PickConfig.EXTRA_SHOW_ALL, PickConfig.DEFAULT_SHOW_ALL);
        showGif = mBundle.getBoolean(PickConfig.EXTRA_SHOW_GIF, PickConfig.DEFAULT_SHOW_GIF);
    }

    private void initEvent() {
        mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopupWindow.dismiss();
                mDirIndex = position;

                mPhotoAdapter.setCurrDirIndex(position);
                mDirAdapter.setSelPos(mDirIndex);

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

        mPhotoAdapter.setOnPhotoClickListener(this);
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
        toolbarTitle.setText(getString(R.string.picker_title));

        StatusBarUtil.setColor(this, ActivityCompat.getColor(this, toolbarColor));
        toolbar.setBackgroundResource(toolbarColor);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.menu_picker);

        final Drawable upArrow = ActivityCompat.getDrawable(this, R.drawable.ic_go_back);
        assert upArrow != null;
        upArrow.setColorFilter(ActivityCompat.getColor(this, android.R.color.white),
                PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ArrayList<String> selectedImages;
                selectedImages = mPhotoAdapter.getSelectedImages();

                if (selectedImages.size() == 0) {
                    Toast.makeText(PickerActivity.this, getString(R.string.picker_please_sel_photo),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAY_LIST, selectedImages);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return true;
            }
        });
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
    protected void onResume() {
        super.onResume();
        if (null != addPath && !TextUtils.isEmpty(addPath)) {
            PhotoDir directory = mDirs.get(0);
            directory.getPhotos().add(0, new Photo(addPath.hashCode(), addPath));
            directory.setCoverPath(addPath);

            mPhotos.clear();
            mPhotos.addAll(directory.getPhotos());
            mPhotoAdapter.notifyDataSetChanged();
            addPath = "";
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
        mDirAdapter.setSelPos(0);
        mDirAdapter.notifyDataSetChanged();

        mPhotos.clear();
        btnDir.setText(photoDirs.get(0).getName());
        mPhotos.addAll(photoDirs.get(0).getPhotos());
        mPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(PickerPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void OnClick(View view, int pos, int imgSize) {
        Intent intent = new Intent();
        intent.setClass(this, PicPagerActivity.class);

        intent.putExtra(EXTRA_ITEM, pos);
        intent.putStringArrayListExtra(EXTRA_PATH, mDirs.get(mDirIndex).getPhotoPaths());

        startActivity(intent);
    }
}
