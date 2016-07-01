package com.zero.photopicklib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zero.photopicklib.R;

/**
 * see https://github.com/crosswall/Android-PickPhotos/blob/master/pickphotos/src/main/java/me/crosswall/photo/pick/widget/ThumbPhotoView.java
 */
public class ThumbPhotoView extends RelativeLayout {
    ImageView photo_thumbview;

    ImageView photo_thumbview_selected;

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
    }


    public void loadData(String folderPath) {
        Glide.with(getContext())
                .load(folderPath)
                .placeholder(R.drawable.ic_photo)
                .thumbnail(0.5f)
                .error(R.drawable.ic_broken_img)
                .into(photo_thumbview);
        photo_thumbview_selected.setVisibility(VISIBLE);
    }

    public void loadCamera() {
        photo_thumbview.setImageResource(R.drawable.ic_camera);
        photo_thumbview_selected.setVisibility(GONE);
    }

    public void showSelected(boolean showSelected) {
        if (showSelected) {
            photo_thumbview_selected.setImageResource(R.drawable.img_select);
        } else {
            photo_thumbview_selected.setImageResource(R.drawable.img_unselect);
        }
    }

    public ImageView getPhoto_thumbview_selected() {
        return photo_thumbview_selected;
    }
}
