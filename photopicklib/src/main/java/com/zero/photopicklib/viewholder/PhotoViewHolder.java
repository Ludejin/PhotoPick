package com.zero.photopicklib.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zero.photopicklib.R;
import com.zero.photopicklib.entity.Photo;
import com.zero.photopicklib.event.OnPhotoClickListener;
import com.zero.photopicklib.widget.ThumbPhotoView;

import java.util.ArrayList;

/**
 * Created by Jin_ on 2016/6/21
 * 邮箱：dejin_lu@creawor.com
 */
public class PhotoViewHolder extends RecyclerView.ViewHolder {

    public ThumbPhotoView thumbPhotoView;
    private Context mContext;
    private TextView mTitle;

    private OnPhotoClickListener mPhotoClickListener;

    public void setPhotoClickListener(OnPhotoClickListener photoClickListener) {
        mPhotoClickListener = photoClickListener;
    }

    public PhotoViewHolder(View itemView, Context context, TextView title) {
        super(itemView);
        thumbPhotoView = (ThumbPhotoView) itemView;
        mContext = context;
        mTitle = title;
    }

    /**
     *
     */
    public void setData(final ArrayList<String> selectedImages, Photo photo, final int maxPickSize,
                        final int imageSize, final int pos) {
        thumbPhotoView.setLayoutParams(new FrameLayout.LayoutParams(imageSize, imageSize));
        thumbPhotoView.loadData(photo.getPath());

        if (selectedImages.contains(photo.getPath())) {
            thumbPhotoView.showSelected(true);
        } else {
            thumbPhotoView.showSelected(false);
        }

        final String path = photo.getPath();

        thumbPhotoView.getPhoto_thumbview_selected().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImages.contains(path)) {
                    selectedImages.remove(path);
                    thumbPhotoView.showSelected(false);
                } else {
                    if (selectedImages.size() == maxPickSize) {
                        Toast.makeText(mContext, "Select up to " + maxPickSize + "photos",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        selectedImages.add(path);
                        thumbPhotoView.showSelected(true);
                    }
                }
                mTitle.setText(String.format(mContext.getString(R.string.picker_done_with_count),
                        selectedImages.size(), maxPickSize));
            }
        });

        thumbPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mPhotoClickListener) {
                    mPhotoClickListener.OnClick(v, pos, imageSize);
                }
            }
        });
    }

    public void setCamera() {
        thumbPhotoView.loadCamera();
        thumbPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onCameraClickListener) {
                    onCameraClickListener.onClick(v);
                }
            }
        });
    }

    private View.OnClickListener onCameraClickListener = null;

    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.onCameraClickListener = onCameraClickListener;
    }
}
