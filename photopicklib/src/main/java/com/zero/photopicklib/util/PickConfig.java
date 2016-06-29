package com.zero.photopicklib.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zero.photopicklib.ui.PickerActivity;

import java.util.ArrayList;

/**
 * see
 * https://github.com/crosswall/Android-PickPhotos/blob/master/pickphotos/src/main/java/me/crosswall/photo/pick/PickConfig.java
 */
public class PickConfig {

    public static int DEFAULT_SPANCOUNT = 3;

    public static int DEFAULT_PICKSIZE = 1;

    public static boolean DEFALUT_SHOW_GIF = false;

    public static boolean DEFALUT_USE_CURSORLOADER = true;
    public static boolean DEFALUT_CHECK_IMAGE = false;

    public final static int PICK_REQUEST_CODE = 10607;

    public final static String EXTRA_STRING_ARRAYLIST = "extra_string_array_list";

    public final static String EXTRA_PICK_BUNDLE = "extra_pick_bundle";
    public final static String EXTRA_SPAN_COUNT = "extra_span_count";
    public final static String EXTRA_MAX_SIZE = "extra_max_size";
    public final static String EXTRA_SHOW_GIF = "extra_show_gif";
    public final static String EXTRA_SEL_IMAGE = "extra_sel_image";
    public final static String EXTRA_CHECK_IMAGE = "extra_check_image";

    private final int spanCount;
    private final int maxPickSize;
    private final boolean showGif;
    private final boolean checkImage;
    private final ArrayList<String> selImages = new ArrayList<>();

    public PickConfig(Activity context, Builder builder) {
        this.spanCount = builder.spanCount;
        this.maxPickSize = builder.maxPickSize;
        this.showGif = builder.showGif;
        this.checkImage = builder.checkImage;
        this.selImages.clear();
        this.selImages.addAll(builder.selImages);

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SPAN_COUNT, this.spanCount);
        bundle.putInt(EXTRA_MAX_SIZE, this.maxPickSize);
        bundle.putBoolean(EXTRA_SHOW_GIF, this.showGif);
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
        private int spanCount = DEFAULT_SPANCOUNT;
        private int maxPickSize = DEFAULT_PICKSIZE;
        private boolean showGif = DEFALUT_SHOW_GIF;
        private boolean checkImage = DEFALUT_CHECK_IMAGE;
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
                this.spanCount = DEFAULT_SPANCOUNT;
            }
            return this;
        }

        public PickConfig.Builder maxPickSize(int maxPickSize) {
            this.maxPickSize = maxPickSize;
            if (this.maxPickSize == 0) {
                this.maxPickSize = DEFAULT_PICKSIZE;
            }
            return this;
        }

        public PickConfig.Builder showGif(boolean showGif) {
            this.showGif = showGif;
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
