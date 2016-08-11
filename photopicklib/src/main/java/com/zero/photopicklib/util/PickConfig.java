package com.zero.photopicklib.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;

import com.zero.photopicklib.R;
import com.zero.photopicklib.ui.PickerActivity;

import java.util.ArrayList;

/**
 * see
 * https://github.com/crosswall/Android-PickPhotos/blob/master/pickphotos/src/main/java/me/crosswall/photo/pick/PickConfig.java
 */
public class PickConfig {

    public static int DEFAULT_TOOLBAR_COLOR = R.color.colorPrimary;
    public static int DEFAULT_PICK_SIZE     = 1;
    public static int DEFAULT_SPAN_COUNT    = 3;
    public static boolean DEFAULT_SHOW_GIF  = false;
    public static boolean DEFAULT_CHECK_IMAGE = false;
    public static boolean DEFAULT_SHOW_ALL  = true;
    public static boolean DEFAULT_SHOW_CAMERA  = true;

    public final static int PICK_REQUEST_CODE = 10607;

    public final static String EXTRA_STRING_ARRAY_LIST = "extra_string_array_list";

    public final static String EXTRA_TOOLBAR_COLOR  = "extra_toolbar_color";
    public final static String EXTRA_PICK_BUNDLE    = "extra_pick_bundle";
    public final static String EXTRA_CHECK_IMAGE    = "extra_check_image";
    public final static String EXTRA_SPAN_COUNT     = "extra_span_count";
    public final static String EXTRA_SEL_IMAGE      = "extra_sel_image";
    public final static String EXTRA_MAX_SIZE       = "extra_max_size";
    public final static String EXTRA_SHOW_ALL       = "extra_show_all";
    public final static String EXTRA_SHOW_CAMERA    = "extra_show_camera";
    public final static String EXTRA_SHOW_GIF       = "extra_show_gif";

    private final int spanCount;
    private final int maxPickSize;
    private final int toolbarColor;
    private final boolean showGif;
    private final boolean showAll;
    private final boolean showCamera;
    private final boolean checkImage;
    private final ArrayList<String> selImages = new ArrayList<>();

    public PickConfig(Activity context, Builder builder) {
        this.spanCount = builder.spanCount;
        this.maxPickSize = builder.maxPickSize;
        this.toolbarColor = builder.toolbarColor;

        this.showGif = builder.showGif;
        this.showCamera = builder.showCamera;
        this.showAll = builder.showAll;
        this.checkImage = builder.checkImage;

        this.selImages.clear();
        this.selImages.addAll(builder.selImages);

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SPAN_COUNT, this.spanCount);
        bundle.putInt(EXTRA_MAX_SIZE, this.maxPickSize);
        bundle.putInt(EXTRA_TOOLBAR_COLOR,this.toolbarColor);
        bundle.putBoolean(EXTRA_SHOW_GIF, this.showGif);
        bundle.putBoolean(EXTRA_SHOW_ALL, this.showAll);
        bundle.putBoolean(EXTRA_SHOW_CAMERA, this.showCamera);
        bundle.putBoolean(EXTRA_CHECK_IMAGE, this.checkImage);
        bundle.putStringArrayList(EXTRA_SEL_IMAGE, selImages);
        startPick(context, bundle);
    }

    private void startPick(Activity context, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PICK_BUNDLE, bundle);
        intent.setClass(context, PickerActivity.class);
        context.startActivityForResult(intent, PICK_REQUEST_CODE);
    }

    public static class Builder {
        private Activity context;
        private int spanCount = DEFAULT_SPAN_COUNT;
        private int maxPickSize = DEFAULT_PICK_SIZE;
        private int toolbarColor = DEFAULT_TOOLBAR_COLOR;
        private boolean showGif = DEFAULT_SHOW_GIF;
        private boolean showAll = DEFAULT_SHOW_ALL;
        private boolean showCamera = DEFAULT_SHOW_CAMERA;
        private boolean checkImage = DEFAULT_CHECK_IMAGE;
        private ArrayList<String> selImages = new ArrayList<>();

        public Builder(Activity context) {
            if (context == null) {
                throw new IllegalArgumentException("A non-null Context must be provided");
            }
            this.context = context;
        }

        public PickConfig.Builder spanCount(int spanCount) {
            this.spanCount = spanCount;
            if (this.spanCount == 0) {
                this.spanCount = DEFAULT_SPAN_COUNT;
            }
            return this;
        }

        public PickConfig.Builder maxPickSize(int maxPickSize) {
            this.maxPickSize = maxPickSize;
            if (this.maxPickSize == 0) {
                this.maxPickSize = DEFAULT_PICK_SIZE;
            }
            return this;
        }

        public PickConfig.Builder toolbarColor(@ColorRes int toolbarColor) {
            this.toolbarColor = toolbarColor;
            if (0 == this.toolbarColor) {
                this.toolbarColor = DEFAULT_TOOLBAR_COLOR;
            }
            return this;
        }

        public PickConfig.Builder showGif(boolean showGif) {
            this.showGif = showGif;
            return this;
        }

        public PickConfig.Builder showCamera(boolean showCamera) {
            this.showCamera = showCamera;
            return this;
        }

        public PickConfig.Builder showAll(boolean showAll) {
            this.showAll = showAll;
            return this;
        }

        public PickConfig.Builder checkImage(boolean checkImage) {
            this.checkImage = checkImage;
            return this;
        }

        public PickConfig.Builder setSelImage(ArrayList<String> selImages) {
            this.selImages = selImages;
            return this;
        }

        public PickConfig build() {
            return new PickConfig(context, this);
        }
    }
}
