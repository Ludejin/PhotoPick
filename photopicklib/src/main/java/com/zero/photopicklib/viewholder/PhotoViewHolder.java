package com.zero.photopicklib.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.zero.photopicklib.entity.Photo;
import com.zero.photopicklib.widget.ThumbPhotoView;

import java.util.ArrayList;

/**
 * Created by Jin_ on 2016/6/21
 * 邮箱：dejin_lu@creawor.com
 */
public class PhotoViewHolder extends RecyclerView.ViewHolder {

    public ThumbPhotoView thumbPhotoView;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        thumbPhotoView = (ThumbPhotoView) itemView;
    }

    public void setData(final ArrayList<String> selectedImages,
                        Photo photo, final int maxPickSize, int imageSize) {
        thumbPhotoView.setLayoutParams(new FrameLayout.LayoutParams(imageSize, imageSize));
        thumbPhotoView.loadData(photo.getPath());
        if (selectedImages.contains(photo.getPath())) {
            thumbPhotoView.showSelected(true);
        } else {
            thumbPhotoView.showSelected(false);
        }

        final String path = photo.getPath();

        thumbPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImages.contains(path)) {
                    selectedImages.remove(path);
                    thumbPhotoView.showSelected(false);
                } else {
                    if (selectedImages.size() == maxPickSize) {
                        return;
                    } else {
                        selectedImages.add(path);
                        thumbPhotoView.showSelected(true);
                    }
                }
            }
        });
    }
}
