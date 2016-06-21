package com.zero.photopicklib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zero.photopicklib.R;

/**
 * see https://github.com/crosswall/Android-PickPhotos/blob/master/pickphotos/src/main/java/me/crosswall/photo/pick/widget/ThumbPhotoView.java
 */
public class ThumbPhotoView extends RelativeLayout {
    ImageView photo_thumbview;

    ImageView photo_thumbview_selected;

    TextView photo_thumbview_position;

    public ThumbPhotoView(Context context) {
        super(context);
        initView(context);
    }

    public ThumbPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ThumbPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.item_photo, this);
        photo_thumbview = (ImageView) view.findViewById(R.id.photo_thumbview);
        photo_thumbview_selected = (ImageView) view.findViewById(R.id.photo_thumbview_selected);
        photo_thumbview_position = (TextView) view.findViewById(R.id.photo_thumbview_position);
    }


    public void loadData(String folderPath) {
        Glide.with(getContext())
                .load(folderPath)
                .placeholder(R.drawable.ic_broken_image)
                .thumbnail(0.5f)
                .error(R.drawable.ic_broken_image)
                .into(photo_thumbview);
        photo_thumbview_selected.setVisibility(VISIBLE);
    }

    public void showSelected(boolean showSelected) {
        if (showSelected) {
            photo_thumbview_selected.setBackgroundResource(R.drawable.photo_selected);
        } else {
            photo_thumbview_selected.setBackgroundResource(R.drawable.photo_unselected);
        }
    }
}
